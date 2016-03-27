package driver;

import engine.SearchEngine;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import model.FileRelevant;

/**
 *
 * @author hkhoi
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Loading");
        SearchEngine engine = new SearchEngine();
        boolean stop = false;
        System.out.println("Engine loaded");
    
        Scanner scan = new Scanner(System.in);
        
        while (!stop) {
            System.out.println("1. Generate 500 random words");
            System.out.println("2. Test inverted index");
            System.out.println("3. Query (using vector space)");
            System.out.println("4. Quit");
            System.out.print("Command: ");
            String arg = scan.nextLine();
            
            switch (arg) {
                case "1":
                    engine.testRandomWords();
                    break;
                case "2":
                    System.out.print("Input a word: ");
                    String word = scan.nextLine();
                    engine.testInvertedIndex(word);
                    break;
                case "3":
                    System.out.print("Input query: ");
                    String query = scan.nextLine();
                    engine.getRelevantFiles(query);
                    break;
                case "4":
                    stop = true;
                    break;
                default:
                    System.out.println("Command not found!");
                    break;
            }
            
        }
    }
}
