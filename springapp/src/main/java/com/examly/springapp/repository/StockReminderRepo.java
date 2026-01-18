package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.StockReminder;


public interface StockReminderRepo extends JpaRepository<StockReminder,Long> {
    
}
