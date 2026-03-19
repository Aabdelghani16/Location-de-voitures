package org.example.carrental.controller;

import org.example.carrental.model.Car;
import org.example.carrental.model.Purchase;
import org.example.carrental.model.Rental;
import org.example.carrental.model.User;
import org.example.carrental.repository.PurchaseRepository;
import org.example.carrental.repository.RentalRepository;
import org.example.carrental.repository.UserRepository;
import org.example.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }

    @PostMapping("/buy/{id}")
    public String buyCar(@PathVariable Long id, Principal principal) {
        Car car = carService.getCar(id);
        User user = userRepository.findByEmail(principal.getName());

        Purchase purchase = new Purchase();
        purchase.setCar(car);
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setPrice(car.getSalePrice());
        purchase.setStatus("PENDING");

        purchaseRepository.save(purchase);

        return "redirect:/cars/my-orders";
    }

    @PostMapping("/rent/{id}")
    public String rentCar(@PathVariable Long id,
                          @RequestParam LocalDate startDate,
                          @RequestParam LocalDate endDate,
                          Principal principal) {
        Car car = carService.getCar(id);
        User user = userRepository.findByEmail(principal.getName());

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        double total = days * car.getRentalPrice();

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setUser(user);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalPrice(total);
        rental.setStatus("PENDING");

        rentalRepository.save(rental);

        return "redirect:/cars/my-orders";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());

        model.addAttribute("purchases", purchaseRepository.findByUser(user));
        model.addAttribute("rentals", rentalRepository.findByUser(user));

        return "my-orders";
    }
}
