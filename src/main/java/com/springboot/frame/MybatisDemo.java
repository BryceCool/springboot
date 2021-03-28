package com.springboot.frame;

import com.springboot.frame.dao.UserDao;
import com.springboot.frame.entity.Users;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Administrator
 * @Date: 2021/3/28 10:33
 */
public class MybatisDemo {

    public static void main(String[] args) {
        String resource = "config/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            Users userInfo = session.selectOne("com.springboot.frame.dao.UserDao.getUserInfo", 1);
            System.out.println(userInfo.toString());
        }

        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserDao mapper = session.getMapper(UserDao.class);
            Users userInfo = mapper.getUserInfo(1);
            System.out.println(userInfo.toString());
        }


    }

}
