package br.com.webstore.service;

import br.com.webstore.Repository.ProductRepository;
import br.com.webstore.error.ResourceNotFoundException;
import br.com.webstore.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public @NotNull Iterable<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found for ID: " + id));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
