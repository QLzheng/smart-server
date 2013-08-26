/*0级别菜单 1二级菜单*/
var ui_levle = 0;
function moveRight () {
	busyMoving = true;
	var hiddenLeft 	= $ps_albums.offset().left + 163;
	$ps_albums.children('div:nth-child('+first+')').animate({'left': - hiddenLeft + 'px','opacity':0},5);
	var backElems = elems>first+showElems?showElems:elems-first;
	$ps_albums.children('div').slice(first,parseInt(first+backElems)).each(
		function(i){
			var $elem = $(this);
			$elem.animate({'left': positions[i] + 'px','opacity':1},5,function(){
				if(i == backElems-1){
					++first;
					busyMoving = false;
				}
			});
		}
	);		
}

function moveLeft () {
	busyMoving = true;
	var hiddenRight = $(window).width() - $ps_albums.offset().left;
	//show elems is not full
	if(elems<first+showElems-1){
		$ps_albums.children('div:nth-child('+parseInt(first-1)+')').animate({'left': positions[0] + 'px','opacity':1},5,function(){
			$ps_albums.children('div').slice(first-1,elems).each(
				function(i){
					var $elem = $(this);
					$elem.animate({'left': positions[i+1] + 'px'},5,function(){
						if(i == elems-first){
							--first;
							busyMoving = false;
						}
					});
				}
			);
		});
	}
	else{
		$ps_albums.children('div:nth-child('+parseInt(first-1)+')').animate({'left': positions[0] + 'px','opacity':1},5,function(){
			$ps_albums.children('div').slice(first-1,first+showElems-2).each(
				function(i){
					var $elem = $(this);
					$elem.animate({'left': positions[i+1] + 'px'},5,function(){
						if(i == showElems-2){
							$ps_albums.children('div:nth-child('+parseInt(first+showElems-1)+')').animate({'left': positions[showElems] + 'px','opacity':0},5,function(){
								--first;
								busyMoving = false;
							});
						}
					});
				}
			);
		});
	}
}

function disableNavRight () {
	navR = false;
	$ps_slider.find('.next').css('visibility', 'hidden');
}
function disableNavLeft () {
	navL = false;
	$ps_slider.find('.prev').css('visibility', 'hidden');
}
function enableNavRight () {
	navR = true;
	$ps_slider.find('.next').css('visibility', 'visible');
}
function enableNavLeft () {
	navL = true;
	$ps_slider.find('.prev').css('visibility', 'visible');
}		

function activeMoveRight () {
	if(busyMoving || activeIndex==elems) return;
	deSelectMovie(activeIndex);
	disableNavRight();
	disableNavLeft();
	moveRight();
	activeIndex++;
	if(activeIndex<elems)
		enableNavRight();
	enableNavLeft();
	selectMovie(activeIndex);
}

function activeMoveLeft () {
	if(busyMoving || activeIndex==1) return;
	deSelectMovie(activeIndex);
	disableNavRight();
	disableNavLeft();
	moveLeft();
	activeIndex--;
	enableNavRight();
	if(activeIndex > 1)
		enableNavLeft();
	selectMovie(activeIndex);
}

function behaviour () {
	if(busyMoving) return;
	if(activeItemId==null){
		return;
	}
	if(type==TYPE_VOD){
		$ps_albums.children('div:nth-child('+activeIndex+')').fadeOut(180,function(){
			$ps_albums.children('div:nth-child('+activeIndex+')').fadeIn(180,function(){
				var href = "/smart-server/api/clive/vod-behaviour/" + activeItemId + "?channel=" + channel;
				$.ajax({
					url: href,
					cache: false,
					success: function(data){
						if(isWeb==false){
							window.stub.play(data);
						}
						else{
							window.location.href = data;
						}
					},
					error: function(data){
					}
				});
			});
		});
	}
}

function back(){
	history.go(-1);
}

