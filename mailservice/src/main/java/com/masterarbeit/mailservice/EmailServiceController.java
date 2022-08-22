package com.masterarbeit.mailservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterarbeit.mailservice.entity.ComparisonResult;
import com.masterarbeit.mailservice.entity.SimilarityMailFormat;
import com.masterarbeit.mailservice.entity.SimilaritySummary;
import com.masterarbeit.mailservice.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/mail")
@AllArgsConstructor
public class EmailServiceController {
    private final EmailService emailService;
    @PostMapping
    ResponseEntity<String> addComparisonResult(@RequestBody String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimilarityMailFormat result = mapper.readValue(payload, SimilarityMailFormat.class);
        String email = result.getEmail();
        List<SimilaritySummary> submission = result.getSummary();
        String uuid = UUID.randomUUID().toString();
        Integer sizedId = uuid.length();
        String uniqueIdPathUrl = uuid.substring(sizedId-8,sizedId);
        emailService.addSummaryToDatabase(uniqueIdPathUrl, submission, email);
        return new ResponseEntity<String>("Okay", HttpStatus.OK);
    }

    @GetMapping(value={"/result/{id}"})
    public ResponseEntity<Object> checkResult (@PathVariable(value="id") String id) throws JsonProcessingException {
        ComparisonResult result = emailService.getSummaryResult(id);
        ObjectMapper mapper = new ObjectMapper();
        Object jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(jsonString);
        return new ResponseEntity<Object>(jsonString, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> checkEmailWorking () throws JsonProcessingException {
        emailService.sendEmail("s36838@bht-berlin.de","testing email", "asg");
        return new ResponseEntity<Object>("okay", HttpStatus.OK);
    }
}



