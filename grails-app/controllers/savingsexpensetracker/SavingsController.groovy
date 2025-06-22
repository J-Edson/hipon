package savingsexpensetracker

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService
import groovy.sql.Sql
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.text.SimpleDateFormat
import grails.converters.JSON

import category.*
import log.*
import savings.*
import expense.*

@Secured('ROLE_ADMIN')
class SavingsController {

    def dataSource
    def springSecurityService

    def index () {
        def userInstance = springSecurityService.getCurrentUser()
        def savingsList = Savings.list()
        def savingsActiveList = Savings.findAllByClientAndStatus(userInstance, Status.get(1))
        def totalBalance = 0
        for (savings in savingsActiveList) {
            totalBalance += savings.balance
        }
        println totalBalance

        //balance history chart
        def sql = new Sql(dataSource)
        def query = ""
        query = "SELECT UPPER(to_char(record_date::date, 'Mon-YYYY')) AS month_record, SUM(CASE WHEN record_type_id in (1, 3, 8) THEN txn_amt ELSE -txn_amt END) AS balance FROM record WHERE NOT record_type_id in (5, 6) AND client_id = "+userInstance.id+" GROUP BY UPPER(to_char(record_date::date, 'Mon-YYYY'))"
        def savingsBalanceTable = sql.rows(query)
        def savingsBalanceHistory = []
        def balanceHistory = 0
        savingsBalanceTable.each { savingBalance ->
            balanceHistory += savingBalance.balance
            savingsBalanceHistory.add([savingBalance.month_record, balanceHistory])
        }
        savingsBalanceHistory = savingsBalanceHistory as JSON
        //
        query = "SELECT to_char(record_date, 'YYYY-MM-DD HH24:MI:SS') AS date, description, CASE WHEN record_type_id IN (1, 3, 6, 8) THEN txn_amt ELSE -1*txn_amt END AS amount, CASE WHEN expense_id is null THEN 0 ELSE 1 END AS log_type FROM record WHERE client_id = "+userInstance.id+" ORDER BY record_date DESC limit 8"
        def recordList = sql.rows(query)
        println recordList
        [savingsList: savingsList, totalBalance:totalBalance, savingsActiveList:savingsActiveList, savingsBalanceHistory:savingsBalanceHistory, recordList:recordList]
    }

