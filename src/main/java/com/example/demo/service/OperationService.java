package com.example.demo.service;
import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OperationService {
    
    private final OperationRepository operationRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository){
        this.operationRepository = operationRepository;
    }

    public void saveInstance(Operation operation){
        this.operationRepository.save(operation);
    }

    public void deleteById(Long id){
        Operation operation = this.getById(id);
        if(operation!=null){
            this.operationRepository.deleteById(id);
        }
    }

    @Cacheable("OperationLists")
    public Page<Operation> getAll(Pageable pageable){
        return operationRepository.findAll(pageable);
    }

    @Cacheable("operations")
    public Operation getById(Long id){
        return operationRepository.findById(id).orElse(null);
    }

    @Cacheable("operations")
    public List<Operation> getByHash(Integer hash){
        return operationRepository.getByHash(hash);
    }

    public void delete(Operation operation){
        operationRepository.delete(operation);
    }

    public void save(Operation operation){
        operationRepository.save(operation);
    }

}
