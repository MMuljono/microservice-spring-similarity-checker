package com.masterarbeit.extractdocument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Class to manage the async process of extracting the content from submissions
 * */

@EnableAsync
@Component
@AllArgsConstructor
public class DocumentOrchestrator {
    private final RestTemplate restTemplate;
    @Async("processExecutorDocument")
    public void asynctestprocess(File file, String lecturer, String assignment, String module, String semester, String type, String submissionSetName) throws IOException {
        Integer indexType = 0;
        if (type != null && type.contains("set")) {
            indexType = 1;
        }
        HashMap<String, List<DocumentFolderFormat>> content = new DocumentExtract().content(file,indexType);
        content.entrySet().forEach(e-> {
                    e.getValue().forEach(x-> {
                        DocumentFormat jsonPayload = new DocumentFormat(lecturer, module, assignment, semester, submissionSetName, e.getKey(),x.getFolderName(),x.getFiles());
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            Object jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonPayload);
                            ResponseEntity<Object> response = restTemplate.postForEntity("http://fingerprint/fingerprint/convert", jsonString, Object.class);
                        } catch (JsonProcessingException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
        );
    }
}
