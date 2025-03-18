import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Lucas Huang
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */

public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateHelper("", letters);
    }

    public void generateHelper(String newWord, String word) {
        // Base case
        if (word.isEmpty()) {
            return;
        }
        // Recursive case that creates all possible combinations
        for (int i = 0; i < word.length(); i++) {
            String temp = newWord + word.charAt(i);
            words.add(temp);
            generateHelper(temp, word.substring(0, i) + word.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        // Calls recursive method to sort out arraylist
        words = mergeSort(words, 0, words.size() - 1);
    }

    // Mergesort method for the sort method
    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // Base case where if ArrayList is empty don't return anything
        if (high == low) {
            ArrayList<String> result = new ArrayList<String>();
            result.add(words.get(low));
            return result;
        }
        // Calculate middle index
        int mid = (high + low) / 2;
        // Recursively Mergesort each half
        ArrayList<String> left = mergeSort(words,low, mid);
        ArrayList<String> right = mergeSort(words, mid + 1, high);
        // Return the sorted arraylist
        return finalMerge(left, right);
    }

    // Actual sorting part of the method
    public ArrayList<String> finalMerge(ArrayList<String> left, ArrayList<String> right) {
        // Initialize pointers for both halves
        ArrayList<String> combined = new ArrayList<String>();
        int i = 0;
        int j = 0;
        // While loop to compare two arraylists until one is empty
        while (i < left.size() && j < right.size()) {
            // If left arraylist first letter comes after right arraylist first letter put right word first
            if (left.get(i).compareTo(right.get(j)) > 0) {
                combined.add(right.get(j));
                j++;
            }
            // Else add left arraylist word first
            else {
                combined.add(left.get(i));
                i++;
            }
        }
        // Finishes the rest of the arraylist that isn't empty
        if (i == left.size()) {
            while (j < right.size()) {
                combined.add(right.get(j));
                j++;
            }
        }
        else {
            while (i < left.size()) {
                combined.add(left.get(i));
                i++;
            }
        }
        // Returns the sorted arraylist
        return combined;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++) {
            if (!binarySearch(words.get(i))) {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean binarySearch(String word) {
        int low = 0;
        int high = DICTIONARY_SIZE - 1;
        while (low <= high) {
            int mid = (high + low) / 2;
            int comparison = word.compareTo(DICTIONARY[mid]);
            if (comparison == 0) {
                return true;
            }
            else if (comparison > 0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
