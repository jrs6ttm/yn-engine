package com.sso.redis;

import com.sso.rpc.RpcUser;

public interface IRpcUserRedisService {

	public void cacheUser(RpcUser rpcUser);
	
	public void delUser(String userId);
	
	public void syncUserInfo(String userId);
	
	public RpcUser getUserByKey(String redisKey);
	
	public RpcUser getUserById(String userId);
}
