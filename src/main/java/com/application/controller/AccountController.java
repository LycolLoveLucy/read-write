package com.application.controller;

import com.application.entity.Account;
import com.application.mapper.UserMapper;
import com.application.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/getReadAccountInfo")
    public String getAccountInfo()  {
        return accountService.getReadAccountInfo().get(0).getName();
    }

    @GetMapping("/getWriteAccountInfo")
    public String getWriteAccountInfo() throws JsonProcessingException {
        return accountService.getWriteAccountInfo().get(0).getName();

    }

    @PostMapping("/testBatchInsert")
    public ResponseEntity testBatchInsert(@RequestBody List<Account> accountList) throws JsonProcessingException
    {
        accountList.forEach(e->e.setId(UUID.randomUUID().toString().replaceAll("-","")));
        userMapper.insertBatch(accountList);
        return ResponseEntity.ok(accountList);

    }
    @PostMapping("/testPostLog")
    public  Map<String,Object> testPostLog(@RequestBody Map<String,Object> requestMap) throws JsonProcessingException {
        return requestMap;

    }

}
