package com.rafael.vr.dao;

import com.rafael.vr.config.MysqlConnection;
import com.rafael.vr.dao.ProductDao;
import com.rafael.vr.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDaoTest {

    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockStatement;
    @Mock private ResultSet mockResultSet;
    private ProductDao productDao;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        productDao = new ProductDao();

        lenient().when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockStatement);
        lenient().when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    }

    @Test
    void testCreateProduct() throws Exception {

        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);
            when(mockStatement.executeUpdate()).thenReturn(1);
            when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            Product product = new Product(0, "Mouse Gamer", new BigDecimal("150.00"));
            Product createdProduct = productDao.create(product);

            assertNotNull(createdProduct);
            assertEquals(1, createdProduct.getCode());

            verify(mockStatement, times(1)).executeUpdate();
        }
    }

    @Test
    void testFindByCode_ProductExists() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("code")).thenReturn(1);
            when(mockResultSet.getString("description")).thenReturn("Mouse Gamer");
            when(mockResultSet.getBigDecimal("price")).thenReturn(new BigDecimal("150.00"));

            Product product = productDao.findBycode(1);

            assertNotNull(product);
            assertEquals(1, product.getCode());
            assertEquals("Mouse Gamer", product.getDescription());
            assertEquals(new BigDecimal("150.00"), product.getPrice());

            verify(mockStatement, times(1)).executeQuery();
        }
    }

    @Test
    void testFindByCode_ProductDoesNotExist() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Product product = productDao.findBycode(99);

            assertNull(product);
            verify(mockStatement, times(1)).executeQuery();
        }
    }

    @Test
    void testUpdateProduct() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);
            when(mockStatement.executeUpdate()).thenReturn(1);

            Product product = new Product(1, "Teclado Mecânico", new BigDecimal("300.00"));
            assertDoesNotThrow(() -> productDao.update(product));

            verify(mockStatement, times(1)).executeUpdate();
        }
    }

    @Test
    void testDeleteProduct() throws Exception {
        try (MockedStatic<MysqlConnection> mockedStatic = mockStatic(MysqlConnection.class)) {
            mockedStatic.when(MysqlConnection::getConnection).thenReturn(mockConnection);

            when(mockStatement.executeUpdate()).thenReturn(1);

            assertDoesNotThrow(() -> productDao.delete(1));

            verify(mockStatement, times(1)).executeUpdate();
        }
    }
}

