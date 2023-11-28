package com.lzh.lzhframework.constants;

/**
 * 系统常量类
 *
 * @author lzh
 */
public class SysConstants {

    public static final String PRE_LOGIN_USER_REDIS = "user:login:";

    public static final String EMAIL_CODE = "email:code:";

    public static final String EMAIL_UPDATE = "email:update:";

    public static final String NEW_EMAIL_CODE = "newEmail:code";

    public static final String BLOG_RUN_TIME = "blog:run:time";

    public static final String USER_VIEW = "user:watch:";

    public static final String GET_PWD_CODE = "email:pwd:code:";

    public static final String ACCOUNT_CANCEL = "account:cancel:";

    public static final String MSG_RECEIVE_FROM = "msg:receive:from:";
    public static final String IMG_PREFIX = "img_";

    /**
     * 后台管理系统用户登录缓存前缀
     */
    public static final String SYS_USER_LOGIN = "sys:user:login:";

    public static final String HTTP = "http";

    public static final String ARTICLE_VIEW_RANK = "blog:view:rank:";

    public static final String TAG_CACHE_REDIS = "blog:tag:redis";

    public static final String CAROUSEL_IMG_NUM = "CAROUSEL_IMG_NUM";

    public static final String CAROUSEL_IMG_ORDER = "CAROUSEL_IMG_ORDER";

    /**
     * 网站运行时间key
     */
    public static final String WEBSITE_RUNTIME = "WEBSITE_RUNTIME";

    /**
     * 网站信息 - 文章数
     */
    public static final String ARTICLE_COUNT = "ARTICLE_COUNT";

    /**
     * 网站信息 - 标签数
     */
    public static final String TAG_COUNT = "TAG_COUNT";

    /**
     * 网站信息 - 类别数
     */
    public static final String CATEGORY_COUNT = "CATEGORY_COUNT";

    /**
     * 网站信息 - 评论数
     */
    public static final String COMMENT_COUNT = "COMMENT_COUNT";

    /**
     * 网站信息 - 浏览量
     */
    public static final String VIEW_COUNT = "VIEW_COUNT";

    /**
     * 日期格式化 - 年月日时分秒
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 - 年月日
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * IP - OS
     */
    public static final String OS = "OS";

    /**
     * IP - BROWSER
     */
    public static final String BROWSER = "BROWSER";

    /**
     * 日志 - 异常
     */
    public static final Integer ABNORMAL = 1;


}
