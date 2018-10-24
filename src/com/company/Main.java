package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        new Main().run();
    }

    public void run() throws Exception{
        char[] fileContent = Util.openFile("test.txt");
        char[] fileContentRefined = Util.deleteUnusedChars(fileContent);
        //System.out.println(fileContentRefined);
        //fileContent = Util.deleteUnusedChars();

        /*
         我是一条测试文本
         */
         Lexer lexer = new Lexer(fileContentRefined);
        lexer.lex();
        for(Token token:lexer.tokens){
            System.out.println(token);
        }


    }
}
