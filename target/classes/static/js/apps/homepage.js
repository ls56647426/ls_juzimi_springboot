function getConstellation(borthday){
	var res = new String();
	res += borthday.substring(2, 3) + "0后";
	
	var month = parseInt(borthday.substring(5, 7));
	var day = parseInt(borthday.substring(8));
	
	if(month == 1 && day >= 20 || month == 2 && day <= 18){
		res += "水瓶座";
	} else if(month == 2 || month == 3 && day <= 20){
		res += "双鱼座";
	} else if(month == 3 || month == 4 && day <= 20){
		res += "白羊座"
	} else if(month == 4 || month == 5 && day <= 20){
		res += "金牛座";
	} else if(month == 5 || month == 6 && day <= 21){
		res += "双子座";
	} else if(month == 6 || month == 7 && day <= 22){
		res += "巨蟹座";
	} else if(month == 7 || month == 8 && day <= 22){
		res += "狮子座";
	} else if(month == 8 || month == 9 && day <= 22){
		res += "处女座";
	} else if(month == 9 || month == 10 && day <= 22){
		res += "天秤座";
	} else if(month == 10 || month == 11 && day <= 21){
		res += "天蝎座";
	} else if(month == 11 || month == 12 && day <= 21){
		res += "射手座";
	} else {
		res += "摩羯座";
	}
	return res;
}

