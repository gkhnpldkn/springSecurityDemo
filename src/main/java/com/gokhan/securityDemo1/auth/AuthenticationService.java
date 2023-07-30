package com.gokhan.securityDemo1.auth;

import com.gokhan.securityDemo1.connfig.JwtService;
import com.gokhan.securityDemo1.user.Role;
import com.gokhan.securityDemo1.user.User;
import com.gokhan.securityDemo1.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
private final PasswordEncoder passwordEncoder;
private  final JwtService jwtService;
private  final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)

                .build();
    }

    public AuthenticationResponse authenticate(AutenticationRequest request) {
authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        )
);
var user= repository.findByEmail(request.getEmail()).orElseThrow();
var jwtToken = jwtService.generateToken(user);
return AuthenticationResponse.builder().token(jwtToken)
        .build();
    }
}
