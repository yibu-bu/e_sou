package com.yibu.common;

public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, "ok", data);
    }

    public static <T> BaseResponse<T> error(ResultCodeEnum resultCodeEnum) {
        return new BaseResponse<>(resultCodeEnum);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null);
    }
}