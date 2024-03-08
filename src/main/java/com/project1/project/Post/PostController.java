package com.project1.project.Post;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.project1.project.Comment.Comment;
import com.project1.project.Like.Like;
import com.project1.project.Like.likeType;
import com.project1.project.User.User;

@RestController
public class PostController {
    

    public Post createPost(Post post,User user){
        return post;
        
    }

    public List<Post> findAllPost(){
        return null;
        
    }

    public Post sharePost(Long postId,User user){
        return null;
        
    }
    public Post findById(Long postId){
        return null;

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
public List<Like> getAllPostLike(Long PostId){
    return null;
    
}
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
