package cloud.hub.service;

import cloud.hub.Bean.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.hub.bean.Router;
import com.cloud.hub.bean.UserRoleBean;
import com.cloud.hub.entity.Menu;
import com.cloud.hub.entity.Resource;
import com.cloud.hub.entity.User;
import com.cloud.hub.mapper.MenuMapper;
import com.cloud.hub.mapper.ResourceMapper;
import com.cloud.hub.mapper.UserMapper;
import com.cloud.hub.mapper.UserRoleMapper;
import com.cloud.hub.utils.BooleanUtil;
import com.cloud.hub.utils.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 17:44
 */
@Service
@Transactional
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private MenuMapper menuMapper;

    public User findByAccount(String account) {
        Assert.notNull(account, "account is required");
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("account", account);
        query.or().eq("uuid", account);
        User user = baseMapper.selectOne(query);
        return user;
    }

    public LoginUser getShiroAccount(String account) {
        User user = findByAccount(account);
        LoginUser loginUser = new LoginUser();
        if (user != null) {
            BeanUtils.copyProperties(user, loginUser);
            List<UserRoleBean> roleBeans = userRoleMapper.findInfoByUserId(user.getId());
            List<Long> roleIdList = new ArrayList<>();
            roleBeans.forEach(userRole -> {
                loginUser.getRolesCode().add(userRole.getRoleCode());
                roleIdList.add(userRole.getRoleId());
            });
            if (roleIdList.isEmpty()) {
                return loginUser;
            }
            List<Resource> resources = resourceMapper.getRoleResources(roleIdList);
            resources.forEach(resource -> {
                loginUser.getResourcesCode().add(resource.getCode());
            });
            List<Router> routerList = getUserRouters(roleIdList);
            loginUser.setMenu(routerList);
            return loginUser;
        }
        return null;
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
    private List<Menu> selectUserMenus(List<Long> rolesId) {
        if (rolesId.isEmpty()) {
            return null;
        }
        return menuMapper.selectUserMenus(rolesId);
    }

    /**
     * 递归得到所有路由
     *
     * @param parent   父级节点
     * @param allChild 全部子节点
     * @return
     */
    private Router getRouter(Menu parent, List<Menu> allChild) {
        Router router = convertMenuToRouter(parent);
        allChild.forEach(m -> {
            if (parent.getId().longValue() == m.getParentId().longValue()) {
                router.getChildren().add(getRouter(m, allChild));
            }
        });
        return router;
    }

    private Router convertMenuToRouter(Menu menu) {
        Router router = new Router();
        BeanUtils.copyProperties(menu, router);
        router.setAlwaysShow(BooleanUtil.numberToBoolean(menu.getAlwaysShow()));
        router.setHasChildren(BooleanUtil.numberToBoolean(menu.getHasChildren()));
        router.setHidden(BooleanUtil.numberToBoolean(menu.getHidden()));
        router.setMeta(JsonUtils.readObject(menu.getMeta(), Router.Meta.class));
        return router;
    }

    public User findByPhone(String phone) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("phone", phone);
        query.eq("deleted", 0);
        User user = baseMapper.selectOne(query);
        return user;
    }
}
