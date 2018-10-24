package com.company;
public class Token {
    int tokenNum;
    String token;
    int lineNum;

    public Token(int tokenNum, String token, int lineNum) {
        this.tokenNum = tokenNum;
        this.token = token;
        this.lineNum = lineNum;
    }

    @Override
    public String toString(){
        String result = "";
        result = "tokenType: "+tokenNum + "\t|\ttoken: "+token + "\t\t|\tline number: " + lineNum;
        return String.format("Token Type: %-3d   |   Token: %-20s   |   Line Number: %d", tokenNum, token, lineNum);
    }
}
