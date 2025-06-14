<html>
  <head>
    <script type="text/javascript">

      google.charts.load('current', {'packages':['bar']});
      google.charts.setOnLoadCallback(drawWeeklyChart);

      function drawWeeklyChart() {
      let weeklyActivity = document.getElementById('weeklyActivity').value
      let weeklyActivityArr = JSON.parse(weeklyActivity);
      weeklyActivityArr.unshift(['', 'Income', 'Expense']);
        var data = google.visualization.arrayToDataTable(weeklyActivityArr);
        var options = {
            chart: {
                title: 'Daily Activity'
            },
            titleTextStyle: {
                color: '#343C6A', 
                fontSize: 50,
                fontName: 'Dongle'
            },
            series: {
                0: { color: '#26E1D3' },
                1: { color: '#F02C07' }
            },
            bar: {groupWidth: "30%"},
            legend: { 
                textStyle: { 
                    fontSize: 23, 
                    fontName: 'Dongle'
                }
            },
            hAxis: {
                textStyle: {
                    fontSize: 20,
                    fontName: 'Dongle'
                }
            }
            
        };

        var chart = new google.charts.Bar(document.getElementById('activity_chart'));

        chart.draw(data, google.charts.Bar.convertOptions(options));
      }
    </script>
  </head>
  <body>
      <div id="activity_chart" style="width: 50vw; height: 32vh; border: 2px solid #C7EDF0;border-radius:10px; padding:10px; background-color: #FFFFFF"></div>
  </body>
</html>