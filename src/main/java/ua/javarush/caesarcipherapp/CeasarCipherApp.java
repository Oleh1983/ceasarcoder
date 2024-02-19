package ua.javarush.caesarcipherapp;

import ua.javarush.caesarcipherapp.controller.ApplicationController;
import ua.javarush.caesarcipherapp.dao.FileAccess;
import ua.javarush.caesarcipherapp.service.CodeHandler;
import ua.javarush.caesarcipherapp.view.ConsoleViewProvider;

class CeaserCipherApp{


    public static void main(String[] args) {

        FileAccess fileAccess = new FileAccess(args[1]);
        CodeHandler codeHandler = new CodeHandler(fileAccess);
        ConsoleViewProvider consoleViewProvider = new ConsoleViewProvider();
        new ApplicationController(codeHandler, consoleViewProvider).run(args);

    }
}