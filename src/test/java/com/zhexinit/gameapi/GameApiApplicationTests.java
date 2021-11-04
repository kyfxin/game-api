package com.zhexinit.gameapi;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhexinit.gameapi.constant.RedisKeyConstant;
import com.zhexinit.gameapi.domain.BattleHeroInfo;
import com.zhexinit.gameapi.mapper.UserHeroMapper;
import com.zhexinit.gameapi.redis.RedisClient;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class GameApiApplicationTests {
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private UserHeroMapper userHeroMapper;
	
	

	@Test
	void contextLoads() {
		List<BattleHeroInfo> heroList = userHeroMapper.queryBattleHeroList(1, 2);
		System.out.println("****=" + heroList);
	}
	
	@Test
	public void testOrgMaps() {
//		Map<Integer, String> orgMap = orgService.queryAllOrgNameMap();
//		log.info("#################### orgMap={}", orgMap);
//		int memberCount = examMemberMapper.queryMemberCount(107);
//		log.info("################## memberCount={}", memberCount);
//		CertificateExaminationMembers member = examMemberMapper.queryMember(1, 5);
//		CertificateExaminationMembers member = examMemberMapper.queryMember2(1);
//		log.info("################## member={}", member);
//		List<CertificateExaminationModule> examModuleList = certExamModuleMapper.queryExamModuleList(1);
//		
//		log.info("################## examModuleList size={}, examModuleList==null ? {}", examModuleList.size(), examModuleList == null);
		
//		List<CertificateExaminationPlaceWithMember> examPlaceList = examPlaceMapper.queryExamPlaceList(207);
//		System.out.println("#####examPlaceList=" + examPlaceList);
		
//		List<CertificateExamPlaceRespInfo> examPlaceList = examPlaceService.queryExamPlaceRespInfoList(207);
//		System.out.println("#####examPlaceList=" + examPlaceList);
		
//		MemberOrders memberOrder = memberOrdersMapper.queryNotClosedOrder(1, "cert_exam", 37);
//		System.out.println("###memberOrder=" + memberOrder);
		
//		MemberOrders orders = new MemberOrders();
//		orders.setMemberId(123);
//		orders.setOrderType(MemberOrderTypeEnum.CERTIFICATE.value());
//		orders.setProductId(123);
//		orders.setName("test");
//		orders.setAmount(1000);
//		
//		orders.setOrderSn("1234567890");
//		orders.setPayType(MemberOrderPayTypeEnum.ALI.value());
//		orders.setStatus(MemberOrderStatusEnum.STATUS_WAIT_PAY.getStatus());
//		orders.setOrganizationId(9);
//		LocalDateTime now = LocalDateTime.now();
//		orders.setValidAt(now);
//		
//		orders.setCreatedAt(now);
//		orders.setUpdatedAt(now);
//		
//		int insertRow = memberOrdersMapper.insertOrderReturnId(orders);
//		log.info("###############insertRow={}, orders.id={}", insertRow, orders.getId());
		
//		List<CertificateExaminationMockRecords> mockRecords = certExamMockRecordMapper.queryUserMockRecords(222, 16219, 20);
//		System.out.println(mockRecords);
		
//		List<CertificateExamPapersWithQuestion> paperQuestionList = examPapersMapper.queryExamPaperQuestions(230);
//		System.out.println("****** paperQuestionList=" + paperQuestionList);
//		List<CertificateExaminationMemberPapers> memberPapers = examMemberPapersMapper.queryMemberPapers(232, 16219);
//		System.out.println("********** memberPapers=" + memberPapers);
//		Map<Integer, CertificateExaminationMemberPapers> memberPapersMap = memberPapers.stream().collect(Collectors.toMap(CertificateExaminationMemberPapers::getQuestionId, paper -> paper, (k1, k2) -> k1));
//		System.out.println("********** memberPapersMap=" + memberPapersMap);
	}
	
	@Test
	public void testRedis() {
//		String key = "testHash";
////		List<String> list = new ArrayList<>();
////		list.add("a");
////		list.add("b");
////		redisClient.putSet(key, "a");
////		redisClient.putSet(key, "b");
//		
//		Map<String, String> map1 = new HashMap<>();
//		map1.put("a", "1");
//		map1.put("b", "2");
//		redisClient.putHash(key, map1);
		
//		Map<String, String> map2 = new HashMap<>();
//		map2.put("b", "2");
//		redisClient.putHash(key, map2);
		
//		Map resultMap = redisClient.getHash(key);
//		
//		
////		Set<String> cacheList = redisClient.getSet(key);
//		System.out.println("resultMap size=" + resultMap.size() + ", resultMap=" + resultMap);
		
	}
}
