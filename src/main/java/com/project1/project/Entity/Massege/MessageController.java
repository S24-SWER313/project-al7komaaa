package com.project1.project.Entity.Massege;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.NFException;
import com.project1.project.Entity.Post.PostRepo;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/messages")
public class MessageController {
 @Autowired
  HttpServletRequest request;
  @Autowired
  private JwtUtils jwtUtils;


  @Autowired
  private UserRepo userRepo;
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send/{userId}")
    public ResponseEntity<?> sendMessage(@RequestBody String messageDTO,@PathVariable Long userId) {

        Message message = new Message();
        message.setSender(userFromToken(request));
      User rUser=userRepo.findById(userId).orElseThrow(() -> new NFException("share with ID " +  userId+ " not found."));               ;

        message.setReceiver(rUser);
        message.setContent(messageDTO);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return ResponseEntity.ok().build();
    }
 @GetMapping("/user")
    public ResponseEntity<?> getUserMessages() {
    User me=userFromToken(request);
   
        List<Message> userMessages = messageRepository.findBySenderId(me.getId());
        if(userMessages.isEmpty())
        return ResponseEntity.ok().body("userMessages");
        return ResponseEntity.ok().body(userMessages);
    }
    @GetMapping("/messaging/{userId}")
    public ResponseEntity<?> getMessagesBetweenUsers(@PathVariable Long userId) {
        User user1 = userRepo.findById(userId).orElseThrow(() -> new NFException("User with ID " + userId + " not found."));
        User user2 = userFromToken(request);
        
        List<Message> messages = messageRepository.findMessagesBetweenUsers(user1, user2);
        
        return ResponseEntity.ok().body(messages);
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