function detail(){
	var $loading 	= $('<div class="loading" />');
	$('#ps_info').append($loading);
	var href;
	var currentType = type;
	var itemId = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('id');
	if(itemId==undefined){
		$loading.remove();
		return;
	}
	if(type==TYPE_CURRENT_EPG){
		href = "/smart-server/api/clive/current-epg-info-html/" + channel;
	}
	else if(type==TYPE_PRODUCT){
		href = "/smart-server/api/clive/product-info-html/" + itemId;
	}
	else if(type==TYPE_NEWS){
		var class_type = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('class_type');
		href = "/smart-server/api/clive/news-info-html/" + itemId + "?class_type=" + class_type;
	}
	else if(type==TYPE_BAIKE){
		href = "/smart-server/api/clive/baike-info-html/" + itemId;
	}else if(type==TYPE_VOD){
		href = "/smart-server/api/clive/vod-info-html/" + itemId;
	}else if(type==TYPE_FAVORATE){
		href = "/smart-server/api/clive/product-info-html/" + itemId;
	}else{
		cacheDetail = '';
	}
	
	$.ajax({
		url: href,
		cache: false,
		success: function(data){
			$loading.remove();
			if(type!=currentType) return;
			$('#ps_info').html(data);
			activeItemId = itemId;
			cacheDetail = data;
		},
		error: function(data){
			$loading.remove();
			if(type!=currentType) return;
			activeItemId = itemId;
			cacheDetail = data;
		}
	});
}

function selectMovie(selectIndex){
	if(type==TYPE_VOD){
		$ps_albums.children('div:nth-child('+selectIndex+')').children('div.z-movie-playmask').css('visibility', 'visible');
	}
	else if(type==TYPE_FAVORATE){
		$ps_albums.children('div:nth-child('+selectIndex+')').children("#favorate-mask").css('background-image','url(../../clive/images/icon/favorate.png)');
	}
	$ps_albums.children('div:nth-child('+selectIndex+')').css('width','104px');
	$ps_albums.children('div:nth-child('+selectIndex+')').css('height','157px');
	$ps_albums.children('div:nth-child('+selectIndex+')').css('top','10px');
	$ps_albums.children('div:nth-child('+selectIndex+')').find('img:not(#favorate-mask)').css('width','102px');
	$ps_albums.children('div:nth-child('+selectIndex+')').find('img:not(#favorate-mask)').css('height','155px');
	detail();
}

function deSelectMovie(selectIndex){
	if(type==TYPE_VOD){
		$ps_albums.children('div:nth-child('+selectIndex+')').children('div.z-movie-playmask').css('visibility', 'hidden');
	}
	else if(type==TYPE_FAVORATE){
		$ps_albums.children('div:nth-child('+selectIndex+')').children("#favorate-mask").css('background-image','url(../../clive/images/icon/favorate_nohover.png)');
	}
	$ps_albums.children('div:nth-child('+selectIndex+')').css('width','91px');
	$ps_albums.children('div:nth-child('+selectIndex+')').css('height','137px');
	$ps_albums.children('div:nth-child('+selectIndex+')').css('top','26px');
	$ps_albums.children('div:nth-child('+selectIndex+')').find('img:not(#favorate-mask)').css('width','89px');
	$ps_albums.children('div:nth-child('+selectIndex+')').find('img:not(#favorate-mask)').css('height','135px');
}

function clearContent(){
	$('#ps_albums').html('');
	$('#ps_info').html('');
	cacheDetail = '';
}

function hiddenBackImgs(){
	$("#selected1").css('visibility', 'hidden');
	$("#selectedCenter1").css('visibility', 'hidden');
	$("#notselected1").css('visibility', 'hidden');
	
	$("#selected2").css('visibility', 'hidden');
	$("#selectedCenter2").css('visibility', 'hidden');
	$("#notselected2").css('visibility', 'hidden');
	
	$("#selected3").css('visibility', 'hidden');
	$("#selectedCenter3").css('visibility', 'hidden');
	$("#notselected3").css('visibility', 'hidden');
	
	$("#selected4").css('visibility', 'hidden');
	$("#selectedCenter4").css('visibility', 'hidden');
	$("#notselected4").css('visibility', 'hidden');
	
	$("#selected5").css('visibility', 'hidden');
	$("#selectedCenter5").css('visibility', 'hidden');
	$("#notselected5").css('visibility', 'hidden');
	
	$("#selected6").css('visibility', 'hidden');
	$("#selectedCenter6").css('visibility', 'hidden');
	$("#notselected6").css('visibility', 'hidden');
}

