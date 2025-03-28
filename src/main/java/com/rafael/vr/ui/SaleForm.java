package com.rafael.vr.ui;

import com.rafael.vr.dao.CustomerDao;
import com.rafael.vr.dao.ProductDao;
import com.rafael.vr.dao.SaleDao;
import com.rafael.vr.model.Customer;
import com.rafael.vr.model.Sale;
import lombok.Data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.rafael.vr.util.DialogUtil.showError;
import static com.rafael.vr.util.DialogUtil.showSuccess;
import static java.util.Objects.isNull;

@Data
public class SaleForm {
    private JTextField txtCodCliente;
    private JButton btnBuscaCliente;
    private JTextField txtCodProduto;
    private JButton btnAddProduto;
    private JTable tblProdutos;
    private JLabel lblNomeCliente;
    private JLabel lblSelecCliente;
    private JLabel lblCodProduto;
    private JPanel panel1;
    private JTextField txtTotal;
    private JLabel lblTotal;
    private JButton btnSalvar;
    private JButton btnNova;
    private JTextField txtCodVenda;
    private JLabel lblVenda;
    private JButton btnPesquisaVenda;
    private JButton btnExcluirProduto;
    private JLabel lblCodCliente;

    private List<Integer> products = new ArrayList<>();
    private DefaultTableModel tableContent = new DefaultTableModel(new Object[]{"COD", "DESCRIÇÃO", "PREÇO", "QTDE"}, 0);

    private CustomerDao customerDao = new CustomerDao();
    private ProductDao productDao = new ProductDao();
    private SaleDao saleDao = new SaleDao();

    private Customer customer;

    public SaleForm() {
        btnBuscaCliente.addActionListener(e -> findCustomer());
        btnAddProduto.addActionListener(e -> addProduct());
        btnExcluirProduto.addActionListener(e -> deleteProduct());
        btnNova.addActionListener(e -> newSale());
        btnSalvar.addActionListener(e -> save());
        btnPesquisaVenda.addActionListener(e -> findSale());
        tblProdutos.setModel(tableContent);
        tableContent.addTableModelListener(e -> updateTotalPrice());
    }

