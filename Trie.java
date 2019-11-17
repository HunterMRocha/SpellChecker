
// Java implementation of search and insert operations 
// on Trie 
import java.util.*;
import java.io.*;
import java.net.*;

public class Trie implements Tree {

    // Alphabet size (# of symbols)
    static final int ALPHABET_SIZE = 27;
    public int suggestionCount = 0;
    public String[] suggestions = new String[4];

    // trie node
    static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];

        // isEndOfWord is true if the node represents
        // end of a word
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
    };

    private TrieNode root = new TrieNode();

    // If not present, inserts key into trie
    // If the key is prefix of trie node,
    // just marks leaf node
    public void insert(String key) {
        int level;
        int length = key.length();
        int index;

        TrieNode r = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (index == -58) {
                index = 26;
            }
            if (r.children[index] == null)
                r.children[index] = new TrieNode();

            r = r.children[index];
        }

        // mark last node as leaf
        r.isEndOfWord = true;
    }

    // Returns true if key presents in trie, else false
    public boolean search(String key) {
        int level;
        int length = key.length();
        int index;
        TrieNode r = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (index == -58) {
                index = 26;
            }
            if (index < 0) {
                continue;
            }
            if (r.children[index] == null)
                return false;

            r = r.children[index];
        }
        return (r != null && r.isEndOfWord);
    }
}