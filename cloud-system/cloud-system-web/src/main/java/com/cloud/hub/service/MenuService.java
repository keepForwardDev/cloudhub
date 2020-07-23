package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.Router;
import com.cloud.hub.bean.TreeNode;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.entity.Menu;
import com.cloud.hub.mapper.MenuMapper;
import com.cloud.hub.utils.BooleanUtil;
import com.cloud.hub.utils.JsonUtils;
import com.cloud.hub.utils.NumberUtil;
import com.cloud.hub.utils.SqlHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService extends BaseService<MenuMapper, Menu, Router> implements Transformer<Menu, Router> {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 分页获取root菜单
     *
     * @param page
     * @param title
     * @return
     */
    public ResponseResult getRootMenuList(CommonPage page, String title) {
        QueryWrapper<Menu> condition = new QueryWrapper();
        condition.eq("parent_id", 0);
        if (StringUtils.isNotBlank(title)) {
            condition.apply(SqlHelper.getSqlLike("title", 0), SqlHelper.getSqlLikeParams(title));
        }
        condition.orderByAsc("sort", "create_time");
        Page<Menu> iPage = new Page<>();
        iPage.setSize(page.getPageSize());
        iPage.setCurrent(page.getCurrentPage());
        Page<Menu> pageList = page(iPage, condition);
        List<Menu> menuList = pageList.getRecords();
        List<Router> routers = convert(menuList);
        CommonPage commonPage = new CommonPage(pageList, routers);
        ResponseResult result = ResponseResult.getSuccessInstance();
        result.setData(commonPage);
        return result;
    }

    /**
     * 根据父级id获取menu
     *
     * @param parentId
     * @return
     */
    public List<Router> getMenuListByParentId(Long parentId) {
        if (parentId == null) {
            return new ArrayList<>();
        }
        QueryWrapper<Menu> condition = new QueryWrapper();
        condition.eq("parent_id", parentId);
        condition.orderByAsc("sort");
        List<Menu> menuList = list(condition);
        List<Router> routers = convert(menuList);
        return routers;
    }


    /**
     * 递归得到所有路由
     *
     * @param parent   父级节点
     * @param allChild 全部子节点
     * @return
     */
    public Router getRouter(Menu parent, List<Menu> allChild) {
        Router router = convert(parent);
        allChild.forEach(m -> {
            if (parent.getId().longValue() == m.getParentId().longValue()) {
                router.getChildren().add(getRouter(m, allChild));
            }
        });
        return router;
    }


    @Override
    public Router convert(Menu menu) {
        Router router = new Router();
        BeanUtils.copyProperties(menu, router);
        router.setAlwaysShow(BooleanUtil.numberToBoolean(menu.getAlwaysShow()));
        router.setHasChildren(BooleanUtil.numberToBoolean(menu.getHasChildren()));
        router.setHidden(BooleanUtil.numberToBoolean(menu.getHidden()));
        router.setMeta(JsonUtils.readObject(menu.getMeta(), Router.Meta.class));
        return router;
    }

    public ResponseResult saveMenu(Router router) {
        Menu menu;
        if (router.getId() != null && router.getId() > 0) {
            // 更新
            menu = getById(router.getId());
        } else {
            menu = new Menu();
            insertCommonInfo(menu);
        }
        if (router.getParentId() != 0) {
            Menu parentMenu = getById(router.getParentId());
            parentMenu.setHasChildren(1);
            saveOrUpdate(parentMenu);
        }
        convertRouterToMenu(router, menu);
        Optional.ofNullable(menu).ifPresent(m -> {
            saveOrUpdate(menu);
        });
        return ResponseResult.getSuccessInstance();
    }


    public void convertRouterToMenu(Router router, Menu menu) {
        if (menu == null) {
            return;
        }
        BeanUtils.copyProperties(router, menu);
        router.getMeta().setTitle(router.getTitle());
        menu.setAlwaysShow(NumberUtil.booleanToInteger(router.isAlwaysShow()));
        menu.setHidden(NumberUtil.booleanToInteger(router.isHidden()));
        menu.setMeta(JsonUtils.toJsonString(router.getMeta()));
        menu.setHasChildren(NumberUtil.booleanToInteger(hasChildren(menu)));
        //EntityUtil.insertUserInfo(menu);
    }


    public boolean hasChildren(Menu menu) {
        if (menu.idNotNull()) {
            QueryWrapper condition = new QueryWrapper();
            condition.eq("parent_id", menu.getId());
            return getOne(condition) != null;
        }
        return false;
    }

    public List<TreeNode> getMenuTree(Long parentId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        QueryWrapper condition = new QueryWrapper();
        condition.eq("deleted", 0);
        if (parentId != null) {
            condition.eq("parent_id", parentId);
        }
        condition.orderByAsc("sort", "create_time");
        List<Menu> menuList = list(condition);
        // 分为 root 节点 和 子节点
        Map<Boolean, List<Menu>> groupMap = menuList.stream().collect(Collectors.groupingBy(m -> {
            if (m.getParentId() == 0) {
                return true;
            }
            return false;
        }));
        List<Menu> childList = groupMap.get(false);
        if (!CollectionUtils.isEmpty(menuList)) {
            groupMap.get(true).forEach(m -> {
                treeNodes.add(getTreeNode(m, childList));
            });
        }
        return treeNodes;
    }


    public TreeNode getTreeNode(Menu parent, List<Menu> allChild) {
        TreeNode treeNode = new TreeNode();
        treeNode.setLabel(parent.getTitle());
        treeNode.setId(parent.getId());
        if (StringUtils.isNotBlank(parent.getMeta())) {
            Router.Meta meta = JsonUtils.readObject(parent.getMeta(), Router.Meta.class);
            treeNode.setExtra(meta.getIcon());
        }
        if (allChild != null) {
            allChild.forEach(m -> {
                if (parent.getId().longValue() == m.getParentId().longValue()) {
                    treeNode.getChildren().add(getTreeNode(m, allChild));
                }
            });
        }
        return treeNode;
    }

    /**
     * 会删除子节点
     * @param id
     */
    public void deleteMenu(Long id) {
        Menu menu = getById(id);
        removeById(id);
        if (menu.getParentId() !=null && menu.getParentId() > 0) {
            Menu parent = getById(menu.getParentId());
            parent.setHasChildren(NumberUtil.booleanToInteger(hasChildren(parent)));
        }
        // 查询子节点，然后删除
        List<Menu> childList = queryChildren(id);
        List<Long> idList = childList.stream().map(Menu::getId).collect(Collectors.toList());
        removeByIds(idList);
    }

    /**
     * 递归查询所有子节点
     * @param id
     * @return
     */
    public List<Menu> queryChildren(Long id) {
        return baseMapper.getChildMenuByParentId(id);
    }

    /**
     * id获取详情
     * @param id
     * @return
     */
    public Router getRouterById(Long id) {
        return convert(getById(id));
    }


    /**
     * 获取用户路由
     *
     * @param rolesId
     * @return
     */
    public List<Router> getUserRouters(List<Long> rolesId) {
        List<Menu> menu = selectUserMenus(rolesId);
        List<Router> routers = new ArrayList<>();
        //根节点
        List<Menu> parents = menu.stream().filter(m -> m.getParentId() == 0).collect(Collectors.toList());
        //子节点
        List<Menu> child = menu.stream().filter(m -> m.getParentId() != 0).collect(Collectors.toList());
        if (parents.isEmpty()) {
            return routers;
        }
        parents.forEach(p -> {
            routers.add(getRouter(p, child));
        });
        return routers;
    }
    /**
     * 获取用户菜单
     *
     * @param rolesId
     * @return
     */
    public List<Menu> selectUserMenus(List<Long> rolesId) {
        if (rolesId.isEmpty()) {
            return null;
        }
        return baseMapper.selectUserMenus(rolesId);
    }

}
