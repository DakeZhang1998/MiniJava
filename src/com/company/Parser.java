package com.company;

import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;


class TreeNode {
    String curNode;
    TreeNode parent;
    ArrayList<TreeNode> children;

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

    public TreeNode parse() {
        TreeNode root =  P(null);
        if(root!=null){
            System.out.println("true");
        }
        else
            System.out.println("false");
        return root;
    }

    public TreeNode P(TreeNode node) {
        TreeNode curNode = new TreeNode("P", node);
        TreeNode c = C(curNode);
        if (c != null) {
            if (curIndex < tokens.size()) {
                curNode.addChild(c);
                return curNode;
            }
            TreeNode p = P(curNode);
            if (p != null) {
                curNode.addChild(p);
                return curNode;
            }
            return null;
        }
        TreeNode e = new TreeNode("e",node);
        curNode.addChild(e);
        return curNode;
    }


    public TreeNode C(TreeNode node) {
        TreeNode curNode = new TreeNode("C", node);
        System.out.println(tokens.get(curIndex).tokenNum);
        System.out.println(tokens.get(curIndex+1).tokenNum);
        System.out.println(tokens.get(curIndex+2).tokenNum);
        System.out.println(tokens.get(curIndex+3).tokenNum);
        System.out.println(tokens.get(curIndex+4).tokenNum);
        if ((curIndex + 5 < tokens.size()) && tokens.get(curIndex).tokenNum == 2 &&
                tokens.get(curIndex + 1).tokenNum == 48 && tokens.get(curIndex + 2).tokenNum == 4
                && tokens.get(curIndex + 3).tokenNum == 48 && tokens.get(curIndex + 4).tokenNum == 43) {
            TreeNode classNode =  new TreeNode("class", curNode);
            TreeNode IDNode =  new TreeNode("identifier", curNode);
            TreeNode extendsNode =  new TreeNode("extends", curNode);
            TreeNode IDNode2 =  new TreeNode("identifier", curNode);
            TreeNode leftBracket =  new TreeNode("{", curNode);
            curNode.addChild(classNode);
            curNode.addChild(IDNode);
            curNode.addChild(extendsNode);
            curNode.addChild(IDNode2);
            curNode.addChild(leftBracket);
            curIndex = curIndex + 5;
            TreeNode v = V(curNode);
            if (v != null) {
                curNode.addChild(v);
                TreeNode m = M(curNode);
                if (m != null) {
                    curNode.addChild(v);
                    if (tokens.get(curIndex).tokenNum == 44) {
                        return curNode;
                    }
                }
            }
        } else if ((curIndex + 5 < tokens.size()) && tokens.get(curIndex).tokenNum == 2 && tokens.get(curIndex + 1).tokenNum == 48 && tokens.get(curIndex + 2).tokenNum == 43) {
            TreeNode classNode =  new TreeNode("class", curNode);
            TreeNode IDNode =  new TreeNode("identifier", curNode);
            TreeNode leftBracket =  new TreeNode("{", curNode);
            curNode.addChild(classNode);
            curNode.addChild(IDNode);
            curNode.addChild(leftBracket);
            curIndex = curIndex + 3;
            TreeNode vb = VMBlock(curNode);
            if(vb!=null){
                curNode.addChild(vb);
                if (tokens.get(curIndex).tokenNum == 44) {
                    readNextToken();
                    return curNode;
                }
            }
        }
        return null;
    }

