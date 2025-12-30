package com.kata.bank.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class BankControllerTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Getting account number through api call")
    void getAccountNumberTestOK() throws Exception {
        
        mockMvc.perform(get("/bank/account/accountNumber"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Account number']").exists());
    }


    @Test
    @DisplayName("Getting balance through api call")
    void getBalanceTestOK() throws Exception {
        
        mockMvc.perform(get("/bank/account/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Balance']").value(0));
    }


    @Test
    @DisplayName("Getting authorized overwithdrawal through api call")
    void getOverWithdrawalTestOK() throws Exception {
        
        mockMvc.perform(get("/bank/account/authorizedOverwithdrawal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Authorized overwithdrawal']").value(0));
    }


    @Test
    @DisplayName("put deposit through api call")
    void putDepositTestOK() throws Exception {
        
        String body = "{ \"amount\" : 99.99, \"label\" : \"cash\"} ";

        mockMvc.perform(put("/bank/account/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['New balance']").value(99.99));
    }

    @Test
    @DisplayName("put deposit of an incorrect amout through api call")
    void putDepositTestKO() throws Exception {
        
        String body = "{ \"amount\" : -99.99, \"label\" : \"cash\"} ";

        mockMvc.perform(put("/bank/account/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$['Error']").value("The amount you entered is either null or negative"));

        body = "{ \"amount\" : 0, \"label\" : \"cash\"} ";   
        
        mockMvc.perform(put("/bank/account/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$['Error']").value("The amount you entered is either null or negative"));
    }


    @Test
    @DisplayName("get withdrawal through api call")
    void getWithdrawalTestOK() throws Exception {
        
        String body = "{ \"amount\" : 99.99, \"label\" : \"cash\"} ";

        mockMvc.perform(put("/bank/account/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['New balance']").value(99.99));

        mockMvc.perform(put("/bank/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['New balance']").value(0));
    }

    @Test
    @DisplayName("get withdrawal of an incorrect amount through api call")
    void getWithdrawalTestKO() throws Exception {
        
        String body = "{ \"amount\" : -99.99, \"label\" : \"cash\"} ";

        mockMvc.perform(put("/bank/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$['Error']").value("The amount you entered is either null, negative, or greater than the actual balance"));

        body = "{ \"amount\" : 0, \"label\" : \"cash\"} ";   
        
        mockMvc.perform(put("/bank/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$['Error']").value("The amount you entered is either null, negative, or greater than the actual balance"));

        body = "{ \"amount\" : 10, \"label\" : \"cash\"} ";   
        
        mockMvc.perform(put("/bank/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$['Error']").value("The amount you entered is either null, negative, or greater than the actual balance"));        
    }

    @Test
    @DisplayName("update overwithdrawal through api call OK, and check that the update has been made")
    void putOverwithdrawalTestOK() throws Exception {
        
        String body = "99.99";

        mockMvc.perform(put("/bank/account/authorizedOverwithdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['New authorized overwithdrawal']").value(99.99))
                .andExpect(jsonPath("$['Message']").value("Overwithdrawal updated successfully"));

        mockMvc.perform(get("/bank/account/authorizedOverwithdrawal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Authorized overwithdrawal']").value(99.99));
    }

    @Test
    @DisplayName("update overwithdrawal through api call KO")
    void putOverwithdrawalTestKO() throws Exception {
        
        String body = "-99.99";

        mockMvc.perform(put("/bank/account/authorizedOverwithdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$['Error']").value("The amount you entered is negative"));

    }
}
