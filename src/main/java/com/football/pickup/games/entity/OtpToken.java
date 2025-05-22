package com.football.pickup.games.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OtpToken {

    @Id
    private String phoneOrEmail; // could be phone/email as identifier

    private String otp;
    private LocalDateTime expiresAt;


}
