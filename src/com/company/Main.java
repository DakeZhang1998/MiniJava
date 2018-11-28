package com.company;

import java.util.ArrayList;
import java.util.TreeSet;

public class Main {
    /*
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
        char[] fileContent = Util.openFile("test.txt");
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


    }
    */
    public static void main(String[] args) throws Exception {
        // // LL（1）文法产生集合
        ArrayList<String> gsArray = new ArrayList<String>();
        // // Vn非终结符集合
        // TreeSet<Character> nvSet = new TreeSet<Character>();
        // // Vt终结符集合
        // TreeSet<Character> ntSet = new TreeSet<Character>();
        Gs gs = new Gs();
        initGs(gsArray);
        gs.setGsArray(gsArray);
        // getNvNt(gsArray, gs.getNvSet(), gs.getNtSet());
        gs.getNvNt();
        gs.initExpressionMaps();
        gs.getFirst();
        // 设置开始符
        gs.setS('E');
        gs.getFollow();
        gs.getSelect();
        // 创建一个分析器
        Analyzer analyzer = new Analyzer();
        analyzer.setStartChar('E');
        analyzer.setLl1Gs(gs);
        analyzer.setStr("i+i*i#");
        analyzer.analyze();
        gs.genAnalyzeTable();
        System.out.println("");
    }

    /**
     * 初始化LL(1)文法
     *
     * @param gsArray
     */
    private static void initGs(ArrayList<String> gsArray) {
        gsArray.add("D->*FD");
        gsArray.add("D->e");
        gsArray.add("T->FD");
        gsArray.add("E->TC");
        gsArray.add("F->(E)");
        gsArray.add("F->i");
        gsArray.add("C->+TC");
        gsArray.add("C->e");
    }

}
