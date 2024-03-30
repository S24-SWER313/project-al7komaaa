package com.project1.project;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.h2.util.json.JSONArray;
import org.h2.util.json.JSONObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

import com.project1.project.Controllers.AuthController;
import com.project1.project.Payload.Request.LoginRequest;
import com.project1.project.Payload.Response.JwtResponse;
// @ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@SpringJUnitConfig
class ProjectApplicationTests {

	@Autowired
    private MockMvc mockMvc;
    // @InjectMocks
    // private AuthController authController;
	
    @Autowired
    private AuthenticationManager authenticationManager; 


    // public String testAuthenticateUser() {
       
    //     AuthController authController = new AuthController();

    //     LoginRequest loginRequest = new LoginRequest();
    //     loginRequest.setUsername("fatma2");
    //     loginRequest.setPassword("123mai321");

    //     ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

    //     // assertEquals(responseEntity.getBody().getClass(), JwtResponse.class);

    //     JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
	// 	System.out.println(jwtResponse.getAccessToken());
    //    return jwtResponse.getAccessToken();

    // }

    @Autowired
    private AuthController authController;

 
    public String testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("fatma2");
        loginRequest.setPassword("123mai321");

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        return jwtResponse.getAccessToken();
    }

	@Test
	void testGetAllUsers() throws Exception{
		// assertEquals(testAuthenticateUser(), "");
			mockMvc.perform(get("/users")
			.header("Authorization", "Bearer " + testAuthenticateUser()) 
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.users[0].firstname").value("unKnown")); 
	}
// 	@Test
// void testGetOneEmployee() throws Exception {
// mockMvc.perform(get("/employees/1")
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isOk())
// .andExpect(jsonPath("$.firstName").value ("Bil"));
// }

@Test
void testgetUserById() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(get("/user/1")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("celina9")); 
}

@Test
void testgetUserByFirstName() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(get("/userByFirstName/FATMA")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.users[0].firstname").value("FATMA")); 
}


@Test
void testgetUserByLastName() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(get("/userByLastName/NASSIF")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.users[0].lastname").value("NASSIF")); 
}




@Test
void testgetFulltName() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(get("/fullName/FATMA NASSIF")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.users[0].fullname").value("FATMA NASSIF")); 
}



@Test
void testgetUserName() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(get("/UserName/fatma2")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("fatma2")); 
}




@Test
void testgetUserFriend() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(get("/UserFriend/2")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.users[0].username").value("mai7"));
}




@Test
void testaddFriend() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
        mockMvc.perform(post("/AddUserFriend/3")
        .header("Authorization", "Bearer " + testAuthenticateUser()) 

        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
            .andExpect(content().string("Friend already exists"));
}





@Test
void testeditFirstName() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editFirstName")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("FATMA")) 
    .andExpect(status().isOk())
    .andExpect(content().string("firstName changed toFATMA"));
}



@Test
void testeditLastName() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editLastName")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("NASSIF")) 
    .andExpect(status().isOk())
    .andExpect(content().string("lastName changed toNASSIF"));
}




@Test
void testeditMobile() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editMobile")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("059")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Mobile changed to059"));
}



@Test
void testeditEmail() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editEmail")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("M.M@gmail.com")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Email changed toM.M@gmail.com"));
}

@Test
void testEditGender() throws Exception {
    mockMvc.perform(put("/editGender")
            .header("Authorization", "Bearer " + testAuthenticateUser())
            .contentType(MediaType.APPLICATION_JSON)
            .content("\"FEMALE\""))
            .andExpect(status().isOk())
            .andExpect(content().string("Gender changed toFEMALE"));
}


@Test
void testeditBio() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editBio")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("bbbbb")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Bio changed tobbbbb"));
}


@Test
void testeditLocation() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editLocation")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("SomeWhere on Earth")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Location changed toSomeWhere on Earth"));
}



@Test
void testeditImage() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editImage")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("www.p.com")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Image changed towww.p.com"));
}


@Test
void testeditDof() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editDof")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("\"2003-05-31\"")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Date of birth changed to2003-05-31"));
}



@Test
void testeditBackgroundImage() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/editBackgroundImage")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("www.e.com")) 
    .andExpect(status().isOk())
    .andExpect(content().string("Background Image changed towww.e.com"));
}




