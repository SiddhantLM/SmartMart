package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.StockReminder;

public interface StockReminderService {
    StockReminder addReminder (Long productId ,String email);
    boolean sendReminder (Long id);   
    List<StockReminder> getReminder ();
}

