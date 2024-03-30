package com.project1.project.Controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Payload.Request.LoginRequest;
import com.project1.project.Payload.Request.SignupRequest;
import com.project1.project.Payload.Response.JwtResponse;
import com.project1.project.Payload.Response.MessageResponse;
import com.project1.project.Security.Jwt.JwtUtils;
import com.project1.project.Security.Services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  
  @Autowired
  AuthenticationManager authenticationManager;

  
  @Autowired
  UserRepo userRepository;
  @Autowired
  private  UserRepo userRepo;

 

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;


  // @Autowired
  // private JwtTokenProvider jwtTokenProvider;


  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                        roles ));
  }


  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));
               user.setRole(user.getRole());

               

    // Set<String> strRoles = signUpRequest.getRole();
    // Set<Role> roles = new HashSet<>();

    // if (strRoles == null) {
    //   Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    //       .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //   roles.add(userRole);
    // } else {
    //   strRoles.forEach(role -> {
    //     switch (role) {
    //     case "admin":
    //       Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
    //           .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //       roles.add(adminRole);

    //       break;
    //     case "mod":
    //       Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
    //           .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //       roles.add(modRole);

    //       break;
    //     default:
    //       Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    //           .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //       roles.add(userRole);
    //     }
//  });
   // }

    // user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
}



@PutMapping("/ChangePassword")
public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwords, HttpServletRequest request) {
    String oldPassword = passwords.get("oldPassword");
    String newPassword = passwords.get("newPassword");
    
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        
        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Incorrect old password"));
        }
        user.setPassword(encoder.encode(newPassword)); 
        
        userRepository.save(user); 
        
        return ResponseEntity.ok(new MessageResponse("Password changed successfully for user: " + username));
    } else {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: User not found with username "));
    } 
}

    private String parseJwt(HttpServletRequest request) {
      String headerAuth = request.getHeader("Authorization");
  
      if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
        return headerAuth.substring(7);
      }
  
      return null;
    }



//     @PostMapping("/logout")
// public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
//     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//     if (authentication != null) {
//         new SecurityContextLogoutHandler().logout(request, response, authentication);
//     }
//     return ResponseEntity.ok("you Logout successfully");
// }

// @PostMapping("/logout")
// public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
//     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//     if (authentication != null) {
//         new SecurityContextLogoutHandler().logout(request, response, authentication);
//     }

//     // إلغاء صلاحية التوكن هنا
//     String token = extractTokenFromRequest(request);
//     if (token != null) {
//         tokenBlacklistService.addToBlacklist(token); // إضافة التوكن إلى قائمة التوكنات منتهية الصلاحية
//     }

//     return ResponseEntity.ok("you Logout successfully");
// }

// // استخراج التوكن من الطلب
// private String extractTokenFromRequest(HttpServletRequest request) {
//     // تقوم بتنفيذ هذه الوظيفة وفقًا لكيفية استخدامك للتوكنات، مثل استخراج التوكن من الهيدر أو الكوكيز أو الجسم
//     return null;
// }


// @PostMapping("/logout")
// public ResponseEntity<?> logoutUser(HttpServletRequest request) {
//     // استخراج رمز JWT من رأس الطلب
//     String jwt = parseJwt(request);
    
//     // إلغاء صلاحية JWT
//     jwtUtils.setExpiration(0);
    
//     return ResponseEntity.ok("تم تسجيل الخروج بنجاح.");
// }
@PostMapping("/logout")
    public SecurityWebFilterChain logout(ServerHttpSecurity http) {
        // تكوين معالج تسجيل الخروج
        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
            new SecurityContextServerLogoutHandler(), new WebSessionServerLogoutHandler()
        );

        // تكوين مسار تسجيل الخروج
        http.logout(logout -> logout.logoutHandler(logoutHandler))
            .authorizeExchange(exchange -> exchange.anyExchange().authenticated());

        // بناء سلسلة فلاتر الأمان
        return http.build();
    }
  
  }
