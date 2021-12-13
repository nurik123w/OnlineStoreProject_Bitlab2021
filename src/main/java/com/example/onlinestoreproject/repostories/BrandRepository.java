package com.example.onlinestoreproject.repostories;

import com.example.onlinestoreproject.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand,Long> {

}
