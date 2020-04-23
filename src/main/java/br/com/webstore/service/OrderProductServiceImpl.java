package br.com.webstore.service;

import br.com.webstore.model.OrderProduct;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */

@Service
@Transactional
public class OrderProductServiceImpl implements OrderProductService {

    private OrderProductService orderProductService;

    public void setOrderProductService(OrderProductService orderProductService){
        this.orderProductService = orderProductService;
    }

    @Override
    public OrderProduct create(@NotNull(message = "The products for order cannot be null.") @Valid OrderProduct orderProduct) {
        return orderProductService.create(orderProduct);
    }
}
