package br.com.webstore.repository;

import br.com.webstore.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
}
