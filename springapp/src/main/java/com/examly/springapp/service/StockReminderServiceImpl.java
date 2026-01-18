package com.examly.springapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Product;
import com.examly.springapp.model.StockReminder;
import com.examly.springapp.repository.ProductRepo;
import com.examly.springapp.repository.StockReminderRepo;

@Service
public class StockReminderServiceImpl implements StockReminderService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    StockReminderRepo stockReminderRepo;

    @Autowired
    EmailService emailService;

    @Override
    public StockReminder addReminder(Long productId, String email) {
        Product product = productRepo.findById(productId).orElse(null);
        
        if(product != null) {
            StockReminder sr = new StockReminder();
            sr.setEmail(email);
            sr.setProduct(product);
            return stockReminderRepo.save(sr);
        }

        return null;
    }

    @Override
    public boolean sendReminder(Long id) {
        StockReminder sr = stockReminderRepo.findById(id).orElse(null);

        if(sr!=null) {
            emailService.sendStockReminderEmail(sr.getEmail(), sr.getProduct().getName());
            stockReminderRepo.delete(sr);
            return true;

        }

        return false;
    }

    @Override
    public List<StockReminder> getReminder() {
       return stockReminderRepo.findAll();
    }
    
}
