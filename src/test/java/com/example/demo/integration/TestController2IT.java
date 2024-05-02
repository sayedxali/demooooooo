package com.example.demo.integration;

import com.example.demo.config.EurekaClientTestConfiguration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest({"server.port=0"}) // random port
@EnableConfigurationProperties // to use application.yml-test file
@AutoConfigureMockMvc // calling the api itself
@ActiveProfiles("test")
@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /*to call the configuration in the test (for service-registry configs that what instances of services are attached to it)*/
public class TestController2IT {

    //<editor-fold desc="fields">
    private @Autowired MockMvc mockMvc;

    private @Autowired ObjectMapper objectMapper
            = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //</editor-fold>

    @Test
    public void helloTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("")
                                .with(jwt().authorities(new SimpleGrantedAuthority("board_manager")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isNotNull();
    }

    @Test
    public void helloTest_Unauthorized() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }

}
