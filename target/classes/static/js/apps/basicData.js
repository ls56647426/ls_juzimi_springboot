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
	//判断当前是否登录
	var storage = window.localStorage;
	
	// 获取localStorage数据
	var name = storage["name"];
	var ConcernNumber = storage["ConcernNumber"];
	if(typeof(name) != "undefined" && typeof(ConcernNumber) == "undefined") {		//已登录
		//ajax获取数据
		var data = "name=" + name;
		myAjax("post", "/user/UserBasicData", data, function(ajax){
			//获取响应数据
			var res = ajax.responseText;
			var userBasicData = JSON.parse(res);
			
			if(userBasicData){
				//数据接收
				storage['ConcernNumber'] = userBasicData[0];
				storage['FansNumber'] = userBasicData[1];
				storage['SentenceBankNumber'] = userBasicData[2];
				storage['OriginalNumber'] = userBasicData[3];
				storage['SentenceSetNumber'] = userBasicData[4];
				storage['LeavingMessagesNumber'] = userBasicData[5];
				
				window.location.reload();
			}
		});
	}
	else{		//未登录
		//暂不处理，默认都为0
	}
	
	//填充页面
	$("#HeadPortrait").attr("src", (typeof(storage["headPortrait"]) == "undefined" ? "/img/head_portrait/默认头像.png" : storage["headPortrait"]));
	$("#NicknameStrong").text(storage["nickname"]);
	$("#BriefIntroduction").text(getConstellation(storage["borthday"]) + storage["sex"] + "生");
	$("#ConcernNumber").text(storage['ConcernNumber']);
	$("#FansNumber").text(storage['FansNumber']);
	$("#SentenceBankNumber").text(storage['SentenceBankNumber']);
	$("#OriginalNumber").text(storage['OriginalNumber']);
	$("#SentenceSetNumber").text(storage['SentenceSetNumber']);
	$("#LeavingMessagesNumber").text(storage['LeavingMessagesNumber']);
});