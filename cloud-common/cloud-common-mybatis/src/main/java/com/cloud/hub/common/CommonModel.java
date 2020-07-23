package com.cloud.hub.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class CommonModel<T extends CommonModel<?>> extends Model<T> {
    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;//自增id

    protected Integer deleted = 0;//逻辑删除

    @Version
    protected Integer version = 0;//版本号

    protected Date createTime;//创建时间

    protected Date updateTime;//更新时间

    protected Long createUserId;//创建者

    protected Long updateUserId;//更新者

    protected Long createDeptId; // 冗余创建者部门

    protected String uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Long getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(Long createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * id 是否有效
     * @return
     */
    public boolean idNotNull() {
        return idNotNull(id);
    }

    public boolean idNotNull(Long id) {
        return id != null && id > 0;
    }
}
