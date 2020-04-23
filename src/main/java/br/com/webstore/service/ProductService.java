package br.com.webstore.service;

import br.com.webstore.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
@Validated
public interface ProductService {

    @NotNull Iterable<Product> getAllProducts(Pageable pageable);

    Product getProduct(@Min(value = 1L, message = "Invalid product ID.") Long id);

    Product save(Product product);
}
