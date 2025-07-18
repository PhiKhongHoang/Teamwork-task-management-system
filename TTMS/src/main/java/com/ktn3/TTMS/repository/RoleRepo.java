package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.constant.RoleScope;
import com.ktn3.TTMS.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByNameAndScope(String name, RoleScope scope);
}
