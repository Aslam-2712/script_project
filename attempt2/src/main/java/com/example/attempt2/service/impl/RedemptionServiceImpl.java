package com.example.attempt2.service.impl;

import com.example.attempt2.errorHandler.ResponseErrorHandler;
import com.example.attempt2.model.redemptionpojo.CompleteData;
import com.example.attempt2.model.redemptionpojo.RedemptionAttributes;
import com.example.attempt2.service.RedemptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class RedemptionServiceImpl implements RedemptionService {

    @Autowired
    CompleteData completeData;
    
    @Autowired
    RedemptionAttributes redemptionAttributes;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ResponseErrorHandler responseErrorHandler;

    private final List<String> success_response = new ArrayList<>();
    private final List<String> failure_response = new ArrayList<>();

    // static int count=0;
     static int total_records=0;



    public void setRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(responseErrorHandler);
    }

    public void processCsv(List<Map<String, String>> records, String token, String forceRedemptionType,
                           String forceMessage, String url) {


        total_records = records.size();
        System.out.println("this is total record: "+total_records);


        List<Map<String, String>> list1 = new ArrayList<>();
        List<Map<String, String>> list2 = new ArrayList<>();
        List<Map<String, String>> list3 = new ArrayList<>();
        List<Map<String, String>> list4 = new ArrayList<>();
        List<Map<String, String>> list5 = new ArrayList<>();

        int chunkSize = (int) Math.ceil((double) total_records / 5);
        System.out.println("no of records in each file:"+chunkSize);

        for (int i = 0; i < total_records; i++) {
            if (i < chunkSize) {
                list1.add(records.get(i));
            } else if (i < 2 * chunkSize) {
                list2.add(records.get(i));
            } else if (i < 3 * chunkSize) {
                list3.add(records.get(i));
            } else if (i < 4 * chunkSize) {
                list4.add(records.get(i));
            } else {
                list5.add(records.get(i));
            }
        }

        Runnable obj1 =()->{
          // Process each list separately
            processList(list1, token, forceRedemptionType, forceMessage, url);
        
        };
        

        Runnable obj2 =()->{
            processList(list2, token, forceRedemptionType, forceMessage, url);
        };


        Runnable obj3 =()->{
            processList(list3, token, forceRedemptionType, forceMessage, url);
        };


        Runnable obj4 =()->{
            processList(list4, token, forceRedemptionType, forceMessage, url);
        };


        Runnable obj5 =()->{
            processList(list5, token, forceRedemptionType, forceMessage, url);
        };

            Thread t1 = new Thread(obj1, "ThreadOne");
            t1.start();

            Thread t2 = new Thread(obj2, "ThreadTwo");
            t2.start();

            Thread t3 = new Thread(obj3, "ThreadThree");
            t3.start();

            Thread t4 = new Thread(obj4, "ThreadFour");
            t4.start();

            Thread t5 = new Thread(obj5, "ThreadFive");
            t5.start();

            try {
                t1.join();
                t2.join();
                t3.join();
                t4.join();
                t5.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            // Log the results after all threads are complete
            logResponsesToFile();
            

    }

    private void processList(List<Map<String, String>> list, String token, String forceRedemptionType, String forceMessage, String url) {
        List<String> localSuccessResponse = new ArrayList<>();
        List<String> localFailureResponse = new ArrayList<>();

   
        
    
        for (Map<String, String> record : list) {
            // Create new instances for each record
            RedemptionAttributes localRedemptionAttributes = new RedemptionAttributes();
            CompleteData localCompleteData = new CompleteData();
    
            localRedemptionAttributes.setRequested_punches(record.get("requested_punches"));
            localRedemptionAttributes.setForce_redemption_type(forceRedemptionType);
            localRedemptionAttributes.setForce_message(forceMessage);
    
            localCompleteData.setUser_id(record.get("user_id"));
            localCompleteData.setRedemption(localRedemptionAttributes);
    
            HttpHeaders headers = new HttpHeaders();
            headers.set("ContentType", "application/json");
            headers.set("Authorization", "Bearer " + token);
    
            HttpEntity<CompleteData> request = new HttpEntity<>(localCompleteData, headers);
    
            try {
                String response = restTemplate.postForObject(url, request, String.class);
                localSuccessResponse.add("Success," + localCompleteData.getUser_id()+","+Thread.currentThread().getName()+"," + response);
            } catch (HttpClientErrorException e) {
                localFailureResponse.add("Failure, " + localCompleteData.getUser_id()+ "," + e.getStatusCode()+ "," + Thread.currentThread().getName());
            }
        }

        // Merge the thread-local lists into the shared lists
        synchronized (success_response) {
            success_response.addAll(localSuccessResponse);
        }
        synchronized (failure_response) {
            failure_response.addAll(localFailureResponse);
        }
    }
    
private void logResponsesToFile() {
    try (FileWriter successWriter = new FileWriter("logs/SuccessResponses.csv")) {
        for (String successEntry : success_response) {
            successWriter.write(successEntry + "\n");
        }
        System.out.println("Success responses logged to SuccessResponses.csv");
    } catch (IOException e) {
        System.out.println("Error writing to success log file: " + e.getMessage());
    }

    try (FileWriter failureWriter = new FileWriter("logs/FailureResponses.csv")) {
        for (String failureEntry : failure_response) {
            failureWriter.write(failureEntry + "\n");
        }
        System.out.println("Failure responses logged to FailureResponses.csv");
    } catch (IOException e) {
        System.out.println("Error writing to failure log file: " + e.getMessage());
    }
}
}





                         


