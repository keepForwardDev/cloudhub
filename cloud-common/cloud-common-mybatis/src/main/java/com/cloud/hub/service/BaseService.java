package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.ShiroUser;
import com.cloud.hub.common.CommonModel;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.consts.OrderWayConst;
import com.cloud.hub.utils.CamelCaseUtil;
import com.cloud.hub.utils.LoginUserUtil;
import com.cloud.hub.utils.ReflectUtil;
import com.cloud.hub.utils.UUIDUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新增一个表单
 * 1、建立相关实体 entity && bean
 * 2、可以覆盖 钩子函数 @see #beforeSave #afterSave，自定义对实体save前后操作,getIgnoreProperties,可以自定义输入忽略字段
 * 3、基础列表查询只需要覆盖 queryCondition 方法 默认排序 #defaultOrderBy
 *
 * @param <M> dao
 * @param <T> 实体
 * @param <B> 输出值，传入参数bean
 */
public class BaseService<M extends BaseMapper<T>, T extends CommonModel, B> extends ServiceImpl<M, T> implements Transformer<T, B> {

    public void insertCommonInfo(CommonModel t) {
        Date now = new Date();
        ShiroUser shiroUser = LoginUserUtil.getCurrentUser();
        if (t.getId() != null) {
            t.setUpdateTime(now);
            t.setUpdateUserId(shiroUser == null ? null : shiroUser.getId());
        } else {
            t.setCreateDeptId(shiroUser == null ? null : shiroUser.getDeptId());
            t.setCreateTime(now);
            t.setCreateUserId(shiroUser == null ? null : shiroUser.getId());
            t.setUuid(UUIDUtils.genUUid());
        }
    }

    public Page getPage(CommonPage page) {
        Page iPage = new Page<>();
        iPage.setSize(page.getPageSize());
        iPage.setCurrent(page.getCurrentPage());
        return iPage;
    }

    protected List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = new ArrayList<>();
        IGNORE_FIELDS.add("createTime");
        return IGNORE_FIELDS;
    }

    /**
     * 需覆盖
     *
     * @param t
     * @return
     */
    @Override
    public B convert(T t) {
        throw new UnsupportedOperationException("not support this operation");
    }

    public T saveEntity(B bean) {
        T entity = null;
        Object id = ReflectUtil.getFieldValue("id", bean.getClass(), bean);
        if (id == null) { // 新增
            entity = insertEntity(bean);
        } else { // 编辑
            entity = editEntity(bean, (Long) id);
        }
        return entity;
    }

    public T insertEntity(B bean) {
        Class<T> clazz = currentModelClass();
        T entity = (T) BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(bean, entity);
        insertCommonInfo(entity);
        beforeSave(entity, bean);
        baseMapper.insert(entity);
        afterSave(entity, bean);
        return entity;
    }

    public T editEntity(B bean, Long id) {
        T entity = baseMapper.selectById(id);
        if (entity != null) {
            BeanUtils.copyProperties(bean, entity, getIgnoreProperties().toArray(new String[]{}));
            insertCommonInfo(entity);
            beforeSave(entity, bean);
            baseMapper.updateById(entity);
            afterSave(entity, bean);
        }
        return entity;
    }

    /**
     * before save the entity hook
     *
     * @param entity
     */
    protected void beforeSave(T entity, B bean) {

    }

    /**
     * after save the entity hook
     *
     * @param entity
     */
    protected void afterSave(T entity, B bean) {

    }

    public ResponseResult pageList(CommonPage page, QueryWrapper<T> condition) {
        Page<T> iPage = getPage(page);
        Page<T> pageList = page(iPage, condition);
        List<T> dataList = pageList.getRecords();
        List<B> beanList = convert(dataList);
        CommonPage commonPage = new CommonPage(pageList, beanList);
        ResponseResult result = ResponseResult.getSuccessInstance();
        result.setData(commonPage);
        return result;
    }

    /**
     * default pageList
     *
     * @param page
     * @param bean
     * @return
     */
    public ResponseResult pageList(CommonPage page, B bean) {
        QueryWrapper<T> condition = queryCondition(bean);
        return pageList(page, condition);
    }

    private void defaultOrderBy(B bean, QueryWrapper<T> condition) {
        Object orderField = ReflectUtil.getFieldValue("orderField", bean.getClass(), bean);
        Object orderWay = ReflectUtil.getFieldValue("orderWay", bean.getClass(), bean);
        if (orderField != null && !StringUtils.isEmpty(orderField.toString()) && !StringUtils.isEmpty(orderWay)) {
            String entityColumn = CamelCaseUtil.toLine(orderField.toString());
            if (orderWay.toString().equals(OrderWayConst.ASC)) {
                condition.orderByAsc(entityColumn);
            } else {
                condition.orderByDesc(entityColumn);
            }
        } else {
            defaultOrderBy(condition);
        }
    }

    /**
     * 默认空的排序 id倒序
     * @param condition
     */
    protected void defaultOrderBy(QueryWrapper<T> condition) {
        condition.orderByDesc("id");
    }

    /**
     * 列表查询条件，真正使用需覆盖
     *
     * @param bean
     * @return
     */
    public QueryWrapper<T> queryCondition(B bean) {
        QueryWrapper<T> condition = new QueryWrapper();
        condition.eq("deleted", 0);
        return condition;
    }

    /**
     * 加表格默认排序
     * @param bean
     * @return
     */
    public QueryWrapper<T> defaultOrderQueryCondition(B bean) {
        QueryWrapper<T> condition = queryCondition(bean);
        defaultOrderBy(bean, condition);
        return condition;
    }


    /**
     * 逻辑删除
     * @param idList
     */
    public void logicDelete(List<Long> idList) {
        idList.forEach(id -> {
            logicDelete(id);
        });
    }

    /**
     * 逻辑删除
     * @param id
     */
    public void logicDelete(Long id) {
        T entity = getById(id);
        entity.setDeleted(1);
        insertCommonInfo(entity);
        updateById(entity);
    }

}
