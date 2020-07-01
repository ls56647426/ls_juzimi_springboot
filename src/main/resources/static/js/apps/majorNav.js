$(function(){
	
	//判断当前是否登录
	var storage = window.localStorage;
	
	var userStatus = {loginBtnFlag:false};
	
	// 获取localStorage数据
	var name = storage["name"];
	var password = storage["password"];
	if(typeof(name) != "undefined" && typeof(password) != "undefined"){ // 已登录
		$('.NotLogin').css({'display': 'none'});
		$('.AlreadyLogin').css({'display': 'block'});
		$("#userHeadPortrait").text(storage["nickname"]);
	}else{ // 未登录
		$('.NotLogin').css({'display': 'block'});
		$('.AlreadyLogin').css({'display': 'none'});
	}
	
	//登录
	$("#login1").click(function(){
		$("#myModalBody").html("");		//先清空上次句集列表
		$('#myModal h5').text('登录');
		$('#myModal .modal-content').width('400px');
		$('#myModal').modal('show');
		
		var table = $("<table id='loginTable' class='table table-hover'>");
			var tbody = $("<tbody>");
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>账号：</td>");
						var font = $("<font color='red'>*<font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='text' id='userName' style='width:100%;' />");
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var tr = $("<tr class='row'>");
					var td = $("<td class='col-sm-3'>密码：</td>");
						var font = $("<font color='red'>*<font>");
						font.appendTo(td);
					td.appendTo(tr);
					
					var td = $("<td class='col-sm-9'>");
						var input = $("<input type='password' id='userPwd' style='width:100%;' />");
						input.appendTo(td);
					td.appendTo(tr);
				tr.appendTo(tbody);
				
				var button = $("<button type='button' id='loginBtn' class='btn btn-outline-primary btn-block'>登录</button>");
				button.appendTo(tbody);
			tbody.appendTo(table);
		table.appendTo($("#myModalBody"));
		
		userStatus.loginBtnFlag = true;
	});
	
	//退出
	$("#logout1").click(function(){
		localStorage.clear();
		window.location.reload();
	});
	
	//登录按钮创建后，创建下面的click方法
	Object.defineProperty(userStatus, "loginBtnFlag", {
		set: function(value) {
			$("#loginBtn").click(function(){
				var data = "name=" + $('#userName').val() + "&password=" + $('#userPwd').val();
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
		}
	});
	
	//快速注册
	var register1Status = {register1BtnFlag:false};
	$("#register1").click(function(){
		//创建模态框
		$('#myModal .modal-content').width('400px');
		$('#myModal h5').text('快速注册');
		$("#myModalBody").html("");
		$('#myModal').modal('show');

		var table = $("<table id='register1Table' class='table table-hover'>");
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
				
				var button = $("<button type='button' id='register1Btn' class='btn btn-outline-primary btn-block'>注册</button>");
				button.appendTo(tbody);
			tbody.appendTo(table);
		table.appendTo($("#myModalBody"));
		
		register1Status.register1BtnFlag = true;
	});
	
	//快速注册按钮创建后，创建下面的click方法
	Object.defineProperty(register1Status, "register1BtnFlag", {
		set: function(value) {
			$("#register1Btn").click(function(){
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
	
	
});