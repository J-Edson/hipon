  <html>
  <head>
    <script type="text/javascript"> 

      google.charts.load('current', {'packages':['line']});
      google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
      let savingsBalanceHistory = document.getElementById('savingsBalanceHistory').value
      let savingsBalanceHistoryArr = JSON.parse(savingsBalanceHistory);
      savingsBalanceHistoryArr.unshift(['',  0]);
      var data = new google.visualization.DataTable();
      data.addColumn('string', '');
      data.addColumn('number', 'Balance');

      data.addRows(savingsBalanceHistoryArr);

      var options = {
        chart: {
          title: 'Balance History'
        },
        titleTextStyle: {
            color: '#343C6A', 
            fontSize: 50,
            fontName: 'Dongle'
        },
        series: {
            0: { 
              color: '#26E1D3',
              lineWidth: 10,
              pointsVisible: true, // Ensure points are always visible
              pointSize: 10
            }
        },
        legend: { 
          position: 'none'
        },
        hAxis: {
            textStyle: {
                fontSize: 20,
                fontName: 'Dongle'
            }
        }
      };

      var chart = new google.charts.Line(document.getElementById('balance_chart'));

      chart.draw(data, google.charts.Line.convertOptions(options));
    }
  </script>
  </head>
  <body>
    <g:hiddenField id ="savingsBalanceHistory" name="savingsBalanceHistory" value="${savingsBalanceHistory}" />
    <div id="balance_chart" style="width: 50vw; height: 32vh; border: 2px solid #C7EDF0;border-radius:10px; padding:10px; background-color: #FFFFFF;"></div>
  </body>
</html>