    private void save() {
        if (isNull(customer)) {
            showError("Cliente não informado.");
            return;
        }
        var total = new BigDecimal(txtTotal.getText());
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            showError("Nenhum produto adicionado.");
            return;
        }
        if (total.compareTo(customer.getPurchaseLimit()) > 0) {
            showError("Limite do cliente: " + customer.getPurchaseLimit());
            return;
        }
        var sale = buildSale();
        sale = saleDao.create(sale);
        found(sale);
        showSuccess("Salvo com sucesso!");
    }

    private void addProduct() {
        var code = Integer.parseInt(txtCodProduto.getText());
        var product = productDao.findBycode(code);
        if (isNull(product)) {
            showError("Produto não encontrado");
            return;
        }
        if (products.contains(product.getCode())) {
            showError("Produto já incluído.");
            return;
        }
        products.add(product.getCode());
        tableContent.addRow(new Object[]{product.getCode(), product.getDescription(), product.getPrice(), 1});
        tblProdutos.setModel(tableContent);
    }

    private void deleteProduct() {
        int row = tblProdutos.getSelectedRow();
        if (row >= 0) {
            var productCode = Integer.parseInt(tblProdutos.getValueAt(row, 0).toString());
            var productQuantity = Integer.parseInt(tblProdutos.getValueAt(row, 3).toString());
            var productPrice = new BigDecimal(tblProdutos.getValueAt(row, 2).toString());
            var productTotalPrice = productPrice.multiply(BigDecimal.valueOf(productQuantity));
            var total = new BigDecimal(txtTotal.getText()).subtract(productTotalPrice);
            txtTotal.setText(total.toString());
            products.removeIf(p -> p == productCode);
            tableContent.removeRow(row);
        }
    }

    private void findCustomer() {
        var code = Integer.parseInt(txtCodCliente.getText());
        customer = customerDao.findBycode(code);
        if (isNull(customer)) {
            showError("Cliente não encontrado");
            return;
        }
        lblCodCliente.setText(customer.getCode().toString());
        lblNomeCliente.setText(customer.getName());
    }

    private void updateTotalPrice() {
        var rowCount = tblProdutos.getRowCount();
        var total = BigDecimal.ZERO;
        for (int i = 0; i < rowCount; i++) {
            var unitPrice = new BigDecimal(tblProdutos.getValueAt(i, 2).toString());
            var quantty = new BigDecimal(tblProdutos.getValueAt(i, 3).toString());
            total = total.add(unitPrice.multiply(quantty));
        }
        txtTotal.setText(total.toString());
    }

    private Sale buildSale() {
        Sale sale = new Sale();
        sale.setCode(customer.getCode());
        sale.setTotalPrice(new BigDecimal(txtTotal.getText()));
        sale.setCustomerCode(Integer.parseInt(lblCodCliente.getText()));
        sale.setItems(new ArrayList<>());

        var rowCount = tblProdutos.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            var code = Integer.parseInt(tblProdutos.getValueAt(i, 0).toString());
            var description = tblProdutos.getValueAt(i, 1).toString();
            var unitPrice = new BigDecimal(tblProdutos.getValueAt(i, 2).toString());
            var quantty = Integer.parseInt(tblProdutos.getValueAt(i, 3).toString());
            var product = new Sale.SaleItem(code, description, quantty, unitPrice);
            sale.getItems().add(product);
        }

        return sale;
    }

    private void findSale() {
        Integer code = Integer.parseInt(txtCodVenda.getText());
        var sale = saleDao.getByCode(code);
        if (isNull(sale)) {
            showError("Venda não encontrada.");
            return;
        }
        found(sale);
    }

    private void found(Sale sale) {
        txtCodVenda.setEnabled(false);
        txtCodCliente.setEnabled(false);
        txtCodProduto.setEnabled(false);
        txtTotal.setEnabled(false);
        btnSalvar.setEnabled(false);
        btnExcluirProduto.setEnabled(false);
        btnAddProduto.setEnabled(false);
        btnBuscaCliente.setEnabled(false);
        tblProdutos.setEnabled(false);
        tableContent.setRowCount(0);
        tblProdutos.setModel(tableContent);
        txtCodCliente.setText(sale.getCustomerCode().toString());
        txtTotal.setText(sale.getTotalPrice().toString());
        showProducts(sale);
        findCustomer();
    }

    private void showProducts(Sale sale) {
        sale.getItems().forEach(item -> {
            tableContent.addRow(new Object[]{
                    item.getProductCode().toString(),
                    item.getProductDescription(),
                    item.getUnitPrice().toString(),
                    item.getQuantity().toString()
            });
        });
        tblProdutos.setModel(tableContent);
    }

    private void newSale() {
        txtCodVenda.setEnabled(false);
        txtCodCliente.setEnabled(true);
        txtCodProduto.setEnabled(true);
        txtTotal.setEnabled(true);
        btnSalvar.setEnabled(true);
        btnExcluirProduto.setEnabled(true);
        btnAddProduto.setEnabled(true);
        btnBuscaCliente.setEnabled(true);
        tblProdutos.setEnabled(true);
        tableContent.setRowCount(0);
        tblProdutos.setModel(tableContent);
        lblNomeCliente.setText("");
        lblCodCliente.setText("");
        txtTotal.setText("0.00");
        customer = null;
    }

    private void reset() {
        txtCodVenda.setEnabled(true);
        txtCodCliente.setEnabled(false);
        txtCodProduto.setEnabled(false);
        txtTotal.setEnabled(false);
        btnSalvar.setEnabled(false);
        btnExcluirProduto.setEnabled(false);
        btnAddProduto.setEnabled(false);
        btnBuscaCliente.setEnabled(false);
        tblProdutos.setEnabled(false);
        tableContent.setRowCount(0);
        tblProdutos.setModel(tableContent);
        lblNomeCliente.setText("");
        lblCodCliente.setText("");
        txtTotal.setText("0.00");
        customer = null;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 3, new Insets(0, 0, 0, 0), -1, -1));
        lblSelecCliente = new JLabel();
        lblSelecCliente.setText("Código cliente");
        panel1.add(lblSelecCliente, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCodCliente = new JTextField();
        txtCodCliente.setEnabled(false);
        panel1.add(txtCodCliente, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBuscaCliente = new JButton();
        btnBuscaCliente.setEnabled(false);
        btnBuscaCliente.setText("Ok");
        panel1.add(btnBuscaCliente, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblCodProduto = new JLabel();
        lblCodProduto.setText("Código Produto");
        panel1.add(lblCodProduto, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCodProduto = new JTextField();
        txtCodProduto.setEnabled(false);
        panel1.add(txtCodProduto, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnAddProduto = new JButton();
        btnAddProduto.setEnabled(false);
        btnAddProduto.setText("Adicionar");
        panel1.add(btnAddProduto, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotal = new JLabel();
        lblTotal.setText("Total");
        panel1.add(lblTotal, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setText("0.00");
        panel1.add(txtTotal, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSalvar = new JButton();
        btnSalvar.setEnabled(false);
        btnSalvar.setText("Salvar");
        panel1.add(btnSalvar, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNova = new JButton();
        btnNova.setText("Nova");
        panel1.add(btnNova, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblVenda = new JLabel();
        lblVenda.setText("Venda");
        panel1.add(lblVenda, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCodVenda = new JTextField();
        panel1.add(txtCodVenda, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnPesquisaVenda = new JButton();
        btnPesquisaVenda.setText("Pesquisa");
        panel1.add(btnPesquisaVenda, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setEnabled(false);
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblProdutos = new JTable();
        scrollPane1.setViewportView(tblProdutos);
        btnExcluirProduto = new JButton();
        btnExcluirProduto.setEnabled(false);
        btnExcluirProduto.setText("Excluir Selecionado");
        panel1.add(btnExcluirProduto, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblNomeCliente = new JLabel();
        lblNomeCliente.setForeground(new Color(-1769465));
        lblNomeCliente.setHorizontalAlignment(0);
        lblNomeCliente.setText("");
        panel1.add(lblNomeCliente, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblCodCliente = new JLabel();
        lblCodCliente.setText("");
        panel1.add(lblCodCliente, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