$(function () {
	
	function showMyTab(s1, s2){
		var data = "uname=" + name + "&upassword=" + password + "&start=" + start;
		var url;
		if(s1 == "#LatestRelease"){
			url = "/sentence/ShowSentenceSortByPublishDate";
		} else if(s1 == "#HotToday"){
			url = "/sentence/ShowSentenceSortByHotToday";
		} else if(s1 == "#MostPopular"){
			url = "/sentence/ShowSentenceSortByNumbersOfLike";
		} else {
			console.log("showMyTab参数错误！");
			return;
		}
		myAjax("post", url, data, function(ajax){
			//获取响应数据
			var res = ajax.responseText.split("||||||");
			var sentences = JSON.parse(res[0]);
			var likes = JSON.parse(res[1]);
			totalPages = parseInt(res[2]);
			
			//创建卡片显示句子
			$(s1).html("");
			for(i = 0; i < sentences.length; i++){
				var card = $("<div class='card shadow-sm my_sentence_card'>");
					var a = $("<a href='javascript:void(0)'>");
						sentences[i]['content'] = sentences[i]['content'].split("\r\n");
						sentences[i]['content'] = sentences[i]['content'].join("<br/>");
						var font = $("<font color='black' style='line-height:200%; font-family:STZhongsong;'>" + sentences[i]['content'] + "</font>");
						font.appendTo(a);
					a.appendTo(card);
					
					var a = $("<a href='javascript:void(0)'>");
						var work;
						if(sentences[i].work === undefined){
							work = "原创";
						} else {
							work = "—— " + sentences[i].work.user.nickname + " 《" + sentences[i].work.name + "》";
						}
						var p = $("<p class='card-text p_work' align='right'>" + work + "</p>");
						p.appendTo(a);
					a.appendTo(card);
					
					var div = $("<div class='row form-inline'>");
						var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab " + (likes[i] ? "like" : "notlike") + "' id='like_sid_" + sentences[i].id + "'>");
							var img = $("<img alt='" + (likes[i] ? "已喜欢" : "未喜欢") + "' src='/img/喜欢(" + (likes[i] ? "已喜欢" : "未喜欢") + ").png' height='18px'>");
							img.appendTo(a);
							
							var small = $("<small>喜欢(" + sentences[i].numbersOfLike + ")</small>");
							small.appendTo(a);
						a.appendTo(div);
						
						var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab showSentenceSet' id='showSentenceSet" + sentences[i].id + "'>");
							var img = $("<img alt='加入句集' src='/img/加入句集.png' height='20px'>");
							img.appendTo(a);
							
							var small = $("<small>加入句集</small>");
							small.appendTo(a);
						a.appendTo(div);
						
						var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab review' id='review_sid_" + sentences[i].id + "'>");
							var img = $("<img alt='评论' src='/img/评论.png' height='20px'>");
							img.appendTo(a);
							
							var small = $("<small>评论</small>");
							small.appendTo(a);
						a.appendTo(div);
						
						var a = $("<a href='javascript:void(0)' class='my_sentence_sub_tab user'>");
							var img = $("<img alt='用户' src='/img/用户.png' height='20px'>");
							img.appendTo(a);
							
							var small = $("<small>" + sentences[i].user.nickname + "</small>");
							small.appendTo(a);
						a.appendTo(div);
					div.appendTo(card);
				card.appendTo($(s1));
			}
			ls.likeLength = $(".like").length;
			ls.notlikeLength = $(".notlike").length;
			ls.length = sentences.length;
		});
		
		//底部分页栏显示
		var s2PageLink = s2 + " .page-link";
		if(totalPages < 5){							//总页数小于5，则多余项不可选中
			for(i = 1; i <= totalPages; i++){
				$($(s2PageLink)[i]).parent().removeClass("disabled");
				$($(s2PageLink)[i]).text(i);
			}
			for(i = totalPages + 1; i <= 5; i++){
				$($(s2PageLink)[i]).parent().addClass("disabled");
			}
		} else if(start < 2){						//当前页左侧不满2页，则当前页不在中间
			for(i = 1; i <= 5; i++){
				$($(s2PageLink)[i]).parent().removeClass("disabled");
				$($(s2PageLink)[i]).text(i);
			}
		} else if(start > totalPages - 3){			//当前页右侧不满2页，则当前页不在中间
			for(i = 1; i <= 5; i++){
				$($(s2PageLink)[i]).text(totalPages-5+i);
			}
		} else {									//当前页在中间
			for(i = 1; i <= 5; i++){
				$($(s2PageLink)[i]).parent().removeClass("disabled");
				$($(s2PageLink)[i]).text(start-2+i);
			}
		}
		
		$($(s2PageLink)[0]).parent().removeClass("active");
		for(i = 1; i <= 5; i++){
			if(start + 1 == $($(s2PageLink)[i]).text()){
				$($(s2PageLink)[i]).parent().addClass("active");
			} else{
				$($(s2PageLink)[i]).parent().removeClass("active");
			}
		}
		$($(s2PageLink)[6]).parent().removeClass("active");
	}
	
	//顶部主要功能页面
	$("#majorNav").load("/juzimi/majorNav");
	$("#toSdiv").load("/juzimi/southStatement");
	$("#BasicDataDiv").load("/juzimi/basicData");
	
	//判断当前是否登录
	var storage = window.localStorage;
	
	// 获取localStorage数据
	var name = storage["name"];
	var password = storage["password"];
	if(name && password){ // 已登录
		$('#NotLogin').css({'display': 'none'});
		$('#AlreadyLogin').css({'display': 'block'});
	}else{ // 未登录
		$('#NotLogin').css({'display': 'block'});
		$('#AlreadyLogin').css({'display': 'none'});
	}
	
	// 登录
	$('#login').on('click', function () {
		var data = "name=" + $('#name').val() + "&password=" + $('#password').val();
		myAjax("post", "/user/UserLogin", data, function(ajax){
			//获取响应数据
			var res = ajax.responseText;
			var user = JSON.parse(res);
			
			if(user){
				//数据接收
				storage['name'] = user.name;
				storage['password'] = user.password;
				storage['headPortrait'] = user.headPortrait;
				storage['nickname'] = user.nickname;
				storage['profile'] = user.profile;
				storage['sex'] = user.sex;
				storage['borthday'] = user.borthday;
				storage['mobile'] = user.mobile;
				
				window.location.reload();
			}
		});
	});
	
	//快速注册
	var registerStatus = {registerBtnFlag:false};
	$("#register").click(function(){
		//创建模态框
		$('#myModal .modal-content').width('400px');
		$('#myModal h5').text('快速注册');
		$("#myModalBody").html("");
		$('#myModal').modal('show');

		var table = $("<table id='registerTable' class='table table-hover'>");
			var tbody = $("<tbody>");
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>账号：</td>");
						var font = $("<font color='red'>*</font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='text' id='registerName' style='width:100%;' />")
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>密码：</td>");
						var font = $("<font color='red'>*</font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='password' id='registerPassword' style='width:100%;' />")
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>昵称：</td>");
						var font = $("<font color='red'>*</font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='text' id='registerNickname' style='width:100%;' />")
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>简介：</td>");
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='text' id='registerProfile' style='width:100%;' />")
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>性别：</td>");
						var font = $("<font color='red'>*</font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var formCheck = $("<div class='form-check form-check-inline'>")
							var input = $("<input class='form-check-input' type='radio' name='registerSex' id='registerMale' checked />");
							input.appendTo(formCheck);
							
							var label = $("<label class='form-check-label' for='registerMale'>男</label>")
							label.appendTo(formCheck);
						formCheck.appendTo(td);
						
						var formCheck = $("<div class='form-check form-check-inline'>")
							var input = $("<input class='form-check-input' type='radio' name='registerSex' id='registerFemale'>");
							input.appendTo(formCheck);
							
							var label = $("<label class='form-check-label' for='registerFemale'>女</label>")
							label.appendTo(formCheck);
						formCheck.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>出生日期：</td>");
						var font = $("<font color='red'>*</font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='date' id='registerBorthday' style='width:100%;' />")
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>手机号：</td>");
						var font = $("<font color='red'>*</font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='text' id='registerMobile' style='width:100%;' />")
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var button = $("<button type='button' id='registerBtn' class='btn btn-outline-primary btn-block'>注册</button>");
				button.appendTo(tbody);
			tbody.appendTo(table);
		table.appendTo($("#myModalBody"));
		
		registerStatus.registerBtnFlag = true;
	});
	
	//快速注册按钮创建后，创建下面的click方法
	Object.defineProperty(registerStatus, "registerBtnFlag", {
		set: function(value) {
			$("#registerBtn").click(function(){
				var data = "name=" + $('#registerName').val() + "&password=" + $('#registerPassword').val() + "&nickname=" + $("#registerNickname").val() +
					"&profile=" + $("#registerProfile").val() + "&sex=" + $("label[for='" + $("input[name='registerSex']:checked").attr("id") + "']").text() +
					"&borthday=" + $("#registerBorthday").val() + "&mobile=" + $("#registerMobile").val();
				myAjax("post", "/user/UserRegister", data, function(ajax){
					//获取响应数据
					var status = ajax.responseText;
					if(status == 0){
						//注册成功
						alert("注册成功！");
						$('#myModal').modal('hide');
					} else {
						//提示错误状态信息
						var res = getStatusResult(status);
						alert(res);
					}
				});
			});
		}
	});
	
	
	//获取最新发布的句子，并以卡片的形式显示在最新发布面板中
	var ls = {_likeLength:0, _notlikeLength:0, length:0};
	var jss = {length:0};
	var start = 0;
	var totalPages = 0;
	$("#lr").click(function(){
		start = 0;
		showMyTab("#LatestRelease", "#myTabNav");
	});
	$("#ht").click(function(){
		start = 0;
		showMyTab("#HotToday", "#myTabNav");
	});
	$("#mp").click(function(){
		start = 0;
		showMyTab("#MostPopular", "#myTabNav");
	});
	
	//取消喜欢句子
	Object.defineProperty(ls, "likeLength", {
		set: function(value) {
			this._likeLength = value;
			for(var i = 0; i < value; i++){
				$($(".like")[i]).click(function(){
					var a = $(this);
					var sid = a.attr("id").substring(9);
					var data = "uname=" + name + "&upassword=" + password + "&sid=" + sid;
					myAjax("post", "/like/DeleteLikeSentenceByUnameAndUpwdAndSid", data, function(ajax){
						var status = ajax.responseText;
						
						if(status == 0){
							a.attr("class", "my_sentence_sub_tab notlike");
							a.unbind("click");
							
							a.children('img').attr("alt", "喜欢(未喜欢)");
							a.children('img').attr("src", "/img/喜欢(未喜欢).png")
							var tmp = parseInt(a.children('small').text().substring(3, 4)) - 1;
							a.children('small').text("喜欢(" + tmp + ")");
							
							ls.likeLength--;
							ls.notlikeLength++;
						} else {
							console.log("取消喜欢失败！");
						}
					});
				});
			}
		},
		get: function(){
			return this._likeLength
		}
	});
	
	//喜欢句子
	Object.defineProperty(ls, "notlikeLength", {
		set: function(value) {
			this._notlikeLength = value;
			for(var i = 0; i < value; i++){
				$($(".notlike")[i]).click(function(){
					//获取sid
					var a = $(this);
					var sid = a.attr("id").substring(9);
					var data = "uname=" + name + "&upassword=" + password + "&sid=" + sid;
					myAjax("post", "/like/LikeSentenceByUnameAndUpwdAndSid", data, function(ajax){
						var status = ajax.responseText;
						
						if(status == 0){
							a.attr("class", "my_sentence_sub_tab like");
							a.unbind("click");
							
							a.children('img').attr("alt", "喜欢(已喜欢)");
							a.children('img').attr("src", "/img/喜欢(已喜欢).png")
							var tmp = parseInt(a.children('small').text().substring(3, 4)) + 1;
							a.children('small').text("喜欢(" + tmp + ")");
							
							ls.likeLength++;
							ls.notlikeLength--;
						} else if(status == -1){
							console.log("尚未登陆！");
							$("#login1").click();		//顶部主要功能的登录按钮
						} else {
							console.log("喜欢失败！");
						}
					});
				});
			}
		},
		get: function(){
			return this._notlikeLength
		}
	});
	
	var sid;
	var ssid;
	//显示加入句集模态框
	Object.defineProperty(ls, "length", {
		set: function(value) {
			for(var i = 0; i < value; i++){
				$($(".showSentenceSet")[i]).click(function(){
					//获取sid
					sid = $(this).attr("id").substring(15);
					
					//打开模态框
					$('#myModal').modal('show');
					$('#myModal h5').text('加入句集');
					var data = "uname=" + name + "&upassword=" + password + "&sid=" + sid;
					
					myAjax("post", "/sentence_set/ShowSentenceSetByUnameAndUpwdAndSid", data, function(ajax){
						//获取响应数据
						var status = ajax.responseText;
						
						if(status == -1){
							console.log("尚未登录！");
							$("#login1").click();		//顶部主要功能的登录按钮
						} else {
							var res = status.split("||||||");
							
							if(res.length == 2){
								var list = eval(res[0]);		//句集列表
								
								res[1] = "[" + res[1] + "]";	//？不知道为什么一定要转为数组，才能被eval解析
								jss.flag = eval(res[1]);
								
								//创建表格显示句集
								$("#myModalBody").html("");		//先清空上次句集列表
								$('#myModal .modal-content').width('400px');
								var table = $("<table id='showSentenceSetTable' class='table table-hover'>");
								table.appendTo($("#myModalBody"));
								var tbody = $("<tbody>");
								tbody.appendTo(table);
								if(list.length != 0){
									for(var i = 0; i < list.length; i++){
										var tr = $("<tr class='row'>");
										
										var td = $("<td class='col-sm-6'>");
										var span = $("<span data-toggle='tooltip' data-placement='top' title='" + list[i].descriptions + "'>" + list[i].name + "</span>")
										span.appendTo(td);
										td.appendTo(tr);
										
										var td = $("<td class='col-sm-6 text-right'>");
										var a = $("<a href = 'javascript:void(0)' id = 'joinSentenceSet" + list[i].id + "' class = 'joinSentenceSet'>" + (jss.flag[0][list[i].id] == 0 ? "加入句集" : "已加入") + "</a>");
										a.appendTo(td);
										td.appendTo(tr);
										
										tr.appendTo(tbody);
									}
								} else {
									var tr = $("<tr class='row'>");
									var td = $("<td class='col-sm-12 pull-center text-center'>空空如也，去创建一个句集吧！</td>");
									td.appendTo(tr);
									tr.appendTo(tbody);
								}
								
								var btn = $("<button type='button' id='publishSentenceSet' class='btn btn-outline-primary btn-block'>新建句集</a>")
								btn.appendTo($("#myModalBody"));
								
								jss.length = list.length;		//！！！一定要放在建表的后面，否则下面length发生改变的方法被触发的时候，a.joinSentenceSet标签还没有创建
							} else {
								console.log("显示句集失败！");
							}//判断res.length
						}//判断status
					});//myAjax
				});//$($(".showSentenceSet")[i]).click
			}//for
		}//set
	});//Object.defineProperty(ls, "length", {});
	
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
								$("#showSentenceSet" + sid).click();
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
								$("#showSentenceSet" + sid).click();
							} else {
								//提示错误状态信息
								var res = getStatusResult(status);
								alert(res);
							}
						});
					}
				});
			}
			
			$("#publishSentenceSet").click(function(){
				//切换模态框为新建句集窗口
				$("#myModalBody").html("");		//先清空上次句集列表
				$('#myModal h5').text('新建句集');
				$('#myModal .modal-content').width('600px');
				
				var table = $("<table id='publishSentenceSetTable' class='table table-hover'>");
					var tbody = $("<tbody>");
						var tr = $("<tr class='row'>");
							var td = $("<td class='col-sm-3'>句集名*</td>");
							td.appendTo(tr);
							
							var td = $("<td class='col-sm-9'>");
								var input = $("<input type='text' id='sentenceSetName' style='width:400px;' />");
								input.appendTo(td);
							td.appendTo(tr);
						tr.appendTo(tbody);
						
						var tr = $("<tr class='row'>");
							var td = $("<td class='col-sm-3'>句集描述</td>");
							td.appendTo(tr);
							
							var td = $("<td class='col-sm-9'>");
								var textarea = $("<textarea id='sentenceSetDescriptions' style='height:200px; width:400px;' />");
								textarea.appendTo(td);
							td.appendTo(tr);
						tr.appendTo(tbody);
						
						var button = $("<button type='button' id='publishSentenceSetBtn' class='btn btn-outline-primary btn-block'>确定</button>");
						button.appendTo(tbody);
					tbody.appendTo(table);
				table.appendTo($("#myModalBody"));
				
				jss.publishSentenceSetBtnFlag = true;
			});
		}
	});
	
	//加入句集按钮创建后，创建下面的click方法
	Object.defineProperty(jss, "publishSentenceSetBtnFlag", {
		set: function(value) {
			$("#publishSentenceSetBtn").click(function(){
				var data = "uname=" + name + "&upassword=" + password + "&sentenceSetName=" + $("#sentenceSetName").val() + "&sentenceSetDescriptions=" + $("#sentenceSetDescriptions").val();
				alert(data);
				myAjax("post", "/sentence_set/PublishSentenceSetByUnameAndUpwdAndSsnAndSsd", data, function(ajax){
					//获取响应数据
					var status = ajax.responseText;
					if(status == 0){
						//更新showSentenceSet界面
						$("#showSentenceSet" + sid).click();
					} else {
						//提示错误状态信息
						var res = getStatusResult(status);
						alert(res);
					}
				});
			});
		}
	});
	
	//分页栏
	$($("#myTabNav .page-link")[0]).click(function(){
		if(start == 0){
			console.log("已经在第一页，前面没有页了！");
			return;
		}
		start--;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});
	
	$($("#myTabNav .page-link")[1]).click(function(){
		start = parseInt($($("#myTabNav .page-link")[1]).text()) - 1;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});
	
	$($("#myTabNav .page-link")[2]).click(function(){
		start = parseInt($($("#myTabNav .page-link")[2]).text()) - 1;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});
	
	$($("#myTabNav .page-link")[3]).click(function(){
		start = parseInt($($("#myTabNav .page-link")[3]).text()) - 1;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});

	$($("#myTabNav .page-link")[4]).click(function(){
		start = parseInt($($("#myTabNav .page-link")[4]).text()) - 1;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});
	
	$($("#myTabNav .page-link")[5]).click(function(){
		start = parseInt($($("#myTabNav .page-link")[5]).text()) - 1;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});
	
	$($("#myTabNav .page-link")[6]).click(function(){
		if(start >= totalPages - 1){
			console.log("已经在最后一页，后面没有页了！");
			return;
		}
		start++;
		for(i = 0; i < 4; i++){
			if($($("#myTabContent .tab-pane")[i]).hasClass("active")){
				showMyTab("#" + $($("#myTabContent .tab-pane")[i]).attr("id"), "#myTabNav");
				break;
			}
		}
	});
});



