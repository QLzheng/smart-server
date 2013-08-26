<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<script type="text/javascript">
	$(function() {
		$("#divAbout").dialog({
			autoOpen : false,
			modal : true
		});

		$("#about").click(function() {
			$("#divAbout").dialog("open");
		});
	});
</script>
<div id="header" style="background: #2E5166">
	<img style="margin-left: 5px; margin-top: 7px; margin-bottom: 5px;"
		src="<%=path%>/clive/images/logo.png" alt="智播运维平台" width="190" />
	<div
		style="float: right; margin-top: 20px; margin-right: 20px; text-align: right;">
		<span id="about"
			style="color: #AE5176; cursor: pointer; padding: 5px; font-size: medium;"><b>关于</b></span>
	</div>
</div>

<div id="divAbout"
	style="width:469px;padding-top:50px;height:227px;background:url(<%=path%>/clive/images/about.png) no-repeat;">
	<div style="font-size: 10pt; color: #a5a5a5; font-family: hei">
		<div style="margin: 5px;">
			软件版本: <font face="Arial">1.0</font>
		</div>
		<div style="margin: 5px;">TCL工业研究院 版权所有(C)(2013-2016)</div>
	</div>
</div>