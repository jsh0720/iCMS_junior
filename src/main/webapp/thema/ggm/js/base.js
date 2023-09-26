// base
function base() {
    return;
}

// #hd
$(function() {
    // global search
    $('#sch_search').on('click', function() {
        var $this = $(this),
            $root = $('#hd_search'),
            $form = $root.find('form'),
            $that = $('.hd_button_search');
        if ($root.hasClass('is-open')) {
            $root.removeClass('is-open');
            $this.children('.fa').removeClass('fa-close').addClass('fa-search');
            $form.find('input[type="text"]').val('');
            $that.removeClass('active');
        } else {
            $root.addClass('is-open');
            $this.children('.fa').removeClass('fa-search').addClass('fa-close');
            $that.addClass('active');
            setTimeout(function() {
                $form.find('input[type="text"]').focus();
            }, 100);
        }
    });
    $('.hd_button_search').on('click', function() {
        $('#sch_search').trigger('click');
    });

    // delegate #hd_search to .unified-search
    $('#sch_submit').on('click', function(e) {
        var $this = $(this),
            $that = $('#sch_stx'),
            value = $that.val();
        if (value) {
            unifiedSearch(value);
        } else {
            alert("검색어를 입력하시기 바랍니다.");
        }
    });
    $('#sch_stx').on('keypress', function(e) {
        var $this = $(this),
            value = $this.val();
        if (value) {
            if (e.keyCode === 13){
                e.preventDefault();
                $('#sch_submit').trigger('click');
            }
        }
    });

    // global navbar
    $('#map_navbar').on('click', function() {
        var $this = $(this),
            $that = $('.hd_button_navbar'),
            $root = $('#hd_navbar');
        if ($root.hasClass('is-open')) {
            $root.removeClass('is-open');
            $this.children('.fa').removeClass('fa-close').addClass('fa-sitemap');
            $that.removeClass('active');
        } else {
            $root.addClass('is-open');
            $this.children('.fa').removeClass('fa-sitemap').addClass('fa-close');
            $that.addClass('active');
        }
    });
    $('.hd_button_navbar').on('click', function() {
        $('#map_navbar').trigger('click');
    });

    // gadget
    $('.hd_gadget_member').hover(
        function() {
            $('.hd_gadget_member_obj').stop().slideDown(300);
        },
        function() {
            $('.hd_gadget_member_obj').stop().slideUp(300);
        }
    ).on('mouseenter', function() {
        $(this).closest('.hd_gadget').find('.hd_gadget_search_obj').filter(':visible').prev('.hd_gadget_search_btn').trigger('click');
    });
    $('.hd_gadget_search_btn').on('click', function() {
        var $this = $(this),
            $that = $this.next('.hd_gadget_search_obj');
        if ($that.is(':visible')) {
            $that.fadeOut('fast', function() {
                $that.find('input:text').val();
            });
        } else {
            $that.fadeIn('fast', function() {
                $that.find('input:text').focus();
            });
        }
    });

    // family
    $('#ft_family').hover(
        function() {
            $(this).children('ul').stop().slideDown();
        },
        function() {
            $(this).children('ul').stop().slideUp();
        }
    );
    $('#ft_family > ul > li > a').on('click', function() {
        var $this = $(this),
            _href = $this.attr('href') || '';
        if (_href && _href.substr(0,1) !== '#') {
            window.open(_href);
        }
        return false;
    });
});

function unifiedSearch(value) {

  /*  $.ajax({
        url: '/adm/get_page.do',
        data: 'dm_domain=' + encodeURIComponent(value) +'&'
        dataType: 'json',
        success: function(data) {
            if (data.result == 'success') {
                location.href = '?contentId=0f94e9c7a3b2240858b15f776b9fac739dfe62d7aa09b1f683b84fdb0833e80c';
            }
        }
    });*/
	location.href = '?contentId=06ac1cf157950d1c7483eec804d80af3dec6d0c2909f9c38675a34e9cc840547&search_value='+encodeURIComponent(value);
}

$(function () {
    $('.unified-search-submit').on('click', function(e) {
        var $this = $(this),
            $wrap = $this.closest('.unified-search'),
            $text = $wrap.find('.unified-search-keyword'),
            value = $text.val();
        if (value) {
            unifiedSearch(value);
        } else {
        	alert("검색어를 입력하시기 바랍니다.");
        }
    });

    $('.unified-search-keyword').on('keypress', function(e){
        var $this = $(this),
            $wrap = $this.closest('.unified-search'),
            $sbmt = $wrap.find('.unified-search-submit'),
            value = $this.val();
        if (value) {
            if (e.keyCode === 13){
                e.preventDefault();
                $sbmt.trigger('click');
            }
        }
    });
});

//GNB
$(function() {
	
	$("#gnb").hover(function(){
	    $("#header").addClass("on");
    });
	$("#header").mouseleave(function(){
		$("#header").removeClass("on")
        $("#gnb > ul > li").removeClass("on")
	});
	
	$("#gnb > ul > li").hover(function(){
		alert(1);
	    $("#gnb > ul > li").removeClass("on")
	    $(this).addClass("on")
	});
	
	
	$(window).on('scroll', function() {
	    var btnTop = $(".totop");
	    
	    if ($(window).scrollTop() > 50){
	        btnTop.css({'opacity' : 1})
	    } else {
	        btnTop.css({'opacity' : 1})
	    }
	    
	});
});

