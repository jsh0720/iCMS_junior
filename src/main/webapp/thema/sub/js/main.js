//배너 슬라이드
$(function() {
    var sliderAnimation = new SwiperAnimation();
    var mainSlider = new Swiper('#banSlider', {
        slidesPerView: 1.5,
        spaceBetween: 30,
        speed: 700,
        centeredSlides: false,
        setWrapperSize: true,
        loop: false,
        loopAdditionalSlides : 1,
        observer: true,
        observeParents: true,
        autoplay: {
            delay: 4000,
            disableOnInteraction: false,
        },
        navigation: {
            nextEl: ".banner_list_wrap .swiper-button-next",
            prevEl: ".banner_list_wrap .swiper-button-prev",
        },
        pagination: {
            el: ".banner_list_wrap .swiper-pagination",
            type: "progressbar",
        },
        breakpoints: {
            992: {
                slidesPerView: 2.5,
            },
        }
        
    });
});