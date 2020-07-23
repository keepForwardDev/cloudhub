package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.hub.bean.LabelNode;
import com.cloud.hub.bean.ResourceBean;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.TreeNode;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.entity.Menu;
import com.cloud.hub.entity.Resource;
import com.cloud.hub.mapper.ResourceMapper;
import com.cloud.hub.utils.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResourceService extends BaseService<ResourceMapper, Resource, ResourceBean> implements Transformer<Resource, ResourceBean> {

    @Autowired
    private MenuService menuService;

    public ResponseResult save(ResourceBean bean) {
        ResponseResult responseResult = new ResponseResult();
        try {
            saveEntity(bean);
            responseResult.setCode(ResponseConst.CODE_SUCCESS);
            responseResult.setMsg(ResponseConst.CODE_SUCCESS_STR);
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("该资源已存在，请重新输入！");
        }
        return responseResult;
    }

    @Override
    public ResourceBean convert(Resource resource) {
        ResourceBean bean = new ResourceBean();
        BeanUtils.copyProperties(resource, bean);
        if (bean.getMenuId() != null) {
            Menu menu = menuService.getById(resource.getMenuId());
            bean.setMenuName(menu.getTitle());
        }
        return bean;
    }

    @Override
    public QueryWrapper<Resource> queryCondition(ResourceBean bean) {
        QueryWrapper<Resource> condition = new QueryWrapper<>();
        condition.eq("deleted", 0 );
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }
        if (StringUtils.isNotBlank(bean.getCode())) {
            condition.apply(SqlHelper.getSqlLike("code", 0), SqlHelper.getSqlLikeParams(bean.getCode()));
        }
        if (bean.getMenuId() != null) {
            condition.eq("menu_id", bean.getMenuId());
        }
        return condition;
    }

    public List<LabelNode> menuResources(Long menuId) {
        List<LabelNode> labelNodes = new ArrayList<>();
        QueryWrapper<Resource> condition = super.queryCondition(null);
        condition.eq("menu_id", menuId);
        List<Resource> resourceList = list(condition);
        resourceList.forEach(resource -> {
            labelNodes.add(new LabelNode(resource.getName(), resource.getId()));
        });
        return labelNodes;
    }


    public List<TreeNode> menuResources() {
        List<TreeNode> labelNodes = new ArrayList<>();
        QueryWrapper<Resource> condition = super.queryCondition(null);
        List<TreeNode> menuNodes = menuService.getMenuTree(null);
        List<Resource> resourceList = list(condition);
        Map<Long, List<Resource>> resourceGroup = resourceList.stream().collect(Collectors.groupingBy(Resource::getMenuId));
        menuNodes.forEach(menuNode -> {
            TreeNode menuResourceNode = getMenuResource(menuNode, resourceGroup);
            if (!CollectionUtils.isEmpty(menuResourceNode.getChildren())) {
                labelNodes.add(menuResourceNode);
            }
        });
        return labelNodes;
    }

    public TreeNode getMenuResource(TreeNode menuNode, Map<Long, List<Resource>> menuResourceMap) {
        List<Resource> resourceList = menuResourceMap.get(menuNode.getId());
        List<TreeNode> childNodes = menuNode.getChildren();
        TreeNode resourceNode = new TreeNode();
        resourceNode = new TreeNode();
        resourceNode.setId(menuNode.getId());
        resourceNode.setLabel(menuNode.getLabel());
        for (TreeNode childNode : childNodes) {
            TreeNode childResource = getMenuResource(childNode, menuResourceMap);
            if (!CollectionUtils.isEmpty(childResource.getChildren())) {
                resourceNode.getChildren().add(childResource);
            }
        }

        if (!CollectionUtils.isEmpty(resourceList)) {
            for (Resource resource : resourceList) {
                TreeNode childResourceNode = new TreeNode();
                childResourceNode.setChildren(null);
                childResourceNode.setLabel(resource.getName());
                // avoid id repeat
                childResourceNode.setId(-resource.getId());
                resourceNode.getChildren().add(childResourceNode);
            }
        }
        return resourceNode;
    }

}
