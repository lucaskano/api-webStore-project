package br.com.webstore.service;

import br.com.webstore.model.Customer;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
public interface CustomerService {
    @NotNull Iterable<Customer> getAllCustomers(Pageable pageable);

    Customer getCustomer(@Min(value = 1L, message = "Invalid Customer ID.") Long id);

    Customer save(Customer customer);

    Customer update(Customer customer);
}
