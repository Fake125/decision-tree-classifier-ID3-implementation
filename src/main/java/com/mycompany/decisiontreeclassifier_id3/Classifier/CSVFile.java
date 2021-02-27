package com.mycompany.decisiontreeclassifier_id3.Classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVFile {

    private String Dir;
    private ArrayList<ArrayList<String>> columns;
    private ArrayList<String> columns_names;

    public CSVFile(String d) {
        Dir = d;
        columns = new ArrayList<ArrayList<String>>();
        columns_names = new ArrayList<String>();
    }

    public void read() {
        try {
            Scanner getting = new Scanner(new File(Dir));
            String FirstLine = getting.nextLine();
            String[] arrs = FirstLine.split(",");
            for (String c : arrs) {
                columns.add(new ArrayList<String>());
                columns_names.add(c);
            }

            String[] values;
            while (getting.hasNext()) {
                values = getting.nextLine().split(",");
                for (int i = 0; i < values.length; i++) {
                    columns.get(i).add(values[i]);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<ArrayList<String>> getData() {
        return (columns);
    }
    
    public ArrayList<String> getColumnNames(){
        return(columns_names);
    }
    
    public static void Exit(){
        System.out.println("There's something wrong in the dataset");
        System.exit(0);
    }

}