@Test
void testprivacy() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/privacy")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("true")) 
    .andExpect(status().isOk())
    .andExpect(content().string("the privacy of account is true"));
}


@Test
void testAddUserFriend() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/AddUserFriend/3")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("true")
    ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("Friend already exists"));
}




@Test
void testdeleteUserFriend() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/deleteUserFriend/1")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("true")
    ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("Friend not found in user's friend list"));
}


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
// post controler test 



@Test
void testCreatePost() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/create")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"image\":\"image\",\"video\":\"video\", \"content\":\"post1\"}")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"Post Created successfully!\"}"));
}

@Test
void testcreateComment() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/1/comment")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("{\"content\":\"comment1\"}")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.comments[0].content").value("fatma2comment")); 
}

@Test
void testfindAllPost() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/posts")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("{\"content\":\"comment1\"}")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].content").value("postcelina7")); 
}


@Test
void testsharePost() throws Exception{//if post private 
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/share/4")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("Enter your share content here")
    ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("this post is private"));

}


@Test
void testsharePost2() throws Exception{//if post public 
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/share/3")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("Enter your share content here")); 

}
@Test
void testfindById() throws Exception{//if post private and not friend
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/posts/5")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("this post is private"));

}
@Test
void testfindById2() throws Exception{//if post private and friend
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/posts/4")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("postmai7")); 

}
@Test
void testdeleteComment() throws Exception{//if delete comment you are not the owner
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/post/comment/1")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"you are not authorized!\"}"));

}
@Test
void testdeleteComment2() throws Exception{//if delete the post and you are the owner
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/post/comment/4")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"Delete successfully!\"}"));

}
@Test
void testfindByLikesContainsUser() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/user/like")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].content").value("postcelina7")); 

}
@Test
void testcreateLikePost() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/4/like")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"type\": \"LIKE\"}")
    ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("User has already liked the post"));

}


@Test
void testgetAllPostLikes() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/1/likes")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("{\"type\": \"LIKE\"}")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].likeId").value(2)); 

}

@Test
void testUnCreatelikePost() throws Exception{//if delete the like and you are  the owner
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/post/5/like")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"Delete successfully!\"}"));

}
@Test
void testUnCreatelikePost2() throws Exception{//if delete the like and you are not the owner
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/post/1/like")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"you are not the owner of like\"}"));

}

@Test
void testdeleteShearById() throws Exception{//if delete the share and you are  the owner
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/post/share/2")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"delete share successfully\"}"));

}
@Test
void testdeleteShearById2() throws Exception{//if delete the share and you are not the owner
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(delete("/post/share/1")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"User is not authorized to delete this post\"}"));

}

@Test
void testfindyPosts() throws Exception{//MY POST
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/myPosts")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].content").value("FatMai")); 

}
@Test
void testfindFriendPosts() throws Exception{//if you are a friend
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/FriendPosts/3")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].content").value("postcelina7")); 

}
@Test
void testfindFriendPosts2() throws Exception{//if you are not a friend
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/FriendPosts/4")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isUnauthorized())
    .andExpect(content().string("This acount is private to see posts added friend.")); 

}
@Test
void testpostUser() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/postUser/1")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("Enter your share content here")
    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.username").value("celina9")); 
}
@Test
void testeditCoumment() throws Exception{//you are owner the comment 
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/post/3/comment/edit")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("fatma2comment")    ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("fatma2comment")); 
}
@Test
void testeditCoumment2() throws Exception{// you are not the owner the comment
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/post/1/comment/edit")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("fatma2comment")    ) 
    .andExpect(status().isOk())
    .andExpect(content().string("you aren't the owner of this comment ")); 
}


@Test
void testgetUserPost() throws Exception{// 1 is not a friend and public
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/1/user")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("fatma2comment")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].content").value("postcelina7")); 
}


@Test
void testgetUserPost1() throws Exception{// 3 is a friend and private
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/3/user")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("fatma2comment")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].content").value("postmai7")); 
}


@Test
void testgetUserPost2() throws Exception{//  is not a friend and private
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/5/user")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("fatma2comment")   
     ) 
    .andExpect(status().isOk())
    .andExpect(content().string("the account is private you cant view")); 
}


@Test
void testgetReals() throws Exception{
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/reels")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{ \"video\":\"video\",\"content\":\"reel1\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$._embedded.posts[0].video").value("video"));
}




