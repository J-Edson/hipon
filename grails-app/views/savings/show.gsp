<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Savings Expense Tracker</title>
    </head>
<body>
    <script>
        function showAlertConfirm(formId,title,process){
            if(process){
                document.getElementById('process').value = process;
            }
            Swal.fire({
            title: title,
            showDenyButton: true,
            confirmButtonText: "Confirm",
            denyButtonText: `Cancel`
            }).then((result) => {
            if (result.isConfirmed) {
                document.getElementById(formId).submit();
            } else if (result.isDenied) {
                return false
            }
            });
        }
    </script>
    <g:if test="${flash.message}">
        <script>
            showFlashMessage(`${flash.message}`)
        </script>
    </g:if>
    <div class="row">
        <div class="col-8 py-5" style="background-color: #F5F7FA;"> 
            <div class="row" >
                <div class="col-12">
                    <div class="row text-center" >
                        <div class="col mx-5 p-3" style="background-image: linear-gradient(to bottom right, #26E1D3, #3B706D); border-radius: 15px">
                            <div class="row text-center align-items-center" >
                                <div class="col-3">
                                    <div><img id="savingsArrowIcon" src='${assetPath(src: "savings_arrow_icon.svg")}'/></div>
                                </div>
                                <div class="col-9 text-start">
                                    <div class="row text-center align-items-center" >
                                        <div class="col-9 text-start" style="font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 600;font-style: normal;">Total Balance</div>
                                        <div class="col-2 align-self-end" style="color: #41D4A8; background-color: #FFFFFF; border-radius: 5px; font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 500;font-style: normal;">
                                            %
                                        </div>
                                    </div>  
                                    <div style="font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 700;font-style: normal;font-size: 35px;">
                                        &#x20B1;<g:formatNumber number="${savingsInstance.balance}" format="#,##0.00" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col mx-5 p-3" style="background-image: linear-gradient(to bottom right, #B05C31, #F02C07); border-radius: 15px">
                            <div class="row text-center align-items-center" >
                                <div class="col-3">
                                    <img id="expenseArrowIcon" src='${assetPath(src: "expense_arrow_icon.svg")}'/>
                                </div>
                                <div class="col-9 text-start">
                                    <div class="row text-center align-items-center" >
                                        <div class="col-9 text-start" style="font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 600;font-style: normal;color: #FFFFFF">Total Expense</div> 
                                        <div class="col-2 align-self-end" style="color: red; background-color: #FFFFFF; border-radius: 5px; font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 500;font-style: normal;">
                                            %
                                        </div>
                                    </div>
                                    <div style="font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 700;font-style: normal;font-size: 35px;color: #FFFFFF">
                                        &#x20B1;<g:formatNumber number="${totalExpense}" format="#,##0.00" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-12 mx-5 my-5">
                    <g:hiddenField id ="weeklyActivity" name="weeklyActivity" value="${weeklyActivity}" />
                    <g:render template="/components/weekly_activity_chart" />
                </div>
                <div class="col-12 mx-5">
                    <g:render template="/components/savings_history" model="${['savingsBalanceHistory': savingsBalanceHistory]}"/>
                </div>
            </div>
        </div>
        <div class="col py-5" style="background-color: #F5F7FA;">
            <div class="row mr-5">
                <div class="col-12 mb-4 pb-3" style="border: 2px solid #C7EDF0;border-radius:10px; background-color: #FFFFFF">
                    <div style="color: #343C6A;font-size: 40px;">Adjust Balance</div>
                    <form id="updateBalanceForm" action="${createLink(action: 'updateBalance')}">
                        <div class="row justify-content-around">
                            <div class="col-12 mb-2">
                                <input class="form-input" id="txnAmt" name="txnAmt" type="number" step="0.01" min="0" placeholder="0.00" required/>
                            </div>
                            <div class="col-12 mb-2">
                                <input class="form-input" id="details" name="details" type="text" placeholder="Details..."/>
                            </div>
                            <input type="hidden" name="id" value="${savingsInstance.id}" />
                            <input id="process" type="hidden" name="process" value="" />
                        </div>
                    </form>
                    <div class="row justify-content-around">
                        <button class="col-5" onclick="showAlertConfirm('updateBalanceForm','Adjust Balance?','Add')" style="background-color: #41D4A8; border: 2px solid #3B706D; border-radius: 10px; font-size: 20px !important;">Add</button>
                        <button class="col-5" onclick="showAlertConfirm('updateBalanceForm','Adjust Balance?','Deduct')" style="background-color: #F02C07; border: 2px solid #3B706D; border-radius: 10px; color: white; font-size: 20px !important;">Deduct</button>
                    </div>
                </div>
                <div class="col-12 mb-4 pb-3" style="border: 2px solid #C7EDF0;border-radius:10px; background-color: #FFFFFF">
                    <div style="color: #343C6A;font-size: 40px;">Send Balance</div>
                    <form id="transferBalanceForm" action="${createLink(action: 'transferBalance')}">
                        <div class="row justify-content-center">
                            <div class="col-12 mb-2">
                                <select id="debitSavingsID" name="debitSavingsID" class="form-select form-select-lg" aria-label="Default select example">
                                <option disabled selected>Select Savings Account</option>
                                <g:each var="savings" in="${savingsActiveList}">
                                    <option value=${savings.id}>${savings.acctName}</option>
                                </g:each>
                                </select>
                            </div>
                            <div class="col-12 mb-2">
                                <input class="form-input" id="txnAmt" name="txnAmt" type="number" step="0.01" min="0" placeholder="0.00" required/>
                            </div>
                            <div class="col-12 mb-2">
                                <input class="form-input" id="details" name="details" type="text" placeholder="Details..."/>
                            </div>
                            <input type="hidden" name="id" value="${savingsInstance.id}" />
                        </div>
                    </form>
                    <div class="row justify-content-around">
                        <button class="col-5" onclick="showAlertConfirm('transferBalanceForm','Transfer Balance?')" style="background-color: #41D4A8; border: 2px solid #3B706D; border-radius: 10px; font-size: 20px !important;">Transfer Balance</button>
                    </div>
                </div>
                <div class="col-12 mb-4 pb-3" style="border: 2px solid #C7EDF0;border-radius:10px; background-color: #FFFFFF">
                    <div style="color: #343C6A;font-size: 40px;">Other Actions</div>
                    <div class="row justify-content-center">
                        <button type="button" class="btn col-10 mb-2 py-0" data-toggle="modal" data-target="#configureInterestModal" style="background-color: #41D4A8; border: 2px solid #3B706D; border-radius: 10px; font-size: 20px; color: black">Configure Interest</button>
                        <form id="deleteForm" style="display:none" action="${createLink(action: 'delete')}">
                            <input type="hidden" name="id" value="${savingsInstance.id}" />
                        </form>  
                        <button type="button" class="btn col-10 mb-2 py-0" style="background-color: #F02C07; border: 2px solid #3B706D; border-radius: 10px; font-size: 20px; color: white" onclick="showAlertConfirm('deleteForm','Delete Account?','Add')">Delete Account</button>
                    </div>

                    <div class="modal fade" id="configureInterestModal" tabindex="-1" role="dialog" aria-labelledby="configureInterestModal" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <div class="modal-title" id="exampleModalLabel" style="font-size: 30px;">${savingsInstance.acctName} - Interest</div>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <g:if test="${savingsInterestInstance}">
                                    <g:form controller="savings" action="configureInterest">
                                        <input type="hidden" name="id" value="${savingsInstance.id}" />
                                        <input class="form-input mb-3" id="intRate" name="intRate" type="number" step="0.01" min="0" value="${savingsInterestInstance.interestRate}" required/>
                                        <select id="frequencyID" name="frequencyID" class="form-select form-select-lg" aria-label="Default select example">
                                        <option disabled selected>Select Interest Frequency</option>
                                        <g:each var="freq" in="${interestFreq}">
                                            <g:if test="${savingsInterestInstance.interestFrequency.id == freq.id}">
                                                <option selected value=${freq.id}>${freq.name}</option>
                                            </g:if>
                                            <g:else>
                                               <option value=${freq.id}>${freq.name}</option>
                                            </g:else>
                                        </g:each>
                                        </select>
                                        <g:submitButton class="col-12 my-3" name="Update" value="Update Interest" style="background-color: #41D4A8; border: 2px solid #3B706D; border-radius: 10px; font-size: 20px !important;"/>
                                    </g:form>                    
                                </g:if>
                                <g:else>
                                    <g:form controller="savings" action="configureInterest">
                                        <input type="hidden" name="id" value="${savingsInstance.id}" />
                                        <input class="form-input mb-3" id="intRate" name="intRate" type="number" step="0.01" min="0" placeholder="Annual Percentage %" required/>
                                        <select id="frequencyID" name="frequencyID" class="form-select form-select-lg" aria-label="Default select example">
                                        <option  disabled selected>Select Interest Frequency</option>
                                        <g:each var="freq" in="${interestFreq}">
                                            <option value=${freq.id}>${freq.name}</option>
                                        </g:each>
                                        </select>
                                        <g:submitButton class="col-12 my-3" name="Update" value="Update Interest" style="background-color: #41D4A8; border: 2px solid #3B706D; border-radius: 10px; font-size: 20px !important;"/>
                                    </g:form>
                                </g:else>
                            </div>
                        </div>
                    </div>
                    </div>
                </div>
                <div class="col-12">
                    <g:render template="/components/expense_chart"/>
                </div>
            </div>
        </div>
    </div>
</body>
</html>