    public TreeNode VMBlock(TreeNode node){
        TreeNode curNode = new TreeNode("VMBlock", node);
        if (tokens.get(curIndex).tokenNum == 1 || tokens.get(curIndex).tokenNum == 7 || tokens.get(curIndex).tokenNum == 48
                || tokens.get(curIndex).tokenNum == 20 || tokens.get(curIndex).tokenNum == 18){
            TreeNode v = V(curNode);
            if (v != null) {
                curNode.addChild(v);
                TreeNode vb = VMBlock(curNode);
                if(vb!=null){
                    curNode.addChild(vb);
                    return curNode;
                }
            }
        }
        else{
            TreeNode m = M(curNode);
            if(m!=null){
                curNode.addChild(m);
                TreeNode vb = VMBlock(curNode);
                if(vb!=null){
                    curNode.addChild(vb);
                    return curNode;
                }
            }
        }
        TreeNode e = new TreeNode("e",node);
        curNode.addChild(e);
        return curNode;
    }
    public TreeNode V(TreeNode node) {
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
            }else if(tokens.get(curIndex).tokenNum == 36){
                readNextToken();
                return curNode;
            }
            return curNode;
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode VSBlock(TreeNode node){
        TreeNode curNode = new TreeNode("VSBlock", node);
        if (tokens.get(curIndex).tokenNum == 1 || tokens.get(curIndex).tokenNum == 7 || tokens.get(curIndex).tokenNum == 48
                || tokens.get(curIndex).tokenNum == 20 || tokens.get(curIndex).tokenNum == 18){
            TreeNode v = V(curNode);
            if (v != null) {
                curNode.addChild(v);
                TreeNode vs = VSBlock(curNode);
                if(vs!=null){
                    curNode.addChild(vs);
                    return curNode;
                }
            }
        }
        else if(tokens.get(curIndex).tokenNum==14||tokens.get(curIndex).tokenNum==48||tokens.get(curIndex).tokenNum==6
                ||tokens.get(curIndex).tokenNum==17||tokens.get(curIndex).tokenNum==19||tokens.get(curIndex).tokenNum==12){
            TreeNode s = S(curNode);
            if(s!=null){
                curNode.addChild(s);
                TreeNode vs = VSBlock(curNode);
                if(vs!=null){
                    curNode.addChild(vs);
                    return curNode;
                }
            }
        }
        TreeNode e = new TreeNode("e",node);
        curNode.addChild(e);
        return curNode;
    }
    public TreeNode M(TreeNode node) {
        TreeNode curNode = new TreeNode("M", node);
        if (tokens.get(curIndex).tokenNum == 11 && tokens.get(curIndex + 1).tokenNum != 16 && tokens.get(curIndex).tokenNum != 13) {
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
                        if(vsBlock!=null){
                            curNode.addChild(vsBlock);
                            if(tokens.get(curIndex + 1).tokenNum == 44)
                                return curNode;
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
                    if(vsBlock!=null){
                        curNode.addChild(vsBlock);
                        if(tokens.get(curIndex + 1).tokenNum == 44)
                            return curNode;
                    }
                }
            }
        } else if (tokens.get(curIndex).tokenNum == 11 && tokens.get(curIndex + 1).tokenNum == 13 && tokens.get(curIndex + 2).tokenNum == 16
                && tokens.get(curIndex + 3).tokenNum == 9 && tokens.get(curIndex + 4).tokenNum == 39 && tokens.get(curIndex + 5).tokenNum == 18
                && tokens.get(curIndex + 6).tokenNum == 41 && tokens.get(curIndex + 7).tokenNum == 42) {
            curIndex = curIndex + 8;
            if (tokens.get(curIndex).tokenNum == 43) {
                readNextToken();
                TreeNode vsBlock = VSBlock(curNode);
                if(vsBlock!=null){
                    curNode.addChild(vsBlock);
                    if(tokens.get(curIndex + 1).tokenNum == 44)
                        return curNode;
                }
            }
        }
        return null;
    }

    public TreeNode F(TreeNode node) {
        TreeNode curNode = new TreeNode("F", node);
        TreeNode type = Type(curNode);
        if (type != null && tokens.get(curIndex).tokenNum == 48) {
            curNode.addChild(type);
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 37) {
                readNextToken();
                TreeNode f2 = F2(curNode);
                if (f2 != null) {
                    curNode.addChild(f2);
                    return curNode;
                }
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode F2(TreeNode node){
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
    public TreeNode Type(TreeNode node) {
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
        return null;
    }

    public TreeNode StateBlock(TreeNode node){
        TreeNode curNode = new TreeNode("StateBlock", node);
        if(tokens.get(curIndex).tokenNum==43){
            readNextToken();
            TreeNode ss = SS(curNode);
            if(ss!=null){
                curNode.addChild(ss);
                if(tokens.get(curIndex).tokenNum==44){
                    readNextToken();
                    return curNode;
                }
            }
        }
        else{
            TreeNode s = S(curNode);
            if(s!=null){
                curNode.addChild(s);
                return curNode;
            }
        }
        return null;
    }

    public TreeNode SS(TreeNode node){
        TreeNode curNode = new TreeNode("SS", node);
        TreeNode s = S(curNode);
        if(s!=null){
            curNode.addChild(s);
            TreeNode ss = SS(curNode);
            if(ss!=null){
                curNode.addChild(ss);
                return curNode;
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }
    public TreeNode S(TreeNode node) {
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
                return null;
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
                                else
                                    return null;
                                return curNode;
                            }
                            return curNode;
                        }
                        return null;
                    }
                    return null;
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
                    TreeNode s = S(curNode);
                    if (s != null) {
                        curNode.addChild(s);
                        return curNode;
                    }
                    return null;
                }
                return null;
            }
            return null;
        } else if (tokens.get(curIndex).tokenNum == 19) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 39) {
                readNextToken();
                TreeNode pa = PA(curNode);
                if (pa != null) {
                    curNode.addChild(pa);
                    if (tokens.get(curIndex).tokenNum == 40) {
                        readNextToken();
                        if(tokens.get(curIndex).tokenNum == 36){
                            readNextToken();
                            return curNode;
                        }
                    }
                    return null;
                }
            }
            return null;
        } else if (tokens.get(curIndex).tokenNum == 12) {
            readNextToken();
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                if (tokens.get(curIndex).tokenNum == 36) {
                    readNextToken();
                    return curNode;
                }
                return null;
            }
            return null;
        } else {
            TreeNode l = L(curNode);
            TreeNode v = V(curNode);
            if (l != null && v != null) {
                curNode.addChild(l);
                curNode.addChild(v);
                if (tokens.get(curIndex).tokenNum == 22) {
                    TreeNode fe = FE(node);
                    readNextToken();
                    if (tokens.get(curIndex).tokenNum == 36) {
                        curNode.addChild(fe);
                        return curNode;
                    }
                } else if (tokens.get(curIndex).tokenNum == 39) {
                    TreeNode pa = PA(curNode);
                    readNextToken();
                    if (tokens.get(curIndex).tokenNum == 40) {
                        readNextToken();
                        if (tokens.get(curIndex).tokenNum == 36) {
                            curNode.addChild(pa);
                            return curNode;
                        }
                    }
                    return null;
                }
            }
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode FE(TreeNode node) {
        TreeNode curNode = new TreeNode("FE", node);
        if (tokens.get(curIndex).tokenNum == 10) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 48) {
                readNextToken();
                if (tokens.get(curIndex).tokenNum == 39) {
                    readNextToken();
                    TreeNode p = P(curNode);
                    if (p != null) {
                        curNode.addChild(p);
                        if (tokens.get(curIndex).tokenNum == 40) {
                            readNextToken();
                            return curNode;
                        }
                        return null;
                    }
                }
                return null;
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

    public TreeNode L(TreeNode node) {
        return null;
    }

    public TreeNode PA(TreeNode node) {
        TreeNode curNode = new TreeNode("PA", node);
        TreeNode ex = EX(curNode);
        if(ex!=null){
            curNode.addChild(ex);
            TreeNode pa2 = PA2(curNode);
            if(pa2!=null){
                curNode.addChild(pa2);
            }
            return curNode;
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode PA2(TreeNode node){
        TreeNode curNode = new TreeNode("PA'", node);
        if (tokens.get(curIndex).tokenNum == 37) {
            readNextToken();
            TreeNode ex = EX(curNode);
            if (ex != null) {
                curNode.addChild(ex);
                TreeNode pa2 = PA2(curNode);
                if(pa2!=null){
                    curNode.addChild(pa2);
                }
                return curNode;
            }
            return null;
        }
        curNode.addChild(new TreeNode("e", curNode));
        return curNode;
    }

    public TreeNode EX(TreeNode node) {
        TreeNode curNode = new TreeNode("EX", node);
        if (tokens.get(curIndex).tokenNum == 14 || tokens.get(curIndex).tokenNum == 29){
            TreeNode u = U(curNode);
            if(u!=null){
                curNode.addChild(u);
                TreeNode e = E(curNode);
                if(e!=null){
                    curNode.addChild(e);
                    TreeNode ex2 = EX2(curNode);
                    if(ex2!=null){
                        curNode.addChild(ex2);
                        return curNode;
                    }
                }
            }
        }
        else if(tokens.get(curIndex).tokenNum==39){
            readNextToken();
            TreeNode ex = EX(curNode);
            if(ex!=null){
                curNode.addChild(ex);
                if(tokens.get(curIndex).tokenNum==40){
                    readNextToken();
                    TreeNode ex2 = EX2(curNode);
                    if(ex2!=null){
                        curNode.addChild(ex2);
                        return curNode;
                    }
                }
            }
        }
        else if(tokens.get(curIndex).tokenNum==14||tokens.get(curIndex).tokenNum==48){
            TreeNode lv = LV(curNode);
            if(lv!=null){
                curNode.addChild(lv);
                if(tokens.get(curIndex).tokenNum==39){
                    readNextToken();
                    TreeNode p = P(curNode);
                    if(p!=null){
                        curNode.addChild(p);
                        if(tokens.get(curIndex).tokenNum==40){
                            readNextToken();
                            TreeNode ex2 = EX2(curNode);
                            if(ex2!=null){
                                curNode.addChild(ex2);
                                return curNode;
                            }
                        }
                    }
                }
                else{
                    TreeNode ex2 = EX2(curNode);
                    if(ex2!=null){
                        curNode.addChild(ex2);
                        return curNode;
                    }
                }
            }
        }
        else if(tokens.get(curIndex).tokenNum == 46 || tokens.get(curIndex).tokenNum == 47 || tokens.get(curIndex).tokenNum == 45
                || tokens.get(curIndex).tokenNum == 15 || tokens.get(curIndex).tokenNum == 5){
            TreeNode li = LI(curNode);
            if(li!=null){
                curNode.addChild(li);
                TreeNode ex2 = EX2(curNode);
                if(ex2!=null){
                    curNode.addChild(ex2);
                    return curNode;
                }
            }
        }
        return null;
    }

    public TreeNode EX2(TreeNode node) {
        TreeNode curNode = new TreeNode("EX2", node);
        TreeNode b = B(curNode);
        if(b!=null){
            curNode.addChild(b);
            TreeNode ex = EX(curNode);
            if(ex!=null){
                curNode.addChild(ex);
                TreeNode ex2 = EX2(curNode);
                if(ex2!=null){
                    curNode.addChild(ex2);
                    return curNode;
                }
                return null;
            }
            return null;
        }
        curNode.addChild(new TreeNode("e",curNode));
        return curNode;
    }


    public TreeNode E(TreeNode node) {
        TreeNode curNode = new TreeNode("E", node);
        if (tokens.get(curIndex).tokenNum == 1 || tokens.get(curIndex).tokenNum == 7 || tokens.get(curIndex).tokenNum == 48
                || tokens.get(curIndex).tokenNum == 20 || tokens.get(curIndex).tokenNum == 18) {
            curNode.addChild(new TreeNode(tokens.get(curIndex).token, curNode));
            readNextToken();
            return curNode;
        }
        return null;
    }

    public TreeNode B(TreeNode node) {
        TreeNode curNode = new TreeNode("B", node);
        if (tokens.get(curIndex).tokenNum == 23 || tokens.get(curIndex).tokenNum == 24 || tokens.get(curIndex).tokenNum == 25
                || tokens.get(curIndex).tokenNum == 26 || tokens.get(curIndex).tokenNum == 27 || tokens.get(curIndex).tokenNum == 28
                || tokens.get(curIndex).tokenNum == 30 || tokens.get(curIndex).tokenNum == 31 || tokens.get(curIndex).tokenNum == 32
                || tokens.get(curIndex).tokenNum == 33 || tokens.get(curIndex).tokenNum == 34 || tokens.get(curIndex).tokenNum == 35) {
            curNode.addChild(new TreeNode(tokens.get(curIndex).token, curNode));
            readNextToken();
            return curNode;
        }
        return null;
    }

    public TreeNode U(TreeNode node) {
        TreeNode curNode = new TreeNode("U", node);
        if (tokens.get(curIndex).tokenNum == 14 || tokens.get(curIndex).tokenNum == 29) {
            readNextToken();
            curNode.addChild(new TreeNode(tokens.get(curIndex).token, curNode));
            return curNode;
        }
        return null;
    }

    public TreeNode LV(TreeNode node) {
        TreeNode curNode = new TreeNode("LV", node);
        if (tokens.get(curIndex).tokenNum == 14) {
            readNextToken();
            if (tokens.get(curIndex).tokenNum == 38) {
                readNextToken();
                if (tokens.get(curIndex).tokenNum == 48) {
                    readNextToken();
                    TreeNode d = D(curNode);
                    if (d != null) {
                        curNode.addChild(d);
                        return curNode;
                    }
                }
                return null;
            }
            return null;
        } else if (tokens.get(curIndex).tokenNum == 48) {
            readNextToken();
            TreeNode d = D(curNode);
            if (d != null) {
                curNode.addChild(d);
                return curNode;
            }
        }
        return null;
    }

    public TreeNode D(TreeNode node) {
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
        TreeNode e = new TreeNode("e", curNode);
        return curNode;
    }

    public TreeNode LI(TreeNode node) {
        TreeNode curNode = new TreeNode("LI", node);
        if (tokens.get(curIndex).tokenNum == 46 || tokens.get(curIndex).tokenNum == 47 || tokens.get(curIndex).tokenNum == 45
                || tokens.get(curIndex).tokenNum == 15 || tokens.get(curIndex).tokenNum == 5) {
            readNextToken();
            return curNode;
        }
        return null;
    }
}


