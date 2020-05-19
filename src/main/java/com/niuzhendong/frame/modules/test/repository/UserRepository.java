package com.niuzhendong.frame.modules.test.repository;

import com.niuzhendong.frame.modules.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
