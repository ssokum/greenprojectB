        window.onload = function() {
            let container = document.getElementById('map');
            let option = {
              center: new kakao.maps.LatLng(33.450701, 126.570667),
              level: 3
            };
            let map = new kakao.maps.Map(container, option);
        };
