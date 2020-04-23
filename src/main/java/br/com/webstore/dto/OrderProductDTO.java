package br.com.webstore.dto;

import br.com.webstore.model.Customer;
import br.com.webstore.model.Product;

/**
 * Project: api-webstore
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 23/04/2020
 */
public class OrderProductDTO {
    private Product product;
    private Customer customer;
    private Integer quantity;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
