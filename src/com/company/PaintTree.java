package com.company;

import java.awt.*;

import javax.swing.*;

import java.lang.reflect.Array;
import java.util.ArrayList;



class TreePanel extends JPanel {
    private TreeNode tree;				//保存整棵树
    private int gridWidth = 90;		//每个结点的宽度
    private int gridHeight = 30;	//每个结点的高度
    private int vGap = 40;			//每2个结点的垂直距离
    private int hGap = 40;			//每2个结点的水平距离

    private int startY = 10;		//根结点的Y，默认距离顶部10像素
    private int startX = 0;			//根结点的X，默认水平居中对齐

    private int childAlign;						//孩子对齐方式
    public static int CHILD_ALIGN_ABSOLUTE = 0;	//相对Panel居中
    public static int CHILD_ALIGN_RELATIVE = 1;	//相对父结点居中

    private Font font = new Font("微软雅黑", Font.BOLD,20);	//描述结点的字体

    private Color gridColor = Color.PINK;		//结点背景颜色
    private Color linkLineColor = Color.GREEN;	//结点连线颜色
    private Color stringColor = Color.BLUE;	//结点描述文字的颜色

    public Dimension theSize = new Dimension(800, 600);

    /**
     * 根据传入的Node绘制树，以绝对居中的方式绘制
     * @param n 要绘制的树
     */
    public TreePanel(TreeNode n){
        this(n,CHILD_ALIGN_ABSOLUTE);
    }

    /*
     * 设置要绘制时候的对齐策略
     * @param childAlign 对齐策略
     * @see tree.TreePanel#CHILD_ALIGN_RELATIVE
     * @see tree.TreePanel#CHILD_ALIGN_ABSOLUTE
     */

    public TreePanel(int childAlign){
        this(null,childAlign);
    }

    /**
     * 根据孩子对齐策略childAlign绘制的树的根结点n
     * @param n 要绘制的树的根结点
     * @param childAlign 对齐策略
     */
    public TreePanel(TreeNode n, int childAlign){
        super();
        setTree(n);
        this.childAlign = childAlign;
    }

    /**
     * 设置用于绘制的树
     * @param n 用于绘制的树的
     */
    public void setTree(TreeNode n) {
        tree = n;
        prepareTree();
    }

    public void prepareTree() {
        prepareTree(tree, 0);
    }

    public int prepareTree(TreeNode node, int layer) {
        node.layer = layer;

        if (node.children.size() == 0)
            return node.colCount;

        int sum = 0;
        for (TreeNode tempNode: node.children) {
            sum += prepareTree(tempNode, layer + 1);
        }

        node. colCount = sum;
        return sum;
    }


    //重写而已，调用自己的绘制方法
    public void paintComponent(Graphics g){
        startX = - gridWidth/2 + (tree.colCount * (gridWidth + hGap) - hGap) / 2 + 20;
        // startX = (getWidth()-gridWidth)/2;
        super.paintComponent(g);
        g.setFont(font);
        drawAllNode(tree, startX, g);
        theSize.width = tree.colCount * (gridWidth + hGap) - hGap + 150;
        setSize(theSize);
    }

    /**
     * 递归绘制整棵树
     * @param n 被绘制的Node
     * @param xPos 根节点的绘制X位置
     * @param g 绘图上下文环境
     */
    public void drawAllNode(TreeNode n, int x, Graphics g){
        int y = n.layer * (vGap + gridHeight) + startY;
        int fontY = y + gridHeight - 5;		//5为测试得出的值，你可以通过FM计算更精确的，但会影响速度

        if (y > theSize.height)
            theSize.height = y + 50;

        g.setColor(gridColor);
        g.fillRoundRect(x, y, gridWidth, gridHeight, 10, 10);	//画结点的格子

        g.setColor(stringColor);
        int biass = n.curNode.length();
        g.drawString(n.curNode, x+40-biass*5, fontY-2);		//画结点的名字

        if(n.children.size() != 0){
            ArrayList<TreeNode> c = n.children;
            int size = n.colCount;
            int tempPosx = childAlign == CHILD_ALIGN_RELATIVE
                    ? x + gridWidth/2 - (size * (gridWidth + hGap) - hGap) / 2
                    : (getWidth() - size*(gridWidth+hGap)+hGap)/2;

            ArrayList<Integer> columns = new ArrayList<Integer>();
            for(TreeNode node : c){
                columns.add(node.colCount);
                int i = 0;
                for (Integer in : columns)
                    i += in;
                double bias = i - node.colCount / 2.0;
                int newX = (tempPosx+(gridWidth+hGap) * (int)bias);	//孩子结点起始X
                g.setColor(linkLineColor);
                g.drawLine(x+gridWidth/2, y+gridHeight, newX+gridWidth/2, y+gridHeight+vGap);	//画连接结点的线
                drawAllNode(node, newX, g);
            }
        }
    }

    public Color getGridColor() {
        return gridColor;
    }

    /**
     * 设置结点背景颜色
     * @param gridColor 结点背景颜色
     */
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public Color getLinkLineColor() {
        return linkLineColor;
    }

    /**
     * 设置结点连接线的颜色
     * @param gridLinkLine 结点连接线的颜色
     */
    public void setLinkLineColor(Color gridLinkLine) {
        this.linkLineColor = gridLinkLine;
    }

    public Color getStringColor() {
        return stringColor;
    }

    /**
     * 设置结点描述的颜色
     * @param stringColor 结点描述的颜色
     */
    public void setStringColor(Color stringColor) {
        this.stringColor = stringColor;
    }

    public int getStartY() {
        return startY;
    }

    /**
     * 设置根结点的Y位置
     * @param startY 根结点的Y位置
     */
    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getStartX() {
        return startX;
    }

    /**
     * 设置根结点的X位置
     * @param startX 根结点的X位置
     */
    public void setStartX(int startX) {
        this.startX = startX;
    }


}


public class PaintTree extends JFrame{
    private TreeNode root;
    private JScrollPane contentPane;

    public PaintTree(TreeNode root){
        super("Graph of Tree");
        this.root = root;
        initComponents();
    }

    public void reprint(TreeNode root) {
        this.root = root;

        TreePanel panel = new TreePanel(TreePanel.CHILD_ALIGN_RELATIVE);
        panel.setTree(root);

        contentPane = new JScrollPane();
        contentPane.setViewportView(panel);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Set the size of the image panel, so the scroll bar will appear.
        panel.setPreferredSize(panel.theSize);
        panel.updateUI();

        add(contentPane);
    }

    public void initComponents(){
        TreePanel panel = new TreePanel(root, TreePanel.CHILD_ALIGN_RELATIVE);
        panel.setTree(root);

        contentPane = new JScrollPane();
        contentPane.setViewportView(panel);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Set the size of the image panel, so the scroll bar will appear.
        panel.setPreferredSize(panel.theSize);
        panel.updateUI();

        add(contentPane);
    }
}

