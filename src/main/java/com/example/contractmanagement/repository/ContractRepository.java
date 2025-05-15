package com.example.contractmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.contractmanagement.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByStatus(String status);
    @Query("SELECT c FROM Contract c WHERE c.status = :status AND c.countersignUsers LIKE %:userId%")
    List<Contract> findByStatusAndCountersignUsersContaining(
    @Param("status") String status, 
    @Param("userId") String userId);
    List<Contract> findByCreatorIdAndStatus(Long creatorId, String status);
}