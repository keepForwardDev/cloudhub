package com.cloud.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.hub.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 16:36
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    public List<Resource> getRoleResources(@Param("roleIds") List<Long> roleIds);

}
