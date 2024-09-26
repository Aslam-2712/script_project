package com.example.attempt2.service;

import java.util.List;
import java.util.Map;

public interface RedemptionService  {

     void processCsv(List<Map<String, String>> records, String token, String forceRedemptionType, String forceMessage, String url);


}
