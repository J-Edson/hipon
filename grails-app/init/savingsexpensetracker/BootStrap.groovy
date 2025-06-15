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
        def statusTypeList = ["0-Active", "1-Closed", "2-Processed", "3-Reversed"]

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
        def recordTypeList = ["0-New Savings", "1-Remove Savings", "2-Debit Balance", "3-Credit Balance", "4-Transfer Credit", "5-Transfer Debit", "6-Log Expense", "7-Reverse Expense"]

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
        def expenseCategoryList = ["0-Food", "1-Transportation", "2-Entertainment", "3-Utilities", "4-Personal Care", "5-Housing", "6-Healthcare", "7-Loans", "8-Insurance"]

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
        def frequencyList = ["0-N/A", "1-Daily", "2-Monthly", "3-Annually"]

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
