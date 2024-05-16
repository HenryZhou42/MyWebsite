package com.zyh.mywebsite.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;


}

