package br.com.webstore.service;

import br.com.webstore.model.Order;
import com.sun.istack.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
@Validated
public interface OrderService {

    @NotNull Iterable<Order> getAllOrders(Pageable pageable);

    Order create(@Valid Order order);

    void update(@Valid Order order);
}
