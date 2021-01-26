package com.example.demo.сontroller;

import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@ResponseBody
@Controller
public class OperationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);

    private final OperationRepository operationRepository;

    @Autowired
    public OperationController(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }


    @ApiResponse(description = "Operation by specified id")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getOperationById(@Parameter(name = "id", description = "operation id")
                                       @RequestParam("id") Long id) throws JsonProcessingException {

        LOGGER.info("GET operation with id: " + id);

        String response = "No such operation";

        Operation operation = this.operationRepository.findById(id).orElse(null);

        ObjectMapper objectMapper = new ObjectMapper();
        TreeMap<String, Operation> map = new TreeMap<>();
        map.put("operation", operation);

        if (operation != null) {
            response = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } else {
            LOGGER.info("No such operation with id: " + id);
        }

        return response;
    }

    @ApiResponse(description = "List of operation corresponding specified page and limit parameters")
    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public String getAll(@Parameter(description = "page number")
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @Parameter(description = "size of page")
                         @RequestParam(value = "limit", defaultValue = "5") int limit) throws JsonProcessingException {

        LOGGER.info("GET * with page: " + page + " and limit: " + limit);

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

        Map<String, List<Operation>> map = new TreeMap<>();

        ObjectMapper objectMapper = new ObjectMapper();

        map.put("operations", operationRepository.findAll(pageable).toList());

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }


    @ApiResponse(responseCode = "200",
            description = "Creates new operation if it does not exist. Otherwise returns message: such operation already exists")
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

        LOGGER.info("POST operation: " + operation);


        Collection<Operation> operations = operationRepository.getByHash(operation.hashCode());

        if (operations.isEmpty()) {
            this.operationRepository.save(operation);
            body = "OK";
            httpStatus = HttpStatus.CREATED;
            LOGGER.info("operation already exists");
        }

        LOGGER.info("Success. Operation hash: " + operation.hashCode());


        System.out.println("ON POST REQUEST");
        return new ResponseEntity<>(body, httpStatus);
    }

    @ApiResponse(description = "Updates currency by id")
    @RequestMapping(method = RequestMethod.PATCH)
    void patchById(@RequestParam(value = "id") Long id, @RequestParam(value = "currency") String currency) {
        System.out.println("visited");
        Operation operation = operationRepository.findById(id).orElse(null);

        if (operation != null) {
            operation.setCurrency(currency);
            operationRepository.save(operation);
            LOGGER.info("PATCH. Modified. Patched operation: " + operation);
        } else {
            LOGGER.info("PATCH. No such operation with id: " + id);
        }
    }

    @ApiResponse(description = "Deletes operation by id. If the operation with specified id does not exist, " +
            "returns message: no operation with id... Otherwise returns: Deleted")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteById(@RequestParam(name = "id") Long id) {

        String body = "no operation with id=" + id.toString();

        Operation operation = operationRepository.findById(id).orElse(null);

        if (operation != null) {
            operationRepository.delete(operation);
            body = "DELETED";
            LOGGER.info("DELETE. Success. Deleted operation: " + operation);
        } else {
            LOGGER.info("DELETE. No such operation");
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

