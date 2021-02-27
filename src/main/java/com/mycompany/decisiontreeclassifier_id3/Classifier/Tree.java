/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.decisiontreeclassifier_id3.Classifier;

import java.util.ArrayList;

public class Tree {

    public Node root;
    public boolean empty;
    public int size;
    public Tree() {
        empty = true;
        size = 0;
    }

    public void add(ArrayList<String> path, Node node) {
        if (empty) {
            root = node;
            empty = false;
        } else {
            Node goal = traverse(path);
            goal.Children.add(node);
            
        }
        size++;
    }
    
    public Node getLeaf(){
        Node n = traverse(new ArrayList<String>());
        while(!n.leaf){
            n = n.Children.get(0);
        }
        return(n);
    }

    public Node traverse(ArrayList<String> path) {
        Node temp = root;
        for (String b : path) {
            for (Node n : temp.Children) {
                if (n.branch.equals(b)) {
                    temp = n;
                }
            }

        }
        return (temp);
    }

}
