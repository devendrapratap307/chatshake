package com.chatshake.chat.chatshake_app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class AuditBO {
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
