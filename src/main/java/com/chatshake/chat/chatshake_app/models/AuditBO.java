package com.chatshake.chat.chatshake_app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AuditBO {

    @CreatedBy
    private String createdBy;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedDate;
}
//@Getter
//@Setter
//@MappedSuperclass
//@EntityListeners({AuditingEntityListener.class, AuditListenerHandler.class})
//@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "createdBy", "updatedBy"}, allowGetters = true)
//public class Audit {
//
//    @Column(nullable = false, updatable = false, name = "created_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    @CreationTimestamp
//    private Date createdAt;
//
//    @Column(name = "created_by", updatable = false)
//    private Long createdBy;
//
//    @Column(nullable = false, name = "updated_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    @UpdateTimestamp
//    private Date updatedAt;
//
//    @Column(name = "updated_by")
//    private Long updatedBy;
//
//}
