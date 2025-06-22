package savingsexpensetracker

import user.Person
import user.Authority
import user.PersonAuthority
import category.Status
import log.RecordType
import expense.ExpenseCategory
import savings.InterestFrequency

import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        addInitialPersonAuthority()
        addInitialStatus()
        addInitialRecordType()
        addInitialExpenseCategory()
        addInitialInterestFrequency()
    }
    def destroy = {
    }

    @Transactional
    void addInitialPersonAuthority() {
        def adminRole = Authority.findOrSaveByAuthority('ROLE_ADMIN')
        def userRole = Authority.findOrSaveByAuthority('ROLE_USER')
        def adminUser = Person.findByUsername('admin')
        if(!adminUser){
            adminUser = new Person(username: 'admin', password: 'admin').save()
            PersonAuthority.create adminUser, adminRole
        }
        PersonAuthority.withSession {
            it.flush()
            it.clear()
        }

        def adminUser1 = Person.findByUsername('user1')
        if(!adminUser1){
            adminUser = new Person(username: 'user1', password: 'user1').save()
            PersonAuthority.create adminUser, adminRole
        }
        PersonAuthority.withSession {
            it.flush()
            it.clear()
        }
    }

    @Transactional
    void addInitialStatus() {
        def statusTypeList = ["1-Active", "2-Closed", "3-Processed", "4-Reversed"]

        statusTypeList.each { type ->
            String[] statusType = type.split("-");
            def code = Integer.parseInt(statusType[0])
            def name = statusType[1]
            def newStatus = Status.findByName(name)
            if(!newStatus){
                newStatus = new Status(name: name, code: code).save()
            }
        }
    }

    @Transactional
    void addInitialRecordType() {
        def recordTypeList = ["1-New Savings", "2-Remove Savings", "3-Debit Balance", "4-Credit Balance", "5-Transfer Credit", "6-Transfer Debit", "7-Log Expense", "8-Reverse Expense", "9-Savings Interest"]

        recordTypeList.each { type ->
            String[] recordType = type.split("-");
            def code = Integer.parseInt(recordType[0])
            def name = recordType[1]
            def newRecordType = RecordType.findByName(name)
            if(!newRecordType){
                newRecordType = new RecordType(name: name, code: code).save()
            }
        }
    }

    @Transactional
    void addInitialExpenseCategory() {
        def expenseCategoryList = ["1-Food", "2-Transportation", "3-Entertainment", "4-Utilities", "5-Personal Care", "6-Housing", "7-Healthcare", "8-Loans", "9-Insurance"]

        expenseCategoryList.each { category ->
            String[] expenseCategory = category.split("-");
            def code = Integer.parseInt(expenseCategory[0])
            def name = expenseCategory[1]
            def newCategory = ExpenseCategory.findByName(name)
            if(!newCategory){
                newCategory = new ExpenseCategory(name: name, code: code).save()
            }
        }
    }

    @Transactional
    void addInitialInterestFrequency() {
        def frequencyList = ["1-N/A", "2-Daily", "3-Monthly", "4-Annually"]

        frequencyList.each { freq ->
            String[] frequency = freq.split("-");
            def code = Integer.parseInt(frequency[0])
            def name = frequency[1]
            def newFrequency = InterestFrequency.findByName(name)
            if(!newFrequency){
                newFrequency = new InterestFrequency(name: name, code: code).save()
            }
        }
    }

}
