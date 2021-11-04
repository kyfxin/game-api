package com.zhexinit.gameapi.constant;

/**
 * redis缓存key 常量类
 * @author ThinkPad
 *
 */
public class RedisKeyConstant {
	
	/**数据库数据在redis中的默认缓存时间*/
	public static final int DB_DATA_DEFAULT_CACHE_TIME = 5 * 60;
	
	/**用户登录失败次数缓存key前缀*/
	public static final String USER_LOGIN_ERROR_COUNT_KEY_PREFIX = "user_login_err_count_";
	
	/**所有的学校数据*/
	public static final String ALL_SCHOOLS_LIST_KEY = "OPENAPI_ALL_SCHOOLS";
	
	/**职业技能鉴定在线考试试卷的缓存key*/
	public static final String CACHE_CERTIFICATE_EXAM_KEY   = "java_cache_before_start_cert_exam_";
	
	/**职业技能鉴定在线考试缓存学员试卷考题key前缀*/
	public static final String CERT_EXAM_MEMBER_PAPER_KEY_PREFIX = "java_cert_exam_id_";
	
	/**从certificate_examination_papers表中获取的数据的缓存key*/
	public static final String DB_DATA_CERT_EXAM_PAPER_KEY_PREFIX = "db_data_cert_exam_paper_";
	
	/**从examination_questions表中根据examid查询出来的试题列表缓存可以*/
	public static final String DB_DATA_CERT_EXAM_PAPER_QUESTIONS_KEY_PREFIX = "db_data_cert_exam_paper_questions_";
}
