<div style="border: 2px solid #C7EDF0;border-radius:10px; padding:10px; background-color: #FFFFFF;">
  <div class="row ">
    <div class="col-8 ">
      <div style="color: #343C6A;font-size: 40px;">Recent Transactions</div>
    </div>
    <div class="col-4 text-end align-content-end">
      <g:link controller="log" action="index" style="color: #343C6A;font-size: 30px;">See All</g:link>
    </div>
  </div>
  <ul style="border-radius: 10px; list-style-type: none;">
  <g:each var="record" in="${recordList}">
    <li>
      <div class="row align-items-center">
        <div class="col-2">
          <g:if test="${record.log_type == 0}">
            <img src='${assetPath(src: "savings_transaction_icon.svg")}'/>
          </g:if>
          <g:else>
            <img src='${assetPath(src: "expense_transaction_icon.svg")}'/>
          </g:else>
        </div>
        <div class="col">
          <div class="row">
            <div class="col-12" style="color: #000000;font-size: 23px;">
              ${record.description}
            </div>
            <div class="col-12" style="color: #718EBF;font-size: 20px;">
              ${record.date}
            </div>
          </div>
        </div>
        <g:if test="${record.amount > 0}">
          <div class="col-4" style="color: #41D4A8;font-size: 30px;">
            +<g:formatNumber number="${record.amount}" format="#,##0.00" />
          </div>
        </g:if>
        <g:else>
          <div class="col-4" style="color: red;font-size: 30px;">
            <g:formatNumber number="${record.amount}" format="#,##0.00" />
          </div>
        </g:else>
      </div>
    </li>
  </g:each>
  </ul>
</div>