package com.example.demo.repository;

import com.example.demo.model.Operation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface OperationRepository extends PagingAndSortingRepository<Operation, Long> {

    @Query(value = "SELECT * FROM operation o WHERE o.hash = :hash", nativeQuery = true)
    List<Operation> getByHash(@Param("hash") Integer hash);
}
