package com.example.productbatch.intrastructure.repository.jpa;

import com.example.productbatch.intrastructure.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
}
