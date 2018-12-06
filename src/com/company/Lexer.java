package com.company;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
    public static String[] keywordsString = {"boolean", "class", "else", "extends", "false", "if", "int", "length", "main",
            "new", "public", "return", "static", "this", "true", "void", "while", "String", "println","double","int"};
    public static String[] OperatorAndDelimitersString = {"=", "+", "-", "*", "/", "&&", "||", "!", "==", "!=", "<", "<=", ">", ">=", ";", ",", ".", "(", ")",
            "[", "]", "{", "}"};
    public static List<String> keywords = Arrays.asList(keywordsString);
    public static List<String> operatorAndDelimiters = Arrays.asList(OperatorAndDelimitersString);
    ArrayList<Token> tokens;
    char[] chars;
    static int curIndex = 0;
    static int lastIndex = -1;
    static int lineNum = 1;

    public Lexer(char[] chars) {
        this.tokens = new ArrayList<>();
        this.chars = chars;
    }

    public void lex() throws Exception {
        while(curIndex<chars.length){
            char c = chars[curIndex];
            if(('a'<=c&&c<='z')||('A'<=c&&c<='Z')||c=='_'||c=='$'){
                Token token = isIdentifier();
                tokens.add(token);
            }
            else if('0'<=c&&c<='9'){
                //如果为-，可能返回一个operator
                Token token = isIntegerOrDouble();
                tokens.add(token);
            }
            else if(operatorAndDelimiters.contains(String.valueOf(c))||c=='&'||c=='|'){
                Token token = isOperatorOrDelimiter();
                tokens.add(token);
            }
            else if(c==13||c==10||c==32||c==9){
                lastIndex++;
                readNextChar();
            }
            else if(c=='\"'){
                Token token = isString();
                tokens.add(token);
            }
            else{
                System.out.println(curIndex);
                throw new Exception("Line " + lineNum + ": invalid character");
            }
        }
    }

    public boolean readNextChar() {
        if (curIndex < chars.length) {
            if(chars[curIndex] == '\n')
                lineNum++;
            curIndex++;
            if (curIndex == chars.length)
                return false;
            return true;
        }
        return false;
    }

    public boolean rollBackChar() {
        if (curIndex < 0)
            return false;
        curIndex--;
        if (chars[curIndex] == '\n')
            lineNum--;
        return true;
    }

    public Token isIntegerOrDouble() throws Exception{
        char c = chars[curIndex];
        boolean isDouble = false;
        Token tokenInformation = null;

        if ('0' <= c && c <= '9') {
            if (c == '0') {
                if (readNextChar()) {
                    c = chars[curIndex];
                    if (c == '.')
                        rollBackChar();
                    else {
                        if(chars[curIndex+1]>='0'&&chars[curIndex+1]<='9')
                            throw new Exception("Line " + lineNum + ": invalid interger");
                        tokenInformation = new Token(46, "0", lineNum);
                        lastIndex++;
                        return tokenInformation;
                    }
                }
                else {
                    tokenInformation = new Token(46, "0", lineNum);
                    lastIndex++;
                    return tokenInformation;
                }
            }
            if(!readNextChar()){
                String information = getString(lastIndex+1,curIndex-1);
                if (isDouble)
                    tokenInformation = new Token(47, information, lineNum);
                else
                    tokenInformation = new Token(46, information, lineNum);
                return tokenInformation;
            }
            else
                rollBackChar();
            c = chars[curIndex];
            int dotCount = 0;
            while (curIndex < chars.length && (('0' <= c && c <= '9') || c == '.')) {
                if (c == '.' && dotCount > 0) {
                    throw new Exception("Line " + lineNum + ": more than one dots");   // 错误：两个小数点
                }
                else if (c == '.') {
                    dotCount++;
                    isDouble = true;
                    readNextChar();
                    if (curIndex < chars.length)
                        c = chars[curIndex];
                }
                else {
                    readNextChar();
                    if (curIndex < chars.length)
                        c = chars[curIndex];
                }
            }
            if (('a' <= c && c <= 'z') || ('A'<= c && c <= 'Z') || c == '_') {
                // 注意：这里只考虑了数字里面不能存在字母的情况，其他特殊情况之后按需添加。
                throw new Exception("Line " + lineNum + ": invalid expression of numbers");
            }
            String information = getString(lastIndex+1,curIndex-1);
            if (isDouble)
                tokenInformation = new Token(47, information, lineNum);
            else
                tokenInformation = new Token(46, information, lineNum);

            lastIndex = curIndex-1;
            return tokenInformation;
        }
        else
            throw new Exception("Line " + lineNum + ": unknown error");
    }

    public Token formToken(String information, int tokenNum) {
        Token token = new Token(tokenNum, information, lineNum);
        if (token.tokenNum == 45 || token.tokenNum==48) {
            if (keywords.contains(information)) {
                token.tokenNum = keywords.indexOf(information) + 1;
            }
        }
        return token;
    }

    public Token isIdentifier() {
        if (!readNextChar()) {
            String information = getString(lastIndex + 1, curIndex - 1);
            return formToken(information, 48);
        }
        char c = chars[curIndex];
        while (curIndex < chars.length && ('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || ('A' <= c && c <= 'Z') || c == '_' || c == '$') {
            if (!readNextChar()) {
                String information = getString(lastIndex + 1, curIndex - 1);
                return formToken(information, 48);
            }
            c = chars[curIndex];
        }
        String information = getString(lastIndex + 1, curIndex - 1);
        Token token = formToken(information, 48);
        lastIndex = curIndex - 1;
        return token;
    }

    public String getString(int start, int end) {
        char[] chars_part = new char[end - start + 1];
        for (int i = 0; i <= end - start; i++) {
            chars_part[i] = chars[start + i];
        }
        return String.copyValueOf(chars_part);
    }

    public boolean isKeyword() {
        return false;
    }

    public Token isOperatorOrDelimiter() {
        char c = chars[curIndex];
        if(c=='&'||c=='|'||c=='='){
            if(readNextChar()){
                if(chars[curIndex]==c){
                    char[] operatorOrDelimiterArray={c,c};
                    String operatorOrDelimiter = String.valueOf(operatorOrDelimiterArray);
                    lastIndex+=2;
                    readNextChar();
                    return formToken(operatorOrDelimiter, operatorAndDelimiters.indexOf(operatorOrDelimiter) + 22);
                }
                else{
                    rollBackChar();
                    String operatorOrDelimiter = String.valueOf(c);
                    lastIndex++;
                    readNextChar();
                    return formToken(operatorOrDelimiter, operatorAndDelimiters.indexOf(operatorOrDelimiter) + 22);
                }
            }
            else{
                rollBackChar();

            }
        }
        else if(c=='!'||c=='<'||c=='>'){
            if(readNextChar()){
                char nextChar = chars[curIndex];
                if(nextChar=='='){
                    char[] operatorOrDelimiterArray={c,nextChar};
                    String operatorOrDelimiter = String.valueOf(operatorOrDelimiterArray);
                    lastIndex+=2;
                    readNextChar();
                    return formToken(operatorOrDelimiter, operatorAndDelimiters.indexOf(operatorOrDelimiter) + 22);
                }
                else{
                    rollBackChar();
                    String operatorOrDelimiter = String.valueOf(c);
                    lastIndex++;
                    readNextChar();
                    return formToken(operatorOrDelimiter, operatorAndDelimiters.indexOf(operatorOrDelimiter) + 22);
                }
            }
            else{
                rollBackChar();
            }
        }
        else{
            String operatorOrDelimiter = String.valueOf(c);
            lastIndex++;
            readNextChar();
            return formToken(operatorOrDelimiter, operatorAndDelimiters.indexOf(operatorOrDelimiter) + 22);
        }
        return null;
    }

    public Token isString() throws Exception {
        readNextChar();
        int lineError = lineNum;
        lastIndex++;
        while(chars[curIndex]!='\"'){
            if(!readNextChar()){
                throw new Exception("Line " + lineError +": unclosed double quotation mark");
            }
        }
        String result = getString(lastIndex+1,curIndex-1);
        readNextChar();
        lastIndex = curIndex-1;
        return formToken(result,45);
    }
}
