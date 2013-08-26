<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../../films/css/style.css" type="text/css" media="screen" />
<link href="../../films/css/stars.css" rel="stylesheet" type="text/css" />
<title>Smart TV</title>
<style>
span.reference {
	position: fixed;
	top: 10px;
	right: 10px;
	font-size: 9px;
}

span.reference a {
	color: #aaa;
	text-decoration: none;
	text-transform: uppercase;
	margin-left: 10px;
}

span.reference a:hover {
	color: #ddd;
}

h1.title {
	text-indent: -9000px;
	background: transparent no-repeat top left;
	width: 640px;
	height: 52px;
	position: absolute;
	top: 15px;
	left: 15px;
}

body,a,a:visited {
	color: #666;
}
</style>
</head>
    <body onload="window.stub.setTVPlayPos(210,40,800,600);">
    	<div id="ps_tv" class="ps_tv"></div>
	    <div id="ps_info" class="ps_info">
	    	<h2></h2>
    		<div id="star_show" class="star clearfix">
    			<span id="star_num" class="allstar05"></span>
                <span id="rating_nums" class="rating_nums"></span>
            </div>
            <span id="reason"></span>
            <span id="loveTag"></span>
            <span id="source"></span>
            <span id="director"></span>
            <span id="starring"></span>
    		<span id="content"></span>
    	</div>
    	
		<div id="ps_slider" class="ps_slider">
			<a class="prev disabled"></a>
			<a class="next disabled"></a>
			<div id="ps_albums">
				<c:choose>
					<c:when test="${recommend==null}">
						失败：无法连接推荐引擎！
					</c:when>
					<c:when test="${recommend.success==true}">
		            	<c:forEach var="video" items="${recommend.objects}" varStatus="status">
		            		<div class="ps_album" style="opacity:0;"><div class="source-tag"></div><img id="${video.id}" src="${video.picUrl1}" reason="${video.reason}" loveTag="${video.loveTag}"/><div class="ps_desc"><h2>${video.titleEtc}</h2></div><div class="z-movie-playmask"></div></div>
		            	</c:forEach>
					</c:when>
					<c:otherwise>
						失败：<c:out value='${recommend.error.message}'/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

        <!-- The JavaScript -->
        <script src="../../films/js/jquery.min.js"></script>
		<script type="text/javascript">
			var colors      = new Array("#DAC4A3","#C9D896","#CCFFFF","#99CCCC","#FFFFCC","#FF9999","#993333","#CCCC66","#9999CC","#99CC99");
			var activeItemId;
			var showElems   = 6;
	        var navR,navL	= false;
			var busyMoving  = false;
			var first		= 1;
			var activeIndex = 1;
			var positions 	= {
				'0'		: 0,
				'1' 	: 170,
				'2' 	: 340,
				'3' 	: 510,
				'4' 	: 680,
				'5' 	: 850,
			}
			var $ps_albums 		= $('#ps_albums');
			var elems			= $ps_albums.children().length;
			var $ps_slider		= $('#ps_slider');
			var hiddenRight 	= $(window).width() - $ps_albums.offset().left;
			$ps_albums.children('div').css('left',hiddenRight + 'px');
			
			$ps_albums.children('div:lt(' + showElems + ')').each(
				function(i){
					var $elem = $(this);
					$elem.animate({'left': positions[i] + 'px','opacity':1},100,function(){
						if(elems > showElems)
							enableNavRight();
					});
				}
			);
				
			$ps_slider.find('.next').bind('click',function(){
				activeMoveRight();
			});
				
			function moveRightPage(){
				busyMoving = true;
				var hiddenLeft = $ps_albums.offset().left + 163;
				var cnt = 0;
				$ps_albums.children('div').slice(first-1,first+showElems-1).animate({'left': - hiddenLeft + 'px','opacity':0},100,function(){
					++cnt;
					if(cnt == showElems){
						var cnt2 = 0;
					 	$ps_albums.children('div').slice(first+showElems-1,first+showElems*2-1).each(
							function(i){
								var $elem = $(this);
								$elem.animate({'left': positions[i] + 'px','opacity':1},100,function(){
									++cnt2;
									if(cnt2 == showElems || first+showElems+i==elems){
									 	first = first + showElems;
									 	if(parseInt(first + showElems - 1) < elems)
											enableNavRight();
										enableNavLeft();
									 	busyMoving = false;
									}
								});
							}
						);
					}
				});
			}
			
			$ps_slider.find('.prev').bind('click',function(){
				activeMoveLeft();
			});

			function moveLeftPage(){
				busyMoving = true;
				var hiddenRight = $(window).width() - $ps_albums.offset().left;
				var cnt = 0;
				$ps_albums.children('div').slice(first-1,first+showElems-1).animate({'left': hiddenRight + 'px','opacity':0},100,function(){
					++cnt;
					if(cnt == showElems || first+cnt-1==elems){
						var cnt2 = 0;
					 	$ps_albums.children('div').slice(first-showElems-1,first-1).each(
							function(i){
								var $elem = $(this);
								$elem.animate({'left': positions[i] + 'px','opacity':1},100,function(){
									++cnt2;
									if(cnt2 == showElems){
									 	first = first - showElems;
										enableNavRight();
										if(first > 1)
											enableNavLeft();
									 	busyMoving = false;
									}
								});
							}
						);
					}
				});
			}
			
			function disableNavRight () {
				navR = false;
				$ps_slider.find('.next').addClass('disabled');
			}
			function disableNavLeft () {
				navL = false;
				$ps_slider.find('.prev').addClass('disabled');
			}
			function enableNavRight () {
				navR = true;
				$ps_slider.find('.next').removeClass('disabled');
			}
			function enableNavLeft () {
				navL = true;
				$ps_slider.find('.prev').removeClass('disabled');
			}		

			function activeMoveRight () {
				if(busyMoving || activeIndex==elems) return;
				deSelectMovie(activeIndex);
				activeIndex++;
				selectMovie(activeIndex);
				if(activeIndex==first+showElems){
					if(!$ps_albums.children('div:nth-child('+parseInt(first+showElems)+')').length || !navR) return;
					disableNavRight();
					disableNavLeft();
					moveRightPage();
				}
			}
			
			function activeMoveLeft () {
				if(busyMoving || activeIndex==1) return;
				deSelectMovie(activeIndex);
				activeIndex--;
				selectMovie(activeIndex);
				if(activeIndex==first-1){
					if(first==1  || !navL) return;
					disableNavRight();
					disableNavLeft();
					moveLeftPage();
				}
			}
			
			function behaviour () {
				if(busyMoving) return;
				if(activeItemId==null){
					return;
				}
				$ps_albums.children('div:nth-child('+activeIndex+')').fadeOut(180,function(){
					$ps_albums.children('div:nth-child('+activeIndex+')').fadeIn(180,function(){
						var href = "/smart-server/api/contract/behaviour?userId=" + <c:out value='${userId}'/> + "&itemId=" + activeItemId;
						$.ajax({
							url: href,
							cache: false,
							success: function(data){
								if(<c:out value='${web}'/>==false){
									window.stub.play(data.url);
								}
								else{
									window.location.href = data.url;
								}
							},
							error: function(data){
							}
						});
					});
				});
			}
			
			function back(){
				history.go(-1);
			}
			
			var showExLength = 80;
			var showPageLength = 185;
			var content = null;
			var contentDispIndex = 0;
			var isWholeDetail = true;
			function detail(){
				isWholeDetail = true;
				contentDispIndex = 0;
				var itemId = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('id');
				var href = "/smart-server/api/contract/queryItem?itemId=" + itemId;
				$.ajax({
					url: href,
					cache: false,
					success: function(data){
						var sourceStr = '暂无播放源';
						if(data.source=='youku'){
							sourceStr = '优酷网-最佳片源';
						}else if(data.source=='letv'){
							sourceStr = '乐视网';
						}else if(data.source=='m1905'){
							sourceStr = 'M1905电影网';
						}else if(data.source=='nearest'){
							sourceStr = '优酷网-热门匹配';
						}else if(data.source=='notfound'){
							sourceStr = '暂无播放源';
						}
						var reasonStr = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('reason');
						var loveTagStr = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('loveTag');
						var directorStr = data.directorEtc==null?"暂无信息":data.directorEtc;
						var starringStr = data.starringEtc==null?"暂无信息":data.starringEtc;
						$('#ps_info').find('h2').html('<b>'+data.title+'</b>');
						$ps_albums.children('div:nth-child('+activeIndex+')').children('div.source-tag').text(sourceStr);
						var ran = Math.random()*41;
						var score = (ran%9+1).toFixed(1);
						var r = (Math.floor(ran)%9+1)*5;
						var starCls = 'allstar' + r;
						$('#star_num').attr("class", starCls);
						$('#rating_nums').text(score);
						$('#reason').html('<font color="red"><b>推荐理由：</b>'+reasonStr+'</font><br>');
						$('#loveTag').html('<font color="red"><b>猜您喜欢：</b>'+loveTagStr+'</font><br>');
						$('#source').html('<font color="red"><b>来源：</b>'+sourceStr+'</font><br>');
						$('#director').html('<b>导演：</b>'+directorStr+'<br>');
						$('#starring').html('<b>演员：</b>'+starringStr+'<br>');
						content = data.content;
						if (content.length > showExLength) {
							$('#content').html('<b>简介：</b>'+content.substring(0, showExLength) + '...<font color="#649FC8">(更多)</font>');
						}
						else{
							$('#content').html('<b>简介：</b>'+content);
						}
						activeItemId = data._id;
					}
				});
			}
			
			function selectMovie(selectIndex){
				$ps_albums.children('div:nth-child('+selectIndex+')').css('background-color','#388E8E');
				$ps_albums.children('div:nth-child('+selectIndex+')').css('border','3px solid #565656');
				$ps_albums.children('div:nth-child('+selectIndex+')').children('div.z-movie-playmask').css('visibility', 'visible');
				$ps_albums.children('div:nth-child('+selectIndex+')').children('div.source-tag').css('visibility', 'visible');
				$ps_albums.children('div:nth-child('+selectIndex+')').find('.ps_desc').css('background-color','#388E8E');
				detail();
			}
			
			function deSelectMovie(selectIndex){
				$ps_albums.children('div:nth-child('+selectIndex+')').css('background-color','#F0ECF2');
				$ps_albums.children('div:nth-child('+selectIndex+')').css('border','1px solid #565656');
				$ps_albums.children('div:nth-child('+selectIndex+')').children('div.z-movie-playmask').css('visibility', 'hidden');
				$ps_albums.children('div:nth-child('+selectIndex+')').children('div.source-tag').css('visibility', 'hidden');
				$ps_albums.children('div:nth-child('+selectIndex+')').find('.ps_desc').css('background-color','#F0ECF2');
			}
			
			function nextDetail(){
				if(isWholeDetail){
					if (content.length <= showExLength) {
						return;
					}
					contentDispIndex=0;
					$('#reason').html("");
					$('#loveTag').html("");
					$('#source').html("");
					$('#director').html("");
					$('#starring').html("<font color='#649FC8'>(总体介绍)</font>...<br>");
					$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength));
					if (content.length > contentDispIndex + showPageLength) {
						$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength) + '...<br><font color="#649FC8">(更多)</font>');
					}
					else{
						$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength));
					}
					isWholeDetail = false;
				}
				else{
					if(content.length>contentDispIndex + showPageLength){
						contentDispIndex+=showPageLength;
						$('#reason').html("");
						$('#loveTag').html("");
						$('#source').html("");
						$('#director').html("");
						$('#starring').html("<font color='#649FC8'>(更多)</font>...<br>");
						$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength));
						if (content.length > contentDispIndex + showPageLength) {
							$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength) + '...<br><font color="#649FC8">(更多)</font>');
						}
						else{
							$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength));
						}
						isWholeDetail = false;
					}
				}
			}
					
			function lastDetail(){
				if(contentDispIndex>0){
					contentDispIndex-=showPageLength;
					if (content.length > contentDispIndex + showPageLength) {
						$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength) + '...<br><font color="#649FC8">(更多)</font>');
					}
					else{
						$('#content').html('<b>简介：</b>'+content.substring(contentDispIndex,contentDispIndex + showPageLength));
					}
				}
				else{
					contentDispIndex=0;
					var itemId = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('id');
					var href = "/smart-server/api/contract/queryItem?itemId=" + itemId;
					$.ajax({
						url: href,
						cache: false,
						success: function(data){
							var sourceStr = '暂无播放源';
							if(data.source=='youku'){
								sourceStr = '优酷网-最佳片源';
							}else if(data.source=='letv'){
								sourceStr = '乐视网';
							}else if(data.source=='m1905'){
								sourceStr = 'M1905电影网';
							}else if(data.source=='nearest'){
								sourceStr = '优酷网-热门匹配';
							}else if(data.source=='notfound'){
								sourceStr = '暂无播放源';
							}
							var reasonStr = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('reason');
							var loveTagStr = $ps_albums.children('div:nth-child('+activeIndex+')').find('img').attr('loveTag');
							var directorStr = data.directorEtc==null?"暂无信息":data.directorEtc;
							var starringStr = data.starringEtc==null?"暂无信息":data.starringEtc;
							$ps_albums.children('div:nth-child('+activeIndex+')').children('div.source-tag').text(sourceStr);
							$('#reason').html('<font color="red"><b>推荐理由：</b>'+reasonStr+'</font><br>');
							$('#loveTag').html('<font color="red"><b>猜您喜欢：</b>'+loveTagStr+'</font><br>');
							$('#source').html('<font color="red"><b>来源：</font></b>'+sourceStr+'</font><br>');
							$('#director').html('<b>导演：</b>'+directorStr+'<br>');
							$('#starring').html('<b>演员：</b>'+starringStr+'<br>');
							content = data.content;
							if (content.length > showExLength) {
								$('#content').html('<b>简介：</b>'+content.substring(0, showExLength) + '...<font color="#649FC8">(更多)</font>');
							}
							else{
								$('#content').html('<b>简介：</b>'+content);
							}
						}
					});
					isWholeDetail=true;
				}
			}
			
			if(elems > 0){
				selectMovie(parseInt(first));
			}

			if(<c:out value='${web}'/>==false){
				window.stub.setTVPlayPos(210,30,800,600);
			}
			else{
				document.onkeydown = function(evt)
				{
					key = window.event?evt.keyCode:evt.which; 
					if (key==37){
						activeMoveLeft();
						return false;
					}
					else if (key==39){
						activeMoveRight();
						return false;
					}
					else if (key==38){
						lastDetail();
						return false;
					}
					else if (key==40){
						nextDetail();
						return false;
					}
					else if (key==13){
						behaviour();
						return false;
					}
					return true;
				}
			}
        </script>
</body>
</html>