@Test
void testeditShare() throws Exception{//  the owner of the share
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/post/2/editShare")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("mai fatmeh")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("mai fatmeh"));
}



@Test
void testeditShare1() throws Exception{//  not owner of the share
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/post/1/editShare")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("mai fatmeh")   
     ) 
    .andExpect(status().isOk())
    .andExpect(content().string("you aren't the owner of this post ")); 
}


@Test
void testeditPost() throws Exception{//   owner of the post
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/post/2/editPost")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"content\":\"FatMai\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("FatMai"));
}

@Test
void testeditPost1() throws Exception{//   not the owner of the post
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(put("/post/4/editPost")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"content\":\"FatMai\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(content().string("you aren't the owner of this post ")); 
}


@Test
void testcreaterShareLike() throws Exception{//   scenario when the user already liked the post
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/share/2/createLike")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"type\": \"LIKE\"}")   
     ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("User has already liked the postShare")); 
}


@Test
void testcreateShareComment() throws Exception{//  create comment for a shared post
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/share/2/createComment")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"content\":\"who are you\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("who are you"));
}


@Test
void testcreateReal() throws Exception{//  create real 
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/reals/create")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{ \"video\":\"video\",\"content\":\"reel1\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"Reel Created successfully!\"}")); 
}

@Test
void testcreateReal1() throws Exception{//  create real for content only without a video
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(post("/post/reals/create")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    .content("{ \"content\":\"reel1\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(content().string("{\"message\":\"must be video and content\"}")); 
}


@Test
void testfindsharebyId() throws Exception{// 1 is not a friend and public
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/share/1")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("fatma2comment")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("hi")); 
}


@Test
void testfindsharebyid() throws Exception{// if the user is private and not friend
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/share/6")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("fatma2comment")   
     ) 
    .andExpect(status().isBadRequest())
    .andExpect(content().string("this post is private")); }


@Test
void testfindSharebyId() throws Exception{//  is friend and private
    // assertEquals(testAuthenticateUser(), "");
    mockMvc.perform(get("/post/share/7")
    .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
    // .content("fatma2comment")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").value("maiShare"));
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//authController


@Test
void testsignin() throws Exception{
    mockMvc.perform(post("/api/auth/signin")
   // .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
     .content("{\"username\":\"fatma2\" , \"password\":\"123mai321\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.username").value("fatma2"));
}

@Test
void testsignIn() throws Exception{//wrong password
    mockMvc.perform(post("/api/auth/signin")
   // .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
     .content("{\"username\":\"fatma2\" , \"password\":\"celinannassif\"}")   
     ) 
    .andExpect(status().isUnauthorized())
    .andExpect(jsonPath("$.message").value("Bad credentials"));
}


@Test
void testsignUp() throws Exception{
    mockMvc.perform(post("/api/auth/signup")
   // .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
     .content("{\"email\":\"sara@gmail.com\" , \"username\":\"sara\" , \"password\":\"123mai321\"}")   
     ) 
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.message").value("User registered successfully!"));
}


@Test
void testsignup() throws Exception{//if the user already signup
    mockMvc.perform(post("/api/auth/signup")
   // .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
     .content("{\"email\":\"mai@gmail.com\" , \"username\":\"mai7\" , \"password\":\"123mai321\"}")   
     ) 
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
}


@Test
void testsignUp3() throws Exception{//if the user enter short password
    mockMvc.perform(post("/api/auth/signup")
   // .header("Authorization", "Bearer " + testAuthenticateUser())
    .contentType(MediaType.APPLICATION_JSON)
     .content("{\"email\":\"sara@gmail.com\" , \"username\":\"sara\" , \"password\":\"123\"}")   
     ) 
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.error").value(" the password size must be between 6 and 40"));
}


//if we turn in first method change and must change the password in it

// @Test
// void testchangepassword() throws Exception{
//     mockMvc.perform(put("/api/auth/ChangePassword")
//     .header("Authorization", "Bearer " + testAuthenticateUser())
//     .contentType(MediaType.APPLICATION_JSON)
//      .content("{\"newPassword\":\"123mai321\", \"oldPassword\":\"789celina\" }")   
//      ) 
//     .andExpect(status().isOk())
//     .andExpect(jsonPath("$.message").value("Password changed successfully for user: fatma2"));
// }



}
