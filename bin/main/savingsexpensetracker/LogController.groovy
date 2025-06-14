package savingsexpensetracker
import grails.plugin.springsecurity.annotation.Secured

import log.*

@Secured('ROLE_ADMIN')
class LogController {

    def index() { 
        def recordList = Record.listOrderByLogDate(order: "desc")

        [recordList:recordList]
    }
}
