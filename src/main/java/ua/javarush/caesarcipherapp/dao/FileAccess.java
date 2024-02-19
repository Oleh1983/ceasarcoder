package ua.javarush.caesarcipherapp.dao;


import ua.javarush.caesarcipherapp.view.ConsoleViewProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private final String incomingPath;

    ConsoleViewProvider consoleViewProvider = new ConsoleViewProvider();

    public FileAccess(String incomingPath) {
        this.incomingPath = incomingPath;
    }

    public List<Character> dataReading() {
        List<Character> allChars = new ArrayList<>();
        try (BufferedReader bfReader = new BufferedReader(new FileReader(incomingPath))) {
            while (bfReader.ready()) {
                allChars.add((char) bfReader.read());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return allChars;
    }

    public void fileCreating(List<Character> list, String name) {
        Path filePath = Paths.get(incomingPath);
        String fileName = filePath.getFileName().toString();
        String newFileName = name + fileName;

        Path parentDirectory = filePath.getParent();
        Path newFilePath = parentDirectory.resolve(newFileName);
        try {
            if (Files.exists(newFilePath)) {
                Files.delete(newFilePath);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (!Files.exists(newFilePath.getFileName()) || Files.isRegularFile(newFilePath)) {
            try {
                Files.createFile(newFilePath);
                dataWriting(list, newFilePath);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        consoleViewProvider.print("File is created");
    }

    private void dataWriting(List<Character> list, Path newFilePath) {
        try (BufferedWriter bfWriter = new BufferedWriter(new FileWriter(String.valueOf(newFilePath)))) {

            for (char letter : list) {
                bfWriter.write(letter);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
