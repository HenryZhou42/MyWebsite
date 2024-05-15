package com.zyh.mywebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)
    @Id
    private int id;

    @NotBlank(message = "Username cannot be null or empty.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    @Size(min = 6,max = 100, message = "Password must be between 6 and 100 characters long")
    @Setter(AccessLevel.NONE)
    private String password;

}

