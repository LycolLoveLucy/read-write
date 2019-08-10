package com.application.mapper;

import com.application.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<Account>{

    @Select("select * from t_table")
    List<Account> selectAllUserInfo();

    int insertBatch(@Param(value = "items") Collection collection);
}
