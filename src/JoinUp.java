/**
 * Etude 7 - Joined Up Writing
 * @author Kimberley Louw, Nathan Hardy
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    /**
     * Tree structure for storing words as trees of characters
     */
    private TreeNode tree = new TreeNode();

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

        for (String word : dictionary) {
            tree.putWord(word);
        }
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
        // FIXME: Leaky abstraction
        boolean doubleLinked = !operator.apply(true, false);

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
            int currentWordLength = currentWord.length();

            // If the last word is the same as last, then we have reached the end
            // and can return the entire word chain
            if (currentWord.equals(last)) return currentPath;

            for (int currentWordOffset = 0; currentWordOffset < currentWordLength; currentWordOffset++) {
                // Exit early if we're checking for double linked words and we have less than
                // half of the current word left to check
                if (doubleLinked && currentWordOffset > currentWordLength / 2) break;

                // Portion of the current word that we are checking other words against
                String potentialOverlap = currentWord.substring(currentWordOffset);

                // For every word in the dictionary that starts with our potential overlap
                for (String word : tree.getWordsStartingWith(potentialOverlap)) {
                    // If we have already seen this word, don't bother considering this word
                    if (seen.contains(word)) continue;

                    // If we have the right amount of overlap
                    if (operator.apply(currentWordOffset <= currentWordLength / 2.0, potentialOverlap.length() >= word.length() / 2.0)) {
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

    /**
     * Provides an abstraction over a non-binary Search Tree for words by character
     */
    class TreeNode {
        private HashMap<Character, TreeNode> children = new HashMap<Character, TreeNode>();
        private String word = null;

        /**
         * Returns a list of words that start with the remaining characters. If empty,
         * returns a list of all child words.
         * @param remainingChars remaining characters
         * @return A List of all words starting with {@code remainingChars}
         */
        public ArrayList<String> getWordsStartingWith(String remainingChars) {
            ArrayList<String> result = new ArrayList<String>();

            if (remainingChars.isEmpty()) {
                if (word != null) result.add(word);

                for (TreeNode child : children.values()) {
                    result.addAll(child.getWordsStartingWith(""));
                }
            } else {
                if (children.containsKey(remainingChars.charAt(0))) {
                    result.addAll(children.get(remainingChars.charAt(0)).getWordsStartingWith(remainingChars.substring(1)));
                }
            }

            return result;
        }

        private void putWord(String word, int depth) {
            if (depth == word.length()) {
                this.word = word;
            } else {
                if (!children.containsKey(word.charAt(depth))) {
                    children.put(word.charAt(depth), new TreeNode());
                }
                children.get(word.charAt(depth)).putWord(word, depth + 1);
            }
        }

        /**
         * Puts a word into the search tree
         * @param word Word to insert into the tree
         */
        public void putWord(String word) {
            putWord(word, 0);
        }
    }

    interface BooleanOperator {
        /**
         * Returns a boolean according to some expression involving {@code a} and {@code b}
         * @param a Boolean A
         * @param b Boolean B
         * @return Boolean result
         */
        public boolean apply(boolean a, boolean b);
    }
}
