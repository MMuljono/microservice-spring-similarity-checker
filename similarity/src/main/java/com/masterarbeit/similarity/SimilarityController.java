package com.masterarbeit.similarity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterarbeit.similarity.entity.Submission;
import com.masterarbeit.similarity.service.SimilarityServicePostgres;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/similar")
@AllArgsConstructor
@Controller
public class SimilarityController {

    private final SimilarityServicePostgres similarityServicePostgres;
    @Async("processExecutorSimilarity")
    @PostMapping("/compare/folder")
    public ResponseEntity<String> list(@RequestParam String folderName) throws JsonProcessingException {
        List<SimilaritySummary> result = similarityServicePostgres.compareBasedOnFolder(folderName);
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @Async("processExecutorSimilarity")
    @PostMapping("/add")
    ResponseEntity<Object> postgres(@RequestBody String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DocumentFormat project = mapper.readValue(payload, DocumentFormat.class);
            System.out.println(project.getSubmissionName());
            similarityServicePostgres.addProject(project);
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }

    @Async("processExecutorSimilarity")
    @PostMapping("/compare/set")
    public ResponseEntity<String> comparisonOneSubmissionSet(@RequestParam String submissionSetName) throws JsonProcessingException {
        List<SimilaritySummary> result = similarityServicePostgres.compareBasedOnSubmissionSet(submissionSetName);
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @Async("processExecutorSimilarity")
    @DeleteMapping("/delete/set")
    public ResponseEntity<String> deleteSubmissionSet(@RequestParam String submissionSetName) throws JsonProcessingException {
        similarityServicePostgres.deleteSubmissionSet(submissionSetName);
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping(value={"/submission/{id}"})
    public ResponseEntity<Object> seeSubmission(@PathVariable(value="id") String submissionName) throws JsonProcessingException {
        Submission result = similarityServicePostgres.getSubmissionByName(submissionName);
        ObjectMapper mapper = new ObjectMapper();
        Object jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        return new ResponseEntity<Object>(jsonString, HttpStatus.OK);
    }

    @PostMapping("/submission/compare/two")
    public ResponseEntity<Object> compareSubmissionEachOther(@RequestParam String submissionMain, @RequestParam String submissionTarget) throws JsonProcessingException {
        Submission sub1 = similarityServicePostgres.getSubmissionByName(submissionMain);
        Submission sub2 = similarityServicePostgres.getSubmissionByName(submissionTarget);
        Long similarityCosinus = Math.round(new SimilarityCosinus(sub1, sub2).result() * 100);
        Long similarityEuclianDistance = Math.round(new SimilarityEuclianDistance(sub1, sub2).result());
        Long similarityJaccard = Math.round(new SimilarityJaccard(sub1, sub2).result() * 100);
        JSONObject json = new JSONObject();
        json.put("main", similarityCosinus);
        json.put("distance", similarityEuclianDistance);
        json.put("uniqueHashInCommon", similarityJaccard);
        ObjectMapper mapper = new ObjectMapper();
        Object jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        return new ResponseEntity<Object>(jsonString, HttpStatus.OK);
    }


}

