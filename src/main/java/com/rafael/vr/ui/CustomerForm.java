package com.rafael.vr.ui;

import com.rafael.vr.dao.CustomerDao;
import com.rafael.vr.model.Customer;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import static com.rafael.vr.util.DialogUtil.showError;
import static com.rafael.vr.util.DialogUtil.showSuccess;
import static java.util.Objects.isNull;

@Data
public class CustomerForm {
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtLimite;
    private JTextField txtFechamento;
    private JButton btnPesquisar;
    private JButton btnSalvar;
    private JLabel lblCodigo;
    private JLabel lblNome;
    private JLabel lblFechamento;
    private JLabel lblLimite;
    private JPanel panel1;
    private JButton btnNovo;
    private JButton btnExcluir;

    private CustomerDao customerDao = new CustomerDao();

    public CustomerForm() {
        btnSalvar.addActionListener(e -> save());
        btnPesquisar.addActionListener(e -> find());
        btnNovo.addActionListener(e -> newCustomer());
        btnExcluir.addActionListener(e -> delete());
    }

    private void save() {
        var customer = Customer.builder()
                .name(txtNome.getText())
                .purchaseLimit(new BigDecimal(txtLimite.getText()))
                .closingDay(Integer.parseInt(txtFechamento.getText()))
                .code(txtCodigo.getText().isEmpty() ? null : Integer.parseInt(txtCodigo.getText()))
                .build();
        try {
            if (isNull(customer.getCode())) {
                customer = customerDao.create(customer);
                txtCodigo.setText(customer.getCode().toString());
                showSuccess("Cliente criado com sucesso!");
                return;
            }
            customerDao.update(customer);
            showSuccess("Cliente atualizado com sucesso!");
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }

    }

    private void delete() {
        try {
            customerDao.delete(Integer.parseInt(txtCodigo.getText()));
            reset();
            showSuccess("Cliente excluído com sucesso!");
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void find() {
        if (!txtCodigo.isEnabled()) {
            reset();
            return;
        }
        var code = Integer.parseInt(txtCodigo.getText());
        var customer = customerDao.findBycode(code);
        if (isNull(customer)) {
            showError("Cliente não encontrado");
            return;
        }
        txtNome.setText(customer.getName());
        txtLimite.setText(customer.getPurchaseLimit().toString());
        txtFechamento.setText(customer.getClosingDay().toString());
        found();
    }

    private void reset() {
        txtCodigo.setText("");
        txtCodigo.setEnabled(true);
        txtNome.setEnabled(false);
        txtFechamento.setEnabled(false);
        txtLimite.setEnabled(false);
        btnSalvar.setEnabled(false);
        btnExcluir.setEnabled(false);
        txtNome.setText("");
        txtFechamento.setText("");
        txtLimite.setText("");
    }

    private void found() {
        txtCodigo.setEnabled(false);
        txtNome.setEnabled(true);
        txtFechamento.setEnabled(true);
        txtLimite.setEnabled(true);
        btnSalvar.setEnabled(true);
        btnExcluir.setEnabled(true);
    }

    private void newCustomer() {
        txtCodigo.setText("");
        txtCodigo.setEnabled(false);
        txtNome.setEnabled(true);
        txtFechamento.setEnabled(true);
        txtLimite.setEnabled(true);
        btnSalvar.setEnabled(true);
        btnExcluir.setEnabled(false);
        txtNome.setText("");
        txtFechamento.setText("");
        txtLimite.setText("");
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setMaximumSize(new Dimension(400, 300));
        panel1.setMinimumSize(new Dimension(400, 300));
        panel1.setPreferredSize(new Dimension(400, 300));
        lblCodigo = new JLabel();
        lblCodigo.setText("Código");
        panel1.add(lblCodigo, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblNome = new JLabel();
        lblNome.setText("Nome");
        panel1.add(lblNome, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblLimite = new JLabel();
        lblLimite.setText("Limite de compra");
        panel1.add(lblLimite, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblFechamento = new JLabel();
        lblFechamento.setText("Dia fechamento");
        panel1.add(lblFechamento, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtLimite = new JTextField();
        txtLimite.setEnabled(false);
        panel1.add(txtLimite, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtFechamento = new JTextField();
        txtFechamento.setEnabled(false);
        panel1.add(txtFechamento, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtCodigo = new JTextField();
        panel1.add(txtCodigo, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSalvar = new JButton();
        btnSalvar.setEnabled(false);
        btnSalvar.setText("Salvar");
        panel1.add(btnSalvar, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPesquisar = new JButton();
        btnPesquisar.setText("Pesquisar");
        panel1.add(btnPesquisar, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNome = new JTextField();
        txtNome.setEnabled(false);
        panel1.add(txtNome, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnNovo = new JButton();
        btnNovo.setText("Novo");
        panel1.add(btnNovo, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnExcluir = new JButton();
        btnExcluir.setEnabled(false);
        btnExcluir.setText("Excluir");
        panel1.add(btnExcluir, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
