package com.company;

import javafx.stage.StageStyle;

import java.util.*;

public class LL1 {
    public static void main(String[] args) {
        ll1Table getTable = new ll1Table();
        getTable.generateTable();
    }
}

class ll1Table {
//    String[] productions = {"E -> E or F", "F -> f"};
    String[] productions = {
            "Program -> ClassDecl Program", "Program -> e",
            "ClassDecl -> \"class\" <ID> \"extends\" <ID> \"{\" VarMethodBlock \"}\"",
            "ClassDecl -> \"class\" <ID> \"{\" VarMethodBlock \"}\"",
            "VarMethodBlock -> VarDel VarMethodBlock",
            "VarMethodBlock -> MethodDecl VarMethodBlock",
            "VarMethodBlock -> e",
            "VarStateBlock -> VarDel VarStateBlock",
            "VarStateBlock -> Statement VarStateBlcok",
            "VarStateBlock -> e",
            "MethodDecl -> \"public\" Type <ID> \"(\" Formals \")\" \"{\" VarStateBlock \"}\"",
            "MethodDecl -> \"public\" \"void\" <ID> \"(\" Formals \")\" \"{\" VarStateBlock \"}\"",
            "MethodDecl -> \"public\" \"static\" \"void\" \"main\" \"(\" \"String\" \"[\" \"]\" \")\" \"{\" VarStateBlock \"}\"",
            "Fomals -> Type <ID> Fomals'",
            "Fomals -> e",
            "Fomals' -> \",\" Type <ID>",
            "Fomals' -> e",
            "VarDecl -> Type <ID> \";\"",
            "VarDecl -> Type <ID> \"=\" FullExpr \";\"",
            "Type -> ElmType",
            "Type -> ElmType \"[\" \"]\"",
            "ElmType -> \"boolean\" | \"int\" | <ID> | \"doube\" | \"String\"",
            "StateBlock -> \"{\" Statements \"}\"",
            "StateBlock -> Statement",
            "Statements -> Statement Statements",
            "Statements -> e",
            "Statement -> LValue \"=\" FullExpr \";\"",
            "Statement -> LValue \"(\" Params \")\" \";\"",
            "Statement -> \"if\" \" (\" Expr \")\" StateBlock | \"if\" \"(\" Expr \")\" StateBolck \"else\" StateBolck",
            "Statement -> \"while\" \"(\" Expr \")\" StateBlock",
            "Statement -> \"println\" \"(\" PrintParams \")\" \";\"",
            "Statement -> \"return\" Expr \";\"",
            "Params -> Expr Params'",
            "Params -> e",
            "Params' -> \",\" Expr Params'",
            "Params' -> e",
            "PrintParams -> Expr | <STR>",
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
            "Expr' -> Binop Expr Expr'",
            "Expr' -> Binop Expr Expr'",
            "LValue -> \"this\" \".\" <ID> Deref'",
            "LValue -> <ID> Deref'",
//            "Deref' -> Deref Deref'",
//            "Deref -> \"[\" Expr \"]\"",
//            "Deref -> \".\" <ID>",
//            "Deref -> e",
            "Literal -> <INT>",
            "Literal -> <DOUBLE>",
            "Literal -> <STR>",
            "Literal -> \"true\"",
            "Literal -> \"false\""
    };
    ArrayList<String> nonterminators = new ArrayList<String>();
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
        init();

        int rowCount = terminators.size() + 1;
        int colCount = nonterminators.size() + 1;
        table = new String[rowCount][colCount];
        table[0][0] = "Vt\\Vn";

        for (int i = 0; i < rowCount - 1; i++)
            table[i + 1][0] = (terminators.get(i).equals("e")) ? "$" : terminators.get(i);
        for (int i = 0; i < colCount - 1; i++)
            table[0][i + 1] = nonterminators.get(i);
        for (int i = 0; i < rowCount - 1; i++)
            for (int j = 0; j < colCount - 1; j++)
                table[i + 1][j + 1] = "error";

//        for (String nonterminator: nonterminators) {
//            ArrayList<String> l = experssionSet.get(A);
//            for(String s : l){
//                HashSet<Character> set = firstSetX.get(s);
//                for (char a : set)
//                    insert(A, a, s);
//                if(set.contains('~'))  {
//                    HashSet<Character> setFollow = followSet.get(A);
//                    if(setFollow.contains('$'))
//                        insert(A, '$', s);
//                    for (char b : setFollow)
//                        insert(A, b, s);
//                }
//            }
//        }

        // Print the table in two parts.
//        for (int i = 0; i < table.length; i++) {
//            for (int j = 0; j < table[i].length / 2; j++) {
//                System.out.print(String.format("%-17s", table[i][j]));
//            }
//            System.out.println();
//        }
//        System.out.println("\n-----------------------------------------------------------------------------------------------------------------------\n");
//        for (int i = 0; i < table.length; i++) {
//            for (int j = table[i].length / 2; j < table[i].length; j++) {
//                System.out.print(String.format("%-17s", table[i][j]));
//            }
//            System.out.println();
//        }


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
//        for (String nonterminator : nonterminators) {
//            ArrayList<String> rightParts = productionSet.get(nonterminator);
//            for (String rightPart : rightParts)
//                getFirst(rightPart);
//        }

        System.out.println(firstSet);

        getFollow(S);
        for (String nonterminator: nonterminators) {
            getFollow(nonterminator);
        }
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

    }

    void getFollow(String terminator) {

    }

//    void insert(char X, char a,String s) {
//        if(a == '~') a = '$';
//        for (int i = 0; i < VnSet.size() + 1; i++) {
//            if (table[i][0].charAt(0) == X)
//                for (int j = 0; j < VtSet.size() + 1; j++) {
//                    if (table[0][j].charAt(0) == a){
//                        table[i][j] = s;
//                        return;
//                    }
//                }
//        }
//    }

}