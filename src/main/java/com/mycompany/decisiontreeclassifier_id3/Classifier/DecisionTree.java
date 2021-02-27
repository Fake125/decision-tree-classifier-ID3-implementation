package com.mycompany.decisiontreeclassifier_id3.Classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DecisionTree {

    public ArrayList<ArrayList<String>> Current_D = new ArrayList<>();
    public ArrayList<ArrayList<String>> Original_D = new ArrayList<>();
    public ArrayList<String> Current_FeaturesNames;
    public ArrayList<String> Original_FeatureNames;
    ArrayList<ArrayList<String>> Remaining_Paths;
    ArrayList<String> Current_Path;

    public Tree T;

    public DecisionTree(ArrayList<ArrayList<String>> D, ArrayList<String> Features) {
        int i = 0;
        Original_D = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> A : D) {
            ArrayList<String> temp = new ArrayList<String>();
            for (String v : A) {
                temp.add(v);
            }
            Original_D.add(temp);
        }
        Original_FeatureNames = (ArrayList<String>) Features.clone();
        Current_FeaturesNames = Features;
        Current_D = D;

        T = new Tree();
        Remaining_Paths = new ArrayList<ArrayList<String>>();
        Current_Path = new ArrayList<String>();
    }

    public ArrayList<String> BranchesForA(int i) {
        ArrayList<String> A = Current_D.get(i);
        ArrayList<String> Branches = new ArrayList<String>();
        for (String v : A) {
            if (!Branches.contains(v)) {
                Branches.add(v);
            }
        }
        return (Branches);
    }

    public int PosisitonForA(String name) {
        int i = 0;
        for (String n : Current_FeaturesNames) {
            if (n.toLowerCase().equals(name.toLowerCase())) {
                break;
            }
            if (i == Current_FeaturesNames.size() - 1) {
                return (-1);
            }
            i++;
        }
        return (i);
    }

    public double Gain(double InfoOfD, double InfoForA) {
        return (InfoOfD - InfoForA);
    }

    public double InfoOfD() {
        HashMap<String, Integer> counter = new HashMap();
        for (String classlabel : Current_D.get(Current_D.size() - 1)) {
            if (counter.get(classlabel) == null) {
                counter.put(classlabel, 1);
            } else {
                counter.replace(classlabel, counter.get(classlabel) + 1);
            }
        }

        double Info = 0;
        double p = 0;
        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            p = (Double.valueOf(entry.getValue()) / Double.valueOf(Current_D.get(0).size()));
            Info += -p * LogBase2(p);
        }
        return (Info);
    }

    // Calculates The info(D) Sub A for index a in Current_D
    public double InfoForA(int a) {
        HashMap<String, HashMap<String, Integer>> counter = new HashMap();
        HashMap<String, Integer> temp;
        for (int i = 0; i < Current_D.get(a).size(); i++) {
            String A_value = Current_D.get(a).get(i);
            String C_value = Current_D.get(Current_D.size() - 1).get(i);
            if (counter.get(A_value) == null) {
                temp = new HashMap();
                temp.put(C_value, 1);
                counter.put(A_value, temp);
            } else {
                if (counter.get(A_value).get(C_value) == null) {
                    counter.get(A_value).put(C_value, 1);
                } else {
                    int count = counter.get(A_value).get(C_value);
                    counter.get(A_value).replace(C_value, count + 1);
                }
            }
        }

        double InfoForA = 0;
        int occurrences;
        double p;
        double internal_p;
        double value_EN;
        int total = Current_D.get(a).size();
        for (Map.Entry<String, HashMap<String, Integer>> entry : counter.entrySet()) {
            occurrences = 0;
            value_EN = 0;
            for (Map.Entry<String, Integer> en : entry.getValue().entrySet()) {
                occurrences += en.getValue();
            }
            for (Map.Entry<String, Integer> en : entry.getValue().entrySet()) {
                internal_p = Double.valueOf(en.getValue()) / Double.valueOf(occurrences);
                value_EN -= internal_p * LogBase2(internal_p);

            }

            p = Double.valueOf(occurrences) / Double.valueOf(total);
            //System.out.println(p * value_EN);
            InfoForA += p * value_EN;
        }
        return (InfoForA);

    }

    // Will be Called for each path
    public void Update_D(ArrayList<String> Path, ArrayList<String> Parents) {
        int index = 0;
        for (String path : Path) {
            if (index > Parents.size() - 1) {
                CSVFile.Exit();
            }
            int pos = PosisitonForA(Parents.get(index));
            if (pos == -1) {
                CSVFile.Exit();
            }
            for (int v = 0; v < Current_D.get(pos).size(); v++) {
                String value = Current_D.get(pos).get(v);
                if (!(value.equals(path))) {
                    for (ArrayList a : Current_D) {
                        a.remove(v);
                    }
                    v--;
                }

            }
            index++;

        }

        for (int parent = 0; parent < Parents.size(); parent++) {
            int pos = PosisitonForA(Parents.get(parent));
            Current_D.remove(pos);
            Current_FeaturesNames.remove(pos);
        }

    }

    public void Rebuild_CurrentD() {
        Current_D.clear();
        for (ArrayList<String> A : Original_D) {
            ArrayList<String> temp = new ArrayList<String>();
            for (String v : A) {
                temp.add(v);
            }
            Current_D.add(temp);
        }

        Current_FeaturesNames.clear();
        for (String F : Original_FeatureNames) {
            Current_FeaturesNames.add(F);
        }
    }

    public void NodeSelection(Node current) {

        // Check For leafness
        HashMap<String, Integer> counter = new HashMap();
        for (String classlabel : Current_D.get(Current_D.size() - 1)) {
            if (counter.get(classlabel) == null) {
                counter.put(classlabel, 1);
            } else {
                counter.replace(classlabel, counter.get(classlabel) + 1);
            }
        }
        if (counter.size() == 1) {
            for (Map.Entry<String, Integer> entry : counter.entrySet()) {
                current.name = entry.getKey();
                current.leaf = true;
            }
            T.add(Current_Path, current);
            return;
        }

        HashMap<String, Double> Selection_test = new HashMap<String, Double>();
        double InfoOfD = this.InfoOfD();
        double InfoForA;
        double Gain;

        // Calculate Gain For each A
        for (int i = 0; i < Current_D.size() - 1; i++) {

            if (Current_D.get(i).size() > 0) {
                InfoForA = this.InfoForA(i);
                Gain = this.Gain(InfoOfD, InfoForA);
                Selection_test.put(Current_FeaturesNames.get(i), Gain);
            }
        }

        // Get The Highest information Gain
        String highest_gain = "";
        double temp = -10000;
        for (Map.Entry<String, Double> entry : Selection_test.entrySet()) {
            if (temp < entry.getValue()) {
                temp = entry.getValue();
                highest_gain = entry.getKey();
            }
        }

        current.name = highest_gain; // Set Information for Selected Node
        int i = this.PosisitonForA(highest_gain);

        Node Child;
        ArrayList<String> new_path;
        
        //Set the Branches for selected Feature
        for (String B : this.BranchesForA(i)) {
            new_path = new ArrayList<String>();
            for (String p : Current_Path) {
                new_path.add(p);
            }
            new_path.add(B);
            Remaining_Paths.add((ArrayList<String>) new_path.clone());
            Child = new Node(false);
            Child.Set(null, B);

            if (T.empty) {
                Child.Parents.add(highest_gain);
            } else {
                new_path.remove(new_path.size() - 1);
                Node R = T.traverse(new ArrayList<String>());
                Child.Parents.add(R.name);
                ArrayList<String> branches = new ArrayList<>();
                for (String branch : new_path) {
                    branches.add(branch);
                    Node P = T.traverse(branches);
                    Child.Parents.add(P.name);
                }
            }

            current.Children.add(Child);
        }
        T.add(Current_Path, current); // Add Selected Node to The tree

    }

    public void BuildTree() {
        Node root = new Node(false);
        NodeSelection(root);
        for (int path = 0; path < Remaining_Paths.size(); path++) {
            Current_Path = Remaining_Paths.get(path);
            Node n = T.traverse(Current_Path);
            Update_D(Current_Path, n.Parents);
            NodeSelection(n);
            Rebuild_CurrentD();

        }
    }

    public String predict(HashMap<String, String> ex) {
        ArrayList<String> path = new ArrayList<>();
        Node temp = T.traverse(path);
        int count = 0;
        while (!temp.leaf) {
            path.add(ex.get(temp.name));
            temp = T.traverse(path);
            count++;
            if (count > T.size * 15) {
                CSVFile.Exit();
            }
            count++;
        }
        return (temp.name);
    }

    public static double LogBase2(double in) {
        return (Math.log(in) / Math.log(2));
    }

}
