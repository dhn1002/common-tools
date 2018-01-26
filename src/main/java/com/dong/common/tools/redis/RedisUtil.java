package com.dong.common.tools.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
  
/** 
 * redis工具类 
 *  
 * @author jingyuannet 
 */  
public final class RedisUtil {  
  
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class); 
  
    private static String ADDR = "127.0.0.1";
  
    private static int PORT = 6379;  
  
    private static String AUTH = "123456";  
  
    private static int MAX_ACTIVE = 300;  
  
    private static int MAX_IDLE = 200;  
  
    private static int MAX_WAIT = 10000;  
  
    private static int TIMEOUT = 10000;  
  
    private static boolean TEST_ON_BORROW = true;  
  
    private static JedisPool jedisPool = null;  
  
    private static Jedis jedis = null;  
  
    /** 
     * 初始化Redis连接池 
     */  
    static {  
        try {  
            init();  
        } catch (Exception e) {  
            logger.error("初始化Redis出错", e);  
        }  
    }

    /**
     * 初始化连接池 
     *  
     * @see [类、类#方法、类#成员] 
     */  
    private synchronized static void init() {  
//    	ADDR = (String) LoadProperty.globalMap.get("redis.addr");
//        PORT = Integer.valueOf((String) LoadProperty.globalMap.get("redis.port"));
//        AUTH = (String) LoadProperty.globalMap.get("redis.auth");
//        MAX_ACTIVE = Integer.valueOf((String) LoadProperty.globalMap.get("redis.pool.max_active"));
//        MAX_IDLE = Integer.valueOf((String) LoadProperty.globalMap.get("redis.pool.max_idle"));
//        MAX_WAIT = Integer.valueOf((String) LoadProperty.globalMap.get("redis.pool.max_wait"));
//        TIMEOUT = Integer.valueOf((String) LoadProperty.globalMap.get("redis.pool.timeout"));
  
        JedisPoolConfig config = new JedisPoolConfig();  
        config.setMaxIdle(MAX_IDLE);  
        config.setMaxWaitMillis(MAX_WAIT);  
        config.setTestOnBorrow(TEST_ON_BORROW);  
        config.setMaxTotal(MAX_ACTIVE);  
        jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);  
    }  
    
    public static void shutdown() {
		if (jedisPool != null) {
			jedisPool.close();
		}
	}
  
    /** 
     * 获取Jedis实例 
     *  
     * @return 
     */  
    private static Jedis getJedis() {  
        try {  
            if (jedisPool != null) {  
                jedis = jedisPool.getResource();  
            } else {  
                init();  
                jedis = jedisPool.getResource();  
            }  
        } catch (Exception e) {  
            logger.error("获取Redis实例出错", e);  
        }  
        return jedis;  
    }  
  
    /** 
     * 设置单个值 
     *  
     * @param key 
     * @param value 
     * @return 
     */  
    public static String set(String key, String value) {  
        return set(key, value, null);  
    }  
  
    /** 
     * 设置单个值，并设置超时时间 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param timeout 
     *            超时时间（秒） 
     * @return 
     * @see [类、类#方法、类#成员] 
     */  
    public static String set(String key, String value, Integer timeout) {
    	logger.debug("存入缓存：key【{}】，value【{}】，timeout【{}】",key, value, timeout);
        String result = null;  
  
        Jedis jedis = RedisUtil.getJedis();  
        if (jedis == null) {  
            return result;  
        }  
        try {  
            result = jedis.set(key, value);  
            if (null != timeout) {  
                jedis.expire(key, timeout);  
            }  
        } catch (Exception e) {  
            logger.error("存入缓存出错", e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 获取单个值 
     *  
     * @param key 
     * @return 
     */  
    public static String get(String key) {  
        String result = null;  
        Jedis jedis = RedisUtil.getJedis();  
        if (jedis == null) {  
            return result;  
        }  
        try {  
            result = jedis.get(key);  
        } catch (Exception e) {  
            logger.error("获取缓存出错", e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 删除redis中数据 
     *  
     * @param key 
     * @return 
     * @see [类、类#方法、类#成员] 
     */  
    public static Long del(String key) {  
    	logger.debug("删除缓存：key【{}】", key);
    	Long result = Long.valueOf(0);  
        Jedis jedis = RedisUtil.getJedis();  
        if (null == jedis) {  
            return result;  
        }  
        try {  
        	result = jedis.del(key);  
        } catch (Exception e) {  
            logger.error("删除缓存出错" , e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 追加 
     *  
     * @param key 
     * @param value 
     * @return 
     * @see [类、类#方法、类#成员] 
     */  
    public static Long append(String key, String value) {  
    	logger.debug("追加缓存：key【{}】，value【{}】", key, value);
        Long result = Long.valueOf(0);  
        Jedis jedis = RedisUtil.getJedis();  
        if (null == jedis) {  
            return result;  
        }  
        try {  
            result = jedis.append(key, value);  
        } catch (Exception e) {  
            logger.error("追加缓存出错" , e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 检测是否存在 
     *  
     * @param key 
     * @return 
     * @see [类、类#方法、类#成员] 
     */  
    public static Boolean exists(String key) {  
        Boolean result = Boolean.FALSE;  
        Jedis jedis = RedisUtil.getJedis();  
        if (null == jedis) {  
            return result;  
        }  
        try {  
            result = jedis.exists(key);  
        } catch (Exception e) {  
            logger.error("检查是否存在出错" , e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;  
    }  
    
    /**
     * 设置redis列表
     * @param key
     * @param values
     * @return
     */
    public static Long lpush(String key,String... values) {
    	logger.debug("存入list缓存：key【{}】，value【{}】", key, values);
    	Long result = Long.valueOf(0); 
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.lpush(key,values);  
        } catch (Exception e) {  
            logger.error("存入缓存出错" , e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    /** 
     * @param key 
     * @return 
     */  
    public static List<String> lrange(String key) {  
    	List<String> result = null;  
        Jedis jedis = RedisUtil.getJedis();  
        if (jedis == null) {  
            return result;  
        }  
        try {  
            result = jedis.lrange(key,0,-1);  
        } catch (Exception e) {  
            logger.error("获取缓存出错", e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;  
    }  
    
    public static String hmset(String key,Map<String, String> map) {
    	logger.debug("存入hash缓存：key【{}】，value【{}】",key,map);
    	String result = null;
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.hmset(key, map); 
        } catch (Exception e) {  
        	e.printStackTrace();
            logger.error("存入缓存出错" , e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    public static List<String> hmget(String key,String... fields) {
    	List<String> result = null;
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.hmget(key, fields);
        } catch (Exception e) {  
            logger.error("获取缓存出错", e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    public static Long hset(String key, String field, String value) {
    	logger.debug("存入hash缓存：key【{}】，field【{}】，value【{}】", key, field, value);
    	Long result = Long.valueOf(0);
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.hset(key, field ,value); 
        } catch (Exception e) {  
            logger.error("存入缓存出错",e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    public static String hget(String key,String field) {
    	String result = null;
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.hget(key, field);
        } catch (Exception e) {  
        	logger.error("获取缓存出错", e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    public static Map<String, String> hgetall(String key) {
    	Map<String, String> result = null;
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.hgetAll(key);
        } catch (Exception e) {  
        	logger.error("获取缓存出错", e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    public static Long hdel(String key,String... fields) {
    	logger.debug("删除缓存：key【{}】，field【{}】",key,fields);
    	Long result = Long.valueOf(0);
    	Jedis jedis = RedisUtil.getJedis();
		try {  
			result = jedis.hdel(key, fields);
        } catch (Exception e) {  
            logger.error("删除缓存出错" , e);  
        } finally {  
            if (null != jedis) {  
                jedis.close();  
            }  
        }  
        return result;
	}
    
    public static void main(String[] args) {
	}
}  