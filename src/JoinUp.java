import java.util.ArrayList;
import java.util.Scanner;

/**
 * Etude 7 - Joined Up Writing
 * @author Kimberley Louw, Nathan Hardy
 */

public class JoinUp {
    private String first;
    private String last;
    private ArrayList<String> dictionary = new ArrayList<String>();

    public static void main(String[] args) {
        new JoinUp(args[0], args[1]).run();
    }

    public JoinUp(String first, String last) {
        this.first = first;
        this.last = last;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            dictionary.add(scanner.nextLine());
        }
        scanner.close();
    }

    public void run() {

    }

    public ArrayList<String> getSingleLinkedArray() {
        return new ArrayList<String>();
    }

    public ArrayList<String> getDoubleLinkedArray() {
        return new ArrayList<String>();
    }
}
