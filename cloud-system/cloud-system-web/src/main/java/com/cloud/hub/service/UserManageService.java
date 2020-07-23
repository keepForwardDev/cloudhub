package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.hub.bean.LabelNode;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.UserBean;
import com.cloud.hub.bean.UserRoleBean;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.entity.User;
import com.cloud.hub.entity.UserRole;
import com.cloud.hub.exception.WrapMsgException;
import com.cloud.hub.mapper.DepartmentMapper;
import com.cloud.hub.mapper.UserMapper;
import com.cloud.hub.mapper.UserRoleMapper;
import com.cloud.hub.utils.RandomUtil;
import com.cloud.hub.utils.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManageService extends BaseService<UserMapper, User, UserBean> implements Transformer<User, UserBean> {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Value("${system.default-password}")
    private String defaultPassword;

    @Override
    public UserBean convert(User user) {
        UserBean bean = new UserBean();
        List<UserRoleBean> userRoleList = userRoleMapper.findInfoByUserId(user.getId());
        BeanUtils.copyProperties(user, bean);
        userRoleList.forEach(info -> {
            bean.getRole().add(new LabelNode(info.getRoleName(), info.getRoleId()));
        });
        if (user.getDeptId() != null) {
            bean.setDeptName(departmentMapper.selectById(user.getDeptId()).getName());
        }
        return bean;
    }

    public ResponseResult save(UserBean bean) {
        ResponseResult responseResult = new ResponseResult();
        try {
            saveEntity(bean);
            responseResult.setCode(ResponseConst.CODE_SUCCESS);
            responseResult.setMsg(ResponseConst.CODE_SUCCESS_STR);
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("受唯一约束，该账号不能保存！");
        } catch (WrapMsgException ex) {
            responseResult.setMsg(ex.getMessage());
        }
        return responseResult;
    }

    public QueryWrapper<User> queryCondition(UserBean bean) {
        QueryWrapper<User> condition = super.queryCondition(bean);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }
        if (StringUtils.isNotBlank(bean.getPhone())) {
            condition.apply(SqlHelper.getSqlLike("phone", 0), SqlHelper.getSqlLikeParams(bean.getPhone()));
        }
        if (StringUtils.isNotBlank(bean.getEmail())) {
            condition.apply(SqlHelper.getSqlLike("email", 0), SqlHelper.getSqlLikeParams(bean.getEmail()));
        }
        if (bean.getDeptId() != null) {
            condition.eq("dept_id", bean.getDeptId());
        }
        if (bean.getRoleId() != null) {
            condition.apply("id in (select user_id from sys_user_role where role_id= {0})", bean.getRoleId());
        }
        return condition;
    }


    @Override
    protected void beforeSave(User entity, UserBean bean) {
        QueryWrapper<User> condition = new QueryWrapper<>();
        condition.eq("deleted", 0);
        boolean notEmptyAccount = StringUtils.isNotBlank(entity.getAccount());
        boolean notEmptyPhone = StringUtils.isNotBlank(entity.getPhone());
        boolean notEmptyUnionId = StringUtils.isNotBlank(entity.getUnionId());
        condition.and(query -> {
            if (notEmptyAccount) {
                query.or().eq("account", entity.getAccount());
            }
            if (notEmptyPhone) {
                query.or().eq("phone", entity.getPhone());
            }
            if (notEmptyUnionId) {
                query.or(false).eq("union_id", entity.getUnionId());
            }
        });
        if (entity.getId() != null) {
            condition.ne("id", entity.getId());
            // 删除关联关系
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", entity.getId());
            userRoleMapper.delete(userRoleQueryWrapper);
        } else { // insert with default password
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            entity.setPassword(encoder.encode(defaultPassword));
        }
        User user = getOne(condition, false);
        if (user == null) {
            return;
        }

        if (notEmptyAccount && StringUtils.equals(entity.getAccount(), user.getAccount())) {
            throw new WrapMsgException("该账号已经存在！");
        }
        if (notEmptyPhone && StringUtils.equals(entity.getPhone(), user.getPhone())) {
            throw new WrapMsgException("该手机号已被注册！");
        }
        if (notEmptyUnionId && StringUtils.equals(entity.getUnionId(), user.getUnionId())) {
            throw new WrapMsgException("该微信号号已被注册！");
        }
    }

    @Override
    protected void afterSave(User entity, UserBean bean) {
        bean.getRole().forEach(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(entity.getId());
            userRole.setRoleId(role.getValue());
            userRoleMapper.insert(userRole);
        });
    }

    public List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = super.getIgnoreProperties();
        IGNORE_FIELDS.add("nickName");
        IGNORE_FIELDS.add("account");
        return IGNORE_FIELDS;
    }

    public String resetPassword(List<Long> idList) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String randomString = RandomUtil.getRandomString(5);
        idList.forEach(id -> {
            Optional.ofNullable(getById(id)).ifPresent(user -> {
                user.setPassword(encoder.encode(randomString));
                updateById(user);
            });
        });
        return randomString;
    }
}
