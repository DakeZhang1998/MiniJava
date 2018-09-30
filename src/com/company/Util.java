package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Util {
    /*
     * 该类用于构建一些工具类的静态方法，比如打开文件，处理文件中的无用字符等等
     */

    public static char[] openFile(String fileName) {
        char[] chars = new char[1000];
        String inputContent = "";

        try {
            String pathname = "./codes/" + fileName;
            File f = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            inputContent += line;
            while (line != null) {
                line = br.readLine();
                if (line != null)
                    inputContent += line + "\n";
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail to open source code file.");
        }

        //System.out.println(inputContent);

        return inputContent.toCharArray();
    }


    // 去除无用字符
    public static char[] deleteUnusedChars(char[] inputChars) {
        String inputStr = String.valueOf(inputChars);

//        String[] lines = inputStr.split("\n");
//        ArrayList<String> seperateLines = new ArrayList<String>();
//        Collections.addAll(seperateLines, lines);
//
//        for (String str: seperateLines) {
//            System.out.println(str);
//        }

        List<Character> inputList = new ArrayList<Character>();
        for (Character ch: inputChars) {
            inputList.add(ch);
        }

        char currChar = ' ';
        for (int i = 0; i < inputList.size(); i++) {
            currChar = inputList.get(i);
            if (currChar == '/') {
                if (i < inputList.size() - 1) {
                    currChar = inputList.get(i+1);
                    if (currChar == '/') {
                        while (i < inputList.size() && inputList.get(i) != '\n')
                            inputList.remove(i);
                        i--;
                    }
                    else if (currChar == '*') {
                        char last = ' ';
                        char current = currChar;
                        while (i < inputList.size() - 1 && !(last == '*' && current == '/')) {
                            last = current;
                            current = inputList.get(i+1);
                            if (last != '\n')
                                inputList.remove(i);
                            else
                                i++;
                        }
                        inputList.remove(i);
                    }
                }
            }
        }

        for (Character ch: inputList) {
            System.out.print(ch);
        }


        char[] result = new char[10];
        return result;
    }

    public static void main(String args[]) {
        openFile("test.txt");
    }
}
