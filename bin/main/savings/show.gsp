<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Savings Expense Tracker</title>
    </head>
<body>
    <div class="row py-5" style="background-color: #F5F7FA;">
        <div class="col-12">
            <div>
                Name: ${savingsInstance.acctName}
            </div>
            <div>
                Balance: ${savingsInstance.balance}
            </div>
            <div>
                Status: ${savingsInstance.status.name}
            </div>
            <g:form controller="savings" action="updateBalance">
                <label for="cashAmount">Enter amount:</label><br>
                <input id="txnAmt" name="txnAmt" type="number" step="0.01" min="0" placeholder="0.00" required/><br>
                <input type="hidden" name="id" value="${savingsInstance.id}" />
                <input type="hidden" name="transactionType" value="debit" />
                <g:submitButton name="Add" value="Add"/>
            </g:form>
            <g:form controller="savings" action="updateBalance">
                <label for="cashAmount">Enter amount:</label><br>
                <input id="txnAmt" name="txnAmt" type="number" step="0.01" min="0" placeholder="0.00" required/><br>
                <input type="hidden" name="id" value="${savingsInstance.id}" />
                <input type="hidden" name="transactionType" value="credit" />
                <g:submitButton name="Reduce" value="Reduce"/>
            </g:form>

            <div class="modal fade" id="transferBalanceModal" tabindex="-1" role="dialog" aria-labelledby="transferBalanceModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Transfer Balance</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <g:form controller="savings" action="transferBalance">
                            <div>${savingsInstance.acctName}</div>
                            <input id="txnAmt" name="txnAmt" type="number" step="0.01" min="0" placeholder="0.00" required/><br>
                            <select id="debitSavingsID" name="debitSavingsID" multiple>
                                <g:each var="savings" in="${savingsActiveList}">
                                    <option value=${savings.id}>${savings.acctName}</option>
                                </g:each>
                            </select>
                            <input type="hidden" name="id" value="${savingsInstance.id}" />
                            <g:submitButton name="Transfer" value="Transfer Balance"/>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        </g:form>
                    </div>
                </div>
            </div>
            </div>

            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferBalanceModal">Transfer Balance</button>
            <g:link action="delete" id="${savingsInstance.id}">Remove Account</g:link>
        </div>
    </div>
</body>
</html>