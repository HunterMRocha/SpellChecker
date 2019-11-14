import java.util.*;
import java.io.*;
import java.net.*;

public class AutoCorrect {
    public ST st;
    public AutoCorrect(ST st) {
        this.st = st;
    }

    Trie tree = new Trie();
    static Properties prop = new Properties();

    
    public String[] suggestionsArr = new String[4];
    public int suggestionCount = 0; 

      
    public void writeOutput(String word, String fileName){
        String directory = System.getProperty("user.dir");
        String filename = fileName; 
        String absolutePath = directory + File.separator + filename; 

        InputStream fis; 
        try{
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/A1Properties.txt");
            fis = new FileInputStream(filePath);
            prop.load(fis);
        }catch(IOException e){
            prop.setProperty("storage", "trie");
        }


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath, true))){
            if(prop.getProperty("storage").equals("trie")){
                if(tree.search(word)){
                    bw.write(word);
                }else{
                    suggestionsArr = suggestions(word);
                    for(int i = 0; i < suggestionsArr.length-1; i++){
                       bw.write(suggestionsArr[i]); 
                       bw.write(" ");
                    }
                }
                bw.write("\n");
            }else{
                if(!st.search(word)){
                    bw.write(word);
                }else{
                    suggestionsArr = suggestions(word);
                    for(int i = 0; i < suggestionsArr.length-1; i++){
                       bw.write(suggestionsArr[i]); 
                       bw.write(" ");
                    }
                }
                bw.write("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String[] suggestions(String key){
        String temp; 
        suggestionCount = 0;  

        while(suggestionCount < 3){
            temp = SwitchAdjacentLetters(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp;
            }

            temp = substitute(key);            
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp; 
            }

            temp = removeLetter(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp;
            }

            temp = addOne(key);
            if(temp != null){
                suggestionsArr[suggestionCount++] = temp;
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
            if(tree.search(str) && checkRepeat(str) || st.search(str) && checkRepeat(str)){
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
                if(tree.search(str) && checkRepeat(str) || st.search(str) && checkRepeat(str)){
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
            if(tree.search(str) && checkRepeat(str) || st.search(str) && checkRepeat(str)){
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
        String str; 
        char ch[] = toCharArray(key);
        for(int i = 0; i < ch.length; i++){
            ch = toCharArray(key);
            for(int j = i+1; j < ch.length-1; j++){
                ch[j] = ch[j+1]; 
            }
            str = toStringArray(ch); 
            str = str.substring(0,ch.length-1); 
            if(tree.search(str) && checkRepeat(str) || st.search(str) && checkRepeat(str)){ 
                return str; 
            }
        }
        return null; 
    }
    
}