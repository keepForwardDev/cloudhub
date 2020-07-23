package com.cloud.hub.utils;

import com.cloud.hub.bean.TreeBaseBean;
import com.cloud.hub.bean.TreeNode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 获取树 子父级关系工具
 */
public class TreeNodeUtil {

    /**
     * 递归获取树节点
     * 获取所有父节点，循环父节点，传入全部子节点
     * @param parent
     * @param allChild
     * @param labelName
     * @param <T>
     * @return
     */
    public static <T> TreeNode getTreeNode(T parent, List<T> allChild, String labelName) {
        TreeNode treeNode = new TreeNode();
        String label = (String) ReflectUtil.getFieldValue(labelName, parent.getClass(), parent);
        Long id = (Long) ReflectUtil.getFieldValue("id", parent.getClass(), parent);
        treeNode.setLabel(label);
        treeNode.setId(id);
        if (allChild != null) {
            allChild.forEach(m -> {
                Long parentId = (Long) ReflectUtil.getFieldValue("parentId", m.getClass(), m);
                if (id.longValue() == parentId.longValue()) {
                    treeNode.getChildren().add(getTreeNode(m, allChild, labelName));
                }
            });
        }
        return treeNode;
    }

    public static <T> TreeBaseBean getTreeNode(T parentEntity, TreeBaseBean bean, List<T> allChild) {
        BeanUtils.copyProperties(parentEntity, bean);
        Long id = (Long) ReflectUtil.getFieldValue("id", parentEntity.getClass(), parentEntity);
        Field field = ReflectionUtils.findField(parentEntity.getClass(), "parentId");
        if (allChild != null) {
            allChild.forEach(m -> {
                Long parentId = (Long) ReflectUtil.getFieldValue(field, m);
                if (id.longValue() == parentId.longValue()) {
                    bean.getChildren().add(getTreeNode(m, ReflectUtil.instanceClazz(bean.getClass()) ,allChild));
                }
            });
        }
        return bean;
    }
}
