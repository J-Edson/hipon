<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense</title>
</head>
<body>
    <div>
        Credited Asset: ${expenseInstance.creditAsset?.assetName}
    </div>
    <div>
        Transaction: ${expenseInstance.txnName}
    </div>
    <div>
        Amount: ${expenseInstance.txnAmt}
    </div>
    <g:link action="reverse" id="${expenseInstance.id}">Reverse Expense</g:link>
</body>
</html>