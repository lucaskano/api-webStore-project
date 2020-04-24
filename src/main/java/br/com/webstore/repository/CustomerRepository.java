package br.com.webstore.repository;

import br.com.webstore.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 22/04/2020
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    public List<Customer> findByNameIgnoreCaseContaining(String name);
    public List<Customer> findByDocumentNumberIgnoreCaseContaining(String documentNumber);
}
