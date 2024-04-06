package com.project1.project.Controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.NFException;
import com.project1.project.Entity.Notification.Notification;
import com.project1.project.Entity.Notification.NotificationRepo;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class NotificationController {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    HttpServletRequest request;

    public void sendNotification(String message, User recipient) {
        User sender = userFromToken(request);
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        notificationRepo.save(notification);

    }



    public User userFromToken(HttpServletRequest request){
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User user = userRepo.findByUsername(username).orElseThrow(() -> new NFException("user not found."));
                  return user;}
        return null;
      }

      private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
    
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
          return headerAuth.substring(7);
        }
    
        return null;
      }

}
