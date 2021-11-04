package com.zhexinit.gameapi.service.impl;

import com.zhexinit.gameapi.entity.UserHero;
import com.zhexinit.gameapi.mapper.UserHeroMapper;
import com.zhexinit.gameapi.service.UserHeroService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏用户与英雄的关联信息表 服务实现类
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Service
public class UserHeroServiceImpl extends ServiceImpl<UserHeroMapper, UserHero> implements UserHeroService {

}
