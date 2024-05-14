package com.zyh.mywebsite.service.TestImplementation;

import com.zyh.mywebsite.entity.User;
import com.zyh.mywebsite.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TestImplenmentation {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserMapper userMapper;

    public List<User> getUserByName() {
        List<User> list = new ArrayList<>();
        try{
            list = userMapper.getUserByUsername("testuser3");
            System.out.println(list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
