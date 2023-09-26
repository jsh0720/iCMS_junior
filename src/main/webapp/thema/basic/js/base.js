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
        
        var domain = $(this).data("domain");
        
        if (value) {
            unifiedSearch(value, domain);
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

function unifiedSearch(value, domain) {
	
	$.ajax({
		url: "/web/selectSearchPage.do",
		type: "get",
		data: {dm_domain : domain, dm_page_type : "SEARCH"},
		success: function(data){
			if (data.result == "success") {
				location.href = '?contentId='+data.rows+'&search_value='+encodeURIComponent(value);
			} else {
				location.href= "/404_error";
			}
		}, error: function(request, status, error) {
            alert(request.responseJSON.notice);
        }
	});

}

$(function () {
    $('.unified-search-submit').on('click', function(e) {
        var $this = $(this),
            $wrap = $this.closest('.unified-search'),
            $text = $wrap.find('.unified-search-keyword'),
            value = $text.val();
        var domain = $(this).data("domain");
        if (value) {
            unifiedSearch(value,domain);
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

// #gnb
function submenu_hide() {
    $("#hd").removeClass("hd_zindex");
    $(".gnb_1dli").removeClass("gnb_1dli_over gnb_1dli_over2 gnb_1dli_on");
    $(".gnb_2dli").removeClass("gnb_2dli_over gnb_2dli_over2 gnb_2dli_on");
}
function menu_rearrange(el) {
    var width = $("#gnb_1dul").width();
    var left = w1 = w2 = 0;
    var idx = $(".gnb_1dli").index(el);
    var max_menu_count = 0;
    var $gnb_1dli;

    for(i=0; i<=idx; i++) {
        $gnb_1dli = $(".gnb_1dli:eq("+i+")");
        w1 = $gnb_1dli.outerWidth();

        if($gnb_1dli.find(".gnb_2dul").length)
            w2 = $gnb_1dli.find(".gnb_2dli > a").outerWidth(true);
        else
            w2 = w1;

        if((left + w2) > width) {
            if(max_menu_count == 0)
                max_menu_count = i + 1;
        }

        if(max_menu_count > 0 && (idx + 1) % max_menu_count == 0) {
            el.removeClass("gnb_1dli_over").addClass("gnb_1dli_over2");
            left = 0;
        } else {
            left += w1;
        }
    }
}
$(function() {
    var hide_menu = false;
    var mouse_event = false;
    var oldX = oldY = 0;

    $(document).mousemove(function(e) {
        if(oldX == 0) {
            oldX = e.pageX;
            oldY = e.pageY;
        }

        if(oldX != e.pageX || oldY != e.pageY) {
            mouse_event = true;
        }
    });

    // 주메뉴
    var $gnb = $(".gnb_1dli > a");
    $gnb.mouseover(function() {
        if(mouse_event) {
            $("#hd").addClass("hd_zindex");
            $(".gnb_1dli").removeClass("gnb_1dli_over gnb_1dli_over2 gnb_1dli_on");
            $(this).parent().addClass("gnb_1dli_over gnb_1dli_on");
            menu_rearrange($(this).parent());
            hide_menu = false;
        }
    });

    $gnb.mouseout(function() {
        hide_menu = true;
    });

    $(".gnb_2dli").mouseover(function() {
        hide_menu = false;
    });

    $(".gnb_2dli").mouseout(function() {
        hide_menu = true;
    });

    $gnb.focusin(function() {
        $("#hd").addClass("hd_zindex");
        $(".gnb_1dli").removeClass("gnb_1dli_over gnb_1dli_over2 gnb_1dli_on");
        $(this).parent().addClass("gnb_1dli_over gnb_1dli_on");
        menu_rearrange($(this).parent());
        hide_menu = false;
    });

    $gnb.focusout(function() {
        hide_menu = true;
    });

    $(".gnb_2da").focusin(function() {
        $(".gnb_1dli").removeClass("gnb_1dli_over gnb_1dli_over2 gnb_1dli_on");
        var $gnb_li = $(this).closest(".gnb_1dli").addClass("gnb_1dli_over gnb_1dli_on");
        menu_rearrange($(this).closest(".gnb_1dli"));
        hide_menu = false;
    });

    $(".gnb_2da").focusout(function() {
        hide_menu = true;
    });

    $('#gnb_1dul>li').bind('mouseleave',function(){
        submenu_hide();
    });

    $(document).bind('click focusin',function(){
        if(hide_menu) {
            submenu_hide();
        }
    });

    $('.gnb_2da').on('mouseenter', function() {
        $(".gnb_2dli").removeClass("gnb_2dli_over gnb_2dli_over2 gnb_2dli_on");
        $(this).parent().addClass("gnb_2dli_over gnb_2dli_on");
    });
    $('#gnb_2dul>li').on('mouseleave', function() {
        $(".gnb_2dli").removeClass("gnb_2dli_over gnb_2dli_over2 gnb_2dli_on");
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

$(function() {
    $('#vis #titler .menutab h3').on('click', function() {
        var $this = $(this),
            $that = $this.parent().next('ul');
            $parent = $this.parent();
        if ($that.is(':visible')) {
            $that.slideUp(300);
            $parent.removeClass("open");
        } else {
            $that.slideDown(300);
            $parent.addClass("open");
        }
    });
    $(window).resize(function() {
        $('#vis #titler .menutab ul').removeAttr('style');
    });
});

// back to top
$(function() {
    $('.totop').on('click', function() {
        $("html, body").animate({scrollTop: 0}, 300);
        return false;
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
            $('offcanvas-toggle').removeClass('is-open');
            $(target).removeClass('in');
            $('#off_backdrop').remove();
        } else {
        	$('offcanvas-toggle').addClass('is-open');
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
	
    if (comm == 'pw') { 
        if ($.trim($('#dm_id').val()) == "") {
            alert('아이디를 입력해주세요.');
            $('#dm_id').focus();
            return false;
        }
    }
    
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
    
    if ($.trim($("#dm_hp").val()) == "") {
    	alert("휴대폰 번호를 입력해주세요.");
    	$('#dm_hp').focus();
    	return false;
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
            	if (comm == 'id') {
            		setFindIdResult(data.row);
				} else {
					setFindPwResult(data.row);
				}
            } else {
                alert(data.notice);
            }
        }, error:function(request, status, error) {
            alert(request.responseJSON.notice);
        }

    });
    return false;
}

//비번 검색 결과 세팅
function setFindPwResult(data) {
	$(".mb_find_pw").hide();
	$("#findPwResult").show();
	var target = $("#findPwResult").children("section");
	target.empty();
	str = '<p><span class="highlighter">'+data+'<span></p>';
	target.append(str);
}
//아이디 검색 결과 세팅
function setFindIdResult(data) {
	$(".mb_find_id").hide();
	$("#findIdResult").show();
	var target = $("#findIdResult").children("section");
	target.empty();
	str = "";
	$.each(data,function(i, obj){
		str += '<p>'+obj+'</p>';
	});
	target.append(str);
	
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
	var content = '<div id="draggable_' + id + '" class="layer_popup ui-widget-content" name="layer_popup_' + id + '" style="min-width:300px;position:absolute;width:' + width + 'px;height:auto;overflow-x:hidden; overflow-y:hidden;left:' + x + 'px;top:' + y + 'px;z-index:9999;background-color: #ffffff; border: 0px solid #90C;">';
	content += '</div>';
	var newContent = $(content);
	newContent.appendTo("#layer_popup");
	$(newContent).load(link);
	$( ".ui-widget-content" ).draggable();
}

function parentRemove(type, dm_id) {
	$( '#layer_popup_' + dm_id ).remove();
}
