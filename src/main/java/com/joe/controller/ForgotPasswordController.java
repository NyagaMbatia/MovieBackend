package com.joe.controller;

import com.joe.dto.ResetPassword;
import com.joe.service.impl.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgot-password/")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    // send mail for forgot password
    @PostMapping("verify-email/{email}")
    public ResponseEntity<String> verifyEmailHandler(@PathVariable String email){
        return ResponseEntity.ok(forgotPasswordService.verifyEmail(email));

    }

    // verify otp
    @PostMapping("verify-otp/{otp}/{email}")
    public ResponseEntity<String > verifyOTPHandler(@PathVariable Integer otp, @PathVariable String email){
        return ResponseEntity.ok(forgotPasswordService.verifyOTP(otp, email));
    }

    // Change password
    @PostMapping("change-password/{email}")
    public ResponseEntity<String> changePasswordHandler(@PathVariable String email, @RequestBody ResetPassword resetPassword){
        return ResponseEntity.ok(forgotPasswordService.changePassword(email, resetPassword));


    }
}
