package com.yemuc;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * @Auther: yemuc
 * @Date: 2021/5/1
 * @Description: com.yemuc 状态节点的定义
 * @version: 1.0
 */
;
/**
节点状态的类
 */
public class StatusNode implements Cloneable{
    /**
    数码问题的规模，二维数组的行数/列数
     */
    public static final int NUM = 3;
    /**
    存储状态的二维矩阵
     */
    int[][] status;
    /**
    节点的父亲节点
     */
    StatusNode parent;   //记录父亲节点的



    /**
    根据空白位置，判断能否上移动
     */
    boolean upMovAble = true;
    /**
    根据空白位置，判断能否下移
     */
    boolean downMovable = true;
    /**
    根据空白位置，判断能否左移
     */
    boolean leftMovable = true;
    /**
    根据空白位置，判断能否右移
     */
    boolean rightMovable = true;
    /**
    空白的位置
     */
    int zero_x,zero_y;

    /**
    估计函数的值f
     */
    int fValue = 0;
    /**
    估计函数的值g，历史信息
     */
    int gValue = 0;
    /**
    估计函数的值h，对未来的估计
     */
    int hValue = 0;


    public void setfValue(int fValue) {
        this.fValue = fValue;
    }

    public void setgValue(int gValue) {
        this.gValue = gValue;
    }

    public void sethValue(int hValue) {
        this.hValue = hValue;
    }

    public int getfValue() {
        return fValue;
    }

    public int getgValue() {
        return gValue;
    }

    public int gethValue() {
        return hValue;
    }

    public StatusNode(){}
    /**
     * 构造函数
     * @param arr:int[][] 输入的状态矩阵数据
     */
    public StatusNode(int arr[][]){
        //构造函数
        //初始化数组的大小
        status = new int[NUM][NUM];
        for(int i=0;i<NUM;i++) {
            for (int j = 0; j < NUM; j++) {
                status[i][j] = arr[i][j];
            }
        }
        parent = null;
        setZeroPosition();
    }
    /**
     * 更新可移动数据，需要根据0的位置进行判断
     */
    public void updateMovableStatus() {

        if (zero_y == 0) {
            leftMovable = false;   // 0在第一列
        } else {
            leftMovable = true;
        }

        if (zero_y == 2) {
            rightMovable = false;   //0在最右列
        } else {
            rightMovable = true;
        }
        if (zero_x == 0) {
            upMovAble = false;    //0在第一行
        } else {
            upMovAble = true;
        }
        if (zero_x == 2) {
            downMovable = false;  //0在最下面一行
        } else{
            downMovable = true;
        }
    }
    public boolean isUpMovAble(){
        return upMovAble;
    }
    public boolean isDownMovable(){
        return downMovable;
    }
    public boolean isLeftMovable(){
        return leftMovable;
    }
    public boolean isRightMovable(){
        return rightMovable;
    }
    public boolean movable(){
        //是否存在可移动的位置
        return this.upMovAble || this.downMovable || this.leftMovable || this.rightMovable ;
    }
    /**
    移动后会产生新的状态，克隆原状态，在克隆状态上移动；这样可以用于构建path
     */
    public Object clone(){
        StatusNode node = null;
        int[][] tmp = new int[NUM][NUM];
        for(int i=0;i<NUM;i++)
            for(int j=0;j<NUM;j++){
                tmp[i][j] = this.getStatus(i,j);
            }
        node = new StatusNode(tmp);
        return node;
    }
    /**
    移动的规则
     @param move:移动指令，字符串形式
     */
    public void move(String move){
        //根据move指令进行移动
        //StatusNode result = new StatusNode(this.getStatus().clone());
        //这里需要重新获取0的位置
        setZeroPosition();   //更新0的信息
        int index_i=0,index_j=0;   //要与0交换的位置信息
        switch (move){
            case "up":
//                System.out.println(getZero_x()+" "+getZero_y());
                index_i = this.getZero_x() - 1;
                index_j = this.getZero_y();

                this.setStatus(this.zero_x,this.zero_y,this.getStatus(index_i,index_j));

                //补上0
                this.setStatus(index_i,index_j,0);
//                this.printStatus();

                break;
            case "down":

                index_i = this.getZero_x() + 1;
                index_j = this.getZero_y();
                this.setStatus(this.zero_x,this.zero_y,this.getStatus(index_i,index_j));
                //补上0
                this.setStatus(index_i,index_j,0);
                //result.printStatus();  //输出看看
                break;
            case "left":
                index_i = this.getZero_x();
                index_j = this.getZero_y() - 1;
                this.setStatus(this.zero_x,this.zero_y,this.getStatus(index_i,index_j));
                //补上0
                this.setStatus(index_i,index_j,0);
                //result.printStatus();  //输出看看
                break;
            case "right":
                index_i = this.getZero_x();
                index_j = this.getZero_y() + 1;
                this.setStatus(this.zero_x,this.zero_y,this.getStatus(index_i,index_j));
                //补上0
                this.setStatus(index_i,index_j,0);
                //result.printStatus();  //输出看看
                break;
        }

//        return result;
    }

    /**
     * 输出status矩阵信息
     */
    public void printStatus(){
        System.out.println("输出状态信息：");
        for(int[] a:status){
            for(int e:a){
                System.out.print(e+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    /**
     * 比较两个节点的状态是否相等
     * @param a: StatusNode  判断输入的节点a的状态信息是否和当前调用的节点状态信息相等
     * @return boolean:是否相等的
     */
    public boolean isEqual(StatusNode a){
        for(int i=0;i<NUM;i++){
            for(int j=0;j<NUM;j++){
                if(this.status[i][j] != a.status[i][j])
                    return false;
            }
        }
        return true;
    }

    /**
     * 获取空白的位置并更新可移动的四个状态值
     */
    public void setZeroPosition(){
        String position = getZeroPosition();
        this.setZero_x((int)(position.charAt(1)-'0'));
        this.setZero_y((int)(position.charAt(3)-'0'));
        updateMovableStatus();
    }

    /**
     * 获取位置
     * @return 字符串形式的位置信息
     */
    private String getZeroPosition(){
        String position = "";
        for(int i=0;i<StatusNode.NUM;i++)
            for(int j=0;j<StatusNode.NUM;j++){
                if(this.status[i][j] == 0){
                    position = "("+i+","+j+")";
                    break;
                }
            }
//        System.out.println(position);
        return position;
    }
    /**
     * 获取某个元素x的位置，用于计算h估计值的，获取某个元素在目标状态中的位置信息
     * @param x: int 待查询的数据
     * @return string: 位置信息 （x,y）
     */
    public String getXPosition(int x){
        String position = "";
        for(int i=0;i<StatusNode.NUM;i++){
            for(int j=0;j<StatusNode.NUM;j++){
                if(this.status[i][j] == x){
                    position = "("+i+","+j+")";
                    return position;
                }
            }
        }
        return position;
    }

    public int[][] getStatus() {
        return status;
    }
    public int getStatus(int i,int j){
        return status[i][j];
    }
    public void setStatus(int[][] status) {
        this.status = status;
    }
    public void setStatus(int i,int j, int value) {
        this.status[i][j] = value;
    }
    public StatusNode getParent() {
        return parent;
    }

    public void setParent(StatusNode parent) {
        this.parent = parent;
    }

    public int getZero_x() {
        return zero_x;
    }

    public int getZero_y() {
        return zero_y;
    }

    public void setZero_x(int zero_x) {
        this.zero_x = zero_x;
    }

    public void setZero_y(int zero_y) {
        this.zero_y = zero_y;
    }
}
