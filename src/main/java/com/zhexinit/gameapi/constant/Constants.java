package com.zhexinit.gameapi.constant;

public class Constants {
	/**文件存储位置 local*/
	public static final String FILE_LOCATION_LOCAL = "local";
	
	/**文件存储位置  七牛公有云*/
	public static final String FILE_LOCALTION_QINIU_PUBLIC = "qiniu_public";
	
	/**文件存储位置  七牛私有云*/
	public static final String FILE_LOCATION_QINIU_PRIVATE = "qiniu_private";	
	
	public static final String[] IMAGE_FORMAT = new String[] {"png", "jpg", "jpeg", "gif"};
	
	//推荐类型
	public static final String RECOMMEND_TYPE_EXAM = "exam";
	public static final String RECOMMEND_TYPE_COURSE = "course";
	public static final String RECOMMEND_TYPE_COMPETITION = "competition";
	public static final String RECOMMEND_TYPE_CERT_EXAM   = "cert_exam";
	
	//推荐平台
	public static final String RECOMMEND_PLATFORM_PC = "PC";
	public static final String RECOMMEND_PLATFORM_WX = "WX";
	
	//考试报名类型
	public static final String SIGN_TYPES_PUBLIC_FREE   = "public_free";//免费
	public static final String SIGN_TYPES_PUBLIC_CHARGE = "public_charge";//收费
	public static final String SIGN_TYPES_PRIVATE_FREE  = "private_free";//定向
	
	/**注册是否已阅读过用户协议*/
	public static final String REGISTER_IS_READ_RULE = "1";
	/**注册失败返回消息data字段下的消息key*/
	public static final String REGISTER_ERROR_MSG_KEY_MOBILE = "mobile";
	public static final String REGISTER_ERROR_MSG_KEY_VERIFYCODE = "verify_code";
	public static final String REGISTER_ERROR_MSG_KEY_NICKNAME = "nickname";
	public static final String REGISTER_ERROR_MSG_KEY_READRULE = "is_read_rule";
	public static final String REGISTER_ERROR_MSG_KEY_PASSWORD = "password";
	
	/**需要校验course_memmber的isvalidstudent字段*/
	public static final boolean CHECK_COMP_COURSE_MEMEMBER_IS_VALID_STUDENT = true;
	
	/**不需要校验course_memmber的isvalidstudent字段*/
	public static final boolean NOT_CHECK_COMP_COURSE_MEMEMBER_IS_VALID_STUDENT = false;
	
	/**将COURSE_MEMEBER从普通学生转化为参赛学生*/
	public static final int COMP_COURSE_MEMEBER_OPERTYPE_CHANGETOVALID = 1;
	
	/**将COURSE_MEMEBER从参赛学生转化为参赛普通*/
	public static final int COMP_COURSE_MEMEBER_OPERTYPE_CHANGETOCOMMEN = 0; 
	
	/**CompetitionCourseMember 是参赛学生*/
	public static final int COMP_COURSE_MEMBER_IS_ALREADY_VALID_STUDENT = 1;
	
	/**CompetitionCourseMember 是普通学生*/
	public static final int COMP_COURSE_MEMBER_IS_COMMON_STUDENT = 0;
	
	/**CompetitionCourse 课程是否违规 1：课程违规*/
	public static final int COMPETITION_COURSE_IS_VIOLATED = 1;
	
	/**CompetitionCourse 课程是否已下架 1：已下架*/
	public static final int COURSE_IS_LOWER_SHELF = 1;
	
	public static final String COMPETITION_COURSE_QUESTION_INSERT = "insert";
	
	public static final String COMPETITION_COURSE_QUESTION_UPDATE = "update";
	
	/**课程校验需要校验课程状态的标识*/
	public static final boolean NEED_CHECK_COURSE_STATUS = true;
	
	/**课程校验不需要校验课程状态的标识*/
	public static final boolean NOT_NEED_CHECK_COURSE_STATUS = false;
	
	/**需要校验课程的VIOLATION和SHELFSTATUS*/
	public static final boolean NEED_CHECK_VIOLATION_AND_SHELFSTATUS = true;
	
	/**不需要校验课程的VIOLATION和SHELFSTATUS*/
	public static final boolean NOT_NEED_CHECK_VIOLATION_AND_SHELFSTATUS = false;
	
	/**pagerconfig校验类型 class：小微课时*/
	public static final String PAPER_TYPE_CLASS = "class";
	
	/**paperconfig校验类型 course：课程*/
	public static final String PAPER_TYPE_COURSE = "course";
	
	/**竞赛课程已经发布过考试*/
	public static final int COURSE_EXAM_IS_PUBLISHED = 1;
	
	/**在线课程新增*/
	public static final String TEACH_COURSE_OPERTYPE_INSERT = "insert";
	
	/**在线课程修改*/
	public static final String TEACH_COURSE_OPERTYPE_UPDATE = "update";
	
	/**课程校验需要校验课程下架状态的标识*/
	public static final boolean NEED_CHECK_COURSE_SHELFSTATUS = true;
	
	/**课程校验需要校验课程下架状态的标识*/
	public static final boolean NOT_NEED_CHECK_COURSE_SHELFSTATUS = false;
	
	/**课程校验需要校验课时状态的标识*/
	public static final boolean NEED_CHECK_CLASS_STATUS = true;
	
	/**课程校验不需要校验课时状态的标识*/
	public static final boolean NOT_NEED_CHECK_CLASS_STATUS = false;
	
}
