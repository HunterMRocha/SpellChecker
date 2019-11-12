import java.util.*;
import java.io.*;
import java.net.*;

public class CS245A1{
    static Properties prop = new Properties();
    private static long start, end, totalTime = 0;
    static Trie tree = new Trie();
    static ST st = new ST();

   


    public static void main(String[] args){
         //createTrie();
        InputStream fis; 
        try{
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/A1Properties.txt");
            fis = new FileInputStream(filePath);
            prop.load(fis);
        }catch(IOException e){
            prop.setProperty("storage", "trie");
        }

         if(prop.getProperty("storage").equals("trie")){
            createTrie();
        }else{
            createST();
        }

         if(args.length < 2){
            System.out.println("You are missing at least one argument");
            readInput("input.txt", "output.txt",prop.getProperty("storage"));
         }else{
            readInput(args[0], args[1], prop.getProperty("storage"));
         }

         
        
        
    
        System.out.println("Total time: " + totalTime + "[ms]");

        
    }

    public static void createTrie(){
        System.out.println("Running Trie...");
        totalTime = 0;
        try{
            URL url = new URL("https://raw.githubusercontent.com/magsilva/jazzy/master/resource/dict/english.0");
            Scanner sc = new Scanner(url.openStream());
            
            start = System.currentTimeMillis();
            while(sc.hasNext()){
                if(sc.nextLine().length() == 0){
                    tree.insert(sc.nextLine().toLowerCase());
                }
            }
            long end = System.currentTimeMillis(); 
            sc.close();
            totalTime = end-start; 


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void createST(){
        System.out.println("Running ST...");
        totalTime = 0;
        try{
            URL url = new URL("https://raw.githubusercontent.com/magsilva/jazzy/master/resource/dict/english.0");
            Scanner sc = new Scanner(url.openStream());
            
            start = System.currentTimeMillis();
            while(sc.hasNext()){
                if(sc.nextLine().length() == 0){
                    st.insert(sc.nextLine().toLowerCase());
                }
            }
            long end = System.currentTimeMillis(); 
            sc.close();
            totalTime = end-start; 


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void readInput(String inputFile, String outFile, String type){
        //create a path to the text file
        
        String directory = System.getProperty("user.dir");
        String filename = inputFile;
        String absolutePath = directory + File.separator + filename; 
    
        try(BufferedReader br = new BufferedReader(new FileReader(absolutePath))){
            String line = br.readLine();
            while(line != null){  
                if(type.equals("st")){
                    String lowerCaseWord = line.toLowerCase();
                    st.writeOutput(line, outFile);
                    line = br.readLine();
                }else{
                    String lowerCaseWord = line.toLowerCase();
                    tree.writeOutput(line, outFile);
                    line = br.readLine();
                }
                
            }
        }catch(IOException e){
            e.printStackTrace();
        }
       
    }


}