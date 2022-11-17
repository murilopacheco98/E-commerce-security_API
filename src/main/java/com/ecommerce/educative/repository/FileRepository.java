package com.ecommerce.educative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.educative.model.product.FileInfo;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long>{
  
}
