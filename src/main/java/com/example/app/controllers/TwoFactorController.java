package com.example.app.controllers;

import com.example.app.auth.TotpService;
import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/2fa")
public class TwoFactorController {

    @Autowired
    private TotpService totpService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/setup")
    public ResponseEntity<?> setup(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return ResponseEntity.status(401).build();
        String username = auth.getName();
        UserEntity user = userRepo.findByEmail(username);
        if (user == null) return ResponseEntity.notFound().build();

        if (user.getTwoFactorSecret() == null || user.getTwoFactorSecret().isEmpty()) {
            String secret = totpService.generateSecret();
            user.setTwoFactorSecret(secret);
            userRepo.save(user);
        }
        String otpAuthUrl = totpService.getOtpAuthUrl("NeuroTalks", user.getEmail(), user.getTwoFactorSecret());
        return ResponseEntity.ok(Map.of("otpAuthUrl", otpAuthUrl, "secret", user.getTwoFactorSecret()));
    }

    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qrcode(HttpSession session) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return ResponseEntity.status(401).build();
        String username = auth.getName();
        UserEntity user = userRepo.findByEmail(username);
        if (user == null) return ResponseEntity.notFound().build();

        if (user.getTwoFactorSecret() == null || user.getTwoFactorSecret().isEmpty()) {
            String secret = totpService.generateSecret();
            user.setTwoFactorSecret(secret);
            userRepo.save(user);
        }

        String otpAuthUrl = totpService.getOtpAuthUrl("NeuroTalks", user.getEmail(), user.getTwoFactorSecret());

        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            byte[] pngData = baos.toByteArray();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(pngData);
        } catch (WriterException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body, HttpSession session) {
        String codeStr = body.get("code");
        if (codeStr == null) return ResponseEntity.badRequest().body(Map.of("error", "code missing"));
        int code;
        try { code = Integer.parseInt(codeStr); } catch (NumberFormatException e) { return ResponseEntity.badRequest().body(Map.of("error", "invalid code")); }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return ResponseEntity.status(401).build();
        String username = auth.getName();
        UserEntity user = userRepo.findByEmail(username);
        if (user == null) return ResponseEntity.notFound().build();

        boolean ok = totpService.verifyCode(user.getTwoFactorSecret(), code);
        if (ok) {
            session.setAttribute("2fa_verified", true);
            if (!user.isTwoFactorEnabled()) {
                user.setTwoFactorEnabled(true);
                userRepo.save(user);
            }
            return ResponseEntity.ok(Map.of("verified", true));
        }
        return ResponseEntity.status(403).body(Map.of("verified", false));
    }
}
