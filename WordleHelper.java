package Wordle_Helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordleHelper {
    private Map<Integer, Character> greenLetters = new HashMap<>();
    private List<Character> yellowLetters = new ArrayList<>();
    private List<Integer> yellowSpaces = new ArrayList<>();
    private String grayLetters = new String();
    
    public void addGreenLetter(Integer index, Character letter){
        if(letter != null && index != null && !this.greenLetters.containsKey(index)){
            this.greenLetters.put(index, letter);
        }
    }

    public Map<Integer, Character> getGreenLetter(){
        return this.greenLetters;
    }

    //Returns a List of Integers where yellow letters could possibly be
    public List<Integer> getNonGreenIndex(){
        List<Integer> indexList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        for(Map.Entry<Integer, Character> set : this.greenLetters.entrySet()) {
                if(indexList.contains(set.getKey())){
                    indexList.remove(set.getKey());
                }
        }
        return indexList;
    }

    public void addYellowLetter(Integer index, Character letter){
        this.yellowLetters.add(letter);
        this.yellowSpaces.add(index);
    }

    public List<Character> getYellowLetters(){
        return this.yellowLetters;
    }

    public List<Integer> getYellowSpaces(){
        return this.yellowSpaces;
    }

    public void addGrayLetter(String letter){
        this.grayLetters += letter;
    }

    public String getGrayLetter(){
        return this.grayLetters;
    }

    //if the word contains any of the gray letters, it returns false
    public boolean filterGrayLetters(String word) {
        if(this.grayLetters != null){
            for(Character character : this.grayLetters.toCharArray())
                if(word.contains(character.toString())){
                    return false;
                }
        }
        return true;
    }

    //if the word contains the letter at a specified index, returns true
    public boolean filterGreenLetters(String word) {
        if(this.greenLetters != null){

            int greenFound = 0;
            int greenTotal = this.greenLetters.size();

            for(Map.Entry<Integer, Character> set : this.greenLetters.entrySet())

                if(word.charAt(set.getKey() - 1) == (set.getValue())){
                    greenFound++;
                }

            if (greenFound == greenTotal) {
                return true;
            }
        }
        return false;
    }

    public List<String> filterYellowLetter(List<String> wordList, Character ch, Integer index) {

        List<String> filteredList = new ArrayList<>();
        List<Integer> possibleIndexes = getNonGreenIndex();
        List<Integer> cloneIndex = new ArrayList<>();
        cloneIndex.addAll(possibleIndexes);        
        if(cloneIndex.contains(index)){
            cloneIndex.remove(index);
        }

        for (String word : wordList) {
            boolean shouldAdd = false;

            if(word.contains(ch.toString())){
                if(word.charAt(index - 1) == ch){
                    shouldAdd = false;
                }
                else if(word.charAt(index - 1) != ch){
                    shouldAdd = true;
                }
            }
            
            if(shouldAdd == true){
                filteredList.add(word);
            }
        }
        return filteredList;
    }
    
    public List<String> findPossibleWords(List<String> dictionary) {
        List<String> possibleWords = new ArrayList<>();
        for (String word : dictionary) {
            if (filterGrayLetters(word) == true && filterGreenLetters(word) == true) {
                possibleWords.add(word);
            }
        }
        if(this.yellowLetters != null){
            for (int i = 0; i < yellowLetters.size(); i++) {
                possibleWords = filterYellowLetter(possibleWords, yellowLetters.get(i), yellowSpaces.get(i));
            }
        }
        return possibleWords;
    }

    //takes dictionary.txt file and returns it as a List of Strings
    public List<String> loadDictionary(String fileName) {
        List<String> dictionary = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }
}

