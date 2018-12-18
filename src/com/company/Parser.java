package com.company;

import java.util.ArrayList;


class TreeNode {
    String curNode;
    TreeNode parent;
    ArrayList<TreeNode> children;
    int colCount = 1;
    int layer = 0;

    public TreeNode(String curNode, TreeNode parent) {
        this.curNode = curNode;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode node) {
        this.children.add(node);
    }
}


public class Parser {
    public ArrayList<Token> tokens;
    public int curIndex = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean readNextToken() {
        if (curIndex < tokens.size() - 1) {
            curIndex++;
            return true;
        } else {
            //curIndex++;
            return false;
        }
    }


    public boolean isSafe(int[] selectTokenNum) {
        for (int i : selectTokenNum) {
            if (tokens.get(curIndex).tokenNum == i) {
                return true;
            }
        }
        return false;
    }

    public TreeNode parse() {
        TreeNode root = null;
        try {
            root = P();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        if(root!=null){
//            System.out.println("true");
//        }
//        else
//            System.out.println("false");
        return root;
    }


    // Program -> ClassDecl Program
    //         -> e
    public TreeNode P() throws Exception {
        int[] selectTokenNum = {2, 40, 49};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of program declaration");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of program declaration");
        }
        TreeNode curNode = new TreeNode("P", null);
        TreeNode c = C(curNode);
        if (c != null) {
            curNode.addChild(c);
            if (tokens.get(curIndex).tokenNum == 49) {
                return curNode;
            }
            else if(tokens.get(curIndex).tokenNum==2||tokens.get(curIndex).tokenNum==40){
                TreeNode p = P(curNode);
                if (p != null) {
                    curNode.addChild(p);
                    return curNode;
                }
                return null;
            }
        }
        return null;
    }

    // Program -> ClassDecl Program
    //         -> e
    public TreeNode P(TreeNode node) throws Exception {
        int[] selectTokenNum = {2, 40, 49};
        if (!isSafe(selectTokenNum)) {
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of program declaration");
        }
        TreeNode curNode = new TreeNode("P", node);
        TreeNode c = C(curNode);
        if (c != null) {
            curNode.addChild(c);
            if (tokens.get(curIndex).tokenNum == 49) {
                return curNode;
            }
            else if(tokens.get(curIndex).tokenNum == 2 || tokens.get(curIndex).tokenNum == 40){
                TreeNode p = P(curNode);
                if (p != null) {
                    curNode.addChild(p);
                    return curNode;
                }
                return null;
            }
        }
        TreeNode e = new TreeNode("e", node);
        curNode.addChild(e);
        return curNode;
    }

