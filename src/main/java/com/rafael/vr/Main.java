package com.rafael.vr;

import com.rafael.vr.ui.MenuForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Controle de Vendas");
                frame.setContentPane(new MenuForm().getPanel1());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
            });
        });

    }
}