package org.example.carrental.repository;

import org.example.carrental.model.Rental;
import org.example.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);
}