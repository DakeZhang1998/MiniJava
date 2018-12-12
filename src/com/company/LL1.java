package com.company;

import javafx.stage.StageStyle;

import java.lang.reflect.Array;
import java.util.*;

public class LL1 {
    public static void main(String[] args) {
        ll1Table getTable = new ll1Table();
        getTable.generateTable();
    }
}

class ll1Table {
//    String[] productions = {"E -> e"};
    String[] productions = {
            "Program -> ClassDecl Program",
            "Program -> e",
            "ClassDecl -> \"class\" <ID> \"extends\" <ID> \"{\" VarMethodBlock \"}\"",
            "ClassDecl -> \"class\" <ID> \"{\" VarMethodBlock \"}\"",
            "VarMethodBlock -> VarDecl VarMethodBlock",
            "VarMethodBlock -> MethodDecl VarMethodBlock",
            "VarMethodBlock -> e",
            "VarStateBlock -> VarDecl VarStateBlock",
            "VarStateBlock -> Statement VarStateBlock",
            "VarStateBlock -> e",
            "MethodDecl -> \"public\" Type <ID> \"(\" Formals \")\" \"{\" VarStateBlock \"}\"",
            "MethodDecl -> \"public\" \"void\" <ID> \"(\" Formals \")\" \"{\" VarStateBlock \"}\"",
            "MethodDecl -> \"public\" \"static\" \"void\" \"main\" \"(\" \"String\" \"[\" \"]\" \")\" \"{\" VarStateBlock \"}\"",
            "Formals -> Type <ID> Formals'",
            "Formals -> e",
            "Formals' -> \",\" Type <ID>",
            "Formals' -> e",
            "VarDecl -> Type <ID> \";\"",
            "VarDecl -> Type <ID> \"=\" FullExpr \";\"",
            "Type -> ElmType",
            "Type -> ElmType \"[\" \"]\"",
            "ElmType -> \"boolean\"",
            "ElmType -> \"int\"",
            "ElmType -> <ID>",
            "ElmType -> \"double\"",
            "ElmType -> \"String\"",
            "StateBlock -> \"{\" Statements \"}\"",
            "StateBlock -> Statement",
            "Statements -> Statement Statements",
            "Statements -> e",
            "Statement -> LValue \"=\" FullExpr \";\"",
            "Statement -> LValue \"(\" Params \")\" \";\"",
            "Statement -> \"if\" \"(\" Expr \")\" StateBlock",
            "Statement -> \"if\" \"(\" Expr \")\" StateBlock \"else\" StateBlock",
            "Statement -> \"while\" \"(\" Expr \")\" StateBlock",
            "Statement -> \"println\" \"(\" PrintParams \")\" \";\"",
            "Statement -> \"return\" Expr \";\"",
            "Params -> Expr Params'",
            "Params -> e",
            "Params' -> \",\" Expr Params'",
            "Params' -> e",
            "PrintParams -> Expr",
            "PrintParams -> <STR>",
            "FullExpr -> \"new\" ElmType \"[\" <INT> \"]\"",
            "FullExpr -> \"new\" <ID> \"(\" Params \")\"",
            "FullExpr -> Expr",
            "Binop -> \"+\"",
            "Binop -> \"-\"",
            "Binop -> \"*\"",
            "Binop -> \"/\"",
            "Binop -> \"&&\"",
            "Binop -> \"||\"",
            "Binop -> \"==\"",
            "Binop -> \"!=\"",
            "Binop -> \"<\"",
            "Binop -> \"<=\"",
            "Binop -> \">\"",
            "Binop -> \">=\"",
            "Unop -> \"-\"",
            "Unop -> \"!\"",
            "Expr -> \"(\" Expr \")\" Expr'",
            "Expr -> Unop Expr Expr'",
            "Expr -> LValue \"(\" Params \")\" Expr'",
            "Expr -> LValue Expr'",
            "Expr -> Literal Expr'",
            "Expr -> e",
            "Expr' -> Binop Expr Expr'",
            "Expr' -> Binop Expr Expr'",
            "Expr' -> e",
            "LValue -> \"this\" \".\" <ID> Deref'",
            "LValue -> <ID> Deref'",
            "LValue -> \"this\" \".\" <ID>",
            "LValue -> <ID>",
            "Deref' -> Deref Deref'",
            "Deref' -> e",
            "Deref -> \"[\" Expr \"]\"",
            "Deref -> \".\" <ID>",
            "Literal -> <INT>",
            "Literal -> <DOUBLE>",
            "Literal -> <STR>",
            "Literal -> \"true\"",
            "Literal -> \"false\""
    };
    List<String> productionList = Arrays.asList(productions);
    ArrayList<String> nonterminators = new ArrayList<String>();
    ArrayList<String> initTerminators = new ArrayList<String>();


