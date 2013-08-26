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
<link href="<%=path%>/clive/css/operation.css" rel="stylesheet"
	type="text/css" />
<link href="<%=path%>/clive/css/jquery-ui.min.css" rel="stylesheet"
	type="text/css" />
<link href="<%=path%>/clive/css/jquery.mCustomScrollbar.css"
	rel="stylesheet" type="text/css" />
<style>
#feedback {
	font-size: 1.4em;
}

#channels .ui-selecting {
	background: #FECA40;
}

#channels .ui-selected {
	background: #F39814;
	color: white;
}

#channels {
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#channelsDiv {
	list-style-type: none;
	margin: 0;
	padding: 0;
	float: left;
	width: 17%;
	height: 700px;
}

#channels li {
	margin: 3px;
	padding: 0.4em;
	font-size: 1.4em;
	height: 18px;
}

#programs .ui-selecting {
	background: #FECA40;
}

#programs .ui-selected {
	background: #F39814;
	color: white;
}

#programs {
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#programsDiv {
	list-style-type: none;
	margin: 0;
	padding: 0;
	float: left;
	height: 700px;
	width: 48%;
}

#programs li {
	margin: 3px;
	padding: 0.4em;
	font-size: 1.4em;
	height: 18px;
}

#recommendDiv {
	list-style-type: none;
	margin: 0;
	padding: 0;
	float: left;
	height: 700px;
	width: 34%;
	visibility: hidden;
}

#tabs-recommend-1 {
	height: 637px;
}

#saveKeyWord,#removeKeyWord {
    background-color: #6495ED;
    background-image: -moz-linear-gradient(center top , rgba(255, 255, 255, 0) 100%, rgba(255, 255, 255, 0) 51%, rgba(255, 255, 255, 0.8) 50%, rgba(255, 255, 255, 0.2) 0%), url("../images/button_texture.png");
    border: 1px solid #42629D;
    border-radius: 15px 15px 15px 15px;
    box-shadow: 0 2px 0 rgba(0, 0, 0, 0.2), 0 1px 0 rgba(255, 255, 255, 0.5) inset;
    color: #FFFFFF;
    display: inline-block;
    font-size: 14px;
    line-height: 30px;
    margin: 6px 5px 10px 0;
    padding: 0 10px;
    text-decoration: none;
    text-shadow: 0 -1px 0 #000000;
    transition: background-color 0.2s linear 0s, box-shadow 0.2s linear 0s, text-shadow 0s ease 0s, all 0.2s linear 0s;
}

