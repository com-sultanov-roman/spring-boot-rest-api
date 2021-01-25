package com.example.demo.repository;

import com.example.demo.model.Operation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface OperationRepository extends CrudRepository<Operation, Long> {

    @Query(value = "SELECT * FROM operation o WHERE o.hash = :hash", nativeQuery = true)
    Collection<Operation> getByHash(@Param("hash") Integer hash);


}
