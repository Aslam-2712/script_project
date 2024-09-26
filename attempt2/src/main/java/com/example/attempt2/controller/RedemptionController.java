package com.example.attempt2.controller;

import com.example.attempt2.service.RedemptionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RedemptionController {

    private static final Logger log = LoggerFactory.getLogger(RedemptionController.class);

    @Autowired
    private RedemptionService redemptionService;

    @PostMapping("/redemption")
    public String processRedemption(@RequestParam("file") MultipartFile file,
                                    @RequestParam String token,
                                    @RequestParam String forceRedemptionType,
                                    @RequestParam String forceMessage,
                                    @RequestParam String url) throws IOException {

        if (file.isEmpty() || token.isEmpty() || forceRedemptionType.isEmpty() || forceMessage.isEmpty() || url.isEmpty()) {
            return "Invalid input: All parameters are required.";
        }

        List<Map<String, String>> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             //CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {

            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> recordMap = new HashMap<>(csvRecord.toMap());
                records.add(recordMap);
            }

          System.out.println("file parsed");
     

            // Call the service method with the list of records and the thread count
            redemptionService.processCsv(records, token, forceRedemptionType, forceMessage, url);

            return "Request processing started.";

        } catch (IOException e) {
            log.error("Error processing CSV file", e);
            return "Error processing CSV file.";
        }
    }
}

