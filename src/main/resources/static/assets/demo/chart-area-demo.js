// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

// Area Chart Example
function SensorlineChart(type, sensors){
    let dates = [];
    let values = [];

      sensors.forEach(function(sensor) {
          dates.push(sensor.measureDatetime.split("T")[1].substring(0, 5));
//          let datetime = new Date(sensor.measureDatetime.split("T")[1].substring(0, 5));
//          dates.push(datetime.toLocaleTimeString()); // 중복 방지
          values.push(sensor[type]); // 동적 타입 지원
      });


    var ctx = document.getElementById("myAreaChart");
    var myLineChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: dates,
        datasets: [{
          label: type,
          lineTension: 0.3,
          backgroundColor: "rgba(2,117,216,0.2)",
          borderColor: "rgba(2,117,216,1)",
          pointRadius: 5,
          pointBackgroundColor: "rgba(2,117,216,1)",
          pointBorderColor: "rgba(255,255,255,0.8)",
          pointHoverRadius: 5,
          pointHoverBackgroundColor: "rgba(2,117,216,1)",
          pointHitRadius: 50,
          pointBorderWidth: 2,
          data: values,
        }],
      },
      options: {
        scales: {
          xAxes: [{
            time: {
              parser: 'HH:mm',     // 입력 데이터 시간 포맷
              tooltipFormat: 'HH:mm',  // 툴팁에 보여줄 포맷
              unit: 'minute',      // 눈금 단위 (hour, minute, day 등)
              displayFormats: {
                minute: 'HH:mm'    // 축 눈금 표시 포맷
              }
            },
            gridLines: {
              display: false
            },
            ticks: {
              maxTicksLimit: 6
            }
          }],
          yAxes: [{
            ticks: {
              min: 0,
              max: 6000,
              maxTicksLimit: 5
            },
            gridLines: {
              color: "rgba(0, 0, 0, .125)",
            }
          }],
        },
         tooltips: {
            mode: 'nearest', // 가장 가까운 포인트 하나만 표시
            intersect: true
          },
        legend: {
          display: false
        }
      }
    });
}
