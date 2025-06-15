package savingsexpensetracker

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
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

    private statusList = Status.listOrderByCode()
    private recordTypeList = RecordType.listOrderByCode()
    private userInstance = getAuthenticatedUser()
    def dataSource

    def index () {
        def savingsList = Savings.list()
        def savingsActiveList = Savings.findAllByClientAndStatus(userInstance, statusList[0])
        def totalBalance = 0
        for (savings in savingsActiveList) {
            totalBalance += savings.balance
        }
        println totalBalance

        //balance history chart
        def sql = new Sql(dataSource)
        def query = ""
        println recordTypeList
        query = "SELECT UPPER(to_char(log_date::date, 'Mon-YYYY')) AS month_record, SUM(CASE WHEN record_type_id in (1, 3, 8) THEN txn_amt ELSE -txn_amt END) AS balance FROM record WHERE NOT record_type_id in (5, 6) AND client_id = "+userInstance.id+" GROUP BY UPPER(to_char(log_date::date, 'Mon-YYYY'))"
        def savingsBalanceTable = sql.rows(query)
        def savingsBalanceHistory = []
        def balanceHistory = 0
        savingsBalanceTable.each { savingBalance ->
            balanceHistory += savingBalance.balance
            savingsBalanceHistory.add([savingBalance.month_record, balanceHistory])
        }
        savingsBalanceHistory = savingsBalanceHistory as JSON
        //
        query = "SELECT to_char(log_date, 'YYYY-MM-DD HH24:MI:SS') AS date, description, CASE WHEN record_type_id IN (1, 3, 6, 8) THEN txn_amt ELSE -1*txn_amt END AS amount, CASE WHEN expense_id is null THEN 0 ELSE 1 END AS log_type FROM record WHERE client_id = "+userInstance.id+" ORDER BY log_date DESC limit 8"
        def recordList = sql.rows(query)
        println recordList
        [savingsList: savingsList, totalBalance:totalBalance, savingsActiveList:savingsActiveList, savingsBalanceHistory:savingsBalanceHistory, recordList:recordList]
    }

    def show (Long id) {
        def savingsInstance = Savings.get(id)
        if(userInstance.id != savingsInstance.client.id){
            redirect(action: "index")
        }else{
            def savingsActiveList = Savings.findAllByClientAndStatusAndIdNotEqual(userInstance, statusList[0], savingsInstance.id)

            def expenseList = Expense.findAllByCreditAcctAndStatus(savingsInstance, statusList[2])
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
            println recordTypeList
            for(int x = 6; x >= 0; x--){
                def dataDate = dateToday.minusDays(x)
                query = "SELECT to_char('" + dataDate + "'::date, 'Day') AS record_date, COALESCE((SELECT SUM(CASE WHEN record_type_id = "+recordTypeList[6].id+" THEN txn_amt ELSE 0 END) - SUM(CASE WHEN record_type_id = "+recordTypeList[7].id+" THEN txn_amt ELSE 0 END) AS total_expense FROM record A WHERE A.credit_id = "+savingsInstance.id+" AND  A.log_date::date = '" + dataDate + "' AND A.record_type_id in ("+recordTypeList[6].id+", "+recordTypeList[7].id+") GROUP BY A.log_date::date),0) AS total_expense"
                def activity = sql.rows(query)
                def dailyActivity = [];
                dailyActivity[0] = activity.record_date[0]
                dailyActivity[2] = activity.total_expense[0] 
                query = "SELECT to_char('" + dataDate + "'::date, 'Day') AS record_date,COALESCE((SELECT SUM(CASE WHEN record_type_id in ("+recordTypeList[0].id+", "+recordTypeList[2].id+", "+recordTypeList[5].id+") THEN txn_amt ELSE 0 END) - SUM(CASE WHEN record_type_id in ("+recordTypeList[1].id+", "+recordTypeList[3].id+", "+recordTypeList[4].id+") THEN txn_amt ELSE 0 END) AS total_savings FROM record A WHERE (credit_id = "+savingsInstance.id+" or debit_id = "+savingsInstance.id+") AND  A.log_date::date = '" + dataDate + "' AND A.record_type_id in ("+recordTypeList[0].id+", "+recordTypeList[2].id+", "+recordTypeList[1].id+", "+recordTypeList[3].id+", "+recordTypeList[4].id+", "+recordTypeList[5].id+") GROUP BY A.log_date::date),0) AS total_savings"
                activity = sql.rows(query)
                dailyActivity[1] = activity.total_savings[0]
                weeklyActivity.add(dailyActivity)
            }
            weeklyActivity = weeklyActivity as JSON
            //weekly activity chart end

            //balance history chart
            println recordTypeList
            query = "SELECT UPPER(to_char(log_date::date, 'Mon-YYYY')) AS month_record, SUM(CASE WHEN record_type_id in ("+recordTypeList[0].id+", "+recordTypeList[2].id+", "+recordTypeList[5].id+", "+recordTypeList[7].id+") THEN txn_amt ELSE -txn_amt END) AS balance FROM record WHERE (credit_id = "+savingsInstance.id+" or debit_id = "+savingsInstance.id+") GROUP BY UPPER(to_char(log_date::date, 'Mon-YYYY'))"
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
            println statusList
            query = "SELECT B.name, SUM(A.txn_amt) as amount FROM expense A JOIN expense_category B on B.id = A.category_id WHERE A.credit_acct_id = "+savingsInstance.id+" AND A.status_id = "+statusList[2].id+" GROUP BY B.name"
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
        def savingsInstance = new Savings(
            client: userInstance,
            acctName: params.acctName,
            acctNo: params.acctNo,
            expiryDate: params.expiryDate,
            balance: params.balance,
            status: statusList[0]
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
            recordType: recordTypeList[0],
            status: statusList[2],
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
        def savingsInstance = Savings.get(params.id)
        if(userInstance.id != savingsInstance.client.id){
            redirect(action: "index")
        }else{
            savingsInstance.status = statusList[1]
            savingsInstance.save()
            def recordLog = new Record(
                client: userInstance,
                credit: savingsInstance,
                description: "Closed Savings",
                recordType: recordTypeList[1],
                status: statusList[2],
                txnAmt: savingsInstance.balance
            )
            recordLog.save()
            redirect(action: "index")
        }
    }

    @Transactional
    def updateBalance (Long id) {
        println "%param%" + params
        def savingsInstance = Savings.get(id)
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
                recordLog.description = "Add Balance"
                recordLog.recordType = recordTypeList[2]
                recordLog.status = statusList[2]
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
                    recordLog.description = "Deduct Balance"
                    recordLog.recordType = recordTypeList[3]
                    recordLog.status = statusList[2]
                    recordLog.txnAmt = txnAmt
                    recordLog.save()
                }
            }
            println "%newBalance% " + savingsInstance.balance  
            redirect(action: "show", id: savingsInstance.id)
        }
    }

    @Transactional
    def transferBalance (Long id) {
        println "%transferBalanceParams% " + params
        def creditSavings = Savings.get(id)
        if(userInstance.id != creditSavings.client.id){
            redirect(action: "index")
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
                    recordType: recordTypeList[5],
                    status: statusList[2],
                    txnAmt: txnAmt
                )
                def recordLogCredit = new Record(
                    client: userInstance,
                    credit: creditSavings,
                    description: "Balance Transfer",
                    recordType: recordTypeList[4],
                    status: statusList[2],
                    txnAmt: txnAmt
                )
                recordLogDebit.save()
                recordLogCredit.save()
            }

            creditSavings.save()
            debitSavings.save()
            redirect(action: "show", id: creditSavings.id)
        }
    }

    @Transactional
    def configureInterest () {
        println "%configureInterest% " + params
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
                    lastAccrualDate: LocalDateTime.now(ZoneId.of("Asia/Manila"))
                ).save()
            }
            redirect(action: "show", id: savingsInstance.id)
        }
    }
}
