package com.haikuowuya.microlife.model;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class BaseResponseItem
{
    public String status;
    public String message;
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
