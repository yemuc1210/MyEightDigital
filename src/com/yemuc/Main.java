package com.yemuc;

import java.util.Scanner;

/**
 * 主类，运行main函数
 */
public class Main {

    public static StatusNode init = null;
    public static StatusNode target = null;

    /**
     * 允许自输入初始状态和目标状态信息
     */
    public static void input(){
        System.out.print("use default data, so that test quickly. input(y or n):");
        Scanner in = new Scanner(System.in);
        char choice = in.next().charAt(0);
        if(choice == 'y'){
//            int[][] source = new int[][]{
//            {2,3,1},{5,0,8},{4,6,7}
//        };
//        int[][] des = new int[][]{
//            {1,2,3},{8,0,4},{7,6,5}
//        };    //这是一组不可解的状态
            int[][] source = new int[][]{
                    {2,8,3},{1,0,4},{7,6,5}
            };
            int[][] des = new int[][]{
                    {1,2,3},{8,0,4},{7,6,5}
            };   //这是一组可解的
            init = new StatusNode(source);
            init.printStatus();
            target = new StatusNode(des);
            target.printStatus();
            return ;
        }else {
            System.out.println("输入起始节点的状态信息，形如矩阵形式");
            int[][] source = new int[StatusNode.NUM][StatusNode.NUM];
            for(int i=0;i<StatusNode.NUM;i++)
                for(int j=0;j<StatusNode.NUM;j++)
                    source[i][j] = in.nextInt();
            init = new StatusNode(source);
            System.out.println("输入目标节点的状态信息，形如矩阵形式");
            int[][] des = new int[StatusNode.NUM][StatusNode.NUM];
            for(int i=0;i<StatusNode.NUM;i++)
                for(int j=0;j<StatusNode.NUM;j++)
                    des[i][j] = in.nextInt();


            target = new StatusNode(des);
            init.printStatus();
            target.printStatus();
        }
        in.close();
    }

    public static void main(String[] args) {
	// write your code here
        System.out.println("hello world");
        input();
        AStarAlgorithm aa = new AStarAlgorithm(init,target);
        aa.solveByAlgorithmA(1,2);

    }
}
