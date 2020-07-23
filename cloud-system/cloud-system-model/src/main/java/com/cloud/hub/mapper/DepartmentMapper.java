package com.cloud.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.hub.entity.Department;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 16:32
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    String getMaxLevelCode(@Param("length") Integer length, @Param("parentId") Long parentId);
}
