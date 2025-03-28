package com.rafael.vr.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;

import com.rafael.vr.config.MysqlConnection;
import com.rafael.vr.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;

class SaleDaoTest {

    private SaleDao saleDao;
    private Connection mockConnection;
    private PreparedStatement mockSaleStmt;
    private PreparedStatement mockItemStmt;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        saleDao = new SaleDao();
        mockConnection = mock(Connection.class);
        mockSaleStmt = mock(PreparedStatement.class);
        mockItemStmt = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockSaleStmt);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockItemStmt);
        when(mockSaleStmt.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void testCreateSale() throws SQLException {
        Sale sale = new Sale();
        sale.setCustomerCode(1);
        sale.setTotalPrice(new BigDecimal("100.00"));
        Sale.SaleItem item = new Sale.SaleItem(101, "Produto A", 2, new BigDecimal("50.00"));
        sale.setItems(Arrays.asList(item));

        when(mockSaleStmt.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(10);

        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);
            Sale result = saleDao.create(sale);

            assertNotNull(result);
            assertEquals(10, result.getCode());
            verify(mockSaleStmt, times(1)).executeUpdate();
            verify(mockItemStmt, times(1)).executeBatch();
        }
    }

}
