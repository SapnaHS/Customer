package com.mobile.banking.Customer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.banking.Customer.dto.AuthDTO;
import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.service.CustomerService;
import com.mobile.banking.Customer.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testGetCustomerAccountsInfo() throws Exception {
        String customerId = "1234456";
        List<Customer> customerList = Arrays.asList(new Customer(), new Customer());
        when(customerService.getCustomerAccountsInfo(customerId)).thenReturn(customerList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/customer/{customerId}", customerId)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        List<Customer> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(customerList, result);
    }

    @Test
    void testAuthenticateUser() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setCustomerId("2844");
        authDTO.setPassword("pass");
        String token = "testToken";
        when(jwtService.generateToken(authDTO.getCustomerId())).thenReturn(token);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(authDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate").contentType(
                MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(token, mvcResult.getResponse().getContentAsString());
    }
}
