package com.masterarbeit.extractdocument;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentController {
    private DocumentOrchestrator documentOrchestrator;

    @PostMapping(value={"/add","/add/{type}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity<String> compareDocument (@RequestPart MultipartFile file,
                                                    @RequestParam String lecturer,
                                                    @RequestParam String assignment,
                                                    @RequestParam String module,
                                                    @RequestParam String semester,
                                                    @PathVariable(value="type",required = false) String type,
                                                    @RequestParam String submissionSetName)throws IOException {


        File zip = File.createTempFile(UUID.randomUUID().toString(), "temp");
        FileOutputStream o = new FileOutputStream(zip);
        IOUtils.copy(file.getInputStream(), o);
        o.close();

        documentOrchestrator.asynctestprocess(zip,lecturer, assignment,module,semester,type,submissionSetName);
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}

