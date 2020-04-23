package br.com.webstore.Repository;

import br.com.webstore.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 22/04/2020
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    public List<Product> findByNameIgnoreCaseContaining(String name);
}
