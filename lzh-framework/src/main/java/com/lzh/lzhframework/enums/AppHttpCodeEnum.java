package com.lzh.lzhframework.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "该邮箱已经注册"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    CONTENT_NOTNULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png/jpg文件"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    FILE_UPLOAD_ERROR(513, "文件上传错误"),
    OVER_UPLOAD_LIMIT(514, "超过文件上传限制"),
    CAROUSEL_IMG_COUNT_ERROR(515, "轮播图数量输入错误"),

    USER_NOT_EXIT(303, "用户不存在"),
    CUR_PASSWORD_ERROR(303, "当前密码错误"),
    NEW_CON_PASSWORD_NOT_MATCH(303, "两次密码输入不一致"),
    NEW_PASSWORD_REPEAT(303, "新密码不能与原密码一致"),
    EMAIL_ERROR(303, "邮箱错误"),
    CODE_EXPIRE(303, "验证码已过期，请重新发送"),
    CODE_ERROR(303, "验证码错误"),
    EMAIL_REPEAT(303, "新邮箱不能与原邮箱一致"),
    EMAIL_NOT_EXIT(303, "邮箱不存在"),


    ARTICLE_ID_NULL(303, "文章id为空"),
    USER_NOT_LOGIN(303, "用户未登录"), LOGIN_SUCCESS(200, "登陆成功"),
    USER_STOP(304, "该用户已停用"), LOGOUT_SUCCESS(200, "退出成功"),
    LOGIN_FAIL(305, "登陆失败"),
    FILE_NOT_NULL(306, "请选择要上传的文件"),
    IMG_NOT_NULL(307, "请选择要上传的图片"),
    NOT_SELECT_FILE(308, "请选择要上传的文件"),
    TAG_NOT_NULL(309, "标签名不能为空"),
    TAG_HAS_BLOG(310, "该标签下还有博客"),
    CATEGORY_HAS_BLOG(311, "该分类下还有博客"),
    PARAM_NOT_RIGHT(312, "请输入正确的数据"),

    CATEGORY_IS_TOP(313, "该分类已经置顶了"),
    TAG_IS_TOP(314, "该标签已经置顶了"),
    HAS_SUB_COMMENT(315, "有子评论，无法删除"),
    FRIEND_LINK_IS_TOP(316, "友链已经置顶了"),
    ARTICLE_NOT_EXIST(317, "文章不存在"),

    ORDER_NOT_NULL(516, "排序不能为空"),
    PHONENUMBER_NOT_NULL(517, "手机号不能为空"),
    OLD_PWD_ERROR(518, "修改密码失败，旧密码错误"),
    OLD_NEW_PWD_ALIKE(519, "新密码不能与旧密码相同"),;

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
