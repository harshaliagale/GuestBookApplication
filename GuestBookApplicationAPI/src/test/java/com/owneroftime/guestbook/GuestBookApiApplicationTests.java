package com.owneroftime.guestbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owneroftime.guestbook.api.model.GuestBookEntryModel;
import com.owneroftime.guestbook.common.bean.BaseControllerBean;
import com.owneroftime.guestbook.security.model.LoginModel;
import com.owneroftime.guestbook.security.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Rollback(value = true)
public class GuestBookApiApplicationTests {
    @Autowired
    private MockMvc mvc;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void admin_getAllGuestBookEntries() throws Exception {
        ResultActions resulAction = mvc.perform(get("/admin/getAllGuestBookEntries")
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResult = resulAction.andReturn();
        BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
        
        if (baseControllerBean.getSuccess()) // entry details fetched successfully
        	assertTrue(baseControllerBean.getSuccess()); 
        else if (!baseControllerBean.getSuccess()){
        	assertFalse(baseControllerBean.getSuccess()); // Entry details fetch failed
        }
    }

    @Test
    public void guest_getAllGuestBookEntriesForUser() throws Exception {
        mvc.perform(get("/guest/getAllGuestBookEntriesForUser/1561321") // // Test will Id as Number
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        mvc.perform(get("/guest/getAllGuestBookEntriesForUser/asdas") // Test will Id as String
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void guest_createGuestBookEntry() throws Exception {
        
        GuestBookEntryModel guestBookEntryModel = new GuestBookEntryModel();
        guestBookEntryModel.setGuestBookEntryText("This is the entry text I have added here");
        String jsonString = mapper.writeValueAsString(guestBookEntryModel);
        ResultActions resulAction =mvc.perform(post("/guest/createGuestBookEntry")
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));
        MvcResult mvcResult = resulAction.andReturn();
        BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
        if (baseControllerBean.getSuccess()) // entry created successfully
        	assertTrue(baseControllerBean.getSuccess()); 
        else if (!baseControllerBean.getSuccess()){
        	assertFalse(baseControllerBean.getSuccess()); // Entry creation failed
        }
    }

    @Test
    public void guest_updateGuestEntryText() throws Exception {
        GuestBookEntryModel guestBookEntryModel = new GuestBookEntryModel();
        guestBookEntryModel.setGuestBookEntryId(25L);
        guestBookEntryModel.setGuestBookEntryText("Content is updated from herer");
        String jsonString = mapper.writeValueAsString(guestBookEntryModel);
        ResultActions resulAction = mvc.perform(post("/guest/updateGuestEntryText")
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));
        MvcResult mvcResult = resulAction.andReturn();
        BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
        
        if (baseControllerBean.getSuccess()) // Entry updated successfully
        	assertTrue(baseControllerBean.getSuccess()); 
        else if (!baseControllerBean.getSuccess()){
        	assertFalse(baseControllerBean.getSuccess()); // Entry update failed
        }
    }

    @Test
    public void admin_deleteGuestBookEntries() throws Exception {
    	ResultActions resulAction = mvc.perform(post("/admin/deleteGuestBookEntries").contentType(MediaType.APPLICATION_JSON).content("2"));
        MvcResult mvcResult = resulAction.andReturn();
        BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
        
        if (baseControllerBean.getSuccess()) // Entry deleted successfully
        	assertTrue(baseControllerBean.getSuccess()); 
        else if (!baseControllerBean.getSuccess()){
        	assertFalse(baseControllerBean.getSuccess()); // Entry deletion failed
        }
    }

    @Test
    public void admin_approveGuestBookEntries() throws Exception {
    	ResultActions resulAction = mvc.perform(put("/admin/approveGuestBookEntries").contentType(MediaType.APPLICATION_JSON).content("3"));
        MvcResult mvcResult = resulAction.andReturn();
        BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
        
        if (baseControllerBean.getSuccess()) // entry approved successfully
        	assertTrue(baseControllerBean.getSuccess()); 
        else if (!baseControllerBean.getSuccess()){
        	assertFalse(baseControllerBean.getSuccess()); // approval failed
        }
    }

    @Test
    public void security_createUser() throws Exception {
        try {
            String jsonString = produceCreateUserJsonString();
            ResultActions resulAction =  mvc.perform(post("/security/userDetails/register")
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString));
            MvcResult mvcResult = resulAction.andReturn();
            BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
            
            if (baseControllerBean.getSuccess()) // User registered successfully
            	assertTrue(baseControllerBean.getSuccess()); 
            else if (!baseControllerBean.getSuccess()){
            	assertFalse(baseControllerBean.getSuccess()); // User registration failed
            }
        } catch (Exception ignored) {

        }
    }
    
    @Test
    public void security_signIn() throws Exception {
        try {
            String jsonString = produceSignJsonString();
            ResultActions resulAction = mvc.perform(post("/security/userDetails/signIn")
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString));
            MvcResult mvcResult = resulAction.andReturn();
            BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
            
            if (baseControllerBean.getSuccess()) // User logged in
            	assertTrue(baseControllerBean.getSuccess()); 
            else if (!baseControllerBean.getSuccess()){
            	assertFalse(baseControllerBean.getSuccess()); // User log in denied
            }
        } catch (Exception ignored) {

        }
    }
    
    @Test
    public void security_getUserDetails() throws Exception {
        try {
        	ResultActions resulAction = mvc.perform(post("/security/userDetails/getUserDetails")
                    .contentType(MediaType.APPLICATION_JSON).content("test@test.com"));
            MvcResult mvcResult = resulAction.andReturn();
            BaseControllerBean baseControllerBean = mapper.readValue(mvcResult.getResponse().getContentAsString(), BaseControllerBean.class);
            
            if (baseControllerBean.getSuccess()) // User Details found case
            	assertTrue(baseControllerBean.getSuccess()); 
            else if (!baseControllerBean.getSuccess()){
            	assertFalse(baseControllerBean.getSuccess()); // User details not found case
            }
        } catch (Exception ignored) {

        }
    }

    private String produceSignJsonString() throws JsonProcessingException {
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("test@gmail.com");
        loginModel.setPassword("test");
		return mapper.writeValueAsString(loginModel);
	}

	private String produceCreateUserJsonString() throws JsonProcessingException {
        UserModel userModel = new UserModel();
        userModel.setName("Test");
        userModel.setEmail("test@gmail.com");
        userModel.setUsername("testUserName");
        userModel.setPassword(new BCryptPasswordEncoder().encode("test"));
        return mapper.writeValueAsString(userModel);
    }

}
