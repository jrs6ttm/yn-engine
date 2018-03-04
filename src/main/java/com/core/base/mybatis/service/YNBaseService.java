package com.core.base.mybatis.service;

/**
 * 
 * @ClassName: YNBaseService 
 * @Description: Mybatis Service基础类,Service接口直接继承该类
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:25:40 
 */
public interface YNBaseService<T,PK> {
    /**
     * 根据主键删除记录
     * @param modelPK
     * @return
     */
    int deleteByPrimaryKey(PK modelPK);

    /**
     * 插入
     * @param model
     * @return
     */
    int insert(T model);

    /**
     * 插入非null的记录
     * @param model
     * @return
     */
    int insertSelective(T model);

    /**
     * 根据主键查询出记录
     * @param modelPK
     * @return
     */
    T selectByPrimaryKey(PK modelPK);
    
    /**
     * 根据主键更新非null的属性
     * @param model
     * @return
     */
    int updateByPrimaryKeySelective(T model);

    /**
     * 根据主键更新全部属性
     * @param model
     * @return
     */
    int updateByPrimaryKey(T model);
}
