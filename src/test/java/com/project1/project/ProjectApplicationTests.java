package com.project1.project;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        .andExpect(jsonPath("$._embedded.users[0].username").value("celina9"));
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



}
