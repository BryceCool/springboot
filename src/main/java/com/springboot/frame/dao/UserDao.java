package com.springboot.frame.dao;

import com.springboot.frame.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2021/3/28 10:41
 */
@Repository
public interface UserDao {

    /**
     * 获取查找的用户信息
     *
     * @return 返回查找的用户信息
     */
    Users getUserInfo(@RequestParam("id") int id);


}
