/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.decisiontreeclassifier_id3.View;

import com.mycompany.decisiontreeclassifier_id3.Classifier.CSVFile;
import com.mycompany.decisiontreeclassifier_id3.Classifier.DecisionTree;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author fake
 */
public class UI {
    
    static String dir;
    static HashMap<String, String> ex = new HashMap<>();
    
    public static void main(String[] args) {
        
        JFrame Frame = new JFrame();
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setHgap(5000);
        layout.setVgap(10);
        Frame.setLayout(layout);
        JButton Load = new JButton("Load DataSet");
       
        JPanel panel = new JPanel();
        panel.setVisible(false);
        
        JButton Predict = new JButton("Predict");
        Predict.setVisible(false);
        JLabel Result = new JLabel("");
        Result.setForeground(Color.red);
        Result.setFont(new Font("Verdana", Font.BOLD, 20));
        Result.setVisible(false);
        Load.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                Load.setVisible(false);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    dir = selectedFile.getPath();
                    panel.setVisible(true);
                    CSVFile csv = new CSVFile(dir);
                    csv.read();
                    ArrayList<ArrayList<String>> dataset = csv.getData();
                    ArrayList<String> features = csv.getColumnNames();
                    panel.setLayout(new java.awt.GridLayout(dataset.size(), 2, 0, 0));
                    panel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    DecisionTree Tree = new DecisionTree(dataset, features);
                    Tree.BuildTree();
                    ArrayList<JComboBox> ComboBoxs = new ArrayList<>();
                    for (int a = 0; a < dataset.size() - 1; a++) {
                        ArrayList<String> Values = Tree.BranchesForA(a);
                        String[] ValuesArray = Values.toArray(new String[Values.size()]);
                        JComboBox values = new JComboBox();
                        values.setModel(new javax.swing.DefaultComboBoxModel<>(ValuesArray));
                        ComboBoxs.add(values);
                    }
                    for (int a = 0; a < dataset.size() - 1; a++) {
                        JLabel label = new JLabel(features.get(a));
                        panel.add(label);
                        panel.add(ComboBoxs.get(a));
                    }
                    panel.setVisible(true);
                    Predict.setVisible(true);
                    
                    Predict.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            for (int i = 0; i < ComboBoxs.size(); i++) {
                                ex.put(features.get(i), (String) ComboBoxs.get(i).getSelectedItem());
                            }                            
                            Result.setText(features.get(features.size() - 1) + " : " + Tree.predict(ex));
                            Result.setVisible(true);
                        }
                    });
                    
                }
            }
        });
        
        Frame.add(Load);
        Frame.add(panel);
        Frame.add(Predict);
        Frame.add(Result);
        Frame.setExtendedState(Frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setVisible(true);
        
    }
}
