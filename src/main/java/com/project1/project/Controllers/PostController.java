package com.project1.project.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.Entity.Comment.Comment;
import com.project1.project.Entity.Comment.CommentRepo;
import com.project1.project.Entity.Like.Like;
import com.project1.project.Entity.Like.likeType;
import com.project1.project.Entity.Post.Post;
import com.project1.project.Entity.Post.PostRepo;
import com.project1.project.Entity.Share.Share;
import com.project1.project.Entity.Share.ShareRepo;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private  ShareRepo shareRepo;

    @Autowired
    private  CommentRepo commentRepo;

    @PostMapping("/create")
    public Post createPost(HttpServletRequest request,Post post){
    // post.setUser(user);
    // return postRepo.save(post);
    Long userId = (Long) request.getAttribute("userId"); 
    if (userId == null) {
        return null;
    }
     User user = userRepo.findById(userId).orElse(null);
    post.setUser(user);
   
    return postRepo.save(post);

    // if (user == null) {
    //     return Collections.emptyList();
    // }
    // return user.posts; 
}


    @GetMapping("/posts")
    public List<Post> findAllPost() {

        return postRepo.findAll();
    }

    @PostMapping("/share")
    public Share sharePost(Long postId, HttpServletRequest request , String content) {
        Long userId = (Long) request.getAttribute("userId"); 
        if (userId == null) {
            return null;
        }
        User user = userRepo.findById(userId).orElse(null);
        Post post = postRepo.findById(postId).orElse(null);
       Share share = new Share(content,user,post);


        return shareRepo.save(share);
    }


    @GetMapping("/posts/{postId}")
    public Post findById(@PathVariable Long postId) {
        return postRepo.findById(postId).get();
        // .orElseThrow(new NFException(Post.class));

    }

    @DeleteMapping("/{postId}")
    public void deleteById(Long postId, Long userId) throws Exception {
      
        if (userHasPermissionToDeletePost(postId, userId)) {
    
            postRepo.deleteById(postId);
        } else {
            throw new Exception("User is not authorized to delete this post");
        }
    }
    
    // تحقق مما إذا كان المستخدم مخولًا بحذف المنشور
    private boolean userHasPermissionToDeletePost(Long postId, Long userId) {
        // قم بفحص قاعدة البيانات أو أي نظام مصادقة آخر للتحقق مما إذا كان المستخدم هو صاحب المنشور
        // على سبيل المثال، يمكنك التحقق من أن المستخدم مملوك للمنشور في قاعدة البيانات
        Post post = postRepo.findById(postId).orElse(null);

        return post != null && post.user.getId().equals(userId);
    }
    
    /////////////////////////////////////////////////////////////////////
    @PostMapping("/comment")
    public Comment createComment(Comment coment, HttpServletRequest request, String content , Long postId) {

        Long userId = (Long) request.getAttribute("userId"); 
        if (userId == null) {
            return null;
        }
        User user = userRepo.findById(userId).orElse(null);
        Post post = postRepo.findById(postId).orElse(null);
        Comment comment =new Comment(content,user,post);
        
        return commentRepo.save(comment);
    }

   @DeleteMapping("/{PostId}/Comment{commentId}")
    public List<Comment> deleteComment(Long commentId, Long postId, HttpServletRequest request) throws Exception {
        Long userId = (Long) request.getAttribute("userId"); 
        if (userId == null) {
            return null;
        }
      List<Comment>postComments=postRepo.postComments(postId);
      Comment comment=postComments.stream().filter(e->e.getCommentId().equals(commentId)).collect(Collectors.toList()).getFirst();
      User userComment=comment.getUser();

      if (userHasPermissionToDeleteComment(commentId, userId)) {
        commentRepo.deleteById(commentId);
    } else {
        throw new Exception("User is not authorized to delete this comment");
    }
        return postRepo.postComments(postId);

    }
    private boolean userHasPermissionToDeleteComment(Long commentId, Long userId) {
        // قم بفحص قاعدة البيانات أو أي نظام مصادقة آخر للتحقق مما إذا كان المستخدم هو صاحب المنشور
        // على سبيل المثال، يمكنك التحقق من أن المستخدم مملوك للمنشور في قاعدة البيانات
        Comment comment = commentRepo.findById(commentId).orElse(null);

        return comment != null && comment.getUser().getId().equals(userId);
    }

    ////////////////////////////////////////////////////////////////////
    public List<Post> findByLikesContainsUser(User user) {// البوستات الي اليوزر حاط لايك عليهم
        return null;
    }

    public Like likePost(Long postId, User user) {// يوزر يوزر مش حاسها زابطة
        return null;

    }

    public List<Like> getAllPostLike(Long PostId) {
        return null;

    }

    // @GetMapping("/posts/{id}/like")
    // public List<Like> allPostLike(@PathVariable Long id){
    // Post p= postRepo.findById(id).get();
    // return postRepo.findByLike(p);
    // }

    public Post postLike(Long postId, likeType type) {// اذا في لايك بشيله اذا فش بحط
        return null;

    }

    public Comment commentLike(Long postId, Long commentId, likeType type) {// اذا في لايك بشيله اذا فش بحط
        return null;

    }

    public List<Like> getAllCommentLike(Long PostId) {
        return null;

    }

    //////////////////////////////////////////////////////
    public Post creatShare(Long postId, Long userId, String content) {// بصير كانه بوست بوخذ اي دي جديد ;الكونتينت بكون
                                                                      // ب البادي
        return null;

    }

    public void deleteShearById(Long shearId, Long userId) {
    }



    @GetMapping("/userPosts")
public List<Post> findUserPosts(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId"); 
    if (userId == null) {
        return Collections.emptyList();
    }
    User user = userRepo.findById(userId).orElse(null);
    if (user == null) {
        return Collections.emptyList();
    }
    return user.posts; 
}}