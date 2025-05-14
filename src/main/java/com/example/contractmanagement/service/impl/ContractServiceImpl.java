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
import com.example.contractmanagement.service.ContractService;

@Service
public class ContractServiceImpl implements ContractService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    private final ContractRepository contractRepository;
    
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
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
            String path = "./uploads/" + dto.getFile().getOriginalFilename();
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
    public Contract assignContract(ContractAssignmentDTO assignmentDTO) {
        Contract contract = contractRepository.findById(assignmentDTO.getContractId())
                .orElseThrow(() -> new RuntimeException("合同不存在"));

        // 将用户ID列表转换为逗号分隔的字符串
        String countersignUserIds = String.join(",", 
                assignmentDTO.getCountersignUserIds().stream()
                        .map(String::valueOf)
                        .toArray(String[]::new));

        contract.setCountersignUsers(countersignUserIds);
        contract.setApprovalUserId(assignmentDTO.getApprovalUserId());
        contract.setStatus(Contract.STATUS_ASSIGNED); // 更新状态为已分配

        return contractRepository.save(contract);
    }
}