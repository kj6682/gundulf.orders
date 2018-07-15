package org.kj6682.gundulf.orders;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    List<Product> findAll();

}