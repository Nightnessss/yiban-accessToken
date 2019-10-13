package com.fehead.yiban_interface.error;

/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/7/8 16:27
 */
public enum EmBusinessError implements CommonError {
    // 通用错误类型 10001
    UNKNOWN_ERROR(10000, "未知错误"),
    DATARESOURCE_CONNECT_FAILURE(10001,"数据源错误"),

    // 接口获取异常类型 20001
    CODE_GET_ERROR(20000, "code获取异常"),
    ACCESS_TOKEN_GET_ERROR(20001, "access_token获取异常")

    ;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;



    @Override
    public int getErrorCode() {
        return errCode;
    }

    @Override
    public String getErrorMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
