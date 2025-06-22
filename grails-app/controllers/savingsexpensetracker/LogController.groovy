package savingsexpensetracker
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

import log.*

@Secured('ROLE_ADMIN')
class LogController {

    def springSecurityService

    def index() { 
        def userInstance = springSecurityService.getCurrentUser()
        def recordList = Record.listOrderByLogDate(order: "desc")

        [recordList:recordList]
    }
}
