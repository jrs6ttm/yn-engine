package com.core.base.mybatis.service;

import com.core.base.mybatis.dao.YNBaseDao;

/**
 * 
 * @ClassName: YNBaseServiceImpl 
 * @Description: Mybatis ServiceImpl基础类,ServiceImpl类直接继承该类
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:25:55 
 */
public abstract class YNBaseServiceImpl<T,PK> implements YNBaseService<T,PK> {

    /**
     * 获取具体的实现Dao,继承方需要实现该方法
     * @return
     */
    public abstract YNBaseDao<T, PK> getDao();

    @Override
    public int deleteByPrimaryKey(PK modelPK) {
        return getDao().deleteByPrimaryKey(modelPK);
    }

    @Override
    public int insert(T model) {
        return getDao().insert(model);
    }

    @Override
    public int insertSelective(T model) {
        return getDao().insertSelective(model);
    }

    @Override
    public T selectByPrimaryKey(PK modelPK) {
        return getDao().selectByPrimaryKey(modelPK);
    }

    @Override
    public int updateByPrimaryKeySelective(T model) {
        return getDao().updateByPrimaryKeySelective(model);
    }

    @Override
    public int updateByPrimaryKey(T model) {
        return getDao().updateByPrimaryKey(model);
    }
}
