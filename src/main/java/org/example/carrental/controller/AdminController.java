package org.example.carrental.controller;

import org.example.carrental.dto.PaymentRequest;
import org.example.carrental.messaging.PaymentSender;
import org.example.carrental.model.Car;
import org.example.carrental.model.Purchase;
import org.example.carrental.model.Rental;
import org.example.carrental.repository.PurchaseRepository;
import org.example.carrental.repository.RentalRepository;
import org.example.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CarService carService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PaymentSender paymentSender;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("car", new Car());
        model.addAttribute("rentals", rentalRepository.findAll());
        model.addAttribute("purchases", purchaseRepository.findAll());
        return "admin";
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car) {
        car.setAvailable(true);
        carService.saveCar(car);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editCarPage(@PathVariable Long id, Model model) {
        Car car = carService.getCar(id);
        if (car == null) {
            return "redirect:/admin";
        }
        model.addAttribute("car", car);
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("rentals", rentalRepository.findAll());
        model.addAttribute("purchases", purchaseRepository.findAll());
        return "admin";
    }

    @PostMapping("/edit/{id}")
    public String editCar(@PathVariable Long id, @ModelAttribute Car car) {
        Car existing = carService.getCar(id);
        if (existing != null) {
            car.setId(id);
            car.setAvailable(existing.isAvailable());
            carService.saveCar(car);
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    @Transactional
    public String deleteCar(@PathVariable Long id) {
        rentalRepository.deleteByCar_Id(id);
        purchaseRepository.deleteByCar_Id(id);
        carService.deleteCar(id);
        return "redirect:/admin";
    }

    @PostMapping("/rental/confirm/{id}")
    public String confirmRental(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental != null) {
            rental.setStatus("PROCESSING");
            rentalRepository.save(rental);

            PaymentRequest request = new PaymentRequest(
                    rental.getId(),
                    "RENTAL",
                    rental.getUser().getNumeroCompte(),
                    rental.getTotalPrice()
            );
            paymentSender.sendPaymentRequest(request);
        }
        return "redirect:/admin";
    }

    @PostMapping("/rental/cancel/{id}")
    public String cancelRental(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental != null) {
            rental.setStatus("CANCELLED");
            rentalRepository.save(rental);
        }
        return "redirect:/admin";
    }

    @PostMapping("/purchase/confirm/{id}")
    public String confirmPurchase(@PathVariable Long id) {
        Purchase purchase = purchaseRepository.findById(id).orElse(null);
        if (purchase != null) {
            purchase.setStatus("PROCESSING");
            purchaseRepository.save(purchase);

            PaymentRequest request = new PaymentRequest(
                    purchase.getId(),
                    "PURCHASE",
                    purchase.getUser().getNumeroCompte(),
                    purchase.getPrice()
            );
            paymentSender.sendPaymentRequest(request);
        }
        return "redirect:/admin";
    }

    @PostMapping("/purchase/cancel/{id}")
    public String cancelPurchase(@PathVariable Long id) {
        Purchase purchase = purchaseRepository.findById(id).orElse(null);
        if (purchase != null) {
            purchase.setStatus("CANCELLED");
            purchaseRepository.save(purchase);
        }
        return "redirect:/admin";
    }
}
