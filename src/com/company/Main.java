package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        new Main().run();
    }

    public void run() throws Exception{
        char[] fileContent = Util.openFile("test.txt");
        //fileContent = Util.deleteUnusedChars();
        Lexer lexer = new Lexer(fileContent);
        lexer.lex();
        for(Token token:lexer.tokens){
            System.out.println(token.token);
        }
    }
}
