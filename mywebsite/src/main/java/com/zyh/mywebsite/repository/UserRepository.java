package com.zyh.mywebsite.repository;

import com.zyh.mywebsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
