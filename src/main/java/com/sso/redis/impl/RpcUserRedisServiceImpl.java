package com.sso.redis.impl;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sso.redis.IRpcUserRedisService;
import com.sso.rpc.RpcUser;
import com.util.Seq;

/**
 * 
 * @ClassName: RpcUserRedisService 
 * @Description: 缓存登录用户信息
 * @author 张龙龙 
 * @date 2018年3月4日 下午1:33:53 
 */
@Service("rpcUserRedisService")
public class RpcUserRedisServiceImpl implements IRpcUserRedisService {

	/**
     * 用于缓存登录的用户
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, RpcUser> userCache;
    private static final String PREFIX_USERCACHE = "RPCUSER:PC_TOKEN:";
    
    /**
     * 
     * @Title: cacheUser 
     * @Description: 缓存数据到redis
     * @author 张龙龙
     * @date 2018年3月4日 下午2:15:42
     * @param @param rpcUser     
     * @return void     
     * @throws 
     */
	@Override
	public void cacheUser(RpcUser rpcUser) {
		String redisKey = PREFIX_USERCACHE + rpcUser.getId();
        userCache.set(redisKey,rpcUser);
        //userCache.set(redisKey,rpcUser, 5, TimeUnit.HOURS);
	}

	/**
     * 
     * @Title: delUser 
     * @Description: 从redis清除缓存数据
     * @author 张龙龙
     * @date 2018年3月4日 下午2:16:06
     * @param @param userId     
     * @return void     
     * @throws 
     */
	@Override
    public void delUser(String userId){
    	System.out.println("从redis缓存中清除用户 : " + userId);
        userCache.getOperations().delete(PREFIX_USERCACHE + userId);
    }
   
    /**
     * 
     * @Title: syncUserInfo 
     * @Description: 执行业务操作, 将业务数据同步至redis
     * @author 张龙龙
     * @date 2018年3月4日 下午2:16:34
     * @param @param userId     
     * @return void     
     * @throws 
     */
	@Override
    public void syncUserInfo(String userId){
    	//TODO 自己的业务处理
    	RpcUser user = new RpcUser();
		user.setId(Seq.createSeqNum()+"");
        user.setNickName("yn" + user.getId() + " _redisUser");
        
        cacheUser(user);
    }
    
    /**
     * 
     * @Title: getUserByKey 
     * @Description: 根据redis的key值获取相应缓存数据
     * @author 张龙龙
     * @date 2018年3月4日 下午2:17:05
     * @param @param redisKey
     * @param @return     
     * @return RpcUser     
     * @throws 
     */
	@Override
    public RpcUser getUserByKey(String redisKey){
        RpcUser user = userCache.get(redisKey);
        return user;
    }
    
    /**
     * 
     * @Title: getUserById 
     * @Description: 根据用户id从redis上获取缓存数据
     * @author 张龙龙
     * @date 2018年3月4日 下午2:17:35
     * @param @param userId
     * @param @return     
     * @return RpcUser     
     * @throws 
     */
	@Override
    public RpcUser getUserById(String userId) {
    	System.out.println("从redis缓存中获取用户 : " + userId);
    	
        String redisKey = PREFIX_USERCACHE + userId;
        RpcUser rpcUser = getUserByKey(redisKey);
        if(rpcUser == null){
            syncUserInfo(userId);
            rpcUser = userCache.get(redisKey);
        }
        return rpcUser;
    }

}
