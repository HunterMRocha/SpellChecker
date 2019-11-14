import java.util.*;
import java.io.*;
import java.net.*;

public class Trie {

    // Alphabet size (# of symbols) 
    static final int ALPHABET_SIZE = 27; 

    // trie node 
    public static class TrieNode { 
        TrieNode[] children = new TrieNode[ALPHABET_SIZE]; 
        // isEndOfWord is true if the node represents 
        // end of a word 
        boolean isEndOfWord; 
          
        TrieNode(){ 
            isEndOfWord = false; 
            for (int i = 0; i < ALPHABET_SIZE; i++) 
                children[i] = null; 
        } 
    }; 
       
    static TrieNode root = new TrieNode();  
      
  
    public void insert(String key) 
    { 
        int level; 
        int length = key.length(); 
        int index; 
       
        TrieNode temp = root; 
       
        for (level = 0; level < length; level++) 
        { 
            index = key.charAt(level) - 'a'; 
            if(index == -58){
                index = 26; //if index is an ' then change it to 27
            }
            if (temp.children[index] == null) 
                temp.children[index] = new TrieNode(); 
       
            temp = temp.children[index]; 
        } 
       
        // mark last node as leaf 
        temp.isEndOfWord = true; 
    } 
       
    // Returns true if key presents in trie, else false 
    public boolean search(String key){ 
        int level; 
        int length = key.length(); 
        int index; 
        TrieNode temp = root;
       
        for (level = 0; level < length; level++){ 
            index = key.charAt(level) - 'a'; 
            if(index == -58){
                index = 26; //if index is an ' then change it to 27
            }
            if(index < 0){
                continue; 
            }
       
            if (temp.children[index] == null) 
                return false; 
       
            temp = temp.children[index]; 
        } 
       
        return (temp != null && temp.isEndOfWord); 
    }

}
 