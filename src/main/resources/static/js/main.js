(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner(0);


    // Fixed Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.sticky-top').addClass('shadow-sm').css('top', '0px');
        } else {
            $('.sticky-top').removeClass('shadow-sm').css('top', '-200px');
        }
    });


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
       /* $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');*/
       /* $("html, body").animate({ scrollTop: 0 }, "slow");*/ // 0.5초로 부드럽게
        $('html').stop().animate({ scrollTop: 0 }, 0, "slow"); //텀없이 부드럽게
        return false;
    });


    //가운테 슬라이드?
    // Latest-news-carousel
    $(".latest-news-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            },
            1200:{
                items:4
            }
        }
    });


    // What's New carousel
    //가운데 슬라이드
    $(".whats-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {//반응형으로 화면 크기에 따라 슬라이드에 보여질 아이템 수를 조정
            0: { items: 1 },       // 화면 너비 0px ~ 575px: 슬라이드 1개
            576: { items: 1 },     // 576px ~ 767px: 슬라이드 1개
            768: { items: 2 },     // 768px ~ 991px: 슬라이드 2개
            992: { items: 2 },     // 992px ~ 1199px: 슬라이드 2개
            1200: { items: 2 }     // 1200px 이상: 슬라이드 2개
        }
    });



    // Modal Video
    //모달 창을 통해 YouTube 동영상을 재생하고, 모달이 닫히면 정지시키는 스크립트(현재안쓰임)
    $(document).ready(function () {
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
       /* console.log($videoSrc); 광고수집기능 */

        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })
    });



})(jQuery);
