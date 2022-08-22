package com.masterarbeit.mailservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterarbeit.mailservice.entity.ComparisonResult;
import com.masterarbeit.mailservice.entity.SimilaritySummary;
import com.masterarbeit.mailservice.repository.ComparisonResultRepository;
import com.masterarbeit.mailservice.repository.SimilaritySummaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.DataInput;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private final ComparisonResultRepository comparisonResultRepository;
    private final SimilaritySummaryRepository similaritySummaryRepository;

    public void sendEmail(String toEmail,
                          String subject,
                          String body) {
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom("microservicefinalthesisproject@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail sent");
    }

    public void addSummaryToDatabase(String pathUrl, List<SimilaritySummary> submission, String email){
        ComparisonResult summaryResult = ComparisonResult
                .builder()
                .uniquePath(pathUrl)
                .email(email)
                .build();
        comparisonResultRepository.save(summaryResult);
        submission.forEach(r -> {
                r.setComparisonResult(summaryResult);
                similaritySummaryRepository.save(r);
        });
        sendEmail(email,"Result similarity","Please open the link to see result http://localhost:9191/mail/result/" +pathUrl);
    }

    public ComparisonResult getSummaryResult (String uniquePath) throws JsonProcessingException {
        Optional<ComparisonResult> compareResult = comparisonResultRepository.findAllByUniquePath(uniquePath);
        if (compareResult.isPresent()) {
            return compareResult.get();
        }
        return null;
    }
}
