package com.project1.project.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import com.project1.project.Entity.Massege.Message;
import com.project1.project.Entity.Massege.MessageRepository;
import com.project1.project.Entity.Massege.MessageResponse;
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
        return ResponseEntity.ok().body("you send this message successfully");
    }
    
//  @GetMapping("/user")
//     public ResponseEntity<?> getUserMessages() {
//     User me=userFromToken(request);
   
//         List<Message> userMessages = messageRepository.findBySenderId(me.getId());
//         if(userMessages.isEmpty())
//         return ResponseEntity.ok().body("userMessages");
//         return ResponseEntity.ok().body(userMessages);
//     }
    @GetMapping("/messaging/{userId}")
    public ResponseEntity<?> getMessagesBetweenUsers(@PathVariable Long userId) {
        User user1 = userRepo.findById(userId).orElseThrow(() -> new NFException("User with ID " + userId + " not found."));
        User user2 = userFromToken(request);
        
        List<Message> messages = messageRepository.findMessagesBetweenUsers(user2, user1);
         List<MessageResponse> messageResponses = new ArrayList<>();
        for (Message message : messages) {
            MessageResponse response = new MessageResponse();
            response.setSenderName(message.getReceiver().getUsername()); 
            response.setContent(message.getContent());
            messageResponses.add(response);
        }
        
        return ResponseEntity.ok().body(messageResponses);
    }@GetMapping("/between/{otherUserId}")
    public ResponseEntity<List<MessageResponse>> getMessagesBetweenUserAndOtherUser(@PathVariable Long otherUserId) {
        User currentUser = userFromToken(request);
        User otherUser = userRepo.findById(otherUserId).orElseThrow(() -> new NFException("User with ID " + otherUserId + " not found."));
    
        List<Message> sentMessages = messageRepository.findBySenderAndReceiver(currentUser, otherUser);
    
        List<Message> receivedMessages = messageRepository.findBySenderAndReceiver(otherUser, currentUser);
    
        List<MessageResponse> messageResponses = new ArrayList<>();
        for (Message message : sentMessages) {
            messageResponses.add(new MessageResponse(currentUser.getUsername(), message.getContent(),message.getTimestamp()));
        }
        for (Message message : receivedMessages) {
            messageResponses.add(new MessageResponse(message.getSender().getUsername(), message.getContent(),message.getTimestamp()));
        }
    
        return ResponseEntity.ok().body(messageResponses.stream().sorted(Comparator.comparing(MessageResponse::getTimestamp))
        .collect(Collectors.toList()));
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
