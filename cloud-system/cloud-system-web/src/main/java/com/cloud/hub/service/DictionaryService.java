package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.hub.bean.DictionaryBean;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.TreeNode;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.entity.Dictionary;
import com.cloud.hub.mapper.DictionaryMapper;
import com.cloud.hub.utils.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DictionaryService extends BaseService<DictionaryMapper, Dictionary, DictionaryBean> {

    public ResponseResult saveDictionary(DictionaryBean dictionaryBean) {
        ResponseResult responseResult = new ResponseResult();
        try {
            saveEntity(dictionaryBean);
            responseResult.setCode(ResponseConst.CODE_SUCCESS);
            responseResult.setMsg(ResponseConst.CODE_SUCCESS_STR);
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("该字典编码已存在，请重新输入！");
        }
        return responseResult;
    }


    public ResponseResult getRootDictionaryList() {
        List<TreeNode> treeNodes = new ArrayList<>();
        QueryWrapper<Dictionary> condition = super.queryCondition(null);
        condition.eq("parent_id", 0);
        condition.orderByAsc("sort", "create_time");
        List<Dictionary> dictionaryList = list(condition);
        for (Dictionary dictionary : dictionaryList) {
            TreeNode node = new TreeNode();
            node.setId(dictionary.getId());
            node.setLabel(dictionary.getName());
            node.setExtra(dictionary.getCode());
            treeNodes.add(node);
        }
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(treeNodes);
        return responseResult;
    }

    @Override
    protected void beforeSave(Dictionary entity, DictionaryBean bean) {
        if (entity.getParentId() != null && entity.getParentId() > 0) {
            Dictionary parent = getById(entity.getParentId());
            parent.setHasChildren(true);
            updateById(parent);
        }
    }

    @Override
    public DictionaryBean convert(Dictionary dictionary) {
        DictionaryBean dictionaryBean = new DictionaryBean();
        BeanUtils.copyProperties(dictionary, dictionaryBean);
        return dictionaryBean;
    }

    @Override
    protected void defaultOrderBy(QueryWrapper<Dictionary> condition) {
        condition.orderByAsc("sort");
        condition.orderByDesc("id");
    }

    @Override
    public QueryWrapper<Dictionary> queryCondition(DictionaryBean bean) {
        QueryWrapper<Dictionary> condition =  super.queryCondition(bean);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }

        if (StringUtils.isNotBlank(bean.getCode())) {
            condition.eq("code", bean.getCode());
        }

        if (bean.getParentId() != null) {
            condition.eq("parent_id", bean.getParentId());
        }
        return condition;
    }

    /**
     * id 删除，会更新子集
     * @param idList
     * @param parentId 父级id
     */
    public void deleteByIds(List<Long> idList, Long parentId) {
        if (!CollectionUtils.isEmpty(idList)) {
            boolean updateParent = parentId == null;
            for (Long id : idList) {
                deleteById(getById(id), updateParent);
            }
            updateHasChildren(parentId);
            return;
        } else {
            deleteById(getById(parentId), true);
        }
    }

    /**
     *
     * @param dictionary
     * @param updateParent 是否更新父节点的hasChildren
     */
    public void deleteById(Dictionary dictionary, boolean updateParent) {
        List<Dictionary> childList = baseMapper.findChildrenByParentId(dictionary.getId());
        // 删除子项
        childList.forEach(child -> {
            deleteById(child, false);
        });
        insertCommonInfo(dictionary);
        updateById(dictionary);
        if (updateParent) {
            updateHasChildren(dictionary.getParentId());
        }
    }

    private void updateHasChildren(Long parentId) {
        if (parentId != null && parentId > 0) {
            Dictionary parent = getById(parentId);
            if (parent.getHasChildren()) {
                List<Dictionary> parentChildList = baseMapper.findChildrenByParentId(parent.getId());
                if (CollectionUtils.isEmpty(parentChildList)) {
                    parent.setHasChildren(false);
                    updateById(parent);
                }
            }
        }
    }

    public void enableDictionary(Long id, boolean enable) {
        if (enable) {
            enableDictionary(id);
        } else {
            disabledDictionary(id);
        }
    }

    /**
     * 启用单个节点
     * @param id
     */
    public void enableDictionary(Long id) {
        Dictionary dictionary = getById(id);
        dictionary.setEnabled(true);
        insertCommonInfo(dictionary);
        updateById(dictionary);
        Long parentId = dictionary.getParentId();
        while (dictionary.idNotNull(parentId)) {
            Dictionary parent = getById(parentId);
            // update parent status
            parent.setEnabled(true);
            updateById(parent);
            parentId = parent.getParentId();
        }
    }

    /**
     * 禁用单个节点
     * @param id
     */
    public void disabledDictionary(Long id) {
        Dictionary dictionary = getById(id);
        dictionary.setEnabled(false);
        insertCommonInfo(dictionary);
        updateById(dictionary);
        // update children status
        List<Dictionary> childList = baseMapper.findChildrenByParentId(dictionary.getId());
        childList.forEach(child -> {
            disabledDictionary(child.getId());
        });
    }
}
