package com.application.service;

import com.application.dynamicdatasource.ReadOnlyConnection;
import com.application.dynamicdatasource.WriteConnection;
import com.application.entity.Account;
import com.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.Page;

import java.util.List;

@Service
public class AccountService {


    @Autowired
    UserMapper userMapper;

    @ReadOnlyConnection
    public List<Account> getReadAccountInfo(Page page ){
        page.setPageSize(2);
        page.setCurrentPage(2);
        return  userMapper.selectAllUserInfo(page,"");
    }

    @WriteConnection
    public List<Account> getWriteAccountInfo(){
        return  userMapper.selectAllUserInfo(null,null);
    }
}
