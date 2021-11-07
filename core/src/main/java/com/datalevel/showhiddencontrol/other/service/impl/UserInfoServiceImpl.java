package com.datalevel.showhiddencontrol.other.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.other.entity.UserInfoEntity;
import com.datalevel.showhiddencontrol.other.mapper.UserInfoMapper;
import com.datalevel.showhiddencontrol.other.service.IUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-11-07
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements IUserInfoService {

}
