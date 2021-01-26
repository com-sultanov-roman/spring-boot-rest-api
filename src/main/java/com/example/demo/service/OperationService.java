package com.example.demo.service;


import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {
    
    private final OperationRepository operationRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository){
        this.operationRepository = operationRepository;
    }



    public Operation getById(Long id){
        return this.operationRepository.findById(id).orElse(null);
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

    @Cacheable("id")
    public List<Operation> getAll(){
        return (List<Operation>)operationRepository.findAll();
    }
}
