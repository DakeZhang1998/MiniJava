package com.company;

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

    public void parse() {

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
        return null;
    }


    public TreeNode C(TreeNode node) {
        TreeNode curNode = new TreeNode("C", node);
        if ((curIndex + 5 < tokens.size()) && tokens.get(curIndex).tokenNum == 2 && tokens.get(curIndex + 1).tokenNum == 48 && tokens.get(curIndex + 2).tokenNum == 4 && tokens.get(curIndex + 3).tokenNum == 48 && tokens.get(curIndex + 4).tokenNum == 43) {
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
            curIndex = curIndex + 3;
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
        }
        return null;
    }

    public TreeNode V(TreeNode node) {
        TreeNode curNode = new TreeNode("V", node);
        TreeNode type = Type(curNode);
        if(type!=null&&tokens.get(curIndex).tokenNum==48){
            curNode.addChild(type);
            readNextToken();
            if(tokens.get(curIndex).tokenNum==22){
                readNextToken();
                TreeNode fe = FE(curNode);
                if(fe!=null){
                    if(tokens.get(curIndex).tokenNum==36){
                        readNextToken();
                        curNode.addChild(fe);
                        return curNode;
                    }
                }
            }
            return curNode;
        }
        curNode.addChild(new TreeNode("e",curNode));
        return curNode;
    }

    public TreeNode M(TreeNode node) {
        TreeNode curNode = new TreeNode("M", node);
        if(tokens.get(curIndex).tokenNum==11&&tokens.get(curIndex+1).tokenNum!=16&&tokens.get(curIndex).tokenNum!=13){
            readNextToken();
            TreeNode type = Type(curNode);
            if(type!=null){
                curNode.addChild(type);
                if(tokens.get(curIndex).tokenNum==48&&tokens.get(curIndex+1).tokenNum==39){
                    curIndex+=2;
                    TreeNode f = F(curNode);
                    if(f!=null){
                        curNode.addChild(f);
                    }
                    if(tokens.get(curIndex).tokenNum==40&&tokens.get(curIndex+1).tokenNum==43){
                        curIndex+=2;
                        TreeNode v = V(curNode);
                        TreeNode s = S(curNode);
                        if(v!=null&&s!=null){
                            if(tokens.get(curIndex).tokenNum==44){
                                readNextToken();
                                return curNode;
                            }
                        }
                    }
                }
            }
        }
        else if(tokens.get(curIndex).tokenNum==11&&tokens.get(curIndex+1).tokenNum==16){
            curIndex+=2;
            if(tokens.get(curIndex).tokenNum==48&&tokens.get(curIndex+1).tokenNum==39){
                curIndex+=2;
                TreeNode f = F(curNode);
                if(f!=null){
                    curNode.addChild(f);
                }
                if(tokens.get(curIndex).tokenNum==40&&tokens.get(curIndex+1).tokenNum==43){
                    curIndex+=2;
                    TreeNode v = V(curNode);
                    TreeNode s = S(curNode);
                    if(v!=null&&s!=null){
                        if(tokens.get(curIndex).tokenNum==44){
                            readNextToken();
                            return curNode;
                        }
                    }
                }
            }
        }
        else if(tokens.get(curIndex).tokenNum==11&&tokens.get(curIndex+1).tokenNum==13&&tokens.get(curIndex+2).tokenNum==16
                &&tokens.get(curIndex+3).tokenNum==9&&tokens.get(curIndex+4).tokenNum==39&&tokens.get(curIndex+5).tokenNum==18
                &&tokens.get(curIndex+6).tokenNum==41&&tokens.get(curIndex+7).tokenNum==42)
        {
            curIndex=curIndex+8;
            if(tokens.get(curIndex).tokenNum==43){
                readNextToken();
                TreeNode v = V(curNode);
                TreeNode s = S(curNode);
                if(v!=null&&s!=null){
                    if(tokens.get(curIndex).tokenNum==44){
                        readNextToken();
                        return curNode;
                    }
                }
            }
        }
        return null;
    }

    public TreeNode F(TreeNode node) {
        TreeNode curNode = new TreeNode("F", node);
        TreeNode type = Type(curNode);
        if(type!=null&&tokens.get(curIndex).tokenNum==48){
            curNode.addChild(type);
            readNextToken();
            if(tokens.get(curIndex).tokenNum==37){
                readNextToken();
                TreeNode f = F(curNode);
                if(f!=null){
                    curNode.addChild(f);
                    return curNode;
                }
                return curNode;
            }
            return curNode;
        }
        curNode.addChild(new TreeNode("e",curNode));
        return curNode;
    }

    public TreeNode Type(TreeNode node) {
        TreeNode curNode = new TreeNode("Type", node);
        TreeNode e = E(curNode);
        if(e!=null){
            curNode.addChild(e);
            if(tokens.get(curIndex).tokenNum==41){
                readNextToken();
                if(tokens.get(curIndex).tokenNum==42){
                    readNextToken();
                    return curNode;
                }
            }
            return curNode;
        }
        return null;
    }

    public TreeNode S(TreeNode node) {
        TreeNode curNode = new TreeNode("S", node);
        if(tokens.get(curIndex).tokenNum==43){
            readNextToken();
            TreeNode s = S(curNode);
            if(s!=null){
                curNode.addChild(s);
                if(tokens.get(curIndex).tokenNum==44){
                    readNextToken();
                    return curNode;
                }
                return null;
            }
        }
        else{
            TreeNode l = L(curNode);
            TreeNode v = V(curNode);
            if(l!=null&&v!=null){
                curNode.addChild(l);
                curNode.addChild(v);
                if(tokens.get(curIndex).tokenNum==22){
                    TreeNode fe = FE(node);
                    readNextToken();
                    if(tokens.get(curIndex).tokenNum==36){
                        curNode.addChild(fe);
                        return curNode;
                    }
                }
                else if(tokens.get(curIndex).tokenNum==39){
                    TreeNode pa = PA(curNode);
                    readNextToken();
                    if(tokens.get(curIndex).tokenNum==40){
                        readNextToken();
                        if(tokens.get(curIndex).tokenNum==36){
                            curNode.addChild(pa);
                            return curNode;
                        }
                    }
                    return null;
                }
            }
        }
        curNode.addChild(new TreeNode("e",curNode));
        return curNode;
    }

    public TreeNode FE(TreeNode node) {
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
            if(tokens.get(curIndex).tokenNum==37){
                readNextToken();
                TreeNode ex2 = EX(curNode);
                if(ex2!=null){
                    curNode.addChild(ex2);
                    return curNode;
                }
            }
            else{
                return curNode;
            }
        }
        curNode.addChild(new TreeNode("e",curNode));
        return curNode;    }

    public TreeNode EX(TreeNode node) {

        return null;
    }

    public TreeNode E(TreeNode node) {
        TreeNode curNode = new TreeNode("E", node);
        if(tokens.get(curIndex).tokenNum==1||tokens.get(curIndex).tokenNum==7||tokens.get(curIndex).tokenNum==48
                ||tokens.get(curIndex).tokenNum==20||tokens.get(curIndex).tokenNum==18){
            curNode.addChild(new TreeNode(tokens.get(curIndex).token,curNode));
            readNextToken();
            return curNode;
        }
        return null;
    }
}


