package com.cloud.hub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.hub.bean.DepartmentBean;
import com.cloud.hub.bean.TreeNode;
import com.cloud.hub.entity.Department;
import com.cloud.hub.entity.User;
import com.cloud.hub.mapper.DepartmentMapper;
import com.cloud.hub.mapper.UserMapper;
import com.cloud.hub.utils.SqlHelper;
import com.cloud.hub.utils.TreeNodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService extends BaseService<DepartmentMapper, Department, DepartmentBean> {

    @Autowired
    private UserMapper userMapper;

    /**
     * 初始化部门code
     */
    public final static String initCode = "000";

    @Override
    public DepartmentBean convert(Department department) {
        DepartmentBean departmentBean = new DepartmentBean();
        BeanUtils.copyProperties(department, departmentBean);
        if (department.getParentId() != null) {
            departmentBean.setParentName(getById(department.getParentId()).getName());
        }
        if (department.getMainChargeUserId() != null) {
            User user = userMapper.selectById(department.getMainChargeUserId());
            departmentBean.setMainChargeUserName(user.getName());
        }
        return departmentBean;
    }

    protected List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = super.getIgnoreProperties();
        IGNORE_FIELDS.add("levelCode");
        return IGNORE_FIELDS;
    }

    @Override
    public QueryWrapper<Department> queryCondition(DepartmentBean bean) {
        QueryWrapper<Department> condition =  super.queryCondition(bean);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), bean.getName());
        }
        if (bean.getMainChargeUserId() != null) {
            condition.eq("main_charge_user_id", bean.getMainChargeUserId());
        }
        return condition;
    }

    @Override
    protected void beforeSave(Department entity, DepartmentBean bean) {
        if (entity.getId() == null) {
            entity.setLevelCode(getLevelCode(entity));
        }
        entity.setFullName(getFullName(entity, new ArrayList<>()));
    }


    /**
     * 获取节点levelCode
     *
     * @param entity
     * @return
     */
    private String getLevelCode(Department entity) {
        if (entity.getParentId() != null) {
            Department parent = getById(entity.getParentId());
            //父级节点的 levelCode
            String levelCode = parent.getLevelCode();
            //根据父级节点的levelCode +3 位 为子节点的levelCode
            String maxLevelCode = baseMapper.getMaxLevelCode(levelCode.length() + 3, parent.getId());
            return calLevelCode(maxLevelCode, levelCode);
        } else { // 父级为空代表根节点
            String maxLevelCode = baseMapper.getMaxLevelCode(initCode.length(), null);
            return calLevelCode(maxLevelCode, StringUtils.EMPTY);
        }
    }

    /**
     * 计算节点levelCode
     *
     * @param maxLevelCode
     * @param parentCode
     * @return
     */
    private String calLevelCode(String maxLevelCode, String parentCode) {
        String setLevelCode = null;
        //子节点的最大levelCode如果为空，取父节点的levelCode+默认3位
        if (StringUtils.isBlank(maxLevelCode)) {
            setLevelCode = parentCode + initCode;
        } else {//不为空，取后三位数字+1
            maxLevelCode = maxLevelCode.substring(maxLevelCode.length() - 3, maxLevelCode.length());
            Integer code = Integer.parseInt(maxLevelCode) + 1;
            maxLevelCode = String.format("%03d", code);
            setLevelCode = parentCode + maxLevelCode;
        }
        return setLevelCode;
    }

    /**
     * 全名
     * @param department
     * @param names
     * @return
     */
    private String getFullName(Department department, List<String> names) {
        names.add(0, department.getName());
        if (department.getParentId() != null) {
            Department parent = baseMapper.selectById(department.getParentId());
            getFullName(parent, names);
        }
        return StringUtils.join(names, " ");
    }

    /**
     * 全部部门树
     *
     * @return
     */
    public List<TreeNode> getDepartmentTree() {
        List<TreeNode> nodes = new ArrayList<>();
        QueryWrapper<Department> condition = new QueryWrapper<>();
        condition.eq("deleted",0);
        condition.orderByAsc("sort");
        List<Department> department = baseMapper.selectList(condition);
        //根节点
        List<Department> parents = department.stream().filter(m -> m.getParentId() == null).collect(Collectors.toList());
        //子节点
        List<Department> child = department.stream().filter(m -> m.getParentId() != null).collect(Collectors.toList());
        if (parents.isEmpty()) {
            return nodes;
        }
        parents.forEach(t -> {
            nodes.add(TreeNodeUtil.getTreeNode(t, child, "name"));
        });
        return nodes;
    }
}
