package com.joe.service.impl;

import com.joe.auth.entity.UserEntity;
import com.joe.auth.repository.UserRepository;
import com.joe.dto.MailBody;
import com.joe.dto.ResetPassword;
import com.joe.entity.ForgotPassword;
import com.joe.exception.CustomUsernameNotFoundException;
import com.joe.repository.ForgotPasswordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordService.class);
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    public String verifyEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new CustomUsernameNotFoundException("No user registered with that email")
                );
        Integer otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .body("This is the OTP to reset your password " + otp)
                .subject("Reset Password")
                .build();

        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleEmail(mailBody);
        forgotPasswordRepository.save(forgotPassword);

        return "Email Sent to: " + maskEmail(email) + " for verification";

    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String [] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];

        if (local.length() <= 2) {
            return local.charAt(0) + "***@" + domain;
        }

        return local.charAt(0) + // first character
                "*".repeat(local.length() - 2) +
                local.charAt(local.length() - 1) + // last character
                "@" + domain;
    }

    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    public String verifyOTP(Integer otp, String email) {
        log.info("--------------------------  Verifying email ----------------------------");
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomUsernameNotFoundException("No user registered with that email"));

        log.info("--------------------------- Verifying OTP -----------------------------");
        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new CustomUsernameNotFoundException("OTP is invalid, please enter a valid OTP"));
        log.info("--------------------------- Checking validity of OTP ------------------------");
        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return "OTP Has expired";
        }
        log.info("------------------------ OTP has been successfully verified ----------------------");
        return "OTP has been successfully verified";
    }

    public String changePassword(String email, ResetPassword resetPassword) {
        if (!Objects.equals(resetPassword.password(), resetPassword.repeatPassword())){
            return "Passwords do not much please type password again";
        }

        String encodePassword = encoder.encode(resetPassword.repeatPassword());
        userRepository.updateUserPassword(email, encodePassword);

        return "Password updated successfully";

    }
}