function switchType(toType){
	$("#ps_intro").css('visibility', 'hidden');
	var href = "/smart-server/api/clive/current-program-title/" + channel;
	$.ajax({
		url: href,
		cache: true,
		success: function(data){
			if(toType!=type) return;
			currentProgramName = data;
			$('#programText').text(currentProgramName);
		},
		error: function(data){
			if(toType!=type) return;
			$('#programText').text("无法获取当前节目");
		}
	});
	first		= 1;
	activeIndex = 1;
	disableNavRight();
	disableNavLeft();
	clearContent();
	var $loading 	= $('<div class="loading" />');
	if(toType==TYPE_CURRENT_EPG){
		$('#selectedCenter1').append($loading);
		href = "/smart-server/api/clive/current-epg-html/" + channel;
		hiddenBackImgs();
		$("#ps_intro").css('visibility', 'visible');
		$("#selected1").css('visibility', 'visible');
		$("#selectedCenter1").css('visibility', 'visible');
		$("#notselected1").css('visibility', 'visible');
		$("#ps_detail").css('background-image', 'url(../../clive/images/icon/detail_index.png)');
		$('#tips_content').html(tip_default_1);
	}
	else if(toType==TYPE_PRODUCT){
		$('#selectedCenter2').append($loading);
		href = "/smart-server/api/clive/recommend-products-html/" + channel + "?size=" + size;
		hiddenBackImgs();
		$("#selected2").css('visibility', 'visible');
		$("#selectedCenter2").css('visibility', 'visible');
		$("#notselected2").css('visibility', 'visible');
		$("#ps_detail").css('background-image', 'url(../../clive/images/icon/detail_product.png)');
		$('#tips_content').html(tip_product_1);
	}
	else if(toType==TYPE_NEWS){
		$('#selectedCenter3').append($loading);
		href = "/smart-server/api/clive/recommend-news-html/" + channel + "?size=" + size;
		hiddenBackImgs();
		$("#selected3").css('visibility', 'visible');
		$("#selectedCenter3").css('visibility', 'visible');
		$("#notselected3").css('visibility', 'visible');
		$("#ps_detail").css('background-image', 'url(../../clive/images/icon/detail_news.png)');
		$('#tips_content').html(tip_default_1);
	}
	else if(toType==TYPE_BAIKE){
		$('#selectedCenter4').append($loading);
		href = "/smart-server/api/clive/recommend-baikes-html/" + channel + "?size=" + size;
		hiddenBackImgs();
		$("#selected4").css('visibility', 'visible');
		$("#selectedCenter4").css('visibility', 'visible');
		$("#notselected4").css('visibility', 'visible');
		$("#ps_detail").css('background-image', 'url(../../clive/images/icon/detail_baike.png)');
		$('#tips_content').html(tip_default_1);
	}
	else if(toType==TYPE_VOD){
		$('#selectedCenter5').append($loading);
		href = "/smart-server/api/clive/recommend-vods-html/" + channel + "?size=" + size;
		hiddenBackImgs();
		$("#selected5").css('visibility', 'visible');
		$("#selectedCenter5").css('visibility', 'visible');
		$("#notselected5").css('visibility', 'visible');
		$("#ps_detail").css('background-image', 'url(../../clive/images/icon/detail_vod.png)');
		$('#tips_content').html(tip_vod);
	}
	else if(toType==TYPE_FAVORATE){
		$('#selectedCenter6').append($loading);
		href = "/smart-server/api/clive/product-show-favorates?channel=" + channel;
		hiddenBackImgs();
		$("#selected6").css('visibility', 'visible');
		$("#selectedCenter6").css('visibility', 'visible');
		$("#notselected6").css('visibility', 'visible');
		$("#ps_detail").css('background-image', 'url(../../clive/images/icon/detail_favorate.png)');
		$('#tips_content').html(tip_favorate_1);
	}
	else{
		return;
	}
	$.ajax({
		url: href,
		cache: true,
		success: function(data){
			$loading.remove();
			if(toType!=type) return;
			$('#ps_albums').html(data);
			elems = $ps_albums.children().length;
			$ps_albums.children('div').css('left',hiddenRight + 'px');
			$ps_albums.children('div:lt(' + showElems + ')').each(
				function(i){
					var $elem = $(this);
					$elem.animate({'left': positions[i] + 'px','opacity':1},5,function(){
						if(elems > showElems)
							enableNavRight();
					});
				}
			);
			if(elems > 0){
				selectMovie(parseInt(first));
			}
		},
		error: function(data){
			$loading.remove();
			if(toType!=type) return;
		}
	});
}

//up
function lastFunction(){
	if(type>1){
		type --;
	}
	else
	{
		type = focMax;
	}
	setTabFocus(type,0);
	switchType(type);
}

//down
function nextFunction(){
	if(type<focMax){
		type++;
	}
	else
	{
		type = 1;
	}
	setTabFocus(type,1);
	switchType(type);
}

