<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Savings Expense Tracker</title>
</head>
<body>
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
                                    <div class="col-9 text-start" style="font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 600;font-style: normal;">Total Savings</div>
                                    <div class="col-2 align-self-end" style="color: #41D4A8; background-color: #FFFFFF; border-radius: 5px; font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 500;font-style: normal;">
                                        %
                                    </div>
                                </div>  
                                <div style="font-family: 'Inter', sans-serif;font-optical-sizing: auto;font-weight: 700;font-style: normal;font-size: 35px;">
                                    &#x20B1;<g:formatNumber number="${totalBalance}" format="#,##0.00" />
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
        <div class="row">
            <div class="col-12">
                <g:render template="/components/expense_chart"/>
            </div>
            <div class="col-11 my-3" >
                <g:render template="/components/recent_transactions"/>
            </div>
        </div>
    </div>
</div>
</body>
</html>
