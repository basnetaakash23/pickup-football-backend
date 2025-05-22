package com.football.pickup.games.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.football.pickup.games.dto.request.OtpRequest;
import com.football.pickup.games.dto.request.OtpVerificationRequest;
import com.football.pickup.games.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/otp")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/requests")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) throws JsonProcessingException {
        otpService.sendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        return isValid
                ? ResponseEntity.ok("OTP is valid")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
    }


}