    def show (Long id) {
        def userInstance = springSecurityService.getCurrentUser()
        def savingsInstance = Savings.get(id)
        if(userInstance.id != savingsInstance.client.id){
            redirect(action: "index")
        }else{
            def savingsActiveList = Savings.findAllByClientAndStatusAndIdNotEqual(userInstance, Status.get(1), savingsInstance.id)

            def expenseList = Expense.findAllByCreditAcctAndStatus(savingsInstance, Status.get(3))
            def totalExpense = 0.00D
            for (expense in expenseList) {
                totalExpense += expense.txnAmt
            }

            //weekly activity chart
            def sdf = new SimpleDateFormat('EEEE')
            def dateToday = LocalDate.now(ZoneId.of("Asia/Manila"))
            def sql = new Sql(dataSource)
            def query = ""
            def weeklyActivity = []

            for(int x = 6; x >= 0; x--){
                def dataDate = dateToday.minusDays(x)
                query = "SELECT to_char('" + dataDate + "'::date, 'Day') AS record_date, COALESCE((SELECT SUM(CASE WHEN record_type_id = 7 THEN txn_amt ELSE 0 END) - SUM(CASE WHEN record_type_id = 8 THEN txn_amt ELSE 0 END) AS total_expense FROM record A WHERE A.credit_id = "+savingsInstance.id+" AND  A.record_date::date = '" + dataDate + "' AND A.record_type_id in (7, 8) GROUP BY A.record_date::date),0) AS total_expense"
                def activity = sql.rows(query)
                def dailyActivity = [];
                dailyActivity[0] = activity.record_date[0]
                dailyActivity[2] = activity.total_expense[0] 
                query = "SELECT to_char('" + dataDate + "'::date, 'Day') AS record_date,COALESCE((SELECT SUM(CASE WHEN record_type_id in (1, 3, 6, 9) THEN txn_amt ELSE 0 END) - SUM(CASE WHEN record_type_id in (2, 4, 5) THEN txn_amt ELSE 0 END) AS total_savings FROM record A WHERE (credit_id = "+savingsInstance.id+" or debit_id = "+savingsInstance.id+") AND  A.record_date::date = '" + dataDate + "' AND not A.record_type_id in (7,8) GROUP BY A.record_date::date),0) AS total_savings"
                activity = sql.rows(query)
                if(activity.total_savings[0] <= 0){
                    dailyActivity[1] = 0
                }else{
                    dailyActivity[1] = activity.total_savings[0]
                }
                weeklyActivity.add(dailyActivity)
            }
            weeklyActivity = weeklyActivity as JSON
            //weekly activity chart end

            //balance history chart
            query = "SELECT UPPER(to_char(record_date::date, 'Mon-YYYY')) AS month_record, SUM(CASE WHEN record_type_id in (1, 3, 6, 8, 9) THEN txn_amt ELSE -txn_amt END) AS balance FROM record WHERE (credit_id = "+savingsInstance.id+" or debit_id = "+savingsInstance.id+") GROUP BY UPPER(to_char(record_date::date, 'Mon-YYYY'))"
            def savingsBalanceTable = sql.rows(query)
            def savingsBalanceHistory = []
            def balanceHistory = 0
            savingsBalanceTable.each { savingBalance ->
                balanceHistory += savingBalance.balance
                savingsBalanceHistory.add([savingBalance.month_record, balanceHistory])
            }
            savingsBalanceHistory = savingsBalanceHistory as JSON
            //
        
                //expense chart
            query = "SELECT B.name, SUM(A.txn_amt) as amount FROM expense A JOIN expense_category B on B.id = A.category_id WHERE A.credit_acct_id = "+savingsInstance.id+" AND A.status_id = 3 GROUP BY B.name"
            def expenseSummary = sql.rows(query)
            def expenseData = []
            expenseSummary.each { expense ->
                expenseData.add([expense.name, expense.amount])
            }
            expenseData = expenseData as JSON
            //
            def savingsInterestInstance = SavingsInterest.findBySavings(savingsInstance)
            def interestFreq = InterestFrequency.list()

            [savingsInstance:savingsInstance, savingsActiveList:savingsActiveList, totalExpense:totalExpense, weeklyActivity:weeklyActivity, savingsBalanceHistory:savingsBalanceHistory, expenseData:expenseData, interestFreq:interestFreq, savingsInterestInstance:savingsInterestInstance]
        }
    }

    @Transactional
    def save () {
        println "params " + params
        def userInstance = springSecurityService.getCurrentUser()
        def savingsInstance = new Savings(
            client: userInstance,
            acctName: params.acctName,
            acctNo: params.acctNo,
            expiryDate: params.expiryDate,
            balance: params.balance,
            status: Status.get(1)
        )
        println savingsInstance.acctName
        println savingsInstance.balance
        if(!savingsInstance.save()) {
            savingsInstance.errors.allErrors.each {
                println it
            }
        }else {
            println "SavingsSave"
        }
        def recordLog = new Record(
            client: userInstance,
            debit: savingsInstance,
            description: "New Savings",
            recordType: RecordType.get(1),
            status: Status.get(3),
            txnAmt: savingsInstance.balance
        )
        if(!recordLog.save()) {
            println "%%Error%%"
            recordLog.errors.allErrors.each {
                println "Error " + it
            }
        }else {
            println "RecordSave"
        }
        println "Redirecting"
        redirect(action: "index")
    }

    @Transactional
    def delete () {
        println "params " + params
        def userInstance = springSecurityService.getCurrentUser()
        def savingsInstance = Savings.get(params.id)
        if(userInstance.id != savingsInstance.client.id){
            redirect(action: "index")
        }else{
            savingsInstance.status = Status.get(2)
            savingsInstance.save()
            def recordLog = new Record(
                client: userInstance,
                credit: savingsInstance,
                description: "Closed Savings",
                recordType: RecordType.get(2),
                status: Status.get(3),
                txnAmt: savingsInstance.balance
            )
            recordLog.save()
            redirect(action: "index")
        }
    }