//#off
$(function() {
	$('.off_3dul:not(:has("li"))').remove();
	$('.off_3dli_active')
		.closest('.off_2dli').addClass('off_2dli_active')
		.closest('.off_1dli').addClass('off_1dli_active');
    $('.off_1da').on('click', function() {
    	var $this = $(this),
            $root = $this.closest('#off_1dul'),
            $1dli = $root.find('.off_1dli'),
            $2dul = $root.find('.off_2dul'),
            $wrap = $this.parent('.off_1dli'),
            $that = $wrap.children('.off_2dul');
    	
        if ($that.length > 0) {
            if ($that.is(':visible')) {
                $that.slideUp(300);
                $wrap.removeClass('is-open');
            } else {
            	$1dli.removeClass('is-open');
            	$2dul.slideUp(300);
                $that.slideDown(300);
                $wrap.addClass('is-open');
            }
            return false;
        }
    });
    
    $('.off_1dli_active .off_1da').trigger('click');

    $('.off_2da').on('click', function() {
        var $this = $(this),
            // $root = $this.closest('#off_2dul'),
            // $2dli = $root.find('.off_2dli'),
            // $3dul = $root.find('.off_3dul'),
            $wrap = $this.parent('.off_2dli'),
            $that = $wrap.children('.off_3dul');

        /*if ($that.length > 0) {
            if ($that.is(':visible')) {
                $that.slideUp(300);
                $wrap.removeClass('is-open');
            } else {
                $that.slideDown(300);
                $wrap.addClass('is-open');
            }
            return false;
        }*/
    });
    $('.off_2dli_active .off_2da').trigger('click');
});

// #vis
$(function() {
    $('.menutab h3').on('click', function() {
        var $this = $(this),
            $that = $this.next('ul');
        	$parent = $this.parent('.dep');
        
        if ($parent.hasClass("open")) {
        	$parent.removeClass("open");
        } else {
        	$parent.addClass("open");
        }
    });
    $(window).resize(function() {
        $('.menutab ul').removeAttr('style');
    });
});


