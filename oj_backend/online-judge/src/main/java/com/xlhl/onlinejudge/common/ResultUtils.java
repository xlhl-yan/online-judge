package com.xlhl.onlinejudge.common;

/**
 * 返回工具类
 *
 * @author <a href="https://github.com/xlhl-yan">xlhl</a>
 * 
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> com.xlhl.onlinejudge.common.BaseResponse<T> success(T data) {
        return new com.xlhl.onlinejudge.common.BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static com.xlhl.onlinejudge.common.BaseResponse error(ErrorCode errorCode) {
        return new com.xlhl.onlinejudge.common.BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static com.xlhl.onlinejudge.common.BaseResponse error(int code, String message) {
        return new com.xlhl.onlinejudge.common.BaseResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static com.xlhl.onlinejudge.common.BaseResponse error(ErrorCode errorCode, String message) {
        return new com.xlhl.onlinejudge.common.BaseResponse(errorCode.getCode(), null, message);
    }
}
