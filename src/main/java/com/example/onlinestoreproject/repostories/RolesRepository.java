package com.example.onlinestoreproject.repostories;

import com.example.onlinestoreproject.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findByRole(String role);
}
