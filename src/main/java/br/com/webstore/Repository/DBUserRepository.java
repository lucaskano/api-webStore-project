package br.com.webstore.Repository;

import br.com.webstore.model.DBUser;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 22/04/2020
 */
public interface DBUserRepository extends PagingAndSortingRepository<DBUser, Long> {
    public DBUser findByUsername(String name);
}
