package com.application.mapper;

import com.application.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from t_table")
    List<Account> selectAllUserInfo();
}
