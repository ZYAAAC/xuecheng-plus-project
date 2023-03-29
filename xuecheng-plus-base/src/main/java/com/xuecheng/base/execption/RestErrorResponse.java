package com.xuecheng.base.execption;

import java.io.Serializable;

/**
 * @description 和前端约定返回的异常信息
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */

public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}
