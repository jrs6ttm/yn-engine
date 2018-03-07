package com.yineng.dev_V_3_0.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yineng.dev_V_3_0.dao.ActStudyCommentMapper;
import com.yineng.dev_V_3_0.model.ActStudyComment;
import com.yineng.dev_V_3_0.service.IActStudyCommentService;

@Service("actStudyCommentService")
public class ActStudyCommentServiceImpl implements IActStudyCommentService {

	@Resource
	private ActStudyCommentMapper actStudyCommentMapper;
	
	@Override
	public int deleteByPrimaryKey(String commentid) {
		return actStudyCommentMapper.deleteByPrimaryKey(commentid);
	}

	@Override
	public int insert(ActStudyComment record) {
		return actStudyCommentMapper.insert(record);
	}

	@Override
	public int insertSelective(ActStudyComment record) {
		return actStudyCommentMapper.insertSelective(record);
	}

	@Override
	public ActStudyComment selectByPrimaryKey(String commentid) {
		return actStudyCommentMapper.selectByPrimaryKey(commentid);
	}

	@Override
	public int updateByPrimaryKeySelective(ActStudyComment record) {
		return actStudyCommentMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ActStudyComment record) {
		return actStudyCommentMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ActStudyComment> selectActStudyComments(ActStudyComment record) {
		return actStudyCommentMapper.selectActStudyComments(record);
	}

}
