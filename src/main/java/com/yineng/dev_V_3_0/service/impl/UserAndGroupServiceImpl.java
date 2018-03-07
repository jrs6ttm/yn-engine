package com.yineng.dev_V_3_0.service.impl;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yineng.dev_V_3_0.service.IUserAndGroupService;

@Service("userAndGroupService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class UserAndGroupServiceImpl implements IUserAndGroupService{
	@Autowired
	private IdentityService  identityService;
	
	@Override
	@CacheEvict(value = {"createUser"}, allEntries = true)
	public void createUser(String userId){
		long userCount = identityService.createUserQuery().userId(userId).count();
		if(userCount == 0){
			User user = identityService.newUser(userId);
			//user.setFirstName("");
			//user.setLastName("");
			//user.setPassword("");
			identityService.saveUser(user);
		}
	}
	
	@Override
	@CachePut(value = {"createGrop"})
	public void createGroup(String groupId){
		long groupCount = identityService.createGroupQuery().groupId(groupId).count();
		if(groupCount == 0){
			Group group = identityService.newGroup(groupId);
			//group.setName("");
			//group.setType("");
			identityService.saveGroup(group);
		}
	}
	
	@Override
	@CachePut(value = {"createMembership"})
	public void createMembership(String userId, String groupId){
		  
		  this.createUser(userId);
		  this.createGroup(groupId);
		  
		  identityService.createMembership(userId, groupId);
	}
	
	@Override
	@CacheEvict(value = { "getUser", "getGroup" , "getMembership"}, allEntries = true)
	public void deleteGroup(String groupId){
		identityService.deleteGroup(groupId);
	}
	
	@Override
	@CacheEvict(value = { "getUser", "getGroup" , "getMembership"}, allEntries = true)
	public void deleteUser(String userId){
		identityService.deleteUser(userId);
	}
	
	@Override
	@CacheEvict(value = { "getUser", "getGroup" , "getMembership"}, allEntries = true)
	public void deleteMembership(String userId, String groupId){
		identityService.deleteMembership(userId, groupId);
		
		this.deleteUser(userId);
		this.deleteGroup(groupId);
	}

	@Override
	@Cacheable("getUser")
	public void getUser(String userId) {
		
		
	}

	@Override
	public void getGroup(String groupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMembership(String userId, String groupId) {
		// TODO Auto-generated method stub
		
	}
}
