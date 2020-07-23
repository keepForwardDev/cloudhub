package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.hub.bean.LabelNode;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.RoleBean;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.entity.Role;
import com.cloud.hub.entity.RoleResource;
import com.cloud.hub.mapper.RoleMapper;
import com.cloud.hub.mapper.RoleResourceMapper;
import com.cloud.hub.mapper.UserRoleMapper;
import com.cloud.hub.utils.JsonUtils;
import com.cloud.hub.utils.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleService extends BaseService<RoleMapper, Role, RoleBean> implements Transformer<Role, RoleBean> {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public RoleBean convert(Role role) {
        RoleBean roleBean = new RoleBean();
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(role.getId());
        List<RoleResource> roleResources = roleResourceMapper.getResourceByRoleId(roleIdList);
        roleResources.forEach(r -> {
            if (r.getMenuFlag() == 1) {
                roleBean.getMenuIds().add(r.getMenuId());
            } else {
                roleBean.getResourceIds().add(-r.getResourceId());
            }
            if (StringUtils.isNotBlank(r.getCascaderValue())) {
                List<Long> list = new ArrayList<>();
                ArrayList<Object> array = JsonUtils.readObject(r.getCascaderValue(), ArrayList.class);
                array.forEach(id -> {
                    list.add(Long.valueOf(id.toString()));
                });
                roleBean.getCascaderValue().add(list);
            }
        });
        BeanUtils.copyProperties(role, roleBean);
        return roleBean;
    }

    /**
     * 角色保存
     * @param roleBean
     */
    public ResponseResult saveRole(RoleBean roleBean) {
        ResponseResult responseResult = new ResponseResult();
        try {
            saveEntity(roleBean);
            responseResult.setCode(ResponseConst.CODE_SUCCESS);
            responseResult.setMsg(ResponseConst.CODE_SUCCESS_STR);
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("该角色编码已存在，请重新输入！");
        }
        return responseResult;
    }

    @Override
    public void beforeSave(Role entity, RoleBean bean) {
        if (entity.getId() != null) {
            // 删除资源
            QueryWrapper<RoleResource> condition = new QueryWrapper<>();
            condition.eq("role_id", entity.getId());
            roleResourceMapper.delete(condition);
        }
    }

    @Override
    public void afterSave(Role entity, RoleBean bean) {
        bean.setId(entity.getId());
        saveRoleResource(bean);
    }

    public boolean existCode(RoleBean roleBean) {
        QueryWrapper<Role> condition = new QueryWrapper<>();
        condition.eq("code", roleBean.getCode());
        Role role = getOne(condition);
        return role != null;
    }

    /**
     * 保存角色的相关资源授权
     * @param roleBean
     */
    public void saveRoleResource(RoleBean roleBean) {
        if (roleBean.getId() != null) {
            // 菜单权限
            roleBean.getMenuIds().forEach( menuId -> {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleBean.getId());
                roleResource.setMenuFlag(1);
                roleResource.setMenuId(menuId);
                insertCommonInfo(roleResource);
                roleResourceMapper.insert(roleResource);
            });

            // 资源权限
            int index = 0;
            for (Long resourceId : roleBean.getResourceIds()) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleBean.getId());
                roleResource.setMenuFlag(0);
                // because cascader's id is negative
                roleResource.setResourceId(-resourceId);
                roleResource.setCascaderValue(JsonUtils.toJsonString(roleBean.getCascaderValue().get(index)));
                insertCommonInfo(roleResource);
                roleResourceMapper.insert(roleResource);
                index++;
            }
        }
    }

    @Override
    public QueryWrapper<Role> queryCondition(RoleBean bean) {
        QueryWrapper<Role> condition = super.queryCondition(bean);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }
        return condition;
    }

    public void deleteRole(Long id) {
        removeById(id);
        // 删除关联关系
        QueryWrapper condition = new QueryWrapper<>();
        condition.eq("role_id", id);
        roleResourceMapper.delete(condition);
        userRoleMapper.delete(condition);
    }

    public List<LabelNode> getLabelNodes() {
        List<Role> roleList = list(super.queryCondition(null));
        List<LabelNode> labelNodes = new ArrayList<>();
        roleList.forEach(role -> {
            LabelNode labelNode = new LabelNode();
            labelNode.setLabel(role.getName());
            labelNode.setValue(role.getId());
            labelNodes.add(labelNode);
        });
        return labelNodes;
    }

    protected List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = super.getIgnoreProperties();
        IGNORE_FIELDS.add("code");
        return IGNORE_FIELDS;
    }

}
