<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../../clive/css/style.css" type="text/css"
	media="screen" />
<link href="../../clive/css/stars.css" rel="stylesheet" type="text/css" />
<link href="../../clive/css/clive.css" rel="stylesheet" type="text/css" />
<link href="../../clive/css/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var isWeb = <c:out value='${web}'/>;
	var channel = "<c:out value='${channel}'/>";
	var size = <c:out value='${size}'/>;
	var type = 1;
	
	var TYPE_CURRENT_EPG = 1;
	var TYPE_PRODUCT = 2;
	var TYPE_NEWS = 3;
	var TYPE_BAIKE = 4;
	var TYPE_VOD = 5;
	var TYPE_FAVORATE = 6;
</script>
<title>Smart TV</title>
</head>
<body>

	<!-- 右下角电视区域 -->
	<div id="ps_tv" class="ps_tv"></div>
	
	<!-- 右下角电视区域下方的节目信息 -->
	<div id="tv_info" class="tv_info"><span id="source"><font color="white">&nbsp;&nbsp;<b id="programText">未知节目</b></font></span></div>
	
	<!-- 第二层显示区域 -->
	<div id="ps_detail"  class="ps_detail"></div>
	
	<!-- 上横栏左侧选中区域 -->
	<div id="selected1" class="selected1"></div>
	<!-- 上横栏左侧选中区域的中心小区域 -->
	<div id="selectedCenter1" class="selectedCenter1"></div>
	<!-- 上横栏右侧区域 -->
	<div id="notselected1" class="notselected1"></div>
	
	<div id="selected2" class="selected2"></div>
	<div id="selectedCenter2" class="selectedCenter2"></div>
	<div id="notselected2" class="notselected2"></div>
	
	<div id="selected3" class="selected3"></div>
	<div id="selectedCenter3" class="selectedCenter3"></div>
	<div id="notselected3" class="notselected3"></div>
	
	<div id="selected4" class="selected4"></div>
	<div id="selectedCenter4" class="selectedCenter4"></div>
	<div id="notselected4" class="notselected4"></div>
	
	<div id="selected5" class="selected5"></div>
	<div id="selectedCenter5" class="selectedCenter5"></div>
	<div id="notselected5" class="notselected5"></div>
	
	<div id="selected6" class="selected6"></div>
	<div id="selectedCenter6" class="selectedCenter6"></div>
	<div id="notselected6" class="notselected6"></div>
	
	<!-- 上横栏智播介绍 -->
	<div id="ps_intro" class="ps_intro">
	
		<h1 style="margin-left:50px; margin-top:20px; color:#FFFFFF; font-size:23px;">智播</h1>
		<h1 style="margin-left:50px; margin-top:0px; color:#FFFFFF; font-size:20px;"><pre>
在你观看节目的同时，帮助你更方便的:

1)了解节目里的人和事	2)找到节目相关的新闻和视频

3)了解节目相关的百科知识	4)发现节目里面感兴趣的物品和商品</pre></h1>
		<h1 style="margin-left:50px; margin-top:0px; color:#9CB6E3; font-size:15px;"><pre>智播V1.0				版权所有@2013-2020TCL保留所有权利 </pre></h1>
	</div>

	<!-- 下方详细信息区域 -->
	<div id="ps_info" class="ps_info"></div>
	
	<!-- 最下方提示区域 -->
	<div id="tips" class="tips">
		<span id="tips_span"><b id="tips_content"></b></span>
	</div>
	
	<!-- 左侧栏区域 -->
   	<div id="ps_type" class="ps_type">
	  <div id="ps_up" class="ps_up"></div>
		<div class="types">
			<div id="colee">
			<div id="colee1">
				<div class="cliveitem_1" id="tb_1">
					<div class="icon_1" id="icon_1"><b> 正在播</b></div>
				</div>
				<div class="cliveitem_2" id="tb_2">
					<div class="icon_2" id="icon_2"><b> 找物品</b> </div>
					</div>
				<div class="cliveitem_3" id="tb_3">
					<div class="icon_3" id="icon_3"><b> 看新闻</b></div>
					</div>
				<div class="cliveitem_4" id="tb_4">
					<div class="icon_4" id="icon_4"><b> 查百科</b></div>
					</div>
				<div class="cliveitem_5" id="tb_5">
					<div class="icon_5" id="icon_5"><b> 观影视</b></div>
					</div>
				<div class="cliveitem_6" id="tb_6">
					<div class="icon_6" id="icon_6">
					<font id="favor_num" class="favor_num">0</font>
					<br/>
					<b> 收藏夹</b></div>
					</div>
				</div>
			<div id="colee2" style="height:0px"></div>
			</div>
		 </div>
		 <div id="ps_down" class="ps_down"></div>
	</div>
	
	<!-- 上横栏区域 -->
	<div id="ps_slider" class="ps_slider">
		<a class="prev"></a>
		<a class="next"></a>
		<div id="ps_albums">
		</div>
	</div>
	
	<script src="../../clive/js/jquery.min.js"></script>
	<script src="../../clive/js/selType.js"> </script>
	<script src="../../clive/js/dynamicClive.js"></script>
</body>
<foot>
<script src="../../clive/js/jquery.mCustomScrollbar.min.js"></script>
</foot>
</html>
