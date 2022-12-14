package com.ecommerce.educative.service.user;

import com.ecommerce.educative.dto.UserDto.SignInDto;
import com.ecommerce.educative.dto.UserDto.SignInResponseDto;
import com.ecommerce.educative.dto.UserDto.SignUpDto;
import com.ecommerce.educative.exception.customExceptions.AutheticationFailExeception;
import com.ecommerce.educative.exception.customExceptions.BadRequestException;
import com.ecommerce.educative.exception.customExceptions.NotFoundException;
import com.ecommerce.educative.model.user.AuthenticationToken;
import com.ecommerce.educative.model.user.Role;
import com.ecommerce.educative.model.user.UserEntity;
import com.ecommerce.educative.repository.RoleRepository;
import com.ecommerce.educative.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RoleRepository roleRepository;

    public void signUp(SignUpDto signUpDto) {

        if (Objects.nonNull(userRepository.findByUsername(signUpDto.getUsername()))) {
            throw new BadRequestException("Username already exist");
        }

        if (Objects.nonNull(userRepository.findByEmail(signUpDto.getEmail()))) {
            throw new BadRequestException("Email already exist");
        }

        String encryptedPassword = signUpDto.getPassword();

        try {
            encryptedPassword = hashPassword(signUpDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new BadRequestException("Error to encrypt password");
        }

        Set<Role> roles = new HashSet<>();
        try {
            if (signUpDto.getRolesId().isEmpty()) {
                throw new BadRequestException("The role is obligated.");
            }
            for (Long roleId : signUpDto.getRolesId()) {
                Optional<Role> optionalRole = roleRepository.findById(roleId);
                if (optionalRole == null) {
                    throw new NotFoundException("These roles don't exist.");
                }
                roles.add(optionalRole.get());
            }
        } catch (Exception e) {
            throw new BadRequestException("There are informations missing");
        }
        UserEntity user = new UserEntity(signUpDto.getUsername(),
                signUpDto.getEmail(), encryptedPassword, roles);
        
        final AuthenticationToken authenticationToken = new AuthenticationToken(user);
        
        String encryptedToken = authenticationToken.getToken();
        try {
            encryptedToken = hashPassword(authenticationToken.getToken());
        } catch (NoSuchAlgorithmException e) {
            throw new BadRequestException("Error to encrypt password");
        }
        user.setAuthenticationToken(encryptedToken);
        userRepository.save(user);

        authenticationService.saveConfirmationToken(authenticationToken);   
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();

        return hash;
    }

    public SignInResponseDto signIn(SignInDto signInDto) {
        UserEntity user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AutheticationFailExeception("user is not valid");
        }

        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AutheticationFailExeception("password is not valid");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new BadRequestException("token is not present");
        }
        return new SignInResponseDto("success", token.getToken());
    }

    public List<UserEntity> listUsers() {
        return userRepository.findAll();
    }
}
