//배너 슬라이드
$(function() {
    var sliderAnimation = new SwiperAnimation();
    var mainSlider = new Swiper('#banSlider', {
    	slidesPerView: 1,
        centeredSlides: true,
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
        on: {
            init: function () {
                //sliderAnimation.init(this).animate();
            },
            slideChange: function () {
                //sliderAnimation.init(this).animate();
            }
        }
    });
    
});