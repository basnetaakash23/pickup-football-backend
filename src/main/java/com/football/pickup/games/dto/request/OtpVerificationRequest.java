package com.football.pickup.games.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OtpVerificationRequest {

    private String email;

    private String otp;
}
