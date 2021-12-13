package com.example.onlinestoreproject.repostories;
import com.example.onlinestoreproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
    List<Users>  findAllByIdIsNot(Long id);
}