    ArrayList<String> terminators = new ArrayList<String>();
    HashMap<String, ArrayList<String>> firstSet = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> firstSetX = new HashMap<String, ArrayList<String>>();
    // E T F : i ( | K : + $ |M : * $
    String S = "Program";
    HashMap<String, ArrayList<String>> followSet = new HashMap<String, ArrayList<String>>();
    // E K: ) $ | T M : + ) $ | F : + * ) $
    HashMap<String, ArrayList<String>> productionSet = new HashMap<String, ArrayList<String>>();
    // e.g. StateBlock=["{" Statements "}", Statement]
    String[][] table;

    void generateTable() {
        initTerminators.addAll(Lexer.keywords);
        initTerminators.addAll(Lexer.operatorAndDelimiters);

        init();

        int rowCount = terminators.size() + 1;
        int colCount = nonterminators.size() + 1;
        table = new String[rowCount][colCount];
        table[0][0] = "Vt\\Vn";

        for (int i = 0; i < rowCount - 1; i++) {
                table[i + 1][0] = (terminators.get(i).equals("e")) ? "$" : terminators.get(i);
        }

        for (int i = 0; i < colCount - 1; i++)
            table[0][i + 1] = nonterminators.get(i);
        for (int i = 0; i < rowCount - 1; i++)
            for (int j = 0; j < colCount - 1; j++)
                table[i + 1][j + 1] = "error";

        for (String A: nonterminators) {
            ArrayList<String> l = productionSet.get(A);
            for (String s: l) {
                int productionNum = productionList.indexOf(A + " -> " + s);
                ArrayList<String> set = firstSetX.get(s);
                for (String a: set)
                    insert(A, a, productionNum);
                if(set.contains("e"))  {
                    ArrayList<String> setFollow = followSet.get(A);
                    if(setFollow.contains('$'))
                        insert(A, "$", productionNum);
                    for (String b : setFollow)
                        insert(A, b, productionNum);
                }
            }
        }


        for (int i = 0; i < rowCount - 1; i++) {
            String str = terminators.get(i);
            if (!str.equals("e"))
                str = str.substring(1, str.length() - 1);
            int num = initTerminators.indexOf(str) + 1;
            if (num == 0) {
                table[i + 1][0] = (terminators.get(i).equals("e")) ? "$" : terminators.get(i);
            }
            else {
                table[i + 1][0] = (str.equals("e")) ? "$" : str + "(" + num + ")";
            }

        }
        // Print the table in two parts.
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length / 2; j++) {
                System.out.print(String.format("%-17s", table[i][j]));
            }
            System.out.println();
        }
        System.out.println("\n-----------------------------------------------------------------------------------------------------------------------\n");
        for (int i = 0; i < table.length; i++) {
            System.out.print(String.format("%-15s", table[i][0]));
            for (int j = table[i].length / 2; j < table[i].length; j++) {
                System.out.print(String.format("%-15s", table[i][j]));
            }
            System.out.println();
        }

    }

    void init() {
        for (String production: productions) {
            String[] temp = production.split(" -> ");
            ArrayList<String> rightParts = productionSet.containsKey(temp[0]) ? productionSet.get(temp[0]) : new ArrayList<String>();
            rightParts.add(temp[1]);
            productionSet.put(temp[0], rightParts);
            if (nonterminators.indexOf(temp[0]) == -1)
                nonterminators.add(temp[0]);

        }
        for (String str: productions) {
            String[] temp = str.split(" -> ");
            String[] temp2 = temp[1].split(" ");
            for (String str2: temp2) {
                if (terminators.indexOf(str2) == -1 && nonterminators.indexOf(str2) == -1) {
                    terminators.add(str2);
                }
            }
        }

        for (String nonterminator: nonterminators)
            getFirst(nonterminator);

        for (String terminator: terminators) {
            if (!firstSet.containsKey(terminator)) {
                ArrayList<String> eValue = new ArrayList<String>();
                eValue.add(terminator);
                firstSet.put(terminator, eValue);
            }
        }

        for (String nonterminator: nonterminators) {
            ArrayList<String> rightParts = productionSet.get(nonterminator);
            for (String rightPart: rightParts)
                getFirstX(rightPart);
        }

        getFollow(S);
        for (String nonterminator: nonterminators) {
            getFollow(nonterminator);
        }

//        Iterator iter = followSet.keySet().iterator();
//        while (iter.hasNext()) {
//            String key = String.valueOf(iter.next());
//            ArrayList<String> val = followSet.get(key);
//            System.out.print(key + "\t" + val.toString() + "\n");
//        }
    }

    void getFirst(String symbol) {
        ArrayList<String> result;
        // For terminators, the first set is itself.
        if (terminators.indexOf(symbol) != -1) {
            if (!firstSet.containsKey(symbol)) {
                result = new ArrayList<String>();
                result.add(symbol);
                firstSet.put(symbol, result);
            }
            return;
        }

        // For non-terminators, each production will be executed.
        ArrayList<String> rightParts = productionSet.get(symbol);
        result = firstSet.containsKey(symbol) ? firstSet.get(symbol) : new ArrayList<String>();

        for (String rightPart: rightParts) {
            // X -> e Just add it.
            if (rightPart.equals("e")) {
                if (!result.contains("e"))
                    result.add("e");
            }
            // X -> Y1Y2Y3
            else {
                String[] tokens;
                if (rightPart.indexOf(" ") != -1) {
                    tokens = rightPart.split(" ");
                }
                else {
                    tokens = new String[1];
                    tokens[0] = rightPart;
                }

                int i = 0;
                while (i < tokens.length) {
                    String tn = tokens[i];
                    getFirst(tn);
                    ArrayList<String> temp = firstSet.get(tn);
                    // Add its first set.
                    for (String str: temp)
                        if (!result.contains(str))
                            result.add(str);
                    // If its first set contains e, we use the next symbol.
                    if (temp.indexOf("e") != -1)
                        i++;
                        // Otherwise, we will move to the next production.
                    else
                        break;
                }
            }
        }
        firstSet.put(symbol, result);
    }

    void getFirstX(String str) {
        ArrayList<String> result = (firstSetX.containsKey(str))? firstSetX.get(str) : new ArrayList<String>();
        String[] tokens;
        if (str.indexOf(" ") != -1) {
            tokens = str.split(" ");
        }
        else {
            tokens = new String[1];
            tokens[0] = str;
        }

        int i = 0;
        while (i < tokens.length) {
            String tn = tokens[i];
            ArrayList<String> tvSet = firstSet.get(tn);
            // Add the first set except e.
            for (String temp: tvSet)
                if(!temp.equals("e") && result.indexOf(temp) == -1)
                    result.add(temp);
            // If its first set contains e, we use the next symbol.
            if (tvSet.contains("e"))
                i++;
                // Otherwise, we will move to the next production.
            else
                break;
            // Since the first set of every symbol contains e, we add e.
            if (i == tokens.length && result.indexOf("e") == -1) {
                result.add("e");
            }
        }
        firstSetX.put(str, result);
    }

    void getFollow(String nonterminator) {
        ArrayList<String> rightParts = productionSet.get(nonterminator);
        ArrayList<String> result = followSet.containsKey(nonterminator) ? followSet.get(nonterminator) : new ArrayList<String>();

        // If it is the beginning symbol, we add "$".
        if (nonterminator.equals(S)) {
            if (result.indexOf("$") == -1)
                result.add("$");
        }

        // Search every production to find the terminator following the current non-terminator.
        for (String str: nonterminators) {
            ArrayList<String> temp = productionSet.get(str);
            String[] tokens;
            for (String s: temp) {
                if (s.indexOf(" ") != -1) {
                    tokens = s.split(" ");
                } else {
                    tokens = new String[1];
                    tokens[0] = s;
                }
                for (int i = 0; i < tokens.length; i++)
                    if (tokens[i].equals(str) && i + 1 < tokens.length && terminators.indexOf(tokens[i + 1]) != -1
                            && result.indexOf(tokens[i + 1]) == -1)
                        result.add(tokens[i + 1]);
            }
        }
        followSet.put(nonterminator, result);

        // Process each production of X.
        for (String rightOne: rightParts) {
            String[] tokens;
            if (rightOne.indexOf(" ") != -1) {
                tokens = rightOne.split(" ");
            } else {
                tokens = new String[1];
                tokens[0] = rightOne;
            }
            int i = tokens.length - 1;
            while (i >= 0) {
                String tn = tokens[i];
                // In this case, we only care about the non-terminators.
                if(nonterminators.indexOf(tn) != -1) {
                    // A->αBβ
                    // If β doesn't exist, we add follow(A) into follow(B)
                    // If β exists, we add first(β) except e to follow(B).
                    // If β exists and first(β) contains e, we add follow(A) into follow(B).

                    // If β exists
                    if (tokens.length - i - 1 > 0) {
                        String right = "";
                        for (int j = i + 1; j < tokens.length; j++) {
                            right = right + tokens[j] + " ";
                        }
                        right = right.substring(0, right.length() - 1);

                        // Add first(β) except e into follow(B).
                        ArrayList<String> first = null;
                        if(!right.contains(" ") && firstSet.containsKey(right))
                            first = firstSet.get(right);
                        else {
                            if(!firstSetX.containsKey(right)){
                                ArrayList<String> set = new ArrayList<String>();
                                firstSetX.put(right, set);
                                getFirstX(right);
                            }
                            first = firstSetX.get(right);
                        }

                        ArrayList<String> setB = followSet.containsKey(tn) ? followSet.get(tn) : new ArrayList<String>();
                        for (String str: first)
                            if (!str.equals("e") && !setB.contains(str))
                                setB.add(str);
                        followSet.put(tn, setB);

                        // If first(β) contains e, we add follow(A) into follow(B).
                        if(first.contains("e")){
                            if(!tn.equals(nonterminator)){
                                setB = followSet.containsKey(tn) ? followSet.get(tn) : new ArrayList<String>();
                                for (String str: result)
                                    if (!setB.contains(str))
                                        setB.add(str);
                                followSet.put(tn, setB);
                            }
                        }
                    }
                    // If β doesn't exist, we add follow(A) into follow(B).
                    else{
                        // Only when A and B are not equal.
                        if(!tn.equals(nonterminator)){
                            ArrayList<String> setB = followSet.containsKey(tn) ? followSet.get(tn) : new ArrayList<String>();
                            for (String str: result)
                                if (!setB.contains(str))
                                    setB.add(str);
                            followSet.put(tn, setB);
                        }
                    }
                    i--;
                }
                // If the current token is a terminator, then we move forward,
                // e.g. A->aaaBCDaaaa, where β is CDaaaa
                else i--;

            }
        }
    }

    void insert(String nonterminator, String terminator, int productionNum) {
        productionNum++;
        if (nonterminator.equals("e"))
            nonterminator = "$";
        for (int i = 0; i < terminators.size() + 1; i++) {
            if (table[i][0].equals(terminator))
                for (int j = 0; j < nonterminators.size() + 1; j++) {
                    if (table[0][j].equals(nonterminator)){
                        if (table[i][j].equals("error"))
                            table[i][j] = String.valueOf(productionNum);
                        else {
                            if (!table[i][j].equals(String.valueOf(productionNum))) {
                                table[i][j] += " / ";
                                table[i][j] += String.valueOf(productionNum);
                            }
                        }
                        return;
                    }
                }
        }
    }
}