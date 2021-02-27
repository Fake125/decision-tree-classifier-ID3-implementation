/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.decisiontreeclassifier_id3.Classifier;

import java.util.ArrayList;

/**
 *
 * @author fake
 */
public class Node {

    public boolean leaf;
    public String name;
    public ArrayList<Node> Children;
    public ArrayList<String> Parents;
    public String branch;
    

    public Node(boolean l) {
        leaf = l;
        Parents = new ArrayList<String>();
        if (!leaf) {
            Children = new ArrayList<Node>();
        } else {
            Children = null;
        }

    }

    public void Set(String name, String branch) {
        this.name = name;
        this.branch = branch;
    }
}