.flexigrid div.fbutton .delete {
    background: url(<%=path%>/clive/img/close.png) no-repeat center left;
}
</style>
<script src="<%=path%>/clive/js/jquery-1.9.1.js"></script>
<script src="<%=path%>/clive/js/jquery-ui.min.js"></script>
<script src="<%=path%>/clive/js/jquery.mCustomScrollbar.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/clive/css/flexigrid.css" />
<script type="text/javascript" src="<%=path%>/clive/js/flexigrid.js"></script>
<script type="text/javascript">

	$(function() {
		var selectEpgId;
		$("#tabs").tabs();
		$("#channels").selectable({
			stop : function() {
				$("#select-program").empty();
				$("#recommendDiv").css('visibility', 'hidden');
				var result = $("#select-channel").empty();
				$(".ui-selected", this).each(function() {
					result.append($(this).text() + "(" + $(this).attr('channelCode') + ")");
					selectEpgId = null;
					var $loading = $('<div class="loading" />');
					$('#programsDiv').append($loading);
					var href = "/smart-server/api/clive/program-list/" + $(this).attr('channelCode');
					$.ajax({
						url : href,
						cache : false,
						success : function(data) {
							$loading.remove();
							$('#programs').html(data);
							$('#programsDiv').mCustomScrollbar("update");
						},
						error : function(data) {
							$loading.remove();
						}
					});
				});
			}
		});
		
		$("#programs").selectable({
			stop : function() {
				var result = $("#select-program").empty();
				$(".ui-selected", this).each(function() {
					result.append($(this).text());
					selectEpgId = $(this).attr("epgId");
					$("#recommendDiv").css('visibility', 'visible');
					
					var $loading = $('<div class="loading" />');
					$('#recommendDiv').append($loading);
					var href = "/smart-server/api/clive/query-keyword/" + selectEpgId;
					$.ajax({
						url : href,
						cache : false,
						success : function(data) {
							$loading.remove();
							$('#keyWord').val(data);
						},
						error : function(data) {
							$loading.remove();
						}
					});
				});
			}
		});
		
		$("#saveKeyWord").bind('click',function(){
			var $loading = $('<div class="loading" />');
			$('#recommendDiv').append($loading);
			var href = "/smart-server/api/clive/save-keyword/" + selectEpgId + "?keyword=" + $('#keyWord').val();
			$.ajax({
				url : href,
				cache : false,
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				success : function(data) {
					$loading.remove();
				},
				error : function(data) {
					$loading.remove();
				}
			});
	    });
		
		$("#removeKeyWord").bind('click',function(){
			var $loading = $('<div class="loading" />');
			$('#recommendDiv').append($loading);
			var href = "/smart-server/api/clive/remove-keyword/" + selectEpgId;
			$.ajax({
				url : href,
				cache : false,
				success : function(data) {
					$loading.remove();
					$('#keyWord').val("");
				},
				error : function(data) {
					$loading.remove();
				}
			});
	    });
		
		$("#recommendDiv").tabs();
		$(".tabs-bottom .ui-tabs-nav, .tabs-bottom .ui-tabs-nav > *").removeClass("ui-corner-all ui-corner-top").addClass( "ui-corner-bottom" );
		$(".tabs-bottom .ui-tabs-nav").appendTo(".tabs-bottom");

		var $loading = $('<div class="loading" />');
		$('#channelsDiv').append($loading);
		var href = "/smart-server/api/clive/channel-list";
		$.ajax({
			url : href,
			cache : false,
			success : function(data) {
				$loading.remove();
				$('#channels').html(data);
				$('#channelsDiv').mCustomScrollbar();
			},
			error : function(data) {
				$loading.remove();
			}
		});
		$('#programsDiv').mCustomScrollbar();
		
        $("#keywordManageTable").flexigrid({
            url : '/smart-server/api/clive/list-keyword',
            dataType : 'json',
            colModel : [{
	            	display : '日期',
	                name : 'startDateStr',
	                width : 80,
	                sortable : true,
	                align : 'center'
                }, {
                    display : '开始时间',
                    name : 'startTimeStr',
                    width : 60,
                    sortable : true,
                    align : 'center'
                }, {
                    display : '结束时间',
                    name : 'endTimeStr',
                    width : 60,
                    sortable : true,
                    align : 'center'
                }, {
                    display : 'epgId',
                    name : 'epgId',
                    width : 130,
                    sortable : true,
                    align : 'center',
                    hide : true
                }, {
                    display : '频道码',
                    name : 'channelCode',
                    width : 120,
                    sortable : true,
                    align : 'center'
                }, {
                	display : '频道',
                    name : 'channelName',
                    width : 200,
                    sortable : true,
                    align : 'center'
                }, {
                    display : '节目',
                    name : 'programName',
                    width : 200,
                    sortable : true,
                    align : 'center'
                }, {
                    display : '关键字',
                    name : 'keyword',
                    width : 200,
                    sortable : true,
                    align : 'center',
            } ],
            buttons : [ {
                    name : 'Delete',
                    bclass : 'delete',
                    onpress : deleteKeywords
                }, {
                    separator : true
            } ],
            searchitems : [ {
	                display : '关键字',
	                name : 'keyword'
               	}, {
                    display : '频道',
                    name : 'channelName',
                    isdefault : true
            } ],
            sortname : "sortIso",
            sortorder : "asc",
            usepager : true,
            title : '运维管理',
            useRp : true,
            rp : 15,
            showTableToggleBtn : true,
            width : 750,
            height : 200
        });
        
        function deleteKeywords(com, grid) {
            if (com == 'Delete') {
            	if($('.trSelected', grid).length == 0){
            		return;
            	}
            	var $loading = $('<div class="loading" />');
    			$('#keywordManageTable').append($loading);
                $('.trSelected td:nth-child(4)', grid).each(function () {
					var href = "/smart-server/api/clive/remove-keyword/" + $(this).text();
	    			$.ajax({
	    				url : href,
	    				cache : false,
	    				success : function(data) {
	    					$loading.remove();
	    					$("#keywordManageTable").flexReload(); 
	    				},
	    				error : function(data) {
	    					$loading.remove();
	    					$("#keywordManageTable").flexReload(); 
	    				}
	    			});
				});
            }
        }
	});

</script>
<title>智播运维管理中心</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">推荐定制</a></li>
			<li><a href="#tabs-2">运维管理</a></li>
		</ul>
		<div id="tabs-1" style="height: 770px;">
			<p id="feedback">
				<span id="select-channel" style="color: red;font-weight: bold;"></span>&nbsp;&nbsp;&nbsp;&nbsp;<span id="select-program" style="color: blue;font-weight: bold;"></span>
			</p>
			<div id="channelsDiv">
				<ol id="channels"></ol>
			</div>
			<div id="programsDiv">
				<ol id="programs"></ol>
			</div>
			<div id="recommendDiv" class="tabs-bottom">
				<ul>
					<li><a href="#tabs-recommend-1">关键字</a></li>
				</ul>
				<div class="tabs-recommend-spacer"></div>
				<div id="tabs-recommend-1">
					关键字： <input id="keyWord" name="keyWord" type="text" />
					<input id="saveKeyWord" value="保存" type="button" />
					<input id="removeKeyWord" value="删除" type="button" />
				</div>
			</div>
		</div>
		<div id="tabs-2" style="height: 770px;">
			<table id="keywordManageTable" style="display:none"></table>
		</div>
	</div>
</body>
</html>
