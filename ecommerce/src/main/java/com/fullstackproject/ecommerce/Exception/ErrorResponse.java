package com.fullstackproject.ecommerce.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @Setter
    private String errorMsg;
    @Setter
    private int errorCode;

//    public ErrorResponse(String errorMsg, int errorCode) {
//        this.errorMsg = errorMsg;
//        this.errorCode = errorCode;
//    }

    public String getErrorMsg() {
        return errorMsg;
    }

//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }

    public int getErrorCode() {
        return errorCode;
    }

//    public void setErrorCode(int errorCode) {
//        this.errorCode = errorCode;
//    }
}
