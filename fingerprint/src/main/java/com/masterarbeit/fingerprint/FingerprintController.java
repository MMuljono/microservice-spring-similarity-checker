package com.masterarbeit.fingerprint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/v1/fingerprint")
@AllArgsConstructor
public class FingerprintController {
    private final RestTemplate restTemplate;
    @Async("processExecutorFingerprint")
    @PostMapping
    ResponseEntity<Object> convertToFingerprint(@RequestBody String payload) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            DocumentFormat submission = mapper.readValue(payload, DocumentFormat.class);
            System.out.println(submission.getSubmissionName());
            submission.getContent().stream().forEach((c) -> {
                List<Integer> winnowed = FingerprintWinnowingService.groupingToken((List<String>) c.getFingerprints());
                c.setFingerprints(winnowed);
            });
            ObjectMapper mapper2 = new ObjectMapper();
            Object jsonString = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(submission);
            System.out.println(jsonString);
            ResponseEntity<Object> response = restTemplate.postForEntity("http://similarity/similar/add", jsonString, Object.class);
            return new ResponseEntity<Object>(jsonString, HttpStatus.OK);
    }
}
