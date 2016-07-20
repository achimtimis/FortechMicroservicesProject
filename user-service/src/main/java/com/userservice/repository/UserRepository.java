package com.userservice.repository;

import com.shopcommon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Achim Timis on 7/6/2016.
 */
public interface UserRepository extends JpaRepository<User,Long> {


}
