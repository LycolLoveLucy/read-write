package com.application.service.test;

import com.application.Application;
import com.application.entity.Account;
import com.application.mapper.UserMapper;
import com.application.service.AccountService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import plus.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2019/8/12.
 */
public class AgainTestAccountService  extends  BaseTest{

    @MockBean
    UserMapper userMapper;

    @Autowired
    @InjectMocks
    AccountService accountService;

    @Autowired
    UserMapper userMapper1;

    @Test
    public void testGetAllAcountInfo2(){
        Assert.assertNotNull(userMapper1);

        Page page=new Page();
        Mockito.when(userMapper.selectAllUserInfo(Mockito.any(Page.class),Mockito.anyString())).thenReturn(
                Stream.of(new Account("1","lycol")).collect(Collectors.toList()));
        List<Account> accountInfo= accountService.getReadAccountInfo(new Page());
        Assert.assertTrue(accountInfo.size()>0);
        Assert.assertEquals(accountInfo.get(0).getName(),"lycol");


    }
}
