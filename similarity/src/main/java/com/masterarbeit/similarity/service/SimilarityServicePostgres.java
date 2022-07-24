package com.masterarbeit.similarity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterarbeit.similarity.*;
import com.masterarbeit.similarity.entity.*;
import com.masterarbeit.similarity.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SimilarityServicePostgres {
    private final SubmissionDepotRepository submissionDepotRepository;
    private final SubmissionSetRepository submissionSetRepository;
    private final SubmissionRepository submissionRepository;
    private final SubmissionFolderFileRepository submissionFolderFileRepository;
    private final SubmissionDataFileRepository submissionDataFileRepository;
    private final FingerprintRepository fingerprintRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public void addProject(DocumentFormat project) {
        // Run Upsert for Depot, Set, Submission, Folder
        submissionDepotRepository.upsertByLecturerNameModuleName(project.getLecturer(),project.getModule());
        Optional<SubmissionDepot> depot = submissionDepotRepository.findFirstByLecturerNameAndAndModuleName(project.getLecturer(), project.getModule());

        submissionSetRepository.upsertBySubmissionSetNameSemesterDepotId(project.getSubmissionSetName(), project.getSemester(), project.getAssignment(), depot.get().getSubmissionDepotId());
        Optional<SubmissionSet> set = submissionSetRepository.findFirstBySubmissionSetNameAndSemester(project.getSubmissionSetName(), project.getSemester());

        submissionRepository.upsertBySubmissionName(project.getSubmissionName(), set.get().getSubmissionSetId());
        Optional<Submission> sub = submissionRepository.findFirstBySubmissionName(project.getSubmissionName());


        submissionFolderFileRepository.upsertByFolderName(project.getFolderName(),sub.get().getSubmissionId());
        Optional<SubmissionFolderFile> folder = submissionFolderFileRepository.findFirstByFolderNameAndSubmission(project.getFolderName(), sub.get());

        for (DocumentFileFormat documentFile : project.getContent()) {
            SubmissionDataFile dataFile = SubmissionDataFile
                    .builder()
                    .fileName(documentFile.getFileName())
                    .submissionFolderFile(folder.get()).build();
            submissionDataFileRepository.save(dataFile);

            documentFile.getFingerprints().forEach(hash -> {
                    Fingerprint fingerprint = Fingerprint.builder().hashValue(hash).submissionDataFile(dataFile).build();
                    fingerprintRepository.save(fingerprint);
            });
        }
        }

        @Transactional
        public List<SimilaritySummary> compareBasedOnFolder(String folderName) throws JsonProcessingException {
            Optional<List<SubmissionFolderFile>> result = submissionFolderFileRepository.findAllByFolderName((folderName));
            if (result.isPresent()) {
                List<SimilaritySummary> summaryResult = new ArrayList<SimilaritySummary>();
                List<SubmissionFolderFile> folders = result.get();
                System.out.println("total folder " +folders.size());
                Integer counttotalnumber = 0;
                for (int i = 0; i < folders.size(); i++) {
                    for (int j = i+1; j < folders.size(); j++) {
                        if (!folders.get(i).getSubmission().getSubmissionId().equals(folders.get(j).getSubmission().getSubmissionId())) {
                            SubmissionFolderFile doc1 = folders.get(i);
                            SubmissionFolderFile doc2 = folders.get(j);
                            Long similarityCosinus = Math.round(new SimilarityCosinus(doc1, doc2).result() * 100);
                            Long similarityEuclianDistance = Math.round(new SimilarityEuclianDistance(doc1, doc2).result());
                            Long similarityJaccard = Math.round(new SimilarityJaccard(doc1, doc2).result() * 100);
                            if (similarityCosinus >= 50) {
                                SimilaritySummary summary = SimilaritySummary.builder()
                                        .folderName(doc1.getFolderName())
                                        .mainSubmission(doc1.getSubmission().getSubmissionName())
                                        .targetSubmission(doc2.getSubmission().getSubmissionName())
                                        .mainSubmissionSetName(doc1.getSubmission().getSubmissionSet().getSubmissionSetName())
                                        .targetSubmissionSetName(doc2.getSubmission().getSubmissionSet().getSubmissionSetName())
                                        .mainSubmissionSetSemester(doc1.getSubmission().getSubmissionSet().getSemester())
                                        .targetSubmissionSetSemester(doc2.getSubmission().getSubmissionSet().getSemester())
                                        .similarityResult(similarityCosinus+"%")
                                        .distance(similarityEuclianDistance.toString())
                                        .uniqueHashInCommon(similarityJaccard.toString())
                                        .build();
                                summaryResult.add(summary);
                                System.out.println("Main Project is  " +doc1.getSubmission().getSubmissionName() + "  " + " & " + doc2.getSubmission().getSubmissionName());
                                System.out.println("Main similarity score  "+ similarityCosinus+"%");
                                System.out.println("Distance vector  " + similarityEuclianDistance);
                                System.out.println("Amount Similar component  " + similarityJaccard);
                                counttotalnumber++;
                            }
                        }

                    }
                }
                System.out.println("total calculation done: " + counttotalnumber);
                System.out.println("total result summary " + summaryResult.size());
                ObjectMapper mapper = new ObjectMapper();
                Object jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(summaryResult);
                ResponseEntity<Object> response = restTemplate.postForEntity("http://mailservice/mail", jsonString, Object.class);
//                System.out.println(response);
                return summaryResult;
            }
            return List.of();
        }

    @Transactional
    public List<SimilaritySummary> compareBasedOnSubmissionSet(String submissionSetName) throws JsonProcessingException {
        Optional<SubmissionSet> result = submissionSetRepository.findFirstBySubmissionSetName(submissionSetName);
        if (result.isPresent()) {
            List<SimilaritySummary> summaryResult = new ArrayList<SimilaritySummary>();
            SubmissionSet set = result.get();
            List<Submission> submissions = set.getSubmissions();
            submissions.forEach(e->System.out.println(e.getSubmissionName()));
            Integer counttotalnumber = 0;
            for (int i = 0; i < submissions.size(); i++) {
                for (int j = i+1; j < submissions.size(); j++) {
                    if (!submissions.get(i).getSubmissionName().equals(submissions.get(j).getSubmissionName())) {
                        Submission sub1 = submissions.get(i);
                        Submission sub2 = submissions.get(j);
                        Long similarityCosinus = Math.round(new SimilarityCosinus(sub1, sub2).result() * 100);
                        Long similarityEuclianDistance = Math.round(new SimilarityEuclianDistance(sub1, sub2).result());
                        Long similarityJaccard = Math.round(new SimilarityJaccard(sub1, sub2).result() * 100);
                        counttotalnumber++;
                        if (similarityCosinus >= 70) {
                            SimilaritySummary summary = SimilaritySummary.builder()
                                    .mainSubmission(sub1.getSubmissionName())
                                    .targetSubmission(sub2.getSubmissionName())
                                    .mainSubmissionSetName(sub1.getSubmissionSet().getSubmissionSetName())
                                    .targetSubmissionSetName(sub2.getSubmissionSet().getSubmissionSetName())
                                    .mainSubmissionSetSemester(sub1.getSubmissionSet().getSemester())
                                    .targetSubmissionSetSemester(sub2.getSubmissionSet().getSemester())
                                    .similarityResult(similarityCosinus+"%")
                                    .distance(similarityEuclianDistance.toString())
                                    .uniqueHashInCommon(similarityJaccard.toString())
                                    .build();
                            summaryResult.add(summary);
                            System.out.println("Main Project is  " +sub1.getSubmissionName() + "  " + " & " + sub2.getSubmissionName());
                            System.out.println("Main similarity score  "+ similarityCosinus+"%");
                            System.out.println("Distance vector  " + similarityEuclianDistance);
                            System.out.println("Amount Similar component  " + similarityJaccard);
                        }
                    }
                }
            }
            System.out.println("total result summary " + summaryResult.size() );
            ObjectMapper mapper = new ObjectMapper();
            Object jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(summaryResult);
            ResponseEntity<String> response = restTemplate.postForEntity("http://mailservice/mail", jsonString, String.class);
            return summaryResult;
        }
        return List.of();
    }

    @Transactional
    public void deleteSubmissionSet(String submissionSetName) {
        Optional<SubmissionSet> set = submissionSetRepository.findFirstBySubmissionSetName(submissionSetName);
        if (set.isPresent()) {
        List<Submission> sub = set.get().getSubmissions();
        set.get().getSubmissions().removeAll(sub);
        }
    }

    public Submission getSubmissionByName(String submissionName) {
        Optional<Submission> result =  submissionRepository.findFirstBySubmissionName(submissionName);
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
}
