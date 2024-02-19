package ua.javarush.caesarcipherapp.service;

import ua.javarush.caesarcipherapp.dao.FileAccess;
import ua.javarush.caesarcipherapp.view.ConsoleViewProvider;

import java.util.*;

public class CodeHandler {
    ConsoleViewProvider consoleViewProvider = new ConsoleViewProvider();
    private static final String ALPHABET = "C:/workspace/ceasarcoder/src/main/resources/alphabet.txt";
    FileAccess fileAlphabetAccess = new FileAccess(ALPHABET);
    private final FileAccess fileAccess;

    public CodeHandler(FileAccess fileAccess) {
        this.fileAccess = fileAccess;
    }

    public void encode(List<Character> inputText, int key) {
        fileAccess.fileCreating(textTransformation(inputText, key), "encoded");
    }

    public void decode(List<Character> inputText, int key) {
        fileAccess.fileCreating(textTransformation(inputText, checkKey(key)), "decoded");
    }

    public void handleKeys(List<Character> inputText) {
        int key = checkKey(searchKeys(inputText));
        fileAccess.fileCreating(textTransformation(inputText, key), "searched");
        consoleViewProvider.print("File decoded. Key is " + (key + 1));
    }
    private int checkKey(int key){
        if (key % 2 != 0) {
            key = key + 1;
        }
        if (key > 0) {
            key = key * -1;
        }
        return key;
    }

    private int searchKeys(List<Character> inputText) {

        List<Character> inputAlphabet = fileAlphabetAccess.dataReading();

        List<Character> mostPopularLetters = new ArrayList<>();
        mostPopularLetters.add('e');
        mostPopularLetters.add('t');
        LinkedHashMap<Character, Integer> frequencySymbols = getRatingSymbols(inputText);
        int foundFirstKey = 0;
        int foundSecondKey = 0;
        int largest = 0;
        int secondLargest = 0;
        int totalKey = 0;

        for (Map.Entry<Character, Integer> entry : frequencySymbols.entrySet()) {
            Integer value = entry.getValue();
            if (value > largest) {
                secondLargest = largest;
                largest = value;
            } else if (value > secondLargest) {
                secondLargest = value;
            }
        }
        for (Map.Entry<Character, Integer> entry : frequencySymbols.entrySet()) {
            if (inputAlphabet.contains(entry.getKey()) && entry.getValue().equals(largest)) {
                foundFirstKey = inputAlphabet.indexOf(entry.getKey()) - inputAlphabet.indexOf(mostPopularLetters.get(0));
            }
            if (inputAlphabet.contains(entry.getKey()) && entry.getValue().equals(secondLargest) && Character.isLowerCase(entry.getKey())) {
                foundSecondKey = inputAlphabet.indexOf(entry.getKey()) - inputAlphabet.indexOf(mostPopularLetters.get(1));
            }
        }
        if (foundFirstKey < 0) {
            foundFirstKey = foundFirstKey * -1;
        } else if (foundSecondKey < 0) {
            foundSecondKey = foundSecondKey * -1;
        }
        foundFirstKey = foundFirstKey - 1;
        foundSecondKey = foundSecondKey - 1;
        if (foundFirstKey == foundSecondKey) {
            totalKey = foundFirstKey;
        }
        return totalKey;
    }

    private LinkedHashMap<Character, Integer> getRatingSymbols(List<Character> inputText) {
        LinkedHashMap<Character, Integer> frequencySymbols = new LinkedHashMap<>();
        for (Character character : inputText) {
            if (Character.isLetter(character) && frequencySymbols.containsKey(Character.toLowerCase(character))) {
                frequencySymbols.put(Character.toLowerCase(character), frequencySymbols.get(Character.toLowerCase(character)) + 1);
            } else if (Character.isLetter(character)) {
                frequencySymbols.put(Character.toLowerCase(character), 1);
            }
        }
        List<Map.Entry<Character, Integer>> entryList = new ArrayList<>(frequencySymbols.entrySet());

        entryList.sort(Map.Entry.comparingByValue());
        LinkedHashMap<Character, Integer> sortedFrequencySymbols = new LinkedHashMap<>();

        for (Map.Entry<Character, Integer> entry : entryList) {
            sortedFrequencySymbols.put(entry.getKey(), entry.getValue());
        }
        return sortedFrequencySymbols;
    }

    private List<Character> textTransformation(List<Character> inputText, int key) {
        List<Character> inputAlphabet = fileAlphabetAccess.dataReading();
        List<Character> outputData = new ArrayList<>();
        for (Character eachChar : inputText) {
            if (inputAlphabet.contains(eachChar)) {
                int shiftedIndex = (inputAlphabet.indexOf(eachChar) + key + inputAlphabet.size()) % inputAlphabet.size();
                if (Character.isUpperCase(eachChar)) {
                    outputData.add(Character.toUpperCase(inputAlphabet.get((shiftedIndex + 1) % inputAlphabet.size())));
                } else if (Character.isLowerCase(eachChar)) {
                    outputData.add(Character.toLowerCase(inputAlphabet.get(shiftedIndex)));
                }
            } else {
                outputData.add(eachChar);
            }
        }
        return outputData;
    }
}
