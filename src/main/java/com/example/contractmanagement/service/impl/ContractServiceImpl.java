package com.example.contractmanagement.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.contractmanagement.dto.ContractAssignmentDTO;
import com.example.contractmanagement.dto.ContractDraftDTO;
import com.example.contractmanagement.model.Contract;
import com.example.contractmanagement.repository.ContractRepository;
import com.example.contractmanagement.repository.UserRepository;
import com.example.contractmanagement.service.ContractService;

@Service
public class ContractServiceImpl implements ContractService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    
    public ContractServiceImpl(ContractRepository contractRepository, 
                             UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public Contract draftContract(ContractDraftDTO dto, Long creatorId) {
        Contract contract = new Contract();
        contract.setName(dto.getContractName());
        contract.setCustomer(dto.getCustomerName());
        contract.setStartDate(dto.getStartDate());
        contract.setContent(dto.getContent());
        contract.setCreatorId(creatorId); // 直接使用参数
        contract.setStatus("DRAFT");
        
        // 文件存储（可选）
        if (dto.getFile() != null) {
            // String path = "./uploads/" + dto.getFile().getOriginalFilename();
            String path = storeFile(dto.getFile());
            try {
                dto.getFile().transferTo(new File(path));
            } catch (IOException ex) {
            } catch (IllegalStateException ex) {
            }
            contract.setAttachmentPath(path);
        }
        
        return contractRepository.save(contract);
    }
        
    
    private String storeFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("文件存储失败: " + e.getMessage());
        }
    }

    @Override
    public List<Contract> getAssignableContracts() {
        return contractRepository.findByStatus(Contract.STATUS_DRAFT);
    }

    @Override
    public Contract assignContract(ContractAssignmentDTO dto) {
        Contract contract = contractRepository.findById(dto.getContractId())
            .orElseThrow(() -> new RuntimeException("合同不存在"));
         
        // 调试日志
    System.out.println("接收到的会签人员IDs: " + dto.getCountersignUserIds());
    
        // 设置会签人员（多个ID）
    contract.setCountersignUserIds(dto.getCountersignUserIds());
    
    // 调试日志
    System.out.println("转换后的数据库字段值: " + contract.getCountersignUsers());
    
        // 设置审批人员
        contract.setApprovalUserId(dto.getApprovalUserId());
        
        // 更新状态
        contract.setStatus(Contract.STATUS_COUNTERSIGNING);
        
        Contract saved = contractRepository.save(contract);
        System.out.println("保存后的数据库值: " + saved.getCountersignUsers());
        return saved;
    }
}