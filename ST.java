import java.util.*;
import java.io.*;
import java.net.*;


public class ST implements Tree {
   
    Node root = null;
    public String[] suggestionsArr = new String[4];
    public int suggestionCount = 0; 

    public void insert(String word){
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
        if(node == null){
            return false; 
        }

        if(node.data.compareTo(word) == 0){
            return true; 
        }else if(node.data.compareTo(word) < 0){
            return search(word, node.right); //if node is less than the item then move closer to item by going right
        }else{
            return search(word, node.left);
        }

    }


    public String[] suggestions(String key){
        int lessThanThree = 0;
        String temp; 
        suggestionCount = 0;  
       
        while(suggestionCount < 3){
            temp = SwitchAdjacentLetters(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp;
            }else{
                lessThanThree++; 
            }

            temp = substitute(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp; 
            }else{
                lessThanThree++; 
            }
            temp = removeLetter(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp;
            }else{
                lessThanThree++; 
            }
            temp = addOne(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp;
            }else{
                lessThanThree++; 
            }
        }
        return suggestionsArr;
    }


    public boolean checkRepeat(String key){
        for(int i = 0; i < suggestionCount; i++){
            if(key.equals(suggestionsArr[i])){
                return false; 
            }
        }
        return true;
    }


    //helper func.
    private char[] toCharArray(String key){
        char[] characters = new char[key.length()];
        for(int i = 0; i < key.length(); i++){
            characters[i] = key.charAt(i);
        }
        return characters; 
    }

    //helper func.
    private String toStringArray(char [] chars){
        StringBuilder word = new StringBuilder();
        for(char chr: chars){
            word.append(chr);
        }
        return word.toString();
    }

    public String SwitchAdjacentLetters(String key){
        char ch[] = key.toCharArray(); 
        String temp1 = key; 
        String str; 

        for(int i = 0; i < key.length()-1; i++){
            ch = toCharArray(temp1); //toCharacterArray
            int j = i + 1; 
            char temp = ch[i];
            ch[i] = ch[j];
            ch[j] = temp; 
            str = toStringArray(ch); //toStringArray
            if(search(str) && checkRepeat(str)){
                return str; 
            }


            }
            return null;
        }

    public String substitute(String key){
        char ch[] = key.toCharArray(); //converts string to char array ['h', 'u', 't', 'n']
        String str; 

        for(int i = 0; i < key.length(); i++){
            ch = key.toCharArray();
            for(int j = 0; j < 26; j++){
                ch[i] = (char)(j+97); //changes index i to every letter in the alphabet
                str = toStringArray(ch); //converts char array with new letter to a string
                if(search(str) && checkRepeat(str)){
                    return str; 
                }

            }
        }
        return null; 
    }

    public String addOne(String key){
        String temp1 = key; 
        String str; 
        int length = key.length();
        char ch[] = toCharArray(temp1);
        ch = growArray(ch);
        for(int i = 0; i < 26; i++){
            ch[length] = (char)(i+97);
            str = toStringArray(ch);
            if(search(str) && checkRepeat(str)){
                return str; 
            }
        }
        return null; 
    }

    public char[] growArray(char[] ch){
        int length = ch.length+1; 
        char chr[] = new char[length];
        for(int i = 0; i < ch.length; i++){
            chr[i] = ch[i];
        }
        return chr; 
    }

    public String removeLetter(String key){
        char ch[] = key.toCharArray();
        char temp[] = new char[key.length()-1];
        String str; 

        for(int i = 0; i < key.length() -1; i++){
            temp[i] = ch[i];
            str = toStringArray(temp);
            if(search(str) && checkRepeat(str)){
                return str; 
            }
        }
        return null;
    }

    public void writeOutput(String word, String fileName){
        String wordSuggestions[] = new String[4];
        String directory = System.getProperty("user.dir");
        String filename = fileName; 
        String absolutePath = directory + File.separator + filename; 

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath, true))){
            if(search(word)){
                bw.write(word);
            }else{
                wordSuggestions = suggestions(word);
                for(int i = 0; i < wordSuggestions.length-1; i++){
                   bw.write(wordSuggestions[i]); 
                   bw.write(" ");
                }
            }
            bw.write("\n");
        
        }catch(IOException e){
            e.printStackTrace();
        }
    }




    public class Node {
        Node right, left; 
        String data; 

        public Node(String data){
            left = right = null; 
            this.data = data; 
        }
    }

}