function androidKey(key){
	/*1级别菜单*/
	if(0 == ui_levle){
		if (key=='21'){//左
			activeMoveLeft();
			return false;
		}
		else if (key=='22'){//右
			activeMoveRight();
			return false;
		}
		else if (key=='19'){//上
			lastFunction();
			return false;
		}
		else if (key=='20'){//下
			nextFunction();
			return false;
		}
		else if (key=='66'){//OK
			behaviour();
			if(TYPE_VOD!=type){
				showDetail();
			}
			return false;
		}
	}
	/*二级菜单*/
	else{
		if (key=='21'){//左
			return false;
		}
		else if (key=='22'){//右
			return false;
		}
		else if (key=='19'){//上
			detailMoveUp();
			return false;
		}
		else if (key=='20'){//下
			detailMoveDown();
			return false;
		}
		else if (key=='66'){//OK
			if(TYPE_VOD!=type){
				hidderDetail();
			}
			return false;
		}
	}
	
	if(key=='206'){//3d
		if(TYPE_PRODUCT==type){
			addFavorate();
			return false;
		}
		else if(TYPE_FAVORATE==type){
			removeFavorate();
			return false;
		}
	}
}

function showDetail()
{
	ui_levle = 1;
	$('#ps_detail').css('visibility', 'visible');
	$('#ps_detail').html(cacheDetail);
	$('#mapImg').css('visibility', 'visible');
	$('#productDes').css('display', 'block');
	$('#ps_detail').mCustomScrollbar({
	    callbacks:{
	    	onScrollStart:function(){
	    		startScroll();
	        },
			onScroll:function(){
				endScroll();
	        }
	    }
	});
	if(type==TYPE_PRODUCT){
		$('#tips_content').html(tip_product_2);
	}
	else if(type==TYPE_FAVORATE){
		$('#tips_content').html(tip_favorate_2);
	}
	else{
		$('#tips_content').html(tip_default_2);
	}
}

function hidderDetail()
{
	ui_levle = 0;
	$('#ps_detail').css('visibility', 'hidden');
	$('#mapImg').css('visibility', 'hidden');
	$('#productDes').css('display', 'none');
	cacheDetailScrollTo = 0;
	cacheDetailTopPct = 0;
	cacheDetailScrolling = false;
	if(type==TYPE_PRODUCT){
		$('#tips_content').html(tip_product_1);
	}
	else if(type==TYPE_FAVORATE){
		$('#tips_content').html(tip_favorate_1);
	}
	else if(type==TYPE_VOD){
		$('#tips_content').html(tip_vod);
	}
	else{
		$('#tips_content').html(tip_default_1);
	}
}

function startScroll(){
	cacheDetailScrolling = true;
	cacheDetailTopPct = mcs.topPct;
}

function endScroll(){
	 cacheDetailScrolling = false;
	 cacheDetailTopPct = mcs.topPct;
}

function detailMoveUp(){
	if(cacheDetailScrolling || cacheDetailTopPct==0) return;
	cacheDetailScrollTo -= 500;
	$('#ps_detail').mCustomScrollbar("scrollTo",cacheDetailScrollTo,{
		scrollInertia:250
	});
}

function detailMoveDown(){
	if(cacheDetailScrolling || cacheDetailTopPct==100) return;
	cacheDetailScrollTo += 500;
	$('#ps_detail').mCustomScrollbar("scrollTo",cacheDetailScrollTo,{
		scrollInertia:250
	});
}

function addFavorate(){
	if(TYPE_PRODUCT!=type) return;
	var href = "/smart-server/api/clive/product-add-favorate/" + activeItemId + "?channel=" + channel;
	$.ajax({
		url: href,
		cache: false,
		success: function(data){
			refreshFavorateNum();
			if(isWeb==false){
				window.stub.Toast("恭喜您收藏成功！");
			}
			else{
				alert("恭喜您收藏成功！");
			}
		},
		error: function(data){
			refreshFavorateNum();
			if(isWeb==false){
				window.stub.Toast("很遗憾收藏失败！");
			}
			else{
				alert("很遗憾收藏失败！");
			}
		}
	});
}

function removeFavorate(){
	if(TYPE_FAVORATE!=type) return;
	var href = "/smart-server/api/clive/product-remove-favorate/" + activeItemId + "?channel=" + channel;
	$.ajax({
		url: href,
		cache: false,
		success: function(data){
			refreshFavorateNum();
			switchType(TYPE_FAVORATE);
			hidderDetail()
			if(isWeb==false){
				window.stub.Toast("恭喜您取消收藏成功！");
			}
			else{
				alert("恭喜您取消收藏成功！");
			}
		},
		error: function(data){
			refreshFavorateNum();
			if(isWeb==false){
				window.stub.Toast("很遗憾取消收藏失败！");
			}
			else{
				alert("很遗憾取消收藏失败！");
			}
		}
	});
}

