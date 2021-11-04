package com.zhexinit.gameapi.service.impl;

import com.zhexinit.gameapi.entity.UserInfo;
import com.zhexinit.gameapi.mapper.UserInfoMapper;
import com.zhexinit.gameapi.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏用户信息表 服务实现类
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
