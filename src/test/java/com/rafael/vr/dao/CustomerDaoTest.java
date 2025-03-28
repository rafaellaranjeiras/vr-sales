package com.rafael.vr.dao;

import com.rafael.vr.config.MysqlConnection;
import com.rafael.vr.dao.CustomerDao;
import com.rafael.vr.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDaoTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() throws Exception {
        lenient().when(mockConnection.prepareStatement(anyString(), eq(Statement. RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        lenient().when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    }

    @Test
    void testCreateCustomer() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);

            Customer customer = new Customer(0, "Rafael", new BigDecimal("5000.00"), 10);
            when(mockStatement.executeUpdate()).thenReturn(1);
            when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            Customer createdCustomer = customerDao.create(customer);

            assertNotNull(createdCustomer);
            assertEquals(1, createdCustomer.getCode());
        }
    }

    @Test
    void testFindByCode() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);

            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("code")).thenReturn(1);
            when(mockResultSet.getString("name")).thenReturn("Rafael");
            when(mockResultSet.getBigDecimal("purchase_limit")).thenReturn(new BigDecimal("5000.00"));
            when(mockResultSet.getInt("closing_day")).thenReturn(10);

            Customer customer = customerDao.findBycode(1);

            assertNotNull(customer);
            assertEquals(1, customer.getCode());
            assertEquals("Rafael", customer.getName());
        }
    }

    @Test
    void testUpdateCustomer() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);

            Customer customer = new Customer(1, "Rafael", new BigDecimal("6000.00"), 15);
            when(mockStatement.executeUpdate()).thenReturn(1);

            assertDoesNotThrow(() -> customerDao.update(customer));
        }
    }

    @Test
    void testDeleteCustomer() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);

            when(mockStatement.executeUpdate()).thenReturn(1);

            assertDoesNotThrow(() -> customerDao.delete(1));
        }
    }
}
