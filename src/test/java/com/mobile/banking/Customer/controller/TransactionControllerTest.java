package com.mobile.banking.Customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.banking.Customer.constants.ApplicationConstants;
import com.mobile.banking.Customer.dto.ResponseDTO;
import com.mobile.banking.Customer.service.TransactionService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testDepositAmount() throws Exception {
        String accountNumber = "1234456";
        double amount = 2200;
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Deposit successful");
        responseDTO.setStatus(ApplicationConstants.SUCCESS);
        when(transactionService.depositAmount(accountNumber, amount)).thenReturn(responseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/{accountNumber}/deposit", accountNumber)
                        .param("amount",String.valueOf(amount)).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(result, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testWithdrawAmount() throws Exception {
        String accountNumber = "1234456";
        double amount = 2200;
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Withdraw successful");
        responseDTO.setStatus(ApplicationConstants.SUCCESS);
        when(transactionService.withdrawAmount(accountNumber, amount)).thenReturn(responseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/{accountNumber}/withdraw", accountNumber)
                        .param("amount",String.valueOf(amount)).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(result, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testTransferFunds() throws Exception {
        String senderAccountNumber = "1234456";
        String receiverAccountNumber = "123445611";
        double amount = 2200;
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Withdraw successful");
        responseDTO.setStatus(ApplicationConstants.SUCCESS);
        when(transactionService.transferFunds(senderAccountNumber, receiverAccountNumber, amount)).thenReturn(responseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transfer", receiverAccountNumber, amount)
                        .param("fromAccountId",senderAccountNumber)
                .param("toAccountId", receiverAccountNumber)
                .param("amount",String.valueOf(amount)).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(result, mvcResult.getResponse().getContentAsString());
    }
}
