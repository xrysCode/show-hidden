package com.datalevel.showhiddencontrol.auth.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IAuthGroupServiceTest {
    @Autowired
    IAuthGroupService iAuthGroupService;

    @Test
    void selectByUserAndAppServices() {
        iAuthGroupService.selectByUserAndAppServices(1L,null, Lists.newArrayList(1L));
        iAuthGroupService.selectByUserAndAppServices(1L,1L, Lists.newArrayList(1L));
    }
}