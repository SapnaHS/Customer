package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.repository.CustomerRepository;
import com.mobile.banking.Customer.utility.CustomerUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;


    @Mock
    private CustomerUtility customerUtility;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        mockRequestContext();
    }

    private void mockRequestContext() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }
    @Test
    void testGetCustomerAccountsInfo_Success() {

        String customerId = "1327";

        List<Customer> expectedCustomers = Arrays.asList(new Customer(), new Customer());
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn(customerId);
        when(customerRepository.findByCustomerId(customerId)).thenReturn(expectedCustomers);


        List<Customer> actualCustomers = customerService.getCustomerAccountsInfo(customerId);
        Assertions.assertEquals(expectedCustomers, actualCustomers);
        verify(customerRepository).findByCustomerId(customerId);
    }

    @Test
    void testGetCustomerAccountsInfo_Failure() {
        String customerId = "12406";
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn("345423");
        assertThrows(RuntimeException.class, () -> customerService.getCustomerAccountsInfo(customerId));
    }
}
