package com.service.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.user.entities.DAOUser;

@Repository
public interface UserRepository extends JpaRepository<DAOUser, Long> {

    Optional<DAOUser> findByLogin(String login);
}
