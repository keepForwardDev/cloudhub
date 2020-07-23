package com.cloud.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.hub.entity.RoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleResourceMapper extends BaseMapper<RoleResource> {

    /**
     * 获取角色资源
     * @param roleIds
     * @return
     */
    List<RoleResource> getResourceByRoleId(@Param("roleIds") List<Long> roleIds);

}
