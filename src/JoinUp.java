/**
 * Etude 7 - Joined Up Writing
 * @author Kimberley Louw, Nathan Hardy
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class JoinUp {
    /**
     * First word in the chain, passed in by command line arguments
     */
    private String first;
    /**
     * Last word in the chain, passed in by command line arguments
     */
    private String last;
    /**
     * ArrayList of all words specified by stdin
     */
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

    /**
     * Prints the single linked and double linked word chains
     */
    public void run() {
        printList(getSingleLinkedArray());
        printList(getDoubleLinkedArray());
    }

    /**
     * Prints a list according to the output format specified
     * @param list list of words in a chain
     */
    public void printList(ArrayList<String> list) {
        System.out.print(list.size());
        for (String item : list) {
            System.out.print(" " + item);
        }
        System.out.println();
    }

    /**
     * Returns an ArrayList chain of words
     * @param operator boolean operator (lambda) - AND for double linked, OR for single linked
     * @return ArrayList chain of words
     */
    public ArrayList<String> getLinkedArray(BooleanOperator operator) {
        // Words already seen
        HashSet<String> seen = new HashSet<String>(dictionary.size());
        seen.add(first);

        // Queue of paths
        Queue<ArrayList<String>> paths = new LinkedList<ArrayList<String>>();
        // We start with an initial path: [first]
        ArrayList<String> initial = new ArrayList<String>();
        initial.add(first);
        // Add this to our paths queue
        paths.add(initial);

        // Breadth-First Search
        while (!paths.isEmpty()) {
            // Take the next path off the queue
            ArrayList<String> currentPath = paths.remove();
            // Get the last word from that path
            String currentWord = currentPath.get(currentPath.size() - 1);

            // If the last word is the same as last, then we have reached the end
            // and can return the entire word chain
            if (currentWord.equals(last)) return currentPath;

            // For each word in the dictionary
            for (String word : dictionary) {
                // Ignore words that we've already seen
                if (seen.contains(word)) continue;

                // A word is considered 'adjacent' if:
                // the suffix of the first word is the prefix of the second
                // For single linked words,
                boolean isAdjacent = false;
                // Exit early for double linked chains if one word is less than half the length of the other
                if (operator.apply(true, false) == false
                    && (word.length() < currentWord.length() / 2.0 || currentWord.length() < word.length() / 2.0)
                ) continue;

                // Slide the second word across the first
                for (int i = Math.max(currentWord.length() - word.length(), 0); i < currentWord.length(); i++) {
                    // Potentially overlapping portion of the first word
                    String potentialOverlap = currentWord.substring(i);
                    boolean currentWordHalfOverlapped = potentialOverlap.length() >= currentWord.length() / 2.0;
                    boolean wordHalfOverlapped = potentialOverlap.length() >= word.length() / 2.0;

                    // If neither word is half overlapped with the other, we don't need to check these two words against each other anymore
                    if (!currentWordHalfOverlapped && !wordHalfOverlapped) break;

                    // If the potentially overlapping portion of the word does match, and the condition is met, then the two words are 'adjacent'
                    if (word.indexOf(potentialOverlap) == 0 && operator.apply(currentWordHalfOverlapped, wordHalfOverlapped)) {
                        isAdjacent = true;
                        break;
                    }
                }

                if (isAdjacent) {
                    // Mark the word as seen to prevent cycles or longer paths reusing this word
                    seen.add(word);

                    // Create a new path
                    ArrayList<String> path = new ArrayList<String>();
                    // Add the current path to the new path
                    path.addAll(currentPath);
                    // Add the new word
                    path.add(word);
                    // Add the new path to the queue
                    paths.add(path);
                }
            }
        }

        // If we haven't found a valid word chain, just return an empty ArrayList to avoid a runtime exception
        return new ArrayList<String>();
    }

    /**
     * Returns an ArrayList of single linked words
     * @return ArrayList chain of words
     */
    public ArrayList<String> getSingleLinkedArray() {
        // If the first OR second word overlaps by half or more,
        // we want to include it
        return getLinkedArray((boolean a, boolean b) -> a || b);
    }

    /**
     * Returns an ArrayList of single linked words
     * @return ArrayList chain of words
     */
    public ArrayList<String> getDoubleLinkedArray() {
        // If the first AND second word overlaps by half or more,
        // we want to include it
        return getLinkedArray((boolean a, boolean b) -> a && b);
    }


    interface BooleanOperator {
        public boolean apply(boolean a, boolean b);
    }
}
