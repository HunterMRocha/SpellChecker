import java.util.*;
import java.io.*;
import java.net.*;

public class ST {

    public Node root;

    public ST() {
        this.root = null;
    }

    public class Node {
        Node right, left; 
        String data; 

        public Node(String data){
            this.left = right = null; 
            this.data = data; 
        }
    }

    public void insert(String word){
        this.root = new Node(word);
        //System.out.println("root: " + root.data);
        //if we don't have a tree yet then create first value
        Node newNode = new Node(word); //newNode with new data
        
        if(root == null){
            root = newNode; //set first node to data
        }else{
            
            Node current = root; //this node is going to be used to itterate through the tree
            Node parent; 
            while(true){
                //move towards left
                parent = current; 
                if(current.data.compareTo(word) > 0){
                    current = current.left; 
                    if(current == null){ //it's parent is the leaf node
                        parent.left = newNode; 
                        return; 
                    }
                }else{
                    //move towards right
                    current = current.right; 
                    if(current == null){
                        parent.right = newNode; 
                        return; 
                    }
                }
            }
        }

    }

    public boolean search(String word){

        return search(word, root);
    }

    public boolean search(String word, Node node){
        if (node.data != word){
            return false; 
        }
        System.out.println("now we're here" + node.data);
        if(node.data.compareTo(word) == 0){
            return true; 
        }else if(node.data.compareTo(word) < 0){
            return search(word, node.right); //if node is less than the item then move closer to item by going right
        }else{
         
            return search(word, node.left);
        }

    }

}

