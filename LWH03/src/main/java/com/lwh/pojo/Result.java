package com.lwh.pojo;

import java.io.Serializable;


public class Result implements Serializable {

    private boolean flag;   // 操作成功or失败
    private String message; // 操作的信息

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
