package com.company;

import java.util.ArrayList;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        try {
            new Main().run();
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
    public void run() throws Exception{
        char[] fileContent = Util.openFile("simple.txt");
        char[] fileContentRefined = Util.deleteUnusedChars(fileContent);
        //System.out.println(fileContentRefined);
        //fileContent = Util.deleteUnusedChars();
        Lexer lexer = new Lexer(fileContentRefined);
        lexer.lex();
        for(Token token:lexer.tokens){
            System.out.println(token);
            if(token.tokenNum==20){
                System.out.println(token.token);
            }
        }

        Parser parser = new Parser(lexer.tokens);
        TreeNode root = parser.parse();
        System.out.println("hello world!");

    }


}
