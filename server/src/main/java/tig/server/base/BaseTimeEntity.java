package tig.server.base;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}