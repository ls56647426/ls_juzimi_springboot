//获取状态码status对应的结果
function getStatusResult(status){
	var res;
	switch(parseInt(status)){
	case 0:		//操作成功
		res = "操作成功！";
		break;
	case 1:		//用户注册失败：该用户已存在
		res = "该用户已存在！";
		break;
	case 2:		//用户密码修改失败
		res = "该用户不存在！";
		break;
	case 3:		//分类新建失败：该分类已存在
		res = "该分类已存在！";
		break;
	case 4:		//分类名修改失败：与上次类名相同
		res = "与上次类名相同！";
		break;
	case 5:		//句子发布失败：该句子已存在
		res = "该句子已存在！";
		break;
	case 6:		//句子修改失败
		res = "句子修改失败！";
		break;
	case 7:		//用户密码修改失败：与上次密码相同
		res = "与上次密码相同！";
		break;
	case 8:		//手机号绑定失败：该手机号已绑定
		res = "该手机号已绑定";
		break;
	case 9:		//用户添加/修改失败：该昵称已存在4lx
		res = "该昵称已存在";
		break;
//	case 10:	//句子修改失败：与上次句子内容相同
//		res = "与上次句子内容相同！";
//		break;
	case 11:	//句集创建失败：该句集已存在
		res = "该句集已存在！";
		break;
	case 12:	//句子发布失败：该作品尚未创建，请联系管理员，谢谢！
		res = "该作品尚未创建，请联系管理员，谢谢！";
		break;
	case 13:	//加入句集失败：该句子在该句集中已存在
		res = "该句子在该句集中已存在！";
		break;
	case 14:	//数据错误
		res = "数据错误！";
		break;
	case 15:	//喜欢失败：该句子已被喜欢
		res = "该句子已被喜欢！";
		break;
	case 16:	//喜欢失败：该句集已被喜欢
		res = "该句集已被喜欢！";
		break;
	case 17:	//增加失败：该作品已存在
		res = "该作品已存在！";
		break;
	default:	//未知操作失败
		res = "操作失败！";
		break;
	}
	return res;
}