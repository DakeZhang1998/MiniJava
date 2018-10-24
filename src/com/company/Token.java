package com.company;
public class Token {
    int tokenNum;
    String token;

    public Token(int tokenNum, String token) {
        this.tokenNum = tokenNum;
        this.token = token;
    }

    @Override
    public String toString(){
        String result = "";
        result = "tokenType: "+tokenNum + " token: "+token;
        return result;
    }
}
