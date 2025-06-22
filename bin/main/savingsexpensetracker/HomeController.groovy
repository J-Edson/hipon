package savingsexpensetracker

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

import groovy.sql.Sql
import java.time.LocalDate
import java.time.ZoneId
import java.text.SimpleDateFormat
import grails.converters.JSON

import category.*
import log.*
import savings.*
import expense.*

@Secured('ROLE_ADMIN')
class HomeController {

    def dataSource
    def springSecurityService
    def SavingsService

    def index() { 
        def userInstance = springSecurityService.getCurrentUser()
        println "SavingsService.interestAccrual(userInstance) "+SavingsService.interestAccrual(userInstance)
        def savingsActiveList = Savings.findAllByClientAndStatus(userInstance, Status.get(1))
        def totalBalance = 0.00D
        for (savings in savingsActiveList) {
            totalBalance += savings.balance
        }

        def expenseList = Expense.findAllByClientAndStatus(userInstance, Status.get(3))
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
            query = "SELECT to_char('" + dataDate + "'::date, 'Day') AS record_date, COALESCE((SELECT SUM(CASE WHEN record_type_id = 7 THEN txn_amt ELSE 0 END) - SUM(CASE WHEN record_type_id = 8 THEN txn_amt ELSE 0 END) AS total_expense FROM record A WHERE A.client_id = "+userInstance.id+" AND  A.record_date::date = '" + dataDate + "' AND A.record_type_id in (7, 8) GROUP BY A.record_date::date),0) AS total_expense"
            def activity = sql.rows(query)
            def dailyActivity = [];
            dailyActivity[0] = activity.record_date[0]
            dailyActivity[2] = activity.total_expense[0] 
            query = "SELECT to_char('" + dataDate + "'::date, 'Day') AS record_date,COALESCE((SELECT SUM(CASE WHEN record_type_id in (1, 3, 9) THEN txn_amt ELSE 0 END) - SUM(CASE WHEN record_type_id in (2, 4) THEN txn_amt ELSE 0 END) AS total_savings FROM record A WHERE A.client_id = "+userInstance.id+" AND  A.record_date::date = '" + dataDate + "' AND A.record_type_id in (1,2,3,4,9) GROUP BY A.record_date::date),0) AS total_savings"
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
        query = "SELECT UPPER(to_char(record_date::date, 'Mon-YYYY')) AS month_record, SUM(CASE WHEN record_type_id in (1, 3, 8, 9) THEN txn_amt ELSE -txn_amt END) AS balance FROM record WHERE NOT record_type_id in (5, 6) AND client_id = "+userInstance.id+" GROUP BY UPPER(to_char(record_date::date, 'Mon-YYYY'))"
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
        query = "SELECT B.name, SUM(A.txn_amt) as amount FROM expense A JOIN expense_category B on B.id = A.category_id WHERE A.client_id = "+userInstance.id+" AND A.status_id = 3 GROUP BY B.name"
        def expenseSummary = sql.rows(query)
        def expenseData = []
        expenseSummary.each { expense ->
            expenseData.add([expense.name, expense.amount])
        }
        expenseData = expenseData as JSON
        //

        query = "SELECT to_char(record_date, 'YYYY-MM-DD HH24:MI:SS') AS date, description, CASE WHEN record_type_id IN (1, 3, 6, 8, 9) THEN txn_amt ELSE -1*txn_amt END AS amount, CASE WHEN expense_id is null THEN 0 ELSE 1 END AS log_type FROM record WHERE client_id = "+userInstance.id+" ORDER BY log_date DESC limit 4"
        def recordList = sql.rows(query)

        [userInstance:userInstance, totalBalance:totalBalance, totalExpense:totalExpense, weeklyActivity:weeklyActivity, savingsBalanceHistory:savingsBalanceHistory, expenseData:expenseData, recordList:recordList]
    }
}
