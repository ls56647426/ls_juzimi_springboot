/*
 * 单击加入句集弹出句集窗口
 * 使用要求：
 * 	显示句集窗口标签：class = "showSentenceSet", id = "showSentenceSet${sentence.id}"
 * 	句集窗口:	div: id = "div_ShowSentenceSet";
 * 				div.table: id = "table_ShowSentenceSet"
 */
function getShowSentenceSet(){
	var sid;
	var ssid;
	var jss = {length:0};
	for(var i = 0; i < $("a.showSentenceSet").length; i++){
		$($("a.showSentenceSet")[i]).click(function(){
			//获取sid
			sid = $(this).attr("id").substring(15);
			//打开句集窗口
			$("#div_ShowSentenceSet").window("open");
			var data = "uid=" + 1 + "&sid=" + sid;		//由于当前是后台管理，没有用户id，则默认为id=1
			
			myAjax("post", "/sentence_set/ShowSentenceSetByUid", data, function(ajax){
				//获取响应数据
				var res = ajax.responseText.split("||||||");
				
				var list = eval(res[0]);		//句集列表
				
				res[1] = "[" + res[1] + "]";	//？不知道为什么一定要转为数组，才能被eval解析
				jss.flag = eval(res[1]);
				
				//创建表格显示句集
				$("#table_ShowSentenceSet").html("");		//先清空上次句集列表
				var table = $("<table>");
				table.appendTo($("#table_ShowSentenceSet"));
				for(var i = 0; i < list.length; i++){
					var tr = $("<tr></tr>");
					
					var td = $("<td></td>");
					var span = $("<span>" + list[i].name + "</span>")
					span.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td></td>");
					var a = $("<a href = 'javascript:void(0)' id = 'joinSentenceSet" + list[i].id + "' class = 'joinSentenceSet'>" + (jss.flag[0][list[i].id] == 0 ? "加入句集" : "已加入") + "</a>");
					a.appendTo(td);
					td.appendTo(tr);
					
					tr.appendTo(table);
				}
				
				$("#table_ShowSentenceSet").append("</table>");
				jss.length = list.length;		//！！！一定要放在建表的后面，否则下面length发生改变的方法被触发的时候，a.joinSentenceSet标签还没有创建
			});
	    });
	}
	//如果加入句集(jss)的长度(length)发生变化，则执行以下语句
	Object.defineProperty(jss, "length", {
		set: function(value) {		//length的值变为了value
			for(var i = 0; i < value; i++){
				$($("a.joinSentenceSet")[i]).click(function(){
					//获取ssid
					ssid = $(this).attr("id").substring(15);
					var data = "sid=" + sid + "&ssid=" + ssid;
					
					if(jss.flag[0][ssid] == 0){		//加入句集
						myAjax("post", "/join_sentence_set/JoinSentenceSetBySidAndSsid", data, function(ajax){
							//获取响应数据
							var status = ajax.responseText;
							if(status == 0){
								//更新showSentenceSet界面
								$("a#showSentenceSet" + sid).click();
							} else {
								//提示错误状态信息
								var res = getStatusResult(status);
								alert(res);
							}
						});
					}else{							//已加入(从句集中删除)
						myAjax("post", "/join_sentence_set/DeleteJoinSentenceSetBySidAndSsid", data, function(ajax){
							//获取响应数据
							var status = ajax.responseText;
							if(status == 0){
								//更新showSentenceSet界面
								$("a#showSentenceSet" + sid).click();
							} else {
								//提示错误状态信息
								var res = getStatusResult(status);
								alert(res);
							}
						});
					}
				});
			}
		}
	});
}
