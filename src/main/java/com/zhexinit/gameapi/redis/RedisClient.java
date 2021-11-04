package com.zhexinit.gameapi.redis;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisClient<K, V> {
	
	@Autowired
	private RedisTemplate<K, V> redisTemplate;
	private final long DEFAULT_EXPIRE_TIME = 7 * 24 * 60 * 60;
	@Autowired
	private KryoRedisSerializer<Object> kryoSerializer;

	/**
	 * 放入缓存服务器，默认存活24小时
	 * 
	 * @param key
	 *            缓存key值
	 * @param value
	 *            缓存value值(object对象)
	 */
	public boolean put(K key, V value) {
		BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
		valueOper.set(value);
		boolean cacheResult = redisTemplate.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
		log.info("RedisClient::put, default expire time(7days), cacheKey={}, value={}, cacheResult={}", key, value, cacheResult);
		return cacheResult;
	}

	/**
	 * 放入缓存服务器
	 * 
	 * @param key
	 *            缓存key值
	 * @param value
	 *            缓存value值(object对象)
	 * @param ttl
	 *            缓存有效时间,单位秒,-1表示永久缓存
	 */
	public boolean put(K key, V value, long ttl) {
		BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
		valueOper.set(value);
		boolean cacheResult = false;
		if (ttl > 0) {
			cacheResult = redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
		}
		log.info("RedisClient::put, with expire time,cacheKey={}, value={}, expire={} cacheResult={}", key, value, ttl, cacheResult);
		return cacheResult;
	}

	/**
	 * 保存对象对缓存服务器
	 * 
	 * @param key
	 *            缓存key
	 * @param value
	 *            缓存值
	 * @param expiredDate
	 *            缓存截止日期
	 */
	public boolean put(K key, V value, Date expiredDate) {
		BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
		valueOper.set(value);
		boolean cacheResult = redisTemplate.expireAt(key, expiredDate);
		log.info("RedisClient::put, with expiredDate,cacheKey={}, value={}, expiredDate={} cacheResult={}", key, value, expiredDate, cacheResult);
		return cacheResult;
	}

	/**
	 * 获取缓存服务器存储结果值
	 * 
	 * @param key
	 *            缓存key值
	 * @return 返回结果对象
	 */
	public Object get(K key) {
		BoundValueOperations<K, V> boundValueOper = redisTemplate.boundValueOps(key);
		if (boundValueOper == null) {
			return null;
		}
		return boundValueOper.get();
	}

	/**
	 * 保存List类型数据,存活时间为7 * 24小时
	 * 
	 * @param key 存储key
	 * @param value 单个对象的值
	 */
	@SuppressWarnings("unchecked")
	public void putList(final K key, final V value) {
		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
//				operations.delete(key);
				BoundListOperations<K, V> listOper = operations.boundListOps(key);
				listOper.leftPush(value);
				operations.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
				List resultList = operations.exec(); //resultList=[2, true], 2表示key缓存的列表中包含的对象个数
				log.info("RedisClient::putList, default expire time(7days), cacheKey={}, cacheValue={}, resultList={}", key, value, resultList);
				return null;
			}
		});

	}

	/**
	 * 保存list类型数据
	 * 
	 * @param key 存储key
	 * @param value 单个对象的值
	 * @param ttl 存活时间,单位秒
	 */
	@SuppressWarnings("unchecked")
	public void putList(final K key, final V value, final long ttl) {

		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
//				operations.delete(key);
				BoundListOperations<K, V> listOper = operations.boundListOps(key);
				listOper.leftPush(value);
				operations.expire(key, ttl, TimeUnit.SECONDS);
				List resultList = operations.exec();  //resultList=[2, true], 2表示key缓存的列表中包含的对象个数
				log.info("RedisClient::putList, expire time={}, cacheKey={}, cacheValue={}, resultList={}", ttl, key, value, resultList);
				return null;
			}
		});
	}

	/**
	 * 保存list类型数据
	 * 
	 * @param key 存储key
	 * @param value 单个对象的值
	 * @param expiredDate 存活截止日期
	 */
	public void putList(final K key, final V value, final Date expiredDate) {

		redisTemplate.execute(new SessionCallback<Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
//				operations.delete(key);
				BoundListOperations<K, V> listOper = operations.boundListOps(key);
				listOper.leftPush(value);
				operations.expireAt(key, expiredDate);
				List resultList = operations.exec(); //resultList=[2, true], 2表示key缓存的列表中包含的对象个数
				log.info("RedisClient::putList, expire Date={}, cacheKey={}, cacheValue={}, resultList={}", 
						expiredDate, key, value, resultList);
				return null;
			}
		});
	}

	/**
	 * 获取List值
	 * 
	 * @param key
	 * @return 获取结果为List类型的值
	 */
	public List<? extends Object> getList(K key) {
		BoundListOperations<K, V> listOper = redisTemplate.boundListOps(key);
		if (listOper == null) {
			return null;
		}
		return listOper.range(0, -1);
	}

	public void remove(K key) {
		redisTemplate.delete(key);
	}

	public void remove(Collection<K> ks) {
		redisTemplate.delete(ks);
	}

	public long getExpire(K key) {
		return redisTemplate.getExpire(key);
	}

	public boolean containsKey(K key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 保存Set类型数据
	 * 
	 * @param key 存储key
	 * @param value 单个对象的值
	 * @param ttl 存活时间,单位秒
	 */
	@SuppressWarnings("unchecked")
	public void putSet(final K key, final V value, final long ttl) {
		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
//				operations.delete(key);
				BoundSetOperations<K, V> setOper = operations.boundSetOps(key);
				setOper.add(value);
				operations.expire(key, ttl, TimeUnit.SECONDS);
				List resultList = operations.exec();
				log.info("RedisClient::putSet, expire time={}, cacheKey={}, cacheValue={}, resultList={}", ttl, key, value, resultList);
				return null;
			}
		});
	}

	/**
	 * 保存Set类型数据
	 * 
	 * @param key 存储key
	 * @param value  单个对象的值
	 * @param expiredDate 存活截止日期
	 */
	@SuppressWarnings("unchecked")
	public void putSet(final K key, final V value, final Date expiredDate) {

		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
//				operations.delete(key);
				BoundSetOperations<K, V> setOper = operations.boundSetOps(key);
				setOper.add(value);
				operations.expireAt(key, expiredDate);
				List resultList = operations.exec();
				log.info("RedisClient::putSet, expire Date={}, cacheKey={}, cacheValue={}, resultList={}", 
						expiredDate, key, value, resultList);
				return null;
			}
		});
	}

	/**
	 * 保存set类型的值，默认有效期1天
	 * 
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void putSet(final K key, final V value) {

		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
//				operations.delete(key);
				BoundSetOperations<K, V> setOper = operations.boundSetOps(key);
				setOper.add(value);
				operations.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
				List resultList = operations.exec();
				log.info("RedisClient::putSet, default expire time (7days), cacheKey={}, cacheValue={}, resultList={}", 
						key, value, resultList);
				return null;
			}
		});
	}

	/**
	 * 获取Set类型值
	 * 
	 * @param key
	 * @return
	 */
	public Set<V> getSet(K key) {
		BoundSetOperations<K, V> setOper = redisTemplate.boundSetOps(key);
		return setOper.members();
	}

	public void putHash(final K key, final Map<? extends V, ? extends Object> value) {
		redisTemplate.execute(new SessionCallback<Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public Object execute(@SuppressWarnings("rawtypes") RedisOperations operations) throws DataAccessException {
				operations.watch(rawKey(key));
				operations.multi();
				operations.delete(key);
				BoundHashOperations<K, Object, Object> hashOper = redisTemplate.boundHashOps(key);
				hashOper.putAll(value);
				operations.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
				List resultList = operations.exec();
				log.info("RedisClient::putHash, default expire time (7days), cacheKey={}, cacheValue={}, resultList={}", 
						key, value, resultList);
				return null;
			}
		});
	}

	/**
	 * 获取hash值
	 * 
	 * @param key
	 * @return 获取结果为hash类型的值
	 */
	public Map<Object, Object> getHash(K key) {
		BoundHashOperations<K, Object, Object> hashOper = redisTemplate.boundHashOps(key);
		if (hashOper == null) {
			return null;
		}
		return hashOper.entries();
	}

	public void kset(final K key, V value, final long ttl) {
		final byte[] bytes = kryoSerializer.serialize(value);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(rawKey(key), ttl, bytes);
				return null;
			}
		});
	}

	public void kset(final K key, V value) {
		final byte[] bytes = kryoSerializer.serialize(value);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(rawKey(key), DEFAULT_EXPIRE_TIME, bytes);
				return null;
			}
		});
	}

	public Object kget(final K key) {
		Object result = redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bytes = connection.get(rawKey(key));
				if (bytes == null) {
					return null;
				}
				return kryoSerializer.deserialize(bytes);
			}
		});
		return result;
	}

	public long incrementBy(final K key, final long incrBy, final long ttl) {
		Object obj = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				long ret = connection.incrBy(rawKey(key), incrBy);
				connection.expire(rawKey(key), ttl);
				return ret;
			}
		});
		return (Long) obj;
	}

	public long decrement(final K key, final long ttl) {
		Object obj = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				long ret = connection.decr(rawKey(key));
				connection.expire(rawKey(key), ttl);
				return ret;
			}
		});
		return (Long) obj;
	}

	private byte[] rawKey(K key) {
		return key.toString().getBytes();
	}

	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setKryoSerializer(KryoRedisSerializer<Object> kryoSerializer) {
		this.kryoSerializer = kryoSerializer;
	}

	public Long getLong(final K key) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bytes = connection.get(rawKey(key));
				if (bytes == null) {
					return null;
				}

				return new GenericToStringSerializer<Long>(Long.class).deserialize(bytes);
			}
		});

		return result;
	}

	/**
	 * 将值放到队列中去
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long putToQueue(final K key, final V value) {
		redisTemplate.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 从队列中取值
	 * 
	 * @param key
	 * @return
	 */
	public Object pollFromQueue(K key) {
		return redisTemplate.opsForList().rightPop(key);
	}

	public V rangeZSetFirstAndRemove(K key) {
		RedisSerializer keySerializer = redisTemplate.getKeySerializer();

		final byte[] rawKey = keySerializer.serialize(key);

		Set<byte[]> rawValues = redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

			public Set<byte[]> doInRedis(RedisConnection connection) {
				Set<byte[]> rawValues = connection.zRange(rawKey, 0, 0);
				for (Iterator<byte[]> iter = rawValues.iterator(); iter.hasNext();) {
					byte[] rawValue = iter.next();
					long elementNum = connection.zRem(rawKey, rawValue);
					if (elementNum == 0) {
						iter.remove();
					}
					// System.out.println(elementNum);
				}
				// for (byte[] rawValue : rawValues) {
				// long ind = connection.zRem(rawKey, rawValue);
				// System.out.println(ind);
				// }
				// System.out.println(rawValues.size());
				return rawValues;
			}
		}, true);

		if (rawValues != null && rawValues.size() > 0) {
			return (V) keySerializer.deserialize(rawValues.iterator().next());
		}

		return null;
	}

	public Set<V> rangeByScoreAndRemove(K key, final double min, final double max, final long offset,
			final long count) {
		RedisSerializer keySerializer = redisTemplate.getKeySerializer();

		final byte[] rawKey = keySerializer.serialize(key);

		Set<byte[]> rawValues = redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

			public Set<byte[]> doInRedis(RedisConnection connection) {
				Set<byte[]> rawValues = connection.zRangeByScore(rawKey, min, max, offset, count);
				for (Iterator<byte[]> iter = rawValues.iterator(); iter.hasNext();) {
					byte[] rawValue = iter.next();
					long elementNum = connection.zRem(rawKey, rawValue);
					if (elementNum == 0) {
						iter.remove();
					}
					// System.out.println(elementNum);
				}
				// for (byte[] rawValue : rawValues) {
				// long ind = connection.zRem(rawKey, rawValue);
				// System.out.println(ind);
				// }
				return rawValues;
			}
		}, true);
		Set<V> set = new LinkedHashSet<V>();
		for (byte[] rawValue : rawValues) {
			V value = (V) keySerializer.deserialize(rawValue);
			set.add(value);
		}
		return set;
	}

	public boolean putZset(K key, V value, double score) {
		return redisTemplate.opsForZSet().add(key, value, score);
	}

	public long getZsetSize(K key) {
		return redisTemplate.opsForZSet().size(key);
	}
}
