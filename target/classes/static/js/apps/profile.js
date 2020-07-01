$(function () {
	function showPH(){
		var table = $("<table id='PersonalHomepageTable' class='table'>");
			var tbody = $("<tbody>");
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-12 text-center PHP1'>" + storage['nickname'] + "的个人主页</td>");
					td.appendTo(tr);
				tr.appendTo(tbody);
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-4 PHP2'>此刻心情：</td>");
					td.appendTo(tr);
				tr.appendTo(tbody);
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-4 PHP2'>近况：</td>");
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				//获取用户名name的近况
				var data = "name=" + storage['name'];
				myAjax("post", "/user/UserRecentDevelopments", data, function(ajax){
					//获取响应数据
					var res = ajax.responseText.split("||||||");
					var type = JSON.parse(res[0]);
					var sentences = JSON.parse(res[1]);
					var reviews = JSON.parse(res[2]);
					var likes = JSON.parse(res[3]);
					
					//显示近况
					for(var i = 0, j = 0, k = 0; i < 10; i++){
						if(type[i] == 0) break;
						
						if(type[i] == 1){
							var tr = $("<tr class='row'>");
								var card = $("<div class='card shadow-sm row RD_sentence_card'>");
									var row = $("<div class='row'></div>");
										var col = $("<div class='col-sm-2'></div>");
											var img = $("<img class='img-thumbnail'src='" + storage['headPortrait'] + "' class='card-img' alt='" + storage['nickname'] + "' style='height:80px; width:80px;' />");
											img.appendTo(col);
										col.appendTo(row)
										
										var col = $("<div class='col-sm-10'></div>");
											var p = $("<p class='card-text'>" + storage['nickname'] + " 发布句子：</p>");
											p.appendTo(col);
											
											var a = $("<a href='javascript:void(0)'>");
												sentences[i]['content'] = sentences[i]['content'].split("\r\n");
												sentences[i]['content'] = sentences[i]['content'].join("<br/>");
												var font = $("<font color='black' style='line-height:200%; font-family:STZhongsong;'>" + sentences[i]['content'] + "</font>");
												font.appendTo(a);
											a.appendTo(col);
											
											var a = $("<a href='javascript:void(0)'>");
												var work;
												if(sentences[i].work === undefined){
													work = "原创";
												} else {
													work = "—— " + sentences[i].work.user.nickname + " 《" + sentences[i].work.name + "》";
												}
												var p = $("<p class='card-text p_work' align='right' style='font-size:12px;'>" + work + "</p>");
												p.appendTo(a);
											a.appendTo(col);
											
											var div = $("<div class='row form-inline' style='margin:0;'>");
												var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab" + (likes[i] ? "like" : "notlike") + "' id='like_sid_" + sentences[i].id + "'>");
													var img = $("<img alt='" + (likes[i] ? "已喜欢" : "未喜欢") + "' src='/img/喜欢(" + (likes[i] ? "已喜欢" : "未喜欢") + ").png' height='18px'>");
													img.appendTo(a);
													
													var small = $("<small>喜欢(" + sentences[i].numbersOfLike + ")</small>");
													small.appendTo(a);
												a.appendTo(div);
												
												var nbsp = $("<span style='white-space:pre;'>    </span>");
												nbsp.appendTo(div);
												
												var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab showSentenceSet' id='showSentenceSet" + sentences[i].id + "'>");
													var img = $("<img alt='加入句集' src='/img/加入句集.png' height='20px'>");
													img.appendTo(a);
													
													var small = $("<small>加入句集</small>");
													small.appendTo(a);
												a.appendTo(div);
												
												var nbsp = $("<span style='white-space:pre;'>    </span>");
												nbsp.appendTo(div);
												
												var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab review' id='review_sid_" + sentences[i].id + "'>");
													var img = $("<img alt='评论' src='/img/评论.png' height='20px'>");
													img.appendTo(a);
													
													var small = $("<small>评论</small>");
													small.appendTo(a);
												a.appendTo(div);
												
												var nbsp = $("<span style='white-space:pre;'>    </span>");
												nbsp.appendTo(div);
												
												var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab user'>");
													var img = $("<img alt='用户' src='/img/用户.png' height='20px'>");
													img.appendTo(a);
													
													var small = $("<small>" + sentences[i].user.nickname + "</small>");
													small.appendTo(a);
												a.appendTo(div);
												
												var p = $("<p class='flex-fill card-text text-right'>" + sentences[i]['publishDate'] + "</p>");
												p.appendTo(div);
											div.appendTo(col);
										col.appendTo(row)
									row.appendTo(card);
								card.appendTo(tr);
							tr.appendTo(tbody);
						}
					}
					ls.likeLength = $(".like").length;
					ls.notlikeLength = $(".notlike").length;
					ls.length = sentences.length;
				});
			tbody.appendTo(table);
		table.appendTo($("#PersonalHomepage"));
	}
	
	//顶部主要功能页面
	$("#majorNav").load("/juzimi/majorNav");
	$("#toSdiv").load("/juzimi/southStatement");
	$("#BasicDataDiv").load("/juzimi/basicData");
	
	//判断当前是否登录
	var storage = window.localStorage;
	
	showPH();
});