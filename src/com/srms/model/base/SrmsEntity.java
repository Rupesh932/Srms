package com.srms.model.base;

import java.time.LocalDateTime;

public abstract class SrmsEntity {
    private final String id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public SrmsEntity(String id){
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof SrmsEntity entity)){
            return false;
        }
        return id != null && id.equals(entity.id);
    }

    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : 0 ;
    }

    @Override
    public String toString() {
        return "SrmsEntity{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
