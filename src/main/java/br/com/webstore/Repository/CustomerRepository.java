package br.com.webstore.Repository;

import br.com.webstore.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;

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
