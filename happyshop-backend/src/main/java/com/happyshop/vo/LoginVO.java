package com.happyshop.vo;

import com.happyshop.entity.User;
import lombok.Data;

@Data
public class LoginVO {

    private String token;
    private User user;
}
