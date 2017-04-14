import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Author: Alexandru Valeanu
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        //Scanner in = new Scanner(new FileInputStream("data.in"));
        Scanner in = new Scanner(System.in);
        MatchingEngine matchingEngine = new MatchingEngine();

        while (in.hasNextLine()){
            String line = in.nextLine();

            if (line.length() == 0 || line.contains("#")) // empty line or current line contains a comment
                continue;

            String[] tokens = line.split(",");

            if (tokens.length == 4) // limit order (only 4 tokens)
                matchingEngine.addOrder(new LimitOrder(tokens[0].charAt(0), Integer.parseInt(tokens[1]),
                        Short.parseShort(tokens[2]), Integer.parseInt(tokens[3])));
            else // iceberg order (5 tokens)
                matchingEngine.addOrder(new IcebergOrder(tokens[0].charAt(0), Integer.parseInt(tokens[1]),
                        Short.parseShort(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));

            System.out.println(matchingEngine); // we actually print the order book
        }
    }
}