    // ClassDecl -> "class" <ID> "extends" <ID> "{" VarMethodBlock "}"
    //           -> "class" <ID> "{" VarMethodBlock "}"
    public TreeNode C(TreeNode node) throws Exception {
        int[] selectTokenNum = {2};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
        }
        TreeNode curNode = new TreeNode("C", node);
        if (curIndex >= tokens.size() - 1)
            return null;
        if ((curIndex + 5 < tokens.size()) && tokens.get(curIndex).tokenNum == 2 &&
                tokens.get(curIndex + 1).tokenNum == 48 && tokens.get(curIndex + 2).tokenNum == 4
                && tokens.get(curIndex + 3).tokenNum == 48 && tokens.get(curIndex + 4).tokenNum == 43) {
            curIndex = curIndex + 5;
            TreeNode vm = VMBlock(curNode);
            if (vm != null) {
                curNode.addChild(vm);
                if (tokens.get(curIndex).tokenNum == 44) {
                    readNextToken();
                    return curNode;
                }
            }
        } else if ((curIndex + 5 < tokens.size()) && tokens.get(curIndex).tokenNum == 2 && tokens.get(curIndex + 1).tokenNum == 48 && tokens.get(curIndex + 2).tokenNum == 43) {
            curIndex = curIndex + 3;
            TreeNode vm = VMBlock(curNode);
            if (vm != null) {
                curNode.addChild(vm);
                if (tokens.get(curIndex).tokenNum == 44) {
                    readNextToken();
                    return curNode;
                }
            }
        }
        System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
        throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
//        System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
    }

    // VarMethodBlock -> VarDecl VarMethodBlock
    // VarMethodBlock -> MethodDecl VarMethodBlock
    // VarMethodBlock -> e
    public TreeNode VMBlock(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 44, 11, 40, 18, 1, 20, 7};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable or method declaration");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable or method declaration");
        }
        TreeNode curNode = new TreeNode("VMBlock", node);
        if (tokens.get(curIndex).tokenNum == 1 || tokens.get(curIndex).tokenNum == 7 || tokens.get(curIndex).tokenNum == 48
                || tokens.get(curIndex).tokenNum == 20 || tokens.get(curIndex).tokenNum == 18) {
            TreeNode v = V(curNode);
            if (v != null) {
                curNode.addChild(v);
                TreeNode vb = VMBlock(curNode);
                if (vb != null) {
                    curNode.addChild(vb);
                    return curNode;
                }
            }
        } else if(tokens.get(curIndex).tokenNum==11){
            TreeNode m = M(curNode);
            if (m != null) {
                curNode.addChild(m);
                TreeNode vb = VMBlock(curNode);
                if (vb != null) {
                    curNode.addChild(vb);
                    return curNode;
                }
            }
        }
        TreeNode e = new TreeNode("e", node);
        curNode.addChild(e);
        return curNode;
    }

    // VarDecl -> Type <ID> ";"
    // VarDecl -> Type <ID> "=" FullExpr ";"
    public TreeNode V(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 44, 11, 40, 18, 1, 20, 7};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable declaration");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable declaration");
        }
        TreeNode curNode = new TreeNode("V", node);
        TreeNode type = Type(curNode);
        if (type != null && tokens.get(curIndex).tokenNum == 48) {
            curNode.addChild(type);
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 22) {
                readNextToken();
                TreeNode fe = FE(curNode);
                if (fe != null) {
                    if (tokens.get(curIndex).tokenNum == 36) {
                        readNextToken();
                        curNode.addChild(fe);
                        return curNode;
                    }
                }
            } else if (tokens.get(curIndex).tokenNum == 36) {
                readNextToken();
                return curNode;
            }
            return curNode;
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode VSBlock(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 44, 40, 18, 1, 20, 7, 6, 17, 19, 12, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable declaration or statement");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable declaration or statement");
        }
        TreeNode curNode = new TreeNode("VSBlock", node);
        if (tokens.get(curIndex).tokenNum == 1 || tokens.get(curIndex).tokenNum == 7 || (tokens.get(curIndex).tokenNum == 48 && tokens.get(curIndex + 1).tokenNum == 48)
                || tokens.get(curIndex).tokenNum == 20 || tokens.get(curIndex).tokenNum == 18) {
            TreeNode v = V(curNode);
            if (v != null) {
                curNode.addChild(v);
                TreeNode vs = VSBlock(curNode);
                if (vs != null) {
                    curNode.addChild(vs);
                    return curNode;
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 14 || tokens.get(curIndex).tokenNum == 48 || tokens.get(curIndex).tokenNum == 6
                || tokens.get(curIndex).tokenNum == 17 || tokens.get(curIndex).tokenNum == 19 || tokens.get(curIndex).tokenNum == 12) {
            TreeNode s = S(curNode);
            if (s != null) {
                curNode.addChild(s);
                TreeNode vs = VSBlock(curNode);
                if (vs != null) {
                    curNode.addChild(vs);
                    return curNode;
                }
            }
        }
        TreeNode e = new TreeNode("e", node);
        curNode.addChild(e);
        return curNode;
    }

    public TreeNode M(TreeNode node) throws Exception {
        int[] selectTokenNum = {11};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of method declaration");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable declaration");
        }
        TreeNode curNode = new TreeNode("M", node);
        if (tokens.get(curIndex).tokenNum == 11 && tokens.get(curIndex + 1).tokenNum != 16 && tokens.get(curIndex + 1).tokenNum != 13) {
            readNextToken();
            TreeNode type = Type(curNode);
            if (type != null) {
                curNode.addChild(type);
                if (tokens.get(curIndex).tokenNum == 48 && tokens.get(curIndex + 1).tokenNum == 39) {
                    curIndex += 2;
                    TreeNode f = F(curNode);
                    if (f != null) {
                        curNode.addChild(f);
                    }
                    if (tokens.get(curIndex).tokenNum == 40 && tokens.get(curIndex + 1).tokenNum == 43) {
                        curIndex += 2;
                        TreeNode vsBlock = VSBlock(curNode);
                        if (vsBlock != null) {
                            curNode.addChild(vsBlock);
                            if (tokens.get(curIndex).tokenNum == 44) {
                                readNextToken();
                                return curNode;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 11 && tokens.get(curIndex + 1).tokenNum == 16) {
            curIndex += 2;
            if (tokens.get(curIndex).tokenNum == 48 && tokens.get(curIndex + 1).tokenNum == 39) {
                curIndex += 2;
                TreeNode f = F(curNode);
                if (f != null) {
                    curNode.addChild(f);
                }
                if (tokens.get(curIndex).tokenNum == 40 && tokens.get(curIndex + 1).tokenNum == 43) {
                    curIndex += 2;
                    TreeNode vsBlock = VSBlock(curNode);
                    if (vsBlock != null) {
                        curNode.addChild(vsBlock);
                        if (tokens.get(curIndex).tokenNum == 44) {
                            readNextToken();
                            return curNode;
                        }
                    }
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 11 && tokens.get(curIndex + 1).tokenNum == 13 && tokens.get(curIndex + 2).tokenNum == 16
                && tokens.get(curIndex + 3).tokenNum == 9 && tokens.get(curIndex + 4).tokenNum == 39 && tokens.get(curIndex + 5).tokenNum == 18
                && tokens.get(curIndex + 6).tokenNum == 41 && tokens.get(curIndex + 7).tokenNum == 42 && tokens.get(curIndex + 8).tokenNum == 48
                && tokens.get(curIndex + 9).tokenNum == 40) {
            curIndex = curIndex + 10;
            if (tokens.get(curIndex).tokenNum == 43) {
                readNextToken();
                TreeNode vsBlock = VSBlock(curNode);
                if (vsBlock != null) {
                    curNode.addChild(vsBlock);
                    if (tokens.get(curIndex).tokenNum == 44) {
                        readNextToken();
                        return curNode;
                    }
                }
            }
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of method declaration");
        return null;
    }

    public TreeNode F(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 40, 18, 1, 20, 7};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of formal parameters");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of formal parameters");
        }
        TreeNode curNode = new TreeNode("F", node);
        if (tokens.get(curIndex).tokenNum != 40) {
            TreeNode type = Type(curNode);
            if (type != null && tokens.get(curIndex).tokenNum == 48) {
                curNode.addChild(type);
                readNextToken();
                if (tokens.get(curIndex).tokenNum == 37) {
                    TreeNode f2 = F2(curNode);
                    if (f2 != null) {
                        curNode.addChild(f2);
                        return curNode;
                    }
                }
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode F2(TreeNode node) throws Exception {
        int[] selectTokenNum = {40, 37};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of formal parameters");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of formal parameters");
        }
        TreeNode curNode = new TreeNode("F2", node);
        if (tokens.get(curIndex).tokenNum == 37) {
            readNextToken();
            TreeNode type = Type(curNode);
            if (type != null && tokens.get(curIndex).tokenNum == 48) {
                curNode.addChild(type);
                readNextToken();
                return curNode;
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode Type(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 18, 1, 20, 7};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of object type");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of object type");
        }
        TreeNode curNode = new TreeNode("Type", node);
        TreeNode e = E(curNode);
        if (e != null) {
            curNode.addChild(e);
            if (tokens.get(curIndex).tokenNum == 41) {
                readNextToken();
                if (tokens.get(curIndex).tokenNum == 42) {
                    readNextToken();
                    return curNode;
                }
            }
            return curNode;
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable type");
        return null;
    }

    public TreeNode StateBlock(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 43, 6, 17, 19, 12, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
        }
        TreeNode curNode = new TreeNode("StateBlock", node);
        if (tokens.get(curIndex).tokenNum == 43) {
            readNextToken();
            TreeNode ss = SS(curNode);
            if (ss != null) {
                curNode.addChild(ss);
                if (tokens.get(curIndex).tokenNum == 44) {
                    readNextToken();
                    return curNode;
                }
            }
        } else {
            TreeNode s = S(curNode);
            if (s != null) {
                curNode.addChild(s);
                return curNode;
            }
        }
        System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
        throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
    }

    public TreeNode SS(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 44, 40, 6, 17, 19, 12, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
        }
        TreeNode curNode = new TreeNode("SS", node);
        if (tokens.get(curIndex).tokenNum != 44) {
            TreeNode s = S(curNode);
            if (s != null) {
                curNode.addChild(s);
                TreeNode ss = SS(curNode);
                if (ss != null) {
                    curNode.addChild(ss);
                    return curNode;
                }
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode S(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 6, 17, 19, 12, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
        }
        TreeNode curNode = new TreeNode("S", node);
        if (tokens.get(curIndex).tokenNum == 43) {
            readNextToken();
            TreeNode s = S(curNode);
            if (s != null) {
                curNode.addChild(s);
                if (tokens.get(curIndex).tokenNum == 44) {
                    readNextToken();
                    return curNode;
                }
                //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            }
        } else if (tokens.get(curIndex).tokenNum == 6) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 39) {
                readNextToken();
                TreeNode ex = EX(curNode);
                if (ex != null) {
                    curNode.addChild(ex);
                    if (tokens.get(curIndex).tokenNum == 40) {
                        readNextToken();
                        TreeNode stateBlock = StateBlock(node);
                        if (stateBlock != null) {
                            curNode.addChild(stateBlock);
                            if (tokens.get(curIndex).tokenNum == 3) {
                                readNextToken();
                                TreeNode stateBlock2 = StateBlock(curNode);
                                if (stateBlock2 != null)
                                    curNode.addChild(stateBlock2);
                                else {
                                    //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
                                    return null;
                                }
                                return curNode;
                            }
                            return curNode;
                        }
                        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
                        return null;
                    }
                    System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                    throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                }
                return null;
            }
        } else if (tokens.get(curIndex).tokenNum == 17) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 39) {
                readNextToken();
                TreeNode ex = EX(curNode);
                if (ex != null) {
                    curNode.addChild(ex);
                } else {
                    return null;
                }
                if (tokens.get(curIndex).tokenNum == 40) {
                    readNextToken();
                    TreeNode stateBlock = StateBlock(curNode);
                    if (stateBlock != null) {
                        curNode.addChild(stateBlock);
                        return curNode;
                    }
                    return null;
                }
                System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            }
            //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of class declaration");
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
        } else if (tokens.get(curIndex).tokenNum == 19) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 39) {
                readNextToken();
                TreeNode pa = PA(curNode);
                if (pa != null) {
                    curNode.addChild(pa);
                    if (tokens.get(curIndex).tokenNum == 40) {
                        readNextToken();
                        if (tokens.get(curIndex).tokenNum == 36) {
                            readNextToken();
                            return curNode;
                        }
                    }
                    System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                    throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                }
            }
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
        } else if (tokens.get(curIndex).tokenNum == 12) {
            readNextToken();
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                if (tokens.get(curIndex).tokenNum == 36) {
                    readNextToken();
                    return curNode;
                }
                System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
            }
            return null;
        } else {
            TreeNode lv = LV(curNode);
            if (lv != null) {
                curNode.addChild(lv);
                if (tokens.get(curIndex).tokenNum == 22) {
                    readNextToken();
                    TreeNode fe = FE(node);
                    if (tokens.get(curIndex).tokenNum == 36) {
                        readNextToken();
                        curNode.addChild(fe);
                        return curNode;
                    }
                } else if (tokens.get(curIndex).tokenNum == 39) {
                    readNextToken();
                    TreeNode pa = PA(curNode);
                    if (tokens.get(curIndex).tokenNum == 40) {
                        readNextToken();
                        if (tokens.get(curIndex).tokenNum == 36) {
                            readNextToken();
                            curNode.addChild(pa);
                            return curNode;
                        }
                    }
                    System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                    throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of statement");
                }
            }
        }
        return null;
    }

    public TreeNode FE(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 39, 45, 10, 46, 24, 29, 14, 47, 5, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of full expression");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of full expression");
        }
        TreeNode curNode = new TreeNode("FE", node);
        if (tokens.get(curIndex).tokenNum == 10) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 48) {
                readNextToken();
                if (tokens.get(curIndex).tokenNum == 39) {
                    readNextToken();
                    TreeNode p = PA(curNode);
                    if (p != null) {
                        curNode.addChild(p);
                        if (tokens.get(curIndex).tokenNum == 40) {
                            readNextToken();
                            return curNode;
                        }
                        System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of full expression");
                        throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of full expression");
                    }
                }
                System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of full expression");
                throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of full expression");
            }
            TreeNode e = E(curNode);
            if (e != null) {
                if (tokens.get(curIndex).tokenNum == 41) {
                    readNextToken();
                    if (tokens.get(curIndex).tokenNum == 46) {
                        readNextToken();
                        if (tokens.get(curIndex).tokenNum == 42) {
                            readNextToken();
                            return curNode;
                        }
                    }
                }
            }
        } else {
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                return curNode;
            }
        }
        return null;
    }

    public TreeNode PA(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 39, 40, 45, 46, 24, 29, 14, 47, 5, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of parameters");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of parameters");
        }
        TreeNode curNode = new TreeNode("PA", node);
        TreeNode ex = EX(curNode);
        if (ex != null) {
            curNode.addChild(ex);
            TreeNode pa2 = PA2(curNode);
            if (pa2 != null) {
                curNode.addChild(pa2);
            }
            return curNode;
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode PA2(TreeNode node) throws Exception {
        int[] selectTokenNum = {40, 37};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of parameters");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of parameters");
        }
        TreeNode curNode = new TreeNode("PA'", node);
        if (tokens.get(curIndex).tokenNum == 37) {
            readNextToken();
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                TreeNode pa2 = PA2(curNode);
                if (pa2 != null) {
                    curNode.addChild(pa2);
                }
                return curNode;
            }
            return null;
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode EX(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 39, 45, 46, 24, 29, 14, 47, 5, 15, 40, 36, 37, 42};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of expression");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of expression");
        }
        TreeNode curNode = new TreeNode("EX", node);
        if (tokens.get(curIndex).tokenNum == 14 || tokens.get(curIndex).tokenNum == 29) {
            TreeNode u = U(curNode);
            if (u != null) {
                curNode.addChild(u);
                TreeNode e = E(curNode);
                if (e != null) {
                    curNode.addChild(e);
                    TreeNode ex2 = EX2(curNode);
                    if (ex2 != null) {
                        curNode.addChild(ex2);
                        return curNode;
                    }
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 39) {
            readNextToken();
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                if (tokens.get(curIndex).tokenNum == 40) {
                    readNextToken();
                    TreeNode ex2 = EX2(curNode);
                    if (ex2 != null) {
                        curNode.addChild(ex2);
                        return curNode;
                    }
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 14 || tokens.get(curIndex).tokenNum == 48) {
            TreeNode lv = LV(curNode);
            if (lv != null) {
                curNode.addChild(lv);
                if (tokens.get(curIndex).tokenNum == 39) {
                    readNextToken();
                    TreeNode p = P(curNode);
                    if (p != null) {
                        curNode.addChild(p);
                        if (tokens.get(curIndex).tokenNum == 40) {
                            readNextToken();
                            TreeNode ex2 = EX2(curNode);
                            if (ex2 != null) {
                                curNode.addChild(ex2);
                                return curNode;
                            }
                        }
                    }
                } else {
                    TreeNode ex2 = EX2(curNode);
                    if (ex2 != null) {
                        curNode.addChild(ex2);
                        return curNode;
                    }
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 46 || tokens.get(curIndex).tokenNum == 47 || tokens.get(curIndex).tokenNum == 45
                || tokens.get(curIndex).tokenNum == 15 || tokens.get(curIndex).tokenNum == 5) {
            TreeNode li = LI(curNode);
            if (li != null) {
                curNode.addChild(li);
                TreeNode ex2 = EX2(curNode);
                if (ex2 != null) {
                    curNode.addChild(ex2);
                    return curNode;
                }
            }
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of assignment");
        return null;
    }

    public TreeNode EX2(TreeNode node) throws Exception {
        TreeNode curNode = new TreeNode("EX2", node);
        if (tokens.get(curIndex).tokenNum != 42&&tokens.get(curIndex).tokenNum != 40&&tokens.get(curIndex).tokenNum != 36&&tokens.get(curIndex).tokenNum != 37) {
            int[] selectTokenNum = {40, 36, 37, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35};
            if (!isSafe(selectTokenNum)) {
                System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of expression");
                throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of expression");
            }
            TreeNode b = B(curNode);
            if (b != null) {
                curNode.addChild(b);
                TreeNode ex = EX(curNode);
                if (ex != null) {
                    curNode.addChild(ex);
                    TreeNode ex2 = EX2(curNode);
                    if (ex2 != null) {
                        curNode.addChild(ex2);
                        return curNode;
                    }
                    return null;
                }
                return null;
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }


    public TreeNode E(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 18, 20, 7, 1};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of Elmtype");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of Elmtype");
        }
        TreeNode curNode = new TreeNode("E", node);
        if (tokens.get(curIndex).tokenNum == 1 || tokens.get(curIndex).tokenNum == 7 || tokens.get(curIndex).tokenNum == 48
                || tokens.get(curIndex).tokenNum == 20 || tokens.get(curIndex).tokenNum == 18) {
            curNode.addChild(new TreeNode(tokens.get(curIndex).token, curNode));
            readNextToken();
            return curNode;
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable type");
        return null;
    }

    public TreeNode B(TreeNode node) throws Exception {
        int[] selectTokenNum = {23,24,25,26,27,28,30,31,32,33,34,35};
        if(!isSafe(selectTokenNum)){
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of operators");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of operators");
        }
        TreeNode curNode = new TreeNode("B", node);
        if (tokens.get(curIndex).tokenNum == 23 || tokens.get(curIndex).tokenNum == 24 || tokens.get(curIndex).tokenNum == 25
                || tokens.get(curIndex).tokenNum == 26 || tokens.get(curIndex).tokenNum == 27 || tokens.get(curIndex).tokenNum == 28
                || tokens.get(curIndex).tokenNum == 30 || tokens.get(curIndex).tokenNum == 31 || tokens.get(curIndex).tokenNum == 32
                || tokens.get(curIndex).tokenNum == 33 || tokens.get(curIndex).tokenNum == 34 || tokens.get(curIndex).tokenNum == 35) {
            curNode.addChild(new TreeNode(tokens.get(curIndex).token, curNode));
            readNextToken();
            return curNode;
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of operators");
        return null;
    }

    public TreeNode U(TreeNode node) throws Exception {
        //int[] selectTokenNum = {24, 29};
        //if (!isSafe(selectTokenNum)) {
        //    System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of operators");
        //    throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of operators");
        //}
        TreeNode curNode = new TreeNode("U", node);
        if (tokens.get(curIndex).tokenNum == 14 || tokens.get(curIndex).tokenNum == 29) {
            readNextToken();
            curNode.addChild(new TreeNode(tokens.get(curIndex).token, curNode));
            return curNode;
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of operators");
        return null;
    }

    public TreeNode LV(TreeNode node) throws Exception {
        int[] selectTokenNum = {48, 14};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of left value");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of left value");
        }
        TreeNode curNode = new TreeNode("LV", node);
        if (tokens.get(curIndex).tokenNum == 14) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 38) {
                readNextToken();
                if (tokens.get(curIndex).tokenNum == 48) {
                    readNextToken();
                    if (tokens.get(curIndex).tokenNum == 38 || tokens.get(curIndex).tokenNum == 41) {
                        TreeNode d2 = D2(curNode);
                        if (d2 != null) {
                            curNode.addChild(d2);
                            return curNode;
                        }
                    }
                    return curNode;
                }
                //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable");
                return null;
            }
            //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable");
            return null;
        } else if (tokens.get(curIndex).tokenNum == 48) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 38 || tokens.get(curIndex).tokenNum == 41) {
                TreeNode d2 = D2(curNode);
                if (d2 != null) {
                    curNode.addChild(d2);
                    return curNode;
                }
            }
            return curNode;
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of variable");
        return null;
    }

    public TreeNode D(TreeNode node) throws Exception {
        //int[] selectTokenNum = {41,38};
        //if(!isSafe(selectTokenNum)){
        //    System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of deref");
        //    throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of deref");
        //}
        TreeNode curNode = new TreeNode("D", node);
        if (tokens.get(curIndex).tokenNum == 41) {
            readNextToken();
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                if (tokens.get(curIndex).tokenNum == 42) {
                    readNextToken();
                    return curNode;
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 38) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 48) {
                readNextToken();
                return curNode;
            }
        }
        return null;
    }

    public TreeNode D2(TreeNode node) throws Exception {
        int[] selectTokenNum = {39, 40, 41, 22, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35, 38};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of deref");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of deref");
        }
        TreeNode curNode = new TreeNode("D'", node);
        TreeNode d = D(curNode);
        if (d != null) {
            curNode.addChild(d);
            TreeNode d2 = D2(curNode);
            if (d2 != null) {
                curNode.addChild(d2);
                return curNode;
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode LI(TreeNode node) throws Exception {
        int[] selectTokenNum = {45, 46, 47, 5, 15};
        if (!isSafe(selectTokenNum)) {
            System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of literal");
            throw new Exception("Line " + tokens.get(curIndex).lineNum + ": invalid expression of literal");
        }
        TreeNode curNode = new TreeNode("LI", node);
        if (tokens.get(curIndex).tokenNum == 46 || tokens.get(curIndex).tokenNum == 47 || tokens.get(curIndex).tokenNum == 45
                || tokens.get(curIndex).tokenNum == 15 || tokens.get(curIndex).tokenNum == 5) {
            readNextToken();
            return curNode;
        }
        //System.out.println("Line " + tokens.get(curIndex).lineNum + ": invalid expression of constants");
        return null;
    }
}


