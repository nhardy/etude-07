import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

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
    	HashSet<String> seen = new HashSet<String>(dictionary.size());
    	Queue<ArrayList<String>> paths = new LinkedList<ArrayList<String>>();
    	ArrayList<String> initial = new ArrayList<String>();
    	ArrayList<String> current;

    	seen.add(first);
    	initial.add(first);
    	paths.add(initial);

    	while (!paths.isEmpty()) {
            current = paths.remove();
            String latest = current.get(current.size()-1);
    		if (latest.equals(last)) {
                return current;
            }
            for (int i = 0; i < dictionary.size(); i++) {
                String word = dictionary.get(i);
                if (!seen.contains(word)) {
                    boolean adjacent = false;
                    int n1, n2;
                    n1 = latest.length();
                    n2 = word.length();
                    if (n2 > n1) {
                        String s = 
                    }
                    if (adjacent) {
                        ArrayList<String> path = new ArrayList<String>();
                        path.addAll(current);
                        path.add(word);
                        paths.add(path);
                    }
                }
            }
    	}

        return new ArrayList<String>();
    }
}
