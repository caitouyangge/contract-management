package com.example.contractmanagement.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import lombok.Setter;

@Entity
@Table(name = "contracts")
@Data
@Setter // 单独添加Setter注解
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

    @Column(name = "creator_name")
    private String creatorName;
    
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
    
    @Column(name = "countersign_users", length = 255) // 明确指定长度
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



    // 在Contract类中添加
    @Column(name = "countersign_comments", columnDefinition = "TEXT")
    private String countersignComments;


    // 非数据库字段，用于显示起草人姓名
    @Transient
    private String drafter;

    // 添加辅助方法
    public List<Long> getCountersignUserIds() {
        if (this.countersignUsers == null || this.countersignUsers.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(this.countersignUsers.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
    }

    // 移除Lombok的@Setter注解对这个方法的影响
    public void setCountersignUserIds(List<Long> userIds) {
        if(userIds == null || userIds.isEmpty()) {
            this.countersignUsers = null;
        } else {
            this.countersignUsers = userIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        }
    }
}