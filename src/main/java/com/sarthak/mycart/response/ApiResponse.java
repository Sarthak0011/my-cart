package com.sarthak.mycart.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    Boolean success;
    String message;
    Object data;
}
