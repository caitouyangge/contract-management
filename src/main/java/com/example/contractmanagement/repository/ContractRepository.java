package com.example.contractmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contractmanagement.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByStatus(String status);
}