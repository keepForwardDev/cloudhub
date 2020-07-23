package com.cloud.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.hub.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 16:34
 */
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select * from sys_menu where find_in_set(id,getChildMenu(#{id}))")
    List<Menu> getChildMenuByParentId(Long id);

    /**
     * 查询角色对应的菜单
     * @param roleIds
     * @return
     */
    public List<Menu> selectUserMenus(@Param("roleIds") List<Long> roleIds);

}
