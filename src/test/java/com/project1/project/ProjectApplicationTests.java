package com.project1.project;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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





















}
