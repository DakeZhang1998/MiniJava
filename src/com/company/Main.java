package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class Main {
    static boolean isFirst = true;
    private PaintTree frame;

    public static void main(String[] args){
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
        lexer.tokens.add(new Token(49, "EOF", lexer.tokens.get(lexer.tokens.size() - 1).lineNum + 1));
        for(Token token:lexer.tokens){
            System.out.println(token);
            if(token.tokenNum==20){
                System.out.println(token.token);
            }
        }

        Parser parser = new Parser(lexer.tokens);
        TreeNode root = parser.parse();

        if (isFirst) {
            frame = new PaintTree(root);
            frame.setSize(800, 600);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            isFirst = false;
        }
        else {
            frame.reprint(root);
        }

        System.out.println("hello world!");

    }


}
