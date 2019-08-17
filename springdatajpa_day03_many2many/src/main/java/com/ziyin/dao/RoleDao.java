package com.ziyin.dao;

import com.ziyin.bo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleDao extends JpaRepository<Role,Long> ,JpaSpecificationExecutor<Role> {
}
