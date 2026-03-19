package org.example.carrental.repository;

import org.example.carrental.model.Purchase;
import org.example.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);
    void deleteByCar_Id(Long carId);
}