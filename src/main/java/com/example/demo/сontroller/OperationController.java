package com.example.demo.сontroller;

import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import com.example.demo.service.OperationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class OperationController {

    OperationRepository operationRepository;

    @Autowired
    public OperationController(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getOperationById(@RequestParam("id") Long id) throws JsonProcessingException {

        String response = "No such operation";

        Operation operation = this.operationRepository.findById(id).orElse(null);

        ObjectMapper objectMapper = new ObjectMapper();
        TreeMap<String, Operation> map = new TreeMap<>();
        map.put("operation", operation);

        if (operation != null)
            response = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public String getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "limit", defaultValue = "5") int limit) throws JsonProcessingException {

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

        Map<String, List<Operation>> map = new TreeMap<>();

        ObjectMapper objectMapper = new ObjectMapper();

        map.put("operations", operationRepository.findAll(pageable).toList());

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestParam(value = "status") Integer status,
                                         @RequestParam(value = "senderCardId") Integer senderCardId,
                                         @RequestParam(value = "recipientCardId") Integer recipientCardId,
                                         @RequestParam(value = "paymentAmount") BigDecimal paymentAmount,
                                         @RequestParam(value = "paymentCommission") BigDecimal paymentCommission,
                                         @RequestParam(value = "currency") String currency) {

        String body = "such operation already exists";
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

        Operation operation = new Operation(status,
                senderCardId,
                recipientCardId,
                paymentAmount,
                paymentCommission,
                currency);

        Collection<Operation> operations = operationRepository.getByHash(operation.hashCode());

        if (operations.isEmpty()) {
            this.operationRepository.save(operation);
            body = "OK";
            httpStatus = HttpStatus.CREATED;
        }

        System.out.println("ON POST REQUEST");
        return new ResponseEntity<>(body, httpStatus);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH)
    void patchById(@RequestParam(value = "id") Long id, @RequestParam(value = "currency") String currency) {
        System.out.println("visited");
        Operation operation = operationRepository.findById(id).orElse(null);

        if (operation != null) {
            operation.setCurrency(currency);
            operationRepository.save(operation);

        }
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteById(@RequestParam(name = "id") Long id) {

        String body = "no operation with id=" + id.toString();

        Operation operation = operationRepository.findById(id).orElse(null);

        if (operation != null) {
            operationRepository.delete(operation);
            body = "DELETED";
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

