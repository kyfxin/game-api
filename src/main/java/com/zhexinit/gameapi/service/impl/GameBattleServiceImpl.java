package com.zhexinit.gameapi.service.impl;

import com.zhexinit.gameapi.service.GameBattleService;

import lombok.extern.slf4j.Slf4j;

import com.zhexinit.gameapi.client.respinfo.BattleAttackRespInfo;
import com.zhexinit.gameapi.client.respinfo.BattleAttackTargetRespInfo;
import com.zhexinit.gameapi.client.respinfo.BattleChessRespInfo;
import com.zhexinit.gameapi.client.respinfo.BattleChessesRespInfo;
import com.zhexinit.gameapi.client.respinfo.BattleResultRespInfo;
import com.zhexinit.gameapi.constant.enums.AttackSkillEnum;
import com.zhexinit.gameapi.constant.enums.UserTypeEnum;
import com.zhexinit.gameapi.domain.BattleHeroInfo;
import com.zhexinit.gameapi.entity.BattleHeroRecord;
import com.zhexinit.gameapi.entity.BattleRecord;
import com.zhexinit.gameapi.entity.BattleRoundRecord;
import com.zhexinit.gameapi.entity.BattleRoundStepAttackTargetRecord;
import com.zhexinit.gameapi.entity.BattleRoundStepRecord;
import com.zhexinit.gameapi.entity.UserInfo;
import com.zhexinit.gameapi.exception.ParameterCheckException;
import com.zhexinit.gameapi.mapper.BattleHeroRecordMapper;
import com.zhexinit.gameapi.mapper.BattleRecordMapper;
import com.zhexinit.gameapi.mapper.BattleRoundRecordMapper;
import com.zhexinit.gameapi.mapper.BattleRoundStepAttackTargetRecordMapper;
import com.zhexinit.gameapi.mapper.BattleRoundStepRecordMapper;
import com.zhexinit.gameapi.mapper.UserHeroMapper;
import com.zhexinit.gameapi.mapper.UserInfoMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Service
@Slf4j
public class GameBattleServiceImpl 
	implements GameBattleService {
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private UserHeroMapper userHeroMapper;
	
	@Autowired
	private BattleRecordMapper battleRecordMapper;
	
	@Autowired
	private BattleHeroRecordMapper battleHeroRecordMapper;
	
	@Autowired
	private BattleRoundRecordMapper battleRoundRecordMapper;
	
	@Autowired
	private BattleRoundStepRecordMapper roundStepRecordMapper;
	
	@Autowired
	private BattleRoundStepAttackTargetRecordMapper roundStepAttackTargetRecordMapper;
	
	private Random random = new Random();
	
	/**
	 * 九宫格1-9每一个位置攻打对方的顺序
	 */
	private static final Map<Integer, Integer[]> POSITION_ATTACK_ORDER_MAP 
		= new HashMap<Integer, Integer[]>();
	static {
		POSITION_ATTACK_ORDER_MAP.put(1, new Integer[] {1, 4, 7, 2, 3, 5, 6, 8, 9});
		POSITION_ATTACK_ORDER_MAP.put(4, new Integer[] {1, 4, 7, 2, 3, 5, 6, 8, 9});
		POSITION_ATTACK_ORDER_MAP.put(7, new Integer[] {1, 4, 7, 2, 3, 5, 6, 8, 9});
		
		POSITION_ATTACK_ORDER_MAP.put(2, new Integer[] {2, 5, 8, 1, 3, 4, 6, 7, 9});
		POSITION_ATTACK_ORDER_MAP.put(5, new Integer[] {2, 5, 8, 1, 3, 4, 6, 7, 9});
		POSITION_ATTACK_ORDER_MAP.put(8, new Integer[] {2, 5, 8, 1, 3, 4, 6, 7, 9});
		
		POSITION_ATTACK_ORDER_MAP.put(3, new Integer[] {3, 6, 9, 2, 1, 5, 4, 8, 7});
		POSITION_ATTACK_ORDER_MAP.put(6, new Integer[] {3, 6, 9, 2, 1, 5, 4, 8, 7});
		POSITION_ATTACK_ORDER_MAP.put(9, new Integer[] {3, 6, 9, 2, 1, 5, 4, 8, 7});
	}
	
	/**
	 * 匹配战斗，按照某种条件随机获取一个游戏用户作为对战用户
	 * @return
	 */
	private UserInfo matchBattle() {
		UserInfo defenceUser = userInfoMapper.selectById(2);
		return defenceUser;
	}
	
	/**
	 * 获取对战双方使用的对战的英雄列表
	 * 理论上应该从用户关联的英雄列表中按照某种条件获取，测试时直接使用全部
	 * @param attackUserId
	 * @param defenceUserId
	 * @return
	 */
	private Map<Integer, List<BattleHeroInfo>> queryUserHeroList(Integer attackUserId, Integer defenceUserId) {
		List<BattleHeroInfo> battleHeroList = userHeroMapper.queryBattleHeroList(attackUserId, defenceUserId);
		if (null == battleHeroList || battleHeroList.size() == 0) {
			return new HashMap<Integer, List<BattleHeroInfo>>();
		}
		
		//将对战的英雄列表按照用户id分组
		Map<Integer, List<BattleHeroInfo>> resultMap = battleHeroList.stream()
																		.collect(Collectors.groupingBy(BattleHeroInfo::getUserId));
		return resultMap;
	}
	
	/**
	 * 初始化战斗英雄在9宫格上的位置
	 * @param battleHeroList 战斗中的英雄列表
	 * @param chessList 待返回给客户端的英雄列表
	 * @param battleRecordId 战斗的记录id
	 * @param userType 用户类型 1： 攻击方  2：防守方
	 */
	private void initBattlePosition(List<BattleHeroInfo> battleHeroList, List<BattleChessRespInfo> chessRsponseList, 
			Integer battleRecordId, int userType) {
		LocalDateTime now = LocalDateTime.now();
		List<Integer> randomPositions = randomPositionList(battleHeroList.size());
		for (int i=0; i < randomPositions.size(); i ++) {
			int position = randomPositions.get(i);
			BattleHeroInfo battleHeroInfo = battleHeroList.get(i);
			battleHeroInfo.setPosition(position);
			battleHeroInfo.setHp_left(battleHeroInfo.getHp());
			battleHeroInfo.setFury_left(battleHeroInfo.getFury());
			
			BattleChessRespInfo respInfo = new BattleChessRespInfo();
			BeanUtils.copyProperties(battleHeroInfo, respInfo);
			respInfo.setCfgId(battleHeroInfo.getId());
			respInfo.setPos(position);
			chessRsponseList.add(respInfo);
			
			//记录游戏中用户使用的英雄信息
			BattleHeroRecord battleHeroRecord = new BattleHeroRecord();
			BeanUtils.copyProperties(battleHeroInfo, battleHeroRecord, "id");
			battleHeroRecord.setBattleRecordId(battleRecordId);
			battleHeroRecord.setUserType(userType);
			battleHeroRecord.setHeroId(battleHeroInfo.getId());
//			battleHeroRecord.setPosition(position);
			battleHeroRecord.setCreateTime(now);
			battleHeroRecord.setUpdateTime(now);
			
			battleHeroRecordMapper.insert(battleHeroRecord);
		}
		
		
	}

	
	/**
	 * 生成1-9的随机数
	 * @param size list里面的元素个数限制
	 * @return
	 */
	private List<Integer> randomPositionList(int size) {
		List<Integer> randomPositions = new ArrayList<>();
		while(true) {
			int randomPos = random.nextInt(10);
			
			if (randomPos == 0) {
				continue;
			}
			
			if (!randomPositions.contains(randomPos)) {
				randomPositions.add(randomPos);
			}
			
			if (randomPositions.size() == size) {
				break;
			}
		}

		return randomPositions;
	}
	
	/**
	 * 获取一场战斗的战报
	 * 1、匹配战斗，从游戏用户中按照某种条件匹配出对战的用户
	 * 2、获取到对战双方用户战斗时所用的英雄列表
	 * 3、将英雄列表放到战斗位置（9宫格上的随机位置），然后进行战斗
	 * @return
	 */
	public BattleResultRespInfo doBattle() throws ParameterCheckException {
		
		//战报的响应结果
		BattleResultRespInfo battleResultRespInfo = new BattleResultRespInfo();
		
		//发起攻击的用户
		UserInfo attackUser = userInfoMapper.selectById(1);
		//获取对战用户
		UserInfo defenceUser = matchBattle();
		
		//获取对战双方使用的英雄列表
		Map<Integer, List<BattleHeroInfo>> battleHeroListMap = queryUserHeroList(attackUser.getId(), defenceUser.getId());
		
		//获取攻击方使用的英雄列表
		List<BattleHeroInfo> attackHeroList = battleHeroListMap.get(attackUser.getId());
		if (null == attackHeroList || attackHeroList.size() == 0) {
			throw new ParameterCheckException("未获取到攻击方的英雄列表");
		}
		
		//获取防御方使用的英雄列表
		List<BattleHeroInfo> defenceHeroList = battleHeroListMap.get(defenceUser.getId());
		if (null == defenceHeroList || defenceHeroList.size() == 0) {
			throw new ParameterCheckException("未获取到防御方的英雄列表");
		}
		
		//记录战斗信息
		BattleRecord battleRecord = new BattleRecord();
		battleRecord.setName("battle");
		battleRecord.setAttackUserId(attackUser.getId());
		battleRecord.setDefenceUserId(defenceUser.getId());
		LocalDateTime now = LocalDateTime.now();
		battleRecord.setCreateTime(now);
		battleRecord.setUpdateTime(now);
		battleRecordMapper.insertReturnId(battleRecord);
		
		//战报响应数据中的攻守双方的数据对象
		BattleChessesRespInfo chessesRespInfo = new BattleChessesRespInfo();
		//战报响应数据中的攻击方的数据对象
		List<BattleChessRespInfo> challenger = new ArrayList<>();
		
		//初始化攻击者在九宫格的位置
		initBattlePosition(attackHeroList, challenger, battleRecord.getId(), UserTypeEnum.ATTACK_USER.value());
		
		chessesRespInfo.setChallenger(challenger);
		
		//战报响应数据中的防守方的数据对象
		List<BattleChessRespInfo> defender = new ArrayList<>();
		//初始化防御者在九宫格的位置
		initBattlePosition(defenceHeroList, defender, battleRecord.getId(), UserTypeEnum.DEFENCE_USER.value());
		
		chessesRespInfo.setDefender(defender);
		battleResultRespInfo.setInfo(chessesRespInfo);
		
		//将攻击方的列表按照position属性排序，position小的先出手
		Collections.sort(attackHeroList);
		//将防守方的列表按照position属性排序，position小的先出手
		Collections.sort(defenceHeroList);
		
		//战报响应消息中的对战步骤列表(所有回合)
		List<BattleAttackRespInfo[]> attackRespInfoList = new ArrayList<>();
		
		//进行回合战斗
		battleRound(attackHeroList, defenceHeroList, battleRecord, attackRespInfoList);
		
		battleResultRespInfo.setDetails(attackRespInfoList);

		
		return battleResultRespInfo;
	}
	
	/**
	 * 对战的回合
	 * @param attackHeroList 攻击方的英雄列表
	 * @param defenceHeroList  防守方的英雄列表
	 * @param battleRecord 战斗记录
	 * @param attackRespInfoList 战报响应消息中的对战步骤列表,用于记录对战步骤返回给客户端
	 */
	private void battleRound(List<BattleHeroInfo> attackHeroList, 
			List<BattleHeroInfo> defenceHeroList, BattleRecord battleRecord, 
			List<BattleAttackRespInfo[]> attackRespInfoList) {
		//记录回合数
		int roundIndex = 1;
		//胜利方0:打和 1：攻击方  2：防守方
		int victory = 0;
		
		//不确定有多少回合的对战，先while(true)，满足条件后再退出
		while (true) {
			
			//每一回合的战斗结果
			List<BattleAttackRespInfo> battleAttackRoundRspList = new ArrayList<>();
			
			if (attackHeroList.size() == 0 && defenceHeroList.size() == 0) {
				//打和
				victory = 0;
				break;
			} else if (attackHeroList.size() == 0) {
				//攻击方的英雄全挂掉，防守方胜利
				victory = 2;
				break;
			} else if (defenceHeroList.size() == 0) {
				//防守方的英雄全挂掉，攻击方胜利
				victory = 1;
				break;
			}
			
			//记录对战的回合信息
			BattleRoundRecord battleRoundRecord = new BattleRoundRecord();
			battleRoundRecord.setBattleRecordId(battleRecord.getId());
			battleRoundRecord.setName("round" + roundIndex);
			battleRoundRecord.setStep(roundIndex);
			
			LocalDateTime now = LocalDateTime.now();
			battleRoundRecord.setCreateTime(now);
			battleRoundRecord.setUpdateTime(now);
			battleRoundRecordMapper.insertReturnId(battleRoundRecord);
			
			//进行回合中每一个步骤的战斗
			battleRoundStep(attackHeroList, defenceHeroList, battleRecord.getId(), battleRoundRecord.getId(), battleAttackRoundRspList, roundIndex);
			
			//攻击英雄列表去除已经挂掉的
			for (int i = 0; i < attackHeroList.size(); i ++) {
				BattleHeroInfo attackHero = attackHeroList.get(i);
				if (attackHero.isAlive() == false) {
					attackHeroList.remove(i);
					i--;
				}
			}
			
			//防守英雄列表去除已经挂掉的
			for (int i = 0; i < defenceHeroList.size(); i ++) {
				BattleHeroInfo defenceHero = defenceHeroList.get(i);
				if (defenceHero.isAlive() == false) {
					defenceHeroList.remove(i);
					i -- ;
				}
			}
			
			attackRespInfoList.add(battleAttackRoundRspList.toArray(new BattleAttackRespInfo[battleAttackRoundRspList.size()]));
			
			roundIndex ++ ;
		} //while(true) 循环结束
		
		battleRecord.setVictory(victory);
		battleRecordMapper.updateById(battleRecord);
	}
	
	/**
	 * 每一个回合的攻击步骤
	 * 1、计算攻守双方英雄的速度和，速度大的先出手
	 * 2、每一个回合，双方的英雄都有一次出手的机会
	 * @param attackHeroList   攻击方的英雄列表
	 * @param defenceHeroList  防守方的英雄列表
	 * @param battleRecordId   对战记录id
	 * @param battleRoundRecordId  对战的回合记录id
	 * @param attackRespInfoList 战报响应消息中的对战步骤列表,用于记录对战步骤返回给客户端
	 */
	private void battleRoundStep(List<BattleHeroInfo> attackHeroList, List<BattleHeroInfo> defenceHeroList, 
			int battleRecordId, int battleRoundRecordId, List<BattleAttackRespInfo> attackRespInfoList, int roundIndex) {
		
		//计算攻击方英雄的速度和
		Optional<Integer> attackSpeedSum = attackHeroList.stream().map(BattleHeroInfo::getSpd).reduce(Integer::sum);
		//计算防守方英雄的速度和
		Optional<Integer> defenceSpeedSum = defenceHeroList.stream().map(BattleHeroInfo::getSpd).reduce(Integer::sum);
		
		//出手顺序  0：攻击方  1：防守方 ,速度和相同则攻击方先出手
		int startAttackUserType = 0;
		
		if (attackSpeedSum.isPresent() && defenceSpeedSum.isPresent()) {
			if (attackSpeedSum.get() < defenceSpeedSum.get()) {
				//如果防守方的速度和小于攻击方，则防守方先出手
				startAttackUserType = 1;
			} 
		}
		
		//每一个攻击循环中的步骤序号
		int attackStep = 0;
		
		//遍历攻守双方英雄列表使用的索引
		int attackHeroIndex = 0;
		int defenceHeroIndex = 0;
		
		while (true) {
			if (attackHeroIndex >= attackHeroList.size() && defenceHeroIndex >= defenceHeroList.size()) {
				break;
			}
			
			if (attackHeroIndex >= attackHeroList.size() && defenceHeroIndex < defenceHeroList.size()) {
				//攻击方的英雄出战完毕，防守方的英雄还有未出战的，还需要处理防守方未出战的英雄
				BattleHeroInfo attackHero = null;
				while (true) {
					if (defenceHeroIndex >= defenceHeroList.size()) {
						break;
					}
					
					attackHero = defenceHeroList.get(defenceHeroIndex);
					
					defenceHeroIndex ++;
					
					if (attackHero.isAlive() == true) {
						break;
					}
					
					
				}
				
				if (null != attackHero) {
					if (attackHero.getFury_left() < 100) {
						attack(attackHero, attackHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.DEFENCE_USER.value(), attackRespInfoList, roundIndex);
					} else {
						skillAttack(attackHero, attackHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.DEFENCE_USER.value(), attackRespInfoList, roundIndex);
					}
				}
				
			} else if (defenceHeroIndex >= defenceHeroList.size() && attackHeroIndex < attackHeroList.size()) {
				//防守方的英雄已出战完毕，攻击方的英雄还有未出战的，还需要处理攻击方的未出战的英雄
				
				BattleHeroInfo attackHero = null;
				while (true) {
					if (attackHeroIndex >= attackHeroList.size()) {
						break;
					}
					attackHero = attackHeroList.get(attackHeroIndex);
					
					attackHeroIndex ++;
					
					if (attackHero.isAlive() == true) {
						break;
					}
					
				}
				
				if (null != attackHero) {
					if (attackHero.getFury_left() < 100) {
						attack(attackHero, defenceHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.ATTACK_USER.value(), attackRespInfoList, roundIndex);
					} else {
						skillAttack(attackHero, defenceHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.ATTACK_USER.value(), attackRespInfoList, roundIndex);
					}
					
				}
				
			} else {
				//攻击两方都有英雄要出战时，按照出战顺序依次出手
				if (startAttackUserType == UserTypeEnum.ATTACK_USER.value()) {
					//攻击方先出手
					//i为偶数时遍历攻击方的英雄列表(攻击方先出手，则最先需要遍历攻击方的英雄列表)
					if (attackStep % 2 == 0) {
						BattleHeroInfo attackHero = null;
						while (true) {
							if (attackHeroIndex >= attackHeroList.size()) {
								break;
							}
							attackHero = attackHeroList.get(attackHeroIndex);
							
							attackHeroIndex ++;
							
							if (attackHero.isAlive() == true) {
								break;
							}
						}
						
						if (null != attackHero) {
							if (attackHero.getFury_left() < 100) {
								attack(attackHero, defenceHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.ATTACK_USER.value(), attackRespInfoList, roundIndex);
							} else {
								skillAttack(attackHero, defenceHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.ATTACK_USER.value(), attackRespInfoList, roundIndex);
							}
							
						}
						
					}
					//i为奇数时遍历防守方的英雄列表
					if (attackStep % 2 == 1) {
						BattleHeroInfo attackHero = null;
						while (true) {
							if (defenceHeroIndex >= defenceHeroList.size()) {
								break;
							}
							
							attackHero = defenceHeroList.get(defenceHeroIndex);
							
							defenceHeroIndex ++;
							
							if (attackHero.isAlive() == true) {
								break;
							}
							
						}
						
						if (null != attackHero) {
							if (attackHero.getFury_left()  < 100 ) {
								attack(attackHero, attackHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.DEFENCE_USER.value(), attackRespInfoList, roundIndex);
							} else {
								skillAttack(attackHero, attackHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.DEFENCE_USER.value(), attackRespInfoList, roundIndex);
							}
							
						}
					}
					
				} else {
					//防守方先出手
					//i为偶数时遍历防守方的英雄列表(防守方先出手，则最先需要遍历防守方的英雄列表)
					if (attackStep % 2 == 0) {
						BattleHeroInfo attackHero = null;
						while (true) {
							if (defenceHeroIndex >= defenceHeroList.size()) {
								break;
							}
							
							attackHero = defenceHeroList.get(defenceHeroIndex);
							
							defenceHeroIndex ++;
							
							if (attackHero.isAlive() == true) {
								break;
							}
							
						}
						
						if (null != attackHero) {
							if (attackHero.getFury_left() < 100) {
								attack(attackHero, attackHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.DEFENCE_USER.value(), attackRespInfoList, roundIndex);
							} else {
								skillAttack(attackHero, attackHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.DEFENCE_USER.value(), attackRespInfoList, roundIndex);
							}
							
						}
					}
					//i为奇数时遍历攻击方的英雄列表
					if (attackStep % 2 == 1) {
						BattleHeroInfo attackHero = null;
						while (true) {
							if (attackHeroIndex >= attackHeroList.size()) {
								break;
							}
							attackHero = attackHeroList.get(attackHeroIndex);
							
							attackHeroIndex ++;
							
							if (attackHero.isAlive() == true) {
								break;
							}
						}
						
						if (null != attackHero) {
							if (attackHero.getFury_left() < 100) {
								attack(attackHero, defenceHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.ATTACK_USER.value(), attackRespInfoList, roundIndex);
							} else {
								skillAttack(attackHero, defenceHeroList, battleRecordId, battleRoundRecordId, attackStep, UserTypeEnum.ATTACK_USER.value(), attackRespInfoList, roundIndex);
							}
							
						}
					}
				}
			}
			
			attackStep ++;
			
		} //while (true)循环结束
	}
	
	/**
	 * 每一手的攻击，普通攻击
	 * 1、先获取攻击方英雄在9宫格上的位置
	 * 2、获取当前位置对应的攻击顺序
	 * 3、根据攻击顺序，选择防守方指定位置的英雄进行攻击
	 * @param attackHero   发起攻击的英雄
	 * @param defenceHeroList   防守方的英雄列表
	 * @param battleRecordId  对战的记录id
	 * @param battleRoundRecordId  对战的回合记录id
	 * @param step 每一个回合中的第几步攻击
	 * @param attackUserType 发起攻击的用户类型 0 ： 攻击方  1：防守方
	 * @param attackRespInfoList 战报响应消息中的对战步骤列表,用于记录对战步骤返回给客户端
	 */
	private void attack(BattleHeroInfo attackHero, List<BattleHeroInfo> defenceHeroList, 
			int battleRecordId, int battleRoundRecordId, int step, int attackUserType, 
			List<BattleAttackRespInfo> attackRespInfoList, int roundIndex) {
		
		if (attackHero.isAlive() == false) {
			//出手是
			return;
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		//攻击者在九宫格上的位置
		int position = attackHero.getPosition();
		
		BattleAttackRespInfo attackRespInfo = new BattleAttackRespInfo();
		attackRespInfo.setHolder(attackUserType);
		attackRespInfo.setRole(position);
		attackRespInfo.setSkillId(AttackSkillEnum.COMMON.value());
		
		List<BattleAttackTargetRespInfo> attackTargetRespList = new ArrayList<>();
		
		
		//根据位置获取攻击防守者的顺序
		Integer[] attackOrder = POSITION_ATTACK_ORDER_MAP.get(position);
		
		//被攻击的英雄
		BattleHeroInfo defenceHero = null;
		
		//记录被攻击的英雄在列表中的位置，受到攻击后需要修改列表中对象属性
		int defenceHeroIndex = 0;
		
		for (int i = 0; i < attackOrder.length; i ++) {
			//被攻击英雄的位置
			int attackPosition = attackOrder[i];
			
			for (int j = 0; j < defenceHeroList.size(); j ++) {
				BattleHeroInfo heroInfo = defenceHeroList.get(j);
				//根据被攻击的位置查找被攻击的对象
				if (attackPosition == heroInfo.getPosition() && heroInfo.isAlive()) {
					defenceHero = heroInfo;
					defenceHeroIndex = j;
					break;
				}
			}
			
			if (defenceHero != null) {
				break;
			}
		} //for (int i = 0; i < attackOrder.length; i ++)循环结束
			
		if (defenceHero != null) {
			//攻击方的怒气值 +26
			attackHero.setFury_left(attackHero.getFury_left() + 26);
			
			//插入回合记录，并返回记录对应的id
			int battleRoundStepRecordId = insertBattleRoundStepRecord(battleRecordId, battleRoundRecordId, step, AttackSkillEnum.COMMON.value(), attackHero, now);
			
			//每一次受到的伤害等于攻击方的攻击减去防御方的防御
			Integer hp_harm = attackHero.getAtk() - defenceHero.getDef();
			Integer hp_left = defenceHero.getHp_left() - hp_harm;
			
			BattleHeroInfo waitUpdateHero = defenceHeroList.get(defenceHeroIndex);
			waitUpdateHero.setHp_harm(hp_harm);
			waitUpdateHero.setHp_left(hp_left);
			if (waitUpdateHero.getHp_left() <= 0) {
				waitUpdateHero.setAlive(false);
			}
			//防守方的怒气值+26
			waitUpdateHero.setFury_left(waitUpdateHero.getFury_left() + 26);
			
			insertBattleRoundStepAttackTarget(battleRecordId, battleRoundRecordId, battleRoundStepRecordId, waitUpdateHero, now);
			
			BattleAttackTargetRespInfo attackTargetRespInfo = new BattleAttackTargetRespInfo();
			attackTargetRespInfo.setRole(defenceHero.getPosition());
			attackTargetRespInfo.setDmg(hp_harm);
			
			attackTargetRespList.add(attackTargetRespInfo);
		} else {
			//表示受攻击的对象已经挂了，暂时不处理。如2打1的情况，第一个英雄已经把对方打挂了，第二个英雄出手就无对象可打
			log.info("战斗id：{}, 第{}回合的第{}步攻击, 发起攻击位置{}, 防御方已无英雄可对战", battleRecordId, roundIndex, step, position);
		}
		
		attackRespInfo.setTargets(attackTargetRespList);
		attackRespInfoList.add(attackRespInfo);
	
	}
	
	/**
	 * 每一手的攻击，使用技能攻击（3倍伤害）
	 * 1、先获取攻击方英雄在9宫格上的位置
	 * 2、获取当前位置对应的攻击顺序
	 * 3、根据攻击顺序，选择防守方指定位置的英雄进行攻击
	 * @param attackHero   发起攻击的英雄
	 * @param defenceHeroList   防守方的英雄列表
	 * @param battleRecordId  对战的记录id
	 * @param battleRoundRecordId  对战的回合记录id
	 * @param step 每一个回合中的第几步攻击
	 * @param attackUserType 发起攻击的用户类型 0 ： 攻击方  1：防守方
	 * @param attackRespInfoList 战报响应消息中的对战步骤列表,用于记录对战步骤返回给客户端
	 */
	private void skillAttack(BattleHeroInfo attackHero, List<BattleHeroInfo> defenceHeroList, 
			int battleRecordId, int battleRoundRecordId, int step, int attackUserType, 
			List<BattleAttackRespInfo> attackRespInfoList, int roundIndex) {
		
		if (attackHero.isAlive() == false) {
			//出手是
			return;
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		//攻击者在九宫格上的位置
		int position = attackHero.getPosition();
		
		BattleAttackRespInfo attackRespInfo = new BattleAttackRespInfo();
		attackRespInfo.setHolder(attackUserType);
		attackRespInfo.setRole(position);
		attackRespInfo.setSkillId(AttackSkillEnum.THREE_HUNDRED_HURT.value());
		
		List<BattleAttackTargetRespInfo> attackTargetRespList = new ArrayList<>();
		
		
		//根据位置获取攻击防守者的顺序
		Integer[] attackOrder = POSITION_ATTACK_ORDER_MAP.get(position);
		
		//被攻击的英雄
		BattleHeroInfo defenceHero = null;
		
		//记录被攻击的英雄在列表中的位置，受到攻击后需要修改列表中对象属性
		int defenceHeroIndex = 0;
		
		for (int i = 0; i < attackOrder.length; i ++) {
			//被攻击英雄的位置
			int attackPosition = attackOrder[i];
			
			for (int j = 0; j < defenceHeroList.size(); j ++) {
				BattleHeroInfo heroInfo = defenceHeroList.get(j);
				//根据被攻击的位置查找被攻击的对象
				if (attackPosition == heroInfo.getPosition() && heroInfo.isAlive()) {
					defenceHero = heroInfo;
					defenceHeroIndex = j;
					break;
				}
			}
			
			if (defenceHero != null) {
				break;
			}
		} //for (int i = 0; i < attackOrder.length; i ++)循环结束
			
		if (defenceHero != null) {
			//攻击方的怒气值清0
			attackHero.setFury_left(0);
			
			//插入回合记录，并返回记录对应的id
			int battleRoundStepRecordId = insertBattleRoundStepRecord(battleRecordId, battleRoundRecordId, step, AttackSkillEnum.THREE_HUNDRED_HURT.value(), attackHero, now);
			
			//每一次收到的伤害等于攻击方的攻击减去防御方的防御
			Integer hp_harm = (attackHero.getAtk() - defenceHero.getDef()) * 3;
			Integer hp_left = defenceHero.getHp_left() - hp_harm;
			
			BattleHeroInfo waitUpdateHero = defenceHeroList.get(defenceHeroIndex);
			waitUpdateHero.setHp_harm(hp_harm);
			waitUpdateHero.setHp_left(hp_left);
			if (waitUpdateHero.getHp_left() <= 0) {
				waitUpdateHero.setAlive(false);
			}
			//防守方的怒气值+26
			waitUpdateHero.setFury_left(waitUpdateHero.getFury_left() + 26);
			
			insertBattleRoundStepAttackTarget(battleRecordId, battleRoundRecordId, battleRoundStepRecordId, waitUpdateHero, now);
			
			BattleAttackTargetRespInfo attackTargetRespInfo = new BattleAttackTargetRespInfo();
			attackTargetRespInfo.setRole(defenceHero.getPosition());
			attackTargetRespInfo.setDmg(hp_harm);
			
			attackTargetRespList.add(attackTargetRespInfo);
		} else {
			//表示受攻击的对象已经挂了，暂时不处理。如2打1的情况，第一个英雄已经把对方打挂了，第二个英雄出手就无对象可打
			log.info("战斗id：{}, 第{}回合的第{}步攻击, 发起攻击位置{}, 防御方已无英雄可对战", battleRecordId, roundIndex, step, position);
		}
		
		attackRespInfo.setTargets(attackTargetRespList);
		attackRespInfoList.add(attackRespInfo);
	
	}
	
	
	
	/**
	 * 插入回合中，每一步战斗的记录
	 * @param battleRecordId        战斗的记录id
	 * @param battleRoundRecordId   战斗的回合记录id
	 * @param step                  战斗的步骤0,1,2...
	 * @param skillId               使用的技能
	 * @param attackHero            发起攻击的英雄
	 * @param now
	 * @return 插入记录的id
	 */
	private int insertBattleRoundStepRecord(int battleRecordId, int battleRoundRecordId, 
			int step, int skillId, BattleHeroInfo attackHero, LocalDateTime now) {
		BattleRoundStepRecord battleRoundStepRecord = new BattleRoundStepRecord();
		battleRoundStepRecord.setBattleRecordId(battleRecordId);
		battleRoundStepRecord.setBattleRoundId(battleRoundRecordId);
		battleRoundStepRecord.setStep(step);
		battleRoundStepRecord.setAttackUserId(attackHero.getUserId());
		battleRoundStepRecord.setAttackHeroId(attackHero.getId());
		battleRoundStepRecord.setAttackPosition(attackHero.getPosition());
		battleRoundStepRecord.setAttackSkill(skillId);
		battleRoundStepRecord.setAttackHeroFuryLeft(attackHero.getFury_left());
		
		battleRoundStepRecord.setCreateTime(now);
		battleRoundStepRecord.setUpdateTime(now);
		int insertRow = roundStepRecordMapper.insertReturnId(battleRoundStepRecord);
		int returnId = battleRoundStepRecord.getId();
		return returnId;
	}
	
	/**
	 * 插入攻击对象，返回插入影响的记录条数
	 * @param battleRecordId
	 * @param battleRoundRecordId
	 * @param battleRoundStepRecordId
	 * @param defenceHero
	 * @param now
	 * @return
	 */
	private int insertBattleRoundStepAttackTarget(int battleRecordId, int battleRoundRecordId, 
			int battleRoundStepRecordId, BattleHeroInfo defenceHero, LocalDateTime now) {
		BattleRoundStepAttackTargetRecord attackTargetRecord =new BattleRoundStepAttackTargetRecord();
		attackTargetRecord.setBattleRecordId(battleRecordId);
		attackTargetRecord.setBattleRoundId(battleRoundRecordId);
		attackTargetRecord.setBattleRoundStepId(battleRoundStepRecordId);
		attackTargetRecord.setDefenceUserId(defenceHero.getUserId());
		attackTargetRecord.setDefenceHeroId(defenceHero.getId());
		attackTargetRecord.setHpHarm(defenceHero.getHp_harm());
		attackTargetRecord.setHpLeft(defenceHero.getHp_left());
		attackTargetRecord.setPosition(defenceHero.getPosition());
		attackTargetRecord.setDefenceHeroFuryLeft(defenceHero.getFury_left());
		attackTargetRecord.setCreateTime(now);
		attackTargetRecord.setUpdateTime(now);
		
		int insertRow = roundStepAttackTargetRecordMapper.insert(attackTargetRecord);
		return insertRow;
	}
}
