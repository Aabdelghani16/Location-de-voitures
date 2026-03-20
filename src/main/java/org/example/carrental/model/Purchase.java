package org.example.carrental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Car car;

    private LocalDate purchaseDate;

    private double price;

    private String status;


    public void setCar(Car car){
        this.car = car;
    }

    public void setUser(User user){
        this.user = user;
    }
    public void setPurchaseDate(LocalDate purchaseDate){
        this.purchaseDate = purchaseDate;
    }
    public void setPrice(double price){
        this.price=price;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public Long getId(){
        return id;
    }

    public Car getCar() {
        return car;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}