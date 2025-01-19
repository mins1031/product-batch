package com.example.productbatch.intrastructure.repository.jpa;

import com.example.productbatch.intrastructure.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
