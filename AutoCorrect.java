import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.*;

public class AutoCorrect {

    public static ST st = new ST();
    public static Trie tree = new Trie();
    public Properties prop = new Properties();
    public String[] suggestionsArr = new String[4];
    public int suggestionCount = 0; 


    public static void createTree(int fileType){
        //if user has an internet connection this try-catch should run; pulling the dictionary directly from github
        try {
            URL url = new URL("https://raw.githubusercontent.com/magsilva/jazzy/master/resource/dict/english.0");
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                if(fileType == 0){
                    if(sc.nextLine().length() == 0) {
                        tree.insert(sc.nextLine().toLowerCase());
                    }
                }else{
                    if(sc.nextLine().length() == 0) {
                        st.insert(sc.nextLine().toLowerCase());
                    }
                }
            }
            sc.close();
        } catch (IOException e) { 
            try{ //if user doesn't have internet connection this try-catch should run; pulling the dictionary directly from their directory
                String filePath = new File("").getAbsolutePath(); 
                filePath = filePath.concat("/english0.txt"); 
                File file = new File(filePath); 
                Scanner sc = new Scanner(file);
                while (sc.hasNext()) {
                    if(fileType == 0){
                        if (sc.nextLine().length() == 0) {
                            tree.insert(sc.nextLine().toLowerCase()); //lowercase every word and insert it into a trie structure until their isn't a new line in the file
                        }
                    }else{
                        if (sc.nextLine().length() == 0) {
                            st.insert(sc.nextLine().toLowerCase());
                        }
                    }
                }
                sc.close();
            }
            catch(FileNotFoundException a){
                System.out.println("file not found"); //if ther user doesn't have the file in their directory then this should execute
            }
        }
        
    }

      
    public void writeOutput(String word, int fileType){
        createTree(fileType); //fileType is an int used to distinguish whether storage = trie or a search tree
        String directory = System.getProperty("user.dir"); 
        String filename = "output.txt"; 
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
            if(fileType == 0){
                
                if(tree.search(word)){
                    bw.write("'" + word + "'" + " is already spelled correctly");
                    bw.write("\n");
                }else{
                    suggestionsArr = suggestions(word, fileType);
                    for(int i = 0; i < suggestionsArr.length-1; i++){
                    bw.write(suggestionsArr[i] + " "); 
                    }
                }
                bw.write("\n\n");


            }else{
                if(st.search(word)){
                    bw.write(word);
                }else{
                    suggestionsArr = suggestions(word, fileType);
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

    public String[] suggestions(String key, int FileType) {

        String temp, temp2, temp3, temp4; 
        suggestionCount = 0;
        for(int i = 0; i < suggestionsArr.length -1; i++)  suggestionsArr[i] = ""; //initialize every index to an empty string

        /**
         * this loop exectutes until there are at most three different suggested words. If one of the functions returns null to the temp variable then 
         * count is incremented to prevent an infinite loop from happening. If all of functions return temp as null then the function noSuggestions(...)
         * is called writes "no suggestions found..." to the file
         */
        while (suggestionCount < 3) {
            if (checkNumbers(key)){
                suggestionsArr[suggestionCount] = "No suggestions found. A non-letter was entered";
                suggestionCount++; 
            }

            temp = checkAdjacent(key, FileType);
            if (temp != null)   suggestionsArr[suggestionCount++] = temp;

            temp2 = removeOne(key, FileType);
            if (temp2 != null)  suggestionsArr[suggestionCount++] = temp2;
            
            temp3 = addOne(key, FileType);
            if (temp3 != null)  suggestionsArr[suggestionCount++] = temp3;

            temp4 = substitute(key, FileType);
            if (temp4 != null)  suggestionsArr[suggestionCount++] = temp4;

            noSuggestions(temp, temp2, temp3, temp4, key);
        }

        return suggestionsArr;
    }

    //helper function for suggestions(...)
    public void noSuggestions(String temp, String temp2, String temp3, String temp4, String key){
        if (temp == null && temp2 == null && temp3 == null && temp4 == null) {
            suggestionsArr[suggestionCount] = "NO SUGGESTIONS FOUND FOR '" + key + "'";
            for(int i = 1; i < suggestionCount; i++) 
                suggestionsArr[i] = "";
            
            suggestionCount = 4; 
        }
    }

    public boolean checkNumbers(String key){
        return Pattern.compile( "[0-9]" ).matcher(key).find();
    }

    /**
     * this function removes every letter and check if the user accidentally typed a letter twice; e.g. "accidentt" -> "accident" where the extra 't' is removed
     */
    public String removeOne(String key, int FileType) {
        String str;
        char ch[] = toCharacterArray(key);
        for (int i = 0; i < ch.length; i++) {
            ch = toCharacterArray(key);
            for (int j = i + 1; j < ch.length - 1; j++) {
                ch[j] = ch[j + 1];
            }
            str = charArrayToString(ch);
            str = str.substring(0, ch.length - 1);
            if(FileType == 0){
                if(tree.search(str) && checkRepeats(str)){
                    return str; 
                }
            }else{
                if(st.search(str) && checkRepeats(str)){
                    return str; 
                }
            }
        }
        return null;
    }

    /**
     * this function checks if the user accidentally skipped a letter; e.g. "lette" -> "letter" where the r is added
     */
    public String addOne(String key, int FileType) {
        String temp1 = key;
        String str;
        // int length = key.length();
        char ch[] = toCharacterArray(temp1);
        ch = grow_array(ch);
        // char temp2[] = ch;
        ch = toCharacterArray(temp1);
        ch = grow_array(ch);
        for (int j = ch.length - 1; j > 1; j--) {
            for (int k = 0; k < 26; k++) {
                ch[j] = (char) (k + 97);
                str = charArrayToString(ch);
                if(FileType == 0){
                    if(tree.search(str) && checkRepeats(str)){
                        return str; 
                    }
                }else{
                    if(st.search(str) && checkRepeats(str)){
                        return str; 
                    }
                }
            }
            ch[j] = ch[j - 1];
        }

        return null;
    }

    //helper function for addOne
    public char[] grow_array(char[] ch) {
        int length = ch.length + 1;
        char chr[] = new char[length];
        for (int i = 0; i < ch.length; i++) {
            chr[i] = ch[i];
        }
        return chr;
    }

    /**
     * This function substitues every letter in the word with every letter in the alphabet and searches for a correctly spelled word; 
     * e.g. "houae" -> "house" 
     */
    public String substitute(String key, int fileType) {
        String temp1 = key;
        String str;
        char ch[] = toCharacterArray(key);
        for (int i = 0; i < ch.length; i++) {
            ch = toCharacterArray(temp1);
            for (int j = 0; j < 26; j++) {
                ch[i] = (char) (j + 97);
                str = charArrayToString(ch);
                if(fileType == 0){
                    if(tree.search(str) && checkRepeats(str)){
                        return str; 
                    }
                }else{
                    if(st.search(str) && checkRepeats(str)){
                        return str; 
                    }
                }
            }
        }
        return null;
    }

    //converts a string to a char array
    public char[] toCharacterArray(String key) {
        char[] characters = new char[key.length()];
        for (int i = 0; i < key.length(); i++) {
            characters[i] = key.charAt(i);
        }
        return characters;
    }

    //converts a char array to a string
    public String charArrayToString(char[] chars) {
        return (new String(chars));
    }


    /**
     * This function checks every adjacent pair of letters and flops them; e.g. "teh" -> "the"
     */
    public String checkAdjacent(String key, int fileType) {
        // System.out.println("word in checkAdj: " + key);
        String temp1 = key;
        String str;
        char ch[] = toCharacterArray(key);
        for (int i = 0; i < ch.length - 1; i++) {
            ch = toCharacterArray(temp1);
            int j = i + 1;
            char temp = ch[i];
            ch[i] = ch[j];
            ch[j] = temp;
            str = charArrayToString(ch);
            // System.out.println("word out of conditional: " + str);
            if(fileType == 0){
                if(tree.search(str) && checkRepeats(str)){
                    // System.out.println("word in conditional " + str);
                    return str; 
                }
            }else{
                if(st.search(str) && checkRepeats(str)){

                    return str; 
                }
            }

        }
        return null;
    }

        /**
     * this function is to make sure there are no repeating words from different function; 
     * e.g. if substitue returns "the" and switchAdjacent return "the" it will only accept one. 
     */
    public boolean checkRepeats(String key) {
        for (int i = 0; i < suggestionCount; i++) {
            if (key.equals(suggestionsArr[i])) {
                return false;
            }
        }
        return true;
    }
    
}