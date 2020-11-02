package com.owneroftime.guestbook.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
	
    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    private String createdBy;// = "127.0.0.1";
    
    @Column(name = "CREATED_ON", nullable = false, updatable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "MODIFIED_BY")
    private String modifiedBy; // = "127.0.0.1";
    
    @Column(name = "MODIFIED_ON")
    private LocalDateTime modifiedOn;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @PrePersist
    protected void onPrePersist() {
        this.setCreatedBy("system");
        this.setCreatedOn(LocalDateTime.now());
        this.setModifiedBy("system");
        this.setModifiedOn(LocalDateTime.now());
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.setModifiedBy("system");
        this.setModifiedOn(LocalDateTime.now());
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