    @Transactional
    def updateBalance () {
        println "%param%" + params
        def userInstance = springSecurityService.getCurrentUser()
        def savingsInstance = Savings.get(params.id)
        if(userInstance.id != savingsInstance.client.id){
            redirect(action: "index")
        }else{
            def savingsBalance = savingsInstance.balance
            def txnAmt = params.txnAmt.toDouble()
            def recordLog = new Record()
            if(params.process.equals("Add")) {
                println "debitBalance"
                savingsInstance.balance += txnAmt
                savingsInstance.save()

                recordLog.client = userInstance
                recordLog.debit = savingsInstance
                recordLog.description = "Add Balance - "+params.details
                recordLog.recordType = RecordType.get(3)
                recordLog.status = Status.get(3)
                recordLog.txnAmt = txnAmt
                recordLog.save()
            }else{
                println "creditBalance"
                if(savingsBalance >= txnAmt){
                    println "savingsBalance < txnAmt"
                    savingsInstance.balance -= txnAmt
                    savingsInstance.save()

                    recordLog.client = userInstance
                    recordLog.credit = savingsInstance
                    recordLog.description = "Deduct Balance - "+params.details
                    recordLog.recordType = RecordType.get(4)
                    recordLog.status = Status.get(3)
                    recordLog.txnAmt = txnAmt
                    recordLog.save()
                }else{
                    flash.message = "Insufficient balance."
                }   
            }
            println "%newBalance% " + savingsInstance.balance  
            redirect(action: "show", id: savingsInstance.id)
        }
    }

    @Transactional
    def transferBalance () {
        println "%transferBalanceParams% " + params
        def userInstance = springSecurityService.getCurrentUser()
        def creditSavings = Savings.get(params.id)
        if(userInstance.id != creditSavings.client.id){
            redirect(action: "index")
        }else if(!params.debitSavingsID){
            flash.message = "Transfer Failed, Please select a valid account."
            redirect(action: "show", id: params.id)
        }else{
            def debitSavings = Savings.get(params.debitSavingsID)
            def txnAmt = params.txnAmt.toDouble()

            if(txnAmt <= creditSavings.balance){
                creditSavings.balance -= txnAmt
                debitSavings.balance += txnAmt
                def recordLogDebit = new Record(
                    client: userInstance,
                    debit: debitSavings,
                    description: "Balance Received",
                    recordType: RecordType.get(6),
                    status: Status.get(3),
                    txnAmt: txnAmt
                )
                def recordLogCredit = new Record(
                    client: userInstance,
                    credit: creditSavings,
                    description: "Balance Transfer - "+params.details,
                    recordType: RecordType.get(5),
                    status: Status.get(3),
                    txnAmt: txnAmt
                )
                recordLogDebit.save()
                recordLogCredit.save()
            }else{
                flash.message = "Insufficient balance."
            } 

            creditSavings.save()
            debitSavings.save()
            redirect(action: "show", id: creditSavings.id)
        }
    }

    @Transactional
    def configureInterest () {
        println "%configureInterest% " + params
        def userInstance = springSecurityService.getCurrentUser()
        def savingsInstance = Savings.get(params.id)
        if(userInstance.id != savingsInstance.client.id){
            redirect(action: "index")
        }else{
            def savingsInterestInstance = SavingsInterest.findBySavings(savingsInstance)
            def intRate = params.intRate
            def interestFrequencyInstance = InterestFrequency.get(params.frequencyID)
            if(savingsInterestInstance){
                savingsInterestInstance.interestRate = intRate.toDouble()
                savingsInterestInstance.interestFrequency = interestFrequencyInstance
                savingsInterestInstance.save()
            }
            else{
                def newSavingsInterest = new SavingsInterest(
                    savings: savingsInstance,
                    interestRate: intRate.toDouble(),
                    interestFrequency: interestFrequencyInstance,
                    lastAccrualDate: LocalDate.now(ZoneId.of("Asia/Manila"))
                ).save()
            }
            redirect(action: "show", id: savingsInstance.id)
        }
    }
}
