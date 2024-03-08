package com.project1.project.Post;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project.NFException;
import com.project1.project.Comment.Comment;
import com.project1.project.Like.Like;
import com.project1.project.Like.likeType;
import com.project1.project.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/post") 
public class PostController {

    @Autowired
    private  PostRepo postRepo;


    // @PostMapping("/create")
    // public Post createPost(Post post,User user){
    // //    post.setUser(user);
    // //    return postRepo.save(post);
        
    // } Lazen nrj3 lma n3ml log in


    @GetMapping("/posts")
    public List<Post> findAllPost(){
        return postRepo.findAll(); 
    }



    // public Post sharePost(Long postId,User user){
    //     return null;
        
    // }
    @GetMapping("/posts/{postId}")
    public Post findById(@PathVariable Long postId){
        return postRepo.findById(postId).get();
        // .orElseThrow(new NFException(Post.class));

    }

    public void deleteById(Long postId ,Long userId){

    }

 public List<Post> getUserPost(User user){
    return null;
    
 }
 /////////////////////////////////////////////////////////////////////
  public Post createComment(Comment coment,User user){
        return null;
        
    }
 public Post deleteComment (Long commentId,Long postId,Long userId ){
    return null;
    
 }
 ////////////////////////////////////////////////////////////////////
 public List<Post> findByLikesContainsUser(User user){//البوستات الي اليوزر حاط لايك عليهم 
    return null;

 }  


 public Like likePost(Long postId,User user){//يوزر يوزر مش حاسها زابطة 
    return null;

}

// @GetMapping("/posts/{id}/like")
// public List<Like> allPostLike(@PathVariable Long id){
//   Post p=  postRepo.findById(id).get();
//    return postRepo.findByLike(p);
// }


public Post postLike (Long postId,likeType type){//اذا في لايك بشيله اذا فش بحط
    return null;
    
}
public Comment commentLike (Long postId,Long commentId,likeType type){//اذا في لايك بشيله اذا فش بحط
    return null;
    
}
public List<Like> getAllCommentLike(Long PostId){
    return null;
    
}
//////////////////////////////////////////////////////


public Post creatShare(Long postId,Long userId,String content){// بصير كانه بوست بوخذ اي دي جديد ;الكونتينت بكون ب البادي 
    return null;
    
}
public void deleteShearById(Long shearId ,Long userId){

}

}
