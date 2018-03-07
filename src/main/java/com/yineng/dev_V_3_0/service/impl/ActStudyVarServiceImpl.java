package com.yineng.dev_V_3_0.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yineng.dev_V_3_0.dao.ActStudyVarMapper;
import com.yineng.dev_V_3_0.model.ActStudyVar;
import com.yineng.dev_V_3_0.service.IActStudyVarService;

@Service("actStudyVarService")
public class ActStudyVarServiceImpl implements IActStudyVarService {

	@Resource
	private ActStudyVarMapper actStudyVarMapper;
	
	@Override
	public int deleteByPrimaryKey(String varid) {
		return actStudyVarMapper.deleteByPrimaryKey(varid);
	}

	@Override
	public int insert(ActStudyVar record) {
		return actStudyVarMapper.insert(record);
	}

	@Override
	public int insertSelective(ActStudyVar record) {
		return actStudyVarMapper.insertSelective(record);
	}

	@Override
	public ActStudyVar selectByPrimaryKey(String varid) {
		return actStudyVarMapper.selectByPrimaryKey(varid);
	}

	@Override
	public int updateByPrimaryKeySelective(ActStudyVar record) {
		return actStudyVarMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ActStudyVar record) {
		return actStudyVarMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ActStudyVar> selectActStudyVars(ActStudyVar record) {
		return actStudyVarMapper.selectStudyVarList(record);
	}

	@Override
	public int updateActStudyVar(ActStudyVar record) {
		return actStudyVarMapper.updateActStudyVar(record);
	}

}
