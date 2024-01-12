package com.example.fitness_club_management_system;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据访问对象（DAO）通用接口
 * @param <T> 实体类型
 * @param <K> 主键类型
 */
public interface TDao<T, K> {
    /**
     * 向数据库中插入一条数据实体
     * @param entity 数据实体
     * @throws Exception 插入数据异常
     */
    void insert(T entity) throws Exception;

    /**
     * 向数据库中插入一批数据实体
     * @param entities 数据实体集合
     * @throws Exception 插入数据异常
     */
    void insertBatch(List<T> entities) throws Exception;

    /**
     * 根据主键更新数据库中的一条数据实体
     * @param entity 数据实体
     * @throws Exception 更新数据异常
     */
    void update(T entity) throws Exception;

    /**
     * 根据指定的条件和更新参数更新数据库中的数据
     * @param condition    更新条件
     * @param updateParams 更新参数
     * @throws SQLException 更新数据异常
     */
    void updateByCondition(String condition, String updateParams) throws Exception;

    /**
     * 根据主键删除数据库中的一条数据实体
     * @param id 主键
     * @throws SQLException 删除数据异常
     */
    void delete(K id) throws Exception;

    /**
     * 根据主键删除数据库中的多条数据实体
     * @param ids 主键集合
     * @throws SQLException 删除数据异常
     */
    void deleteBatch(List<K> ids) throws Exception;

    /**
     * 根据指定的条件删除数据库中的数据实体
     * @param condition 删除条件
     * @throws SQLException 删除数据异常
     */
    void deleteByCondition(String condition) throws Exception;

    /**
     * 根据主键查询数据库中的一条数据实体
     * @param id 主键
     * @return 数据实体
     * @throws SQLException 查询数据异常
     */
    T selectById(K id) throws Exception;

    /**
     * 查询数据库中的所有数据实体
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    List<T> selectAll() throws Exception;
    /**
     * 分页查询数据库中的数据实体
     * @param page     页码
     * @param pageSize 每页大小
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    List<T> selectByPage(int page, int pageSize) throws Exception;
    /**
     * 根据指定的条件进行模糊查询数据库中的数据实体
     * @param condition 查询条件
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    List<T> fuzzySelectByCondition(String condition) throws Exception;
}