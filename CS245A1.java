import java.util.*;
import java.io.*;
import java.net.*;

public class CS245A1 {
    public static Properties prop = new Properties();
    public static ST st = new ST();
    public static Trie trie = new Trie();
    public static AutoCorrect ac = new AutoCorrect();
    public static int FileType = 0; // 0 = trie 1 = st
    

    public static void main(String[] args) {
        InputStream fis; 

        try{
            String filePath = new File("").getAbsolutePath(); 
            filePath = filePath.concat("/a1properties.txt"); 
            fis = new FileInputStream(filePath); 
            prop.load(fis); 
        }catch(IOException e){
            prop.setProperty("storage","trie"); 
        }

        if(prop.getProperty("storage").equals("st")){
            FileType = 1; 
        }

        // createTree(FileType);

        if (args.length < 2) {
            System.out.println("Missing at least one parameter!!!");
            readInput("input.txt", "output.txt");
        } else {
            readInput(args[0], args[1]);
        }
    }

    public static void readInput(String inputFile, String outputFile) {
        String directory = System.getProperty("user.dir");
        String filename = inputFile;
        String path = directory + File.separator + filename;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while (line != null) {
                String temp = line.toLowerCase();
                ac.writeOutput(temp, FileType);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