//메인 슬라이드
$(function() {
  var sliderAnimation = new SwiperAnimation();
  var mainSlider = new Swiper('#mainSlider', {
  	slidesPerView: 1,
      centeredSlides: true,
      setWrapperSize: true,
      loop: false,
      loopAdditionalSlides : 1,
      observer: true,
      observeParents: true,
      navigation: {
          nextEl: "#mainSlider .swiper-button-next",
          prevEl: "#mainSlider .swiper-button-prev",
      },
      autoplay: {
          delay: 4000,
          disableOnInteraction: false,
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

// back to top
$(function() {
    $('.totop').on('click', function() {
        $.fn.fullpage.moveTo(1).animate({ scrollTop: 0 }, '500');
    });
});

//scroll top bottom
$(window).on('scroll', function() {
    var ft = $("#ft");
    var btnTop = $("#ft_goto");
    var ftHeight = $("#ft").outerHeight();

    if ($(window).scrollTop() < $(document).height() - $(window).height() - ftHeight - 50) {
        btnTop.addClass('active');
    } else {
        btnTop.removeClass('active');
    }
    
    if ($(window).scrollTop() >= 100) {
        btnTop.addClass('fixed');
    }
    else {
        btnTop.removeClass('fixed');
    }

    var hd = $("#hd");
    var btnTop2 = $("#hd_wrap");
    var hdHeight = $("#hd").outerHeight();

    if ($(window).scrollTop() < $(document).height() - $(window).height() - ftHeight - 50) {
        hd.addClass('hd_active');
    } else {
        hd.removeClass('hd_active');
    }
    
    if ($(window).scrollTop() >= 100) {
        hd.addClass('hd_fixed');
    }
    else {
        hd.removeClass('hd_fixed');
    }
});

// scrolla
$(function() {
    $('[data-animate]').scrolla();
});

$(function() {
    $('.contentMenu > ul > li > a').on('click', function() {
        var $this = $(this),
            $that = $this.closest('li'),
            $next = $this.next('ul');

        $next.slideToggle(125, 'linear', function() {
            $that.toggleClass('on')
        });
        if ($next.length) {
            return false;
        }
    });
});

//offcanvas
$(function() {
    $('[data-toggle="offcanvas"]').on('click', function() {
        var $this = $(this),
            $body = $('body'),
            $back = $('<div id="off_backdrop"></div>'),
            target = $this.data('target');

        $back
            .addClass('modal-backdrop fade show')
            .css('z-index', '12')
            .on('click', function() {
                $this.trigger('click');
            });

        if (!target || target.indexOf('#') !== 0 || !target.substr(1)) return;

        if ($(target).hasClass('in')) {
            $(".offcanvas-toggle").removeClass('is-open');
            $(target).removeClass('in');
            $('#off_backdrop').remove();
        } else {
        	$(".offcanvas-toggle").addClass('is-open');
            $(target).addClass('in');
            $(target).after($back);
        }
    });
});


//Get Scrollbar Width
var DM_SCROLLBAR_WIDTH = 0;
$(function () {
    DM_SCROLLBAR_WIDTH = (function _getScrollbarWidth() {
        var scrollDiv = document.createElement('div');
        scrollDiv.className = 'dm-scrollbar-measure';
        document.body.appendChild(scrollDiv);
        var scrollbarWidth = scrollDiv.getBoundingClientRect().width - scrollDiv.clientWidth;
        document.body.removeChild(scrollDiv);
        return scrollbarWidth;
    }());
});
// 사이트맵
$(function() {
    var $sitemapToggle = $('[data-toggle="sitemap"]');
    var sitemapDuration = 400;
    var sitemapEasing = 'easeInOutCubic';
    $sitemapToggle.on('click', function() {
        var $this = $(this),
            $that = $('.dm-sitemap'),
            $body = $('body');
        if ($that.is(':visible')) {
            $that.stop().slideUp(sitemapDuration, sitemapEasing, function() {
                $body.removeClass('overflow-hidden').css('padding-right', '');
            });
        } else {
            $body.addClass('overflow-hidden').css('padding-right', DM_SCROLLBAR_WIDTH + 'px');
            $that.stop().slideDown(sitemapDuration, sitemapEasing);
        }
        return false;
    });
    $('.dm-sitemap ul').on('mouseleave', function() {
        var $this = $(this);
        $this.find('.is-active, .is-inactive').removeClass('is-active is-inactive');
    });
    $('.dm-sitemap ul > li').on('mouseenter', function() {
        var $this = $(this);
        $this.removeClass('is-inactive').addClass('is-active').siblings().removeClass('is-active').addClass('is-inactive');
    });
    $('.dm-sitemap-close').on('click', function () {
        $sitemapToggle.eq(0).trigger('click');
    });
});


//정보찾기
function fnFindInfo(comm) {
    if (comm == 'id') {
        if ($.trim($('#dm_name').val()) == "") {
            alert("이름을 입력해주세요.");
            $('#dm_name').focus();
            return false;
        }
        if ($.trim($("#dm_email").val()) == "") {
            alert("이메일을 입력해주세요.");
            $('#dm_email').focus();
            return false;
        }
    } else {
        if ($.trim($('#dm_id').val()) == "") {
            alert('아이디를 입력해주세요.');
            $('#dm_id').focus();
            return false;
        }
        if ($.trim($('#dm_email').val()) == "") {
            alert('이메일을 입력해주세요.');
            $('#dm_email').focus();
            return false;
        }
    }

    //$('input[name="command"]').val(comm);
    
    var form = $('#fm')[0];
    var formData = new FormData(form);
    $.ajax({
        url: '/web/selectMemberInfo.do',
        data: formData,
        type: 'POST',
        cache: false,
        async: false,
        contentType: false,
        processData: false,
        dataType: 'json',
        success: function(data) {
            if (data.result == 'success') {
            	alert(data.notice);
            	location.href="/index.do?contentId=bb93875fb9475c5b7218c891afea12da1d4888df92b6741b0c8f5a52977bd3ff";
            } else {
                alert(data.notice);
            }
        },
        error: function(xhr, status, error) {
        }
    });
    return false;
}

/********************************************
//pop open
*********************************************/
function openPopup(link, param, id, x, y, width, height, type, fit) {
	cookiedata = document.cookie;
	if(cookiedata.indexOf("popup_" + id + "=done") < 0) {
		if(type == "1")	{
			openPopupWindows(link, param, id, x, y, width, height, fit);
		} else {
			openPopupLayer(link, param, id, x, y, width, height, fit);
		}
	}	
}

function openPopupWindows(link, param, id, x, y, width, height, fit) {
	var windows_width = width;
	var windows_height = height;
	var screen_width = screen.availWidth;
	var screen_height = screen.availHeight;
	var open_x = x;
	var open_y = y;
	
	window.open(link + param, id, "status=yes, left=" + open_x + ",top=" + open_y + ",width=" + windows_width + ",height=" + windows_height);
}

function openPopupLayer(link, param, id, x, y, width, height, fit) {
	var content = '<div id="draggable_' + id + '" class="ui-widget-content" name="layer_popup_' + id + '" style="position:absolute;width:' + width + 'px;height:' + height + 'px;overflow-x:hidden; overflow-y:hidden;left:' + x + 'px;top:' + y + 'px;z-index:9999;background-color: #ffffff; border: 0px solid #90C;">';
	content += '</div>';
	var newContent = $(content);
	newContent.appendTo("body");
	$(newContent).load(link);
	$( ".ui-widget-content" ).draggable();
}

function setCookie(name, value, expirehours) {
	var todayDate = new Date();
	todayDate.setHours(todayDate.getHours() + expirehours);
	document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toUTCString() + ";"
}

function closeCooKie(popId, expired) {
	setCookie("popup_"+popId , "done" , expired);
}

function parentRemove(type, dm_id) {
	$( '#layer_popup_' + dm_id ).remove();
}
