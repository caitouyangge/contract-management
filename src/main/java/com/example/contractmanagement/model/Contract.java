package com.example.contractmanagement.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "contracts")
@Data
public class Contract {

    public static final String STATUS_DRAFT = "DRAFT"; // 起草
    public static final String STATUS_ASSIGNED = "ASSIGNED"; // 已分配
    public static final String STATUS_COUNTERSIGNING = "COUNTERSIGNING"; // 会签中
    public static final String STATUS_COUNTERSIGNED = "COUNTERSIGNED"; // 会签完成
    public static final String STATUS_FINALIZED = "FINALIZED"; // 定稿完成
    public static final String STATUS_APPROVING = "APPROVING"; // 审批中
    public static final String STATUS_APPROVED = "APPROVED"; // 审批完成
    public static final String STATUS_SIGNED = "SIGNED"; // 签订完成
    


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String customer;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // 改为 LocalDate
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "attachment_path")
    private String attachmentPath;
    
    @Column(nullable = false)
    private String status = "DRAFT";
    
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;
    
    @Column(name = "countersign_users")
    private String countersignUsers;
    
    @Column(name = "approval_user_id")
    private Long approvalUserId;
    
    @Column(name = "sign_user_id")
    private Long signUserId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;


    // 非数据库字段，用于显示起草人姓名
    @Transient
    private String drafter;
}