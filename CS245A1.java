import java.util.*;
import java.io.*;
import java.net.*;

public class CS245A1{
    static Properties prop = new Properties();
    static Trie tree = new Trie();
    static ST st = new ST();
    static AutoCorrect ac = new AutoCorrect(st);
    private static long start, totalTime = 0;
    static int structure = -1; //0 = trie //1 = st
    

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

        createTree();
        // System.out.println(st.root.data);


         if(args.length < 2){
            System.out.println("You are missing at least one argument");
            readInput("input.txt", "output.txt",prop.getProperty("storage"));
         }else{
            readInput(args[0], args[1], prop.getProperty("storage"));
         }
  
        System.out.println("Total time: " + totalTime + "[ms]");

        
    }

    public static void createTree(){
        totalTime = 0;
        try{
            URL url = new URL("https://raw.githubusercontent.com/magsilva/jazzy/master/resource/dict/english.0");
            Scanner sc = new Scanner(url.openStream());

            if(prop.getProperty("storage").equals("trie")){
                structure = 0; 
            }else{
                structure = 1; 
            }

            if(structure == 0){
                System.out.println("Running Trie tree...");
                start = System.currentTimeMillis();
                while(sc.hasNext()){
                    if(sc.nextLine().length() == 0){
                        tree.insert(sc.nextLine().toLowerCase());
                    }
                }
            }else{
                System.out.println("Running Search tree...");
                start = System.currentTimeMillis();
                while(sc.hasNext()){
                    if(sc.nextLine().length() == 0){
                        st.insert(sc.nextLine().toLowerCase());
                    }
                }
            }
            
            long end = System.currentTimeMillis(); 
            sc.close();
            totalTime = end-start; 


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // public static void createST(){
    //     System.out.println("Running Search Tree Data Structure...");
    //     totalTime = 0;
    //     try{
    //         URL url = new URL("https://raw.githubusercontent.com/magsilva/jazzy/master/resource/dict/english.0");
    //         Scanner sc = new Scanner(url.openStream());
            
    //         start = System.currentTimeMillis();
            
    //         while(sc.hasNext()){
    //             if(sc.nextLine().length() == 0){
    //                 st.insert(sc.nextLine().toLowerCase());
    //             }
    //         }
    //         long end = System.currentTimeMillis(); 
    //         sc.close();
    //         totalTime = end-start; 


    //     }catch(IOException e){
    //         e.printStackTrace();
    //     }

    // }

    public static void readInput(String inputFile, String outFile, String type){
        //create a path to the text file
        String directory = System.getProperty("user.dir");
        String filename = inputFile;
        String absolutePath = directory + File.separator + filename; 
    
        try(BufferedReader br = new BufferedReader(new FileReader(absolutePath))){
            String line = br.readLine();
            while(line != null){  
                String lowerCaseWord = line.toLowerCase();

                ac.writeOutput(lowerCaseWord, outFile);
                line = br.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
       
    }


}