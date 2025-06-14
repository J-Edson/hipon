<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Savings Expense Log</title>
</head>
<body>
    <div class="row justify-content-center py-5" style="background-color: #F5F7FA;">
        <div class="col-10">
            <table class="table-primary table-hover table-striped" style="font-size: 20px">
                <thead>
                    <tr>
                    <th scope="col">Description</th>
                    <th scope="col">Transaction ID</th>
                    <th scope="col">Type</th>
                    <th scope="col">Account Name</th>
                    <th scope="col">Date</th>
                    <th scope="col">Debit</th>
                    <th scope="col">Credit</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="record" in="${recordList}">
                        <tr>
                            <td>${record.description}</td>
                            <td>${record.id}</td>
                            <td>${record.recordType.name}</td>
                            <g:if test="${!record?.debit}">
                                <td>${record?.credit?.acctName}</td>
                            </g:if>
                            <g:else>
                                <td>${record?.debit?.acctName}</td>
                            </g:else>
                            <td>${record.logDate}</td>
                            <g:if test="${!record?.debit}">
                                <td></td>
                                <td><g:formatNumber number="${record.txnAmt}" format="#,##0.00" /></td>
                            </g:if>
                            <g:else>
                                <td><g:formatNumber number="${record.txnAmt}" format="#,##0.00" /></td>
                                <td></td>
                            </g:else>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>