package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import com.company.Steganographable;
import javafx.embed.swing.JFXPanel;

public class Stegosaurus {

    private Steganographable stego = new Steganographable();
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton EXECUTEButton;
    private JRadioButton ENCODERadioButton;
    private JRadioButton DECODERadioButton;
    private JPanel modePanel;
    private JTextField textField1;
    private JTextField textField2;

    public Stegosaurus() {
        EXECUTEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (ENCODERadioButton.isSelected()){
                    try {
                        stego.cmd_ENCODE(textField1.getText(), textField2.getText());
                    } catch (IOException e){
                        textField2.setText("FAILED TO FIND/OUTPUT IMAGE");
                    }
                } else if (DECODERadioButton.isSelected()){
                    try {
                        textField2.setText(stego.cmd_DECODE(textField1.getText()));
                    } catch (IOException e){
                        textField2.setText("FAILED TO DECODE IMAGE!");
                    }
                }
            }
        });
        ENCODERadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DECODERadioButton.setSelected(false);
            }
        });
        DECODERadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ENCODERadioButton.setSelected(false);
            }
        });
        ENCODERadioButton.setSelected(true);
    }

    public static void main(String[] args){
        JFXPanel hack = new JFXPanel();
        JFrame frame = new JFrame("Stegosaurus");
        frame.setContentPane(new Stegosaurus().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
