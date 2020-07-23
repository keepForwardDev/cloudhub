package com.cloud.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.hub.bean.UserRoleBean;
import com.cloud.hub.entity.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 16:37
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select(value = "select * from sys_user_role where user_id = #{userId}")
    public List<UserRole> findByUserId(Long userId);

    List<UserRoleBean> findInfoByUserId(Long userId);
}
