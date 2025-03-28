package com.rafael.vr.util;

import javax.swing.*;

public class DialogUtil {

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

}
