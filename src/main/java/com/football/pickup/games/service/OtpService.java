package com.football.pickup.games.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.football.pickup.games.dto.request.EmailRequestDto;
import com.football.pickup.games.entity.OtpToken;
import com.football.pickup.games.repository.OtpTokenRepository;
import com.football.pickup.games.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final EmailEventProducer emailEventProducer;

    public void sendOtp(String phoneOrEmail) throws JsonProcessingException {
        String otp = OtpUtil.generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        OtpToken token = new OtpToken(phoneOrEmail, otp, expiry);
        otpTokenRepository.save(token);

        // Create email request
        EmailRequestDto emailRequest = new EmailRequestDto();
        emailRequest.setTo(phoneOrEmail);
        emailRequest.setSubject("Your OTP Code");
        emailRequest.setMessage("Your OTP is: " + otp);

        // Send to Kafka
        emailEventProducer.sendEmailEvent(emailRequest);

        // Send SMS or email (you need to implement this)
        System.out.println("Sending OTP to " + phoneOrEmail + ": " + otp);
    }

    public boolean verifyOtp(String phoneOrEmail, String enteredOtp) {
        return otpTokenRepository.findById(phoneOrEmail)
                .filter(token -> token.getOtp().equals(enteredOtp))
                .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}

