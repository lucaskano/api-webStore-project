package br.com.webstore.dto;

import br.com.webstore.model.Customer;

/**
 * Project: api-webstore
 *
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 23/04/2020
 */
public class CustomerDTO{
    Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
