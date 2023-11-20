package com.child.web.response;

import lombok.Data;

@Data
public class SuccessResponse {
    private boolean success;
    private String message;
    private Object data;

    public SuccessResponse(String message, Object data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

}
