package ua.javarush.caesarcipherapp.controller;

import ua.javarush.caesarcipherapp.dao.FileAccess;
import ua.javarush.caesarcipherapp.service.CodeOperations;
import ua.javarush.caesarcipherapp.service.CodeHandler;
import ua.javarush.caesarcipherapp.view.ConsoleViewProvider;

import java.util.List;

public class ApplicationController {

    private final CodeHandler codeHandler;
    private final ConsoleViewProvider consoleViewProvider;
    private static final String MESSAGE = "not valid number of args";

    public ApplicationController(CodeHandler codeHandler, ConsoleViewProvider consoleViewProvider) {
        this.codeHandler = codeHandler;
        this.consoleViewProvider = consoleViewProvider;
    }

    public void run(String[] args) {
        FileAccess fileAccess = new FileAccess(args[1]);
        validateParameters(args);
        consoleViewProvider.print("Parameters are correct");
        consoleViewProvider.print("Start process");

        CodeOperations typeOperation = CodeOperations.valueOf(args[0]);
        List<Character> text = fileAccess.dataReading();
        int key = (args.length == 3) ? Integer.parseInt(args[2]) : 0;
        switch (typeOperation) {
            case ENCODE: {
                codeHandler.encode(text, key);
                break;
            }
            case DECODE: {
                codeHandler.decode(text, key);
                break;
            }
            case KEY_SEARCH: {
                codeHandler.handleKeys(text);
                break;
            }

        }
    }

    private void validateParameters(String[] args) {
        if (isValidNumberOfParameters(args)) {
            throw new IllegalArgumentException(MESSAGE);
        }
        String action = args[0];
        CodeOperations typeOperation = CodeOperations.valueOf(action);
        if (typeOperation == CodeOperations.ENCODE || typeOperation == CodeOperations.DECODE && args.length == 3) {
            consoleViewProvider.print("args.length = 3");
        } else if (typeOperation == CodeOperations.KEY_SEARCH && args.length == 2) {
            consoleViewProvider.print("args.length = 2");
        } else {
            throw new IllegalArgumentException(MESSAGE);
        }
    }

    private static boolean isValidNumberOfParameters(String[] args) {
        return args.length < 2 || args.length > 3;
    }
}