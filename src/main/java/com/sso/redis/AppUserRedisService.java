package com.sso.redis;

import com.sso.rpc.RpcUser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;


@Service
public class AppUserRedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> redisCache;
    private static final String PREFIX_USERID_TOKEN = "APP_TOKEN:USERID:";

    /**
     * 用于缓存登录的app用户
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, RpcUser> userCache;
    private static final String PREFIX_USERCACHE = "RPCUSER:APP_TOKEN:";

    /**
     * 获取账户信息
     * @param token app登录的token
     * @return
     */
    public RpcUser getUserByToken(String token){
        String redisKey = PREFIX_USERCACHE+token;
        RpcUser user = userCache.get(redisKey);
        return user;
    }

    public Set<String> getAllAppUserKeys(){
        Set<String> tokens = userCache.getOperations().keys(PREFIX_USERCACHE+"*");
        return tokens;
    }

    public RpcUser getUserByKey(String redisKey){
        RpcUser user = userCache.get(redisKey);
        return user;
    }
    /*
     * 获取账户信息
     * @return
     */
 /*   public RpcUser getAllUser(){
        RpcUser user = userCache.getOperations().opsForList();
        return user;
    }*/
    /**
     * 根据userId返回登录中的app用户
     * @param userId
     * @return
     */
    public RpcUser getLoginUserByUserId(String userId){
        String redisKey = PREFIX_USERID_TOKEN+userId;
        String token = redisCache.get(redisKey);
        RpcUser user = getUserByToken(token);
        return user;
    }

    public void cacheUser(String token, RpcUser user) {
        String redisKey = PREFIX_USERID_TOKEN+user.getId();
        String oldToken = redisCache.get(redisKey);
        if(!StringUtils.isEmpty(oldToken)){
            //删除失效的token
            delUser(oldToken);
        }
        redisCache.set(redisKey,token);
        userCache.set(PREFIX_USERCACHE+token,user);
    }

    public void delUser(String token){
        userCache.getOperations().delete(PREFIX_USERCACHE+token);
    }
}
