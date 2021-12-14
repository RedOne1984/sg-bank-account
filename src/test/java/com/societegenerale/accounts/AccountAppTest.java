package com.societegenerale.accounts;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.societegenerale.accounts.dtos.Request;
import com.societegenerale.accounts.entities.Client;
import com.societegenerale.accounts.entities.Operation;
import com.societegenerale.accounts.security.CustomUserDetail;
import com.societegenerale.accounts.service.AccountService;
import com.societegenerale.accounts.service.OperationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.societegenerale.accounts.entities.Account;
import com.societegenerale.accounts.enums.OperationType;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountAppTest {

    @MockBean
    private OperationService operationService;

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    Client client = new Client(1L, "Radouane", "Aboumehdi", "SG-304891-010984-0001", "aboumehdira", "radouane.aboumehdi@gmail.com", new BCryptPasswordEncoder().encode("Lorena1674"), "USER");

    private static final String LOGIN = "aboumehdira";
    private static final String WRONG_LOGIN = "BAD_USER";
    private static final Long ACCOUNT_NUMBER = 11223344556L;
    private static final Long BAD_ACCOUNT_NUMBER = 113399L;


    @WithUserDetails(value = LOGIN)
    @Test
    public void should_make_deposit_operation() throws Exception {
        Account account = new Account(1L, ACCOUNT_NUMBER, "FR7611000035441122334455688","11000035441122334455688", new BigDecimal(0), client);
        Request request = new Request(ACCOUNT_NUMBER,new BigDecimal(1000));
        Operation operation = new Operation(1L, new Date(), new BigDecimal(1000), new BigDecimal(2000), OperationType.DEPOSIT, account);

        when(accountService.findAccountByAccountNumber(ACCOUNT_NUMBER)).thenReturn(account);
        when(operationService.processOperation(request, account)).thenReturn(operation);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client));
        String json = "{\"accountNumber\":\"" + ACCOUNT_NUMBER + "\",\"operationAmount\":1000}";
        this.mockMvc.perform(post("/operations/").session(session).with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
    
    @WithUserDetails(value = LOGIN)
    @Test
    public void should_make_withdrawal_operation() throws Exception {
        Account account = new Account(1L, ACCOUNT_NUMBER, "FR7611000035441122334455688","11000035441122334455688", new BigDecimal(1000), client);
        Request request = new Request(ACCOUNT_NUMBER,new BigDecimal(-500));
        Operation operation = new Operation(1L, new Date(), new BigDecimal(-500), new BigDecimal(500), OperationType.WITHDRAWAL, account);

        when(accountService.findAccountByAccountNumber(ACCOUNT_NUMBER)).thenReturn(account);
        when(operationService.processOperation(request, account)).thenReturn(operation);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client));
        String json = "{\"accountNumber\":\"" + ACCOUNT_NUMBER + "\",\"operationAmount\":-500}";
        this.mockMvc.perform(post("/operations/").session(session).with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
    
    @WithUserDetails(value = LOGIN)
    @Test
    public void should_search_operations_for_account() throws Exception{
    	Account account = new Account(1L, ACCOUNT_NUMBER, "FR7611000035441122334455688","11000035441122334455688", new BigDecimal(0), client);
        Operation operation = new Operation(1L, new Date(), new BigDecimal(1000), new BigDecimal(1000), OperationType.DEPOSIT, account);

        List<Operation> operations = new ArrayList<>();
        operations.add(operation);

        when(accountService.findAccountByAccountNumber(ACCOUNT_NUMBER)).thenReturn(account);
        when(operationService.getAllOperationByAccount(ACCOUNT_NUMBER, 0, 10)).thenReturn(operations);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client));

        this.mockMvc.perform(get("/operations/").session(session).with(csrf()).param("accountNumber", String.valueOf(ACCOUNT_NUMBER))).andDo(print())
                .andExpect(jsonPath("$[0].operationType").value(OperationType.DEPOSIT.toString()))
                .andExpect(jsonPath("$[0].operationAmount").value(1000))
                .andExpect(status().isOk());
    }
    
    @WithUserDetails(value = LOGIN)
    @Test
    public void should_not_make_withdrawal_if_insufficient_fund() throws Exception {
    	Account account = new Account(1L, ACCOUNT_NUMBER, "FR7611000035441122334455688","11000035441122334455688", new BigDecimal(0), client);
        Request request = new Request(ACCOUNT_NUMBER,new BigDecimal(-1000));
        Operation operation = new Operation(1L, new Date(), new BigDecimal(50), new BigDecimal(-1000), OperationType.WITHDRAWAL, account);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client));

        when(accountService.findAccountByAccountNumber(ACCOUNT_NUMBER)).thenReturn(account);
        when(operationService.processOperation(request, account)).thenReturn(operation);

        String json = "{\"accountNumber\":\"" + ACCOUNT_NUMBER + "\",\"operationAmount\":-1000}";
        this.mockMvc.perform(post("/operations/").session(session).with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    @WithUserDetails(value = LOGIN)
    @Test
    public void operation_should_not_processed_with_no_required_params() throws Exception {
        String json = "{\"accountNumber\":\"" + ACCOUNT_NUMBER + "\"}";
        this.mockMvc.perform(post("/operations/").with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    @WithUserDetails(value = LOGIN)
    @Test
    public void should_received_unauthorized_when_getting_operations_with_wrong_account() throws Exception{
    	Client client2 = new Client(2L, "Elon", "Musk", "SG-473674-170582-0002", "muskel", "muskel@tesla.com", new BCryptPasswordEncoder().encode("pass1234"), "USER");
    	Account account = new Account(2L,33445566778L, "FR761100007853344556677888","1100007853344556677888", new BigDecimal(0), client2);
        when(accountService.findAccountByAccountNumber(BAD_ACCOUNT_NUMBER)).thenReturn(account);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client));

        this.mockMvc.perform(get("/operations/").session(session).with(csrf()).param("accountNumber", String.valueOf(BAD_ACCOUNT_NUMBER))
        ).andDo(print()).andExpect(status().isUnauthorized());
    }


    @WithUserDetails(value = LOGIN)
    @Test
    public void should_received_unauthorized_when_process_operation_with_wrong_account() throws Exception{
    	Client client2 = new Client(2L, "Elon", "Musk", "SG-473674-170582-0002", "muskel", "muskel@tesla.com", new BCryptPasswordEncoder().encode("Lorena1674"), "USER");
    	Account account = new Account(2L,33445566778L, "FR761100007853344556677888","1100007853344556677888", new BigDecimal(0), client2);
        when(accountService.findAccountByAccountNumber(BAD_ACCOUNT_NUMBER)).thenReturn(account);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client));

        when(accountService.findAccountByAccountNumber(ACCOUNT_NUMBER)).thenReturn(account);
    	String json = "{\"accountNumber\":\"" + BAD_ACCOUNT_NUMBER + "\",\"operationAmount\":100}";
        this.mockMvc.perform(post("/operations/").session(session).with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isUnauthorized());
    }


    @WithMockUser(value = WRONG_LOGIN)
    @Test
    public void should_receive_forbidden_when_process_operation_on_not_existed_account() throws Exception{
    	Client client2 = new Client(2L, "Elon", "Musk", "SG-473674-170582-0002", "muskel", "muskel@tesla.com", new BCryptPasswordEncoder().encode("Lorena1674"), "USER");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, CustomUserDetail.create(client2));

    	when(accountService.findAccountByAccountNumber(BAD_ACCOUNT_NUMBER)).thenReturn(null);

        String json = "{\"accountNumber\":\"" + ACCOUNT_NUMBER + "\",\"operationAmount\":1000}";
        this.mockMvc.perform(post("/operations/").with(csrf()).content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isForbidden());
    }


}
