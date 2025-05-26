$(document).ready(function () {
    $(".nav_title").mouseenter(function () {
        $("#header").addClass("open"); // 마우스 오버 시 open 클래스 추가
    });

    $("#header").mouseleave(function () {
        $("#header").removeClass("open"); // #gnb 밖으로 나가면 open 클래스 제거
    });
});
