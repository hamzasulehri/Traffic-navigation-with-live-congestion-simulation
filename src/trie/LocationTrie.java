package trie;

import java.util.*;

public class LocationTrie {

    private TrieNode root;

    public LocationTrie() {
        root = new TrieNode();
    }

    public void insert(String word) {

        TrieNode current = root;

        for(char ch : word.toLowerCase().toCharArray()) {

            current.getChildren()
                    .putIfAbsent(ch, new TrieNode());

            current =
                    current.getChildren().get(ch);
        }

        current.setEndOfWord(true);
    }

    public boolean search(String word) {

        TrieNode current = root;

        for(char ch : word.toLowerCase().toCharArray()) {

            if(!current.getChildren()
                    .containsKey(ch)) {

                return false;
            }

            current =
                    current.getChildren().get(ch);
        }

        return current.isEndOfWord();
    }

    public List<String> autocomplete(
            String prefix) {

        List<String> results =
                new ArrayList<>();

        TrieNode current = root;

        for(char ch :
                prefix.toLowerCase().toCharArray()) {

            if(!current.getChildren()
                    .containsKey(ch)) {

                return results;
            }

            current =
                    current.getChildren().get(ch);
        }

        collectWords(
                current,
                prefix.toLowerCase(),
                results
        );

        return results;
    }

    private void collectWords(
            TrieNode node,
            String word,
            List<String> results) {

        if(node.isEndOfWord()) {

            results.add(word);
        }

        for(Map.Entry<Character, TrieNode> child :
                node.getChildren().entrySet()) {

            collectWords(
                    child.getValue(),
                    word + child.getKey(),
                    results
            );
        }
    }
}