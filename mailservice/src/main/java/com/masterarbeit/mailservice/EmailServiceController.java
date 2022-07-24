package com.masterarbeit.mailservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterarbeit.mailservice.entity.ComparisonResult;
import com.masterarbeit.mailservice.entity.SimilaritySummary;
import com.masterarbeit.mailservice.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("mail")
@AllArgsConstructor
public class EmailServiceController {
    private final EmailService emailService;
    @PostMapping
    ResponseEntity<String> addComparisonResult(@RequestBody String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimilaritySummary[] submission = mapper.readValue(payload, SimilaritySummary[].class);
        Arrays.stream(submission).toList().forEach(e->System.out.println(e));
        String uuid = UUID.randomUUID().toString();
        Integer sizedId = uuid.length();
        String uniqueIdPathUrl = uuid.substring(sizedId-8,sizedId);
        emailService.addSummaryToDatabase(uniqueIdPathUrl,submission);
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



