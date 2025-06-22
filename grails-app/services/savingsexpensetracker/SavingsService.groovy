package savingsexpensetracker

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

import grails.gorm.transactions.Transactional

import category.*
import log.*
import savings.*
import expense.*
import user.*

@Transactional
class SavingsService {

    def interestAccrual(Person userInstance) {
        println "interestAccrual user id"+userInstance
        def savingsActiveList = Savings.findAllByClientAndStatus(userInstance, Status.get(1))
        for(savings in savingsActiveList) {
            def savingsInterestInstance = SavingsInterest.findBySavings(savings)
            if(savingsInterestInstance){
                if(savingsInterestInstance.interestFrequency.name == 'Daily'){
                    println "Daily"
                    interestAccrualDaily(savingsInterestInstance)
                }else{
                    println "Others"
                }
            }
        }
        return true
    }

    def interestAccrualDaily(SavingsInterest savingsInterestInstance) {
        println "interestAccrualDaily "+savingsInterestInstance
        def dateToday = LocalDate.now(ZoneId.of("Asia/Manila"))
        def savingsInstance = savingsInterestInstance.savings
        def lastAccrualDate = savingsInterestInstance.lastAccrualDate
        def newBalance = savingsInstance.balance
        while(lastAccrualDate < dateToday){
            lastAccrualDate = lastAccrualDate.plusDays(1)
            if(lastAccrualDate == dateToday){
                break;
            }
            def dailyInterest = newBalance * (savingsInterestInstance.interestRate / 100) / 365
            def tax = dailyInterest * 0.2
            dailyInterest = dailyInterest - tax
            newBalance = newBalance + dailyInterest.round(2)
            def recordLog = new Record(
                client: savingsInstance.client,
                debit: savingsInstance,
                description: "Add Interest - "+lastAccrualDate,
                recordType: RecordType.get(9),
                status: Status.get(3),
                txnAmt: dailyInterest.round(2),
                recordDate: lastAccrualDate
            ).save(flush: true) 
        }
        savingsInstance.balance = newBalance.round(2)
        savingsInstance.save(flush: true)
        savingsInterestInstance.lastAccrualDate = lastAccrualDate
        savingsInterestInstance.save(flush: true)
    }
}