function refreshFavorateNum(){
	var href = "/smart-server/api/clive/count-favorate-size?channel=" + channel;
	$.ajax({
		url: href,
		cache: true,
		success: function(data){
			setFavorNum(data);
		},
		error: function(data){
			setFavorNum('?');
		}
	});
}

var colors      = new Array("#DAC4A3","#C9D896","#CCFFFF","#99CCCC","#FFFFCC","#FF9999","#993333","#CCCC66","#9999CC","#99CC99");
var activeItemId;
var showElems   = 6;
var navR,navL	= false;
var busyMoving  = false;
var first		= 1;
var activeIndex = 1;
var cacheDetail;
var cacheDetailScrollTo = 0;
var cacheDetailTopPct = 0;
var cacheDetailScrolling = false;
var positions 	= {
	'0'		: 110,
	'1' 	: 401,
	'2' 	: 540,
	'3' 	: 679,
	'4' 	: 818,
	'5' 	: 957,
	'6' 	: 1096,
}
var tip_default_1 = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">切换分类</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">左/右键</font>]<font color="#7c7c7c" size="4">切换内容</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">浏览更多详情</font>';
var tip_default_2 = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">翻页</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">退出详情</font>';
var tip_product_1 = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">切换分类</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">左/右键</font>]<font color="#7c7c7c" size="4">切换内容</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">浏览更多详情</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">3D键</font>]<font color="#7c7c7c" size="4">收藏该物品</font>';
var tip_product_2 = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">翻页</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">退出详情</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">3D键</font>]<font color="#7c7c7c" size="4">收藏该物品</font>';
var tip_favorate_1 = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">切换分类</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">左/右键</font>]<font color="#7c7c7c" size="4">切换内容</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">浏览更多详情</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">3D键</font>]<font color="#7c7c7c" size="4">取消收藏</font>';
var tip_favorate_2 = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">翻页</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">退出详情</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">3D键</font>]<font color="#7c7c7c" size="4">取消收藏</font>';
var tip_vod = '[<font color="#cccccc" size="4">上/下键</font>]<font color="#7c7c7c" size="4">切换分类</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">左/右键</font>]<font color="#7c7c7c" size="4">切换内容</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<font color="#cccccc" size="4">OK键</font>]<font color="#7c7c7c" size="4">直接播放</font>';
var currentProgramName = "";
var $ps_albums 		= $('#ps_albums');
var elems			= showElems;
var $ps_slider		= $('#ps_slider');
var hiddenRight 	= $(window).width() - $ps_albums.offset().left;
switchType(TYPE_CURRENT_EPG);
setTabFocus(type,0);
refreshFavorateNum();
if(isWeb){
	var $loading 	= $('<div class="loading" />');
	$('#ps_tv').append($loading);
}

$ps_slider.find('.next').bind('click',function(){
	activeMoveRight();
});
$ps_slider.find('.prev').bind('click',function(){
	activeMoveLeft();
});

if(isWeb==false){
	window.stub.setTVPlayPos(860,349,1050,650);
}
else{
	document.onkeydown = function(evt){
		/*1级别菜单*/
		key = window.event?evt.keyCode:evt.which; 
		if(0 == ui_levle){
			if (key==37){//左
				activeMoveLeft();
				return false;
			}
			else if (key==39){//右
				activeMoveRight();
				return false;
			}
			else if (key==38){//上
				lastFunction();
				return false;
			}
			else if (key==40){//下
				nextFunction();
				return false;
			}
			else if (key==13){//OK
				behaviour();
				if(TYPE_VOD!=type){
					showDetail();
				}
				return false;
			}
		}
		/*二级菜单*/
		else{
			if (key==37){//左
				return false;
			}
			else if (key==39){//右
				return false;
			}
			else if (key==38){//上
				detailMoveUp();
				return false;
			}
			else if (key==40){//下
				detailMoveDown();
				return false;
			}
			else if (key==13){//OK
				return false;
			}
			else if (key==27){//esc
				if(TYPE_VOD!=type){
					hidderDetail();
				}
				return false;
			}
		}
		
		if(key==32){//空格
			if(TYPE_PRODUCT==type){
				addFavorate();
				return false;
			}
			else if(TYPE_FAVORATE==type){
				removeFavorate();
				return false;
			}
		}
		
		return true;
	}
}
