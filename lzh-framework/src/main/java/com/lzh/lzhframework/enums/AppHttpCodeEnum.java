package com.lzh.lzhframework.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
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
    NOT_SELECT_FILE(308, "请选择要上传的文件");

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
