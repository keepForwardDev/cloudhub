package com.cloud.hub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.hub.entity.Dictionary;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 16:51
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    @Select("select * from sys_dictionary where parent_id = #{parentId}")
    List<Dictionary> findChildrenByParentId(Long parentId);
}
