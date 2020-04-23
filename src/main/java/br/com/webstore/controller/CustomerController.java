package br.com.webstore.controller;

import br.com.webstore.Repository.CustomerRepository;
import br.com.webstore.dto.CustomerDTO;
import br.com.webstore.error.ResourceNotFoundException;
import br.com.webstore.model.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Project: api-webstore
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 22/04/2020
 */
@RestController
@RequestMapping("v1")
@Api("API Rest Customer")
public class CustomerController {
    private final CustomerRepository customerDAO;

    @Autowired
    public CustomerController(CustomerRepository customerDAO){
        this.customerDAO = customerDAO;
    }

    @GetMapping(path = "/admin/customers")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Return a list with all customers", response = Customer[].class, produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    public ResponseEntity<Object> listAll(Pageable pageable){
        return new ResponseEntity<>(customerDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Return customer by given id", response = Customer.class)
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id){
        return new ResponseEntity<>(customerDAO.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/customers/findByDocument/{documentNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Return customer by given document number", response = Customer.class)
    public ResponseEntity<Object> getCustomerByDocumentNumber(@PathVariable String documentNumber){
        return new ResponseEntity<>(customerDAO.findByDocumentNumberIgnoreCaseContaining(documentNumber), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/customers/findByName/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Return customer by given name", response = Customer.class)
    public ResponseEntity<Object> getCustomersByName(@PathVariable String name){
        return new ResponseEntity<>(customerDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping(path = "protected/customers")
    @ApiOperation(value = "Save given customer", response = Customer.class, produces = "application/json" , consumes = "application/json")
    public ResponseEntity<Object> save(@RequestBody Customer customer){
        return new ResponseEntity<>(customerDAO.save(customer), HttpStatus.OK);
    }

    @PutMapping(path = "admin/customers")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update given Customer", response = Customer.class, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> update(@RequestBody CustomerDTO customerDTO){
        verifyIfCustomerExists(customerDTO.getCustomer().getId());
        customerDAO.save(customerDTO.getCustomer());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "admin/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete given Customer", response = Customer.class, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        verifyIfCustomerExists(id);
        customerDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfCustomerExists(Long id){
        if(customerDAO.findById(id).isEmpty())
            throw new ResourceNotFoundException("Customer not found for ID: " + id);
    }
}
