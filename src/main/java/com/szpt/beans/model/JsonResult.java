package com.szpt.beans.model;

/**

 * Created by kane on 17-3-7.
 * 接口返回的Json数据的格式规范
 */
public class JsonResult<T> {
    private boolean success;
    private T result;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
