package com.example.demo.service;

import com.example.demo.model.PriceAdjustmentLog;
import java.util.List;

public interface PriceAdjustmentLogService {
    List<PriceAdjustmentLog> getAdjustmentsByEvent(Long eventId);
}
