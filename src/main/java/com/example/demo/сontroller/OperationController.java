package com.example.demo.сontroller;

import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import com.example.demo.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class OperationController {

    OperationRepository operationRepository;

    @Autowired
    public OperationController(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public String getOperationById(@RequestParam("id") Long id) {

        String response = "No such operation";

        Operation operation = this.operationRepository.findById(id).orElse(null);

        if (operation != null) response = operation.toString();

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/all")
    public String getAll() {
        List<Operation> operations = (List<Operation>) operationRepository.findAll();
        StringBuilder response = new StringBuilder();
        for (Operation operation : operations) {
            response.append("<p>").append(operation.toString()).append("</p>");
        }
        return response.toString();
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
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete() {



        return new ResponseEntity<>("BKG", HttpStatus.ACCEPTED);
    }
}

