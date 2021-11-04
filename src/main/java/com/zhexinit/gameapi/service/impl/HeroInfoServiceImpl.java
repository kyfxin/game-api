package com.zhexinit.gameapi.service.impl;

import com.zhexinit.gameapi.entity.HeroInfo;
import com.zhexinit.gameapi.mapper.HeroInfoMapper;
import com.zhexinit.gameapi.service.HeroInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 英雄基础信息表 服务实现类
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Service
public class HeroInfoServiceImpl extends ServiceImpl<HeroInfoMapper, HeroInfo> implements HeroInfoService {

}
