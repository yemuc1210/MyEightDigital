package com.yemuc;

import java.awt.*;
import java.util.*;

/**
 * @Auther: yemuc
 * @Date: 2021/5/1
 * @Description: com.yemuc A算法
 * @version: 1.0
 */

/**
 * A*算法类，其中包含了BFS的求解方法
 */
public class AStarAlgorithm {
    /**
     * 初始状态的节点
     */
    StatusNode s0;
    /**
     * 目标状态的节点
     */
    StatusNode sg;
    /**
     * 存储在计算过程中的生成的节点，单个节点的扩展数据
     */
    StatusNode[] extendNode;
    /**
     * 用于存储每个节点可扩展的节点数，中间变量，每次使用前归零
     */
    int nextNum = 0;
    /**
     * 保存每一层的扩展节点数
     */
    ArrayList<Integer> nextNumArr = new ArrayList<Integer>();
    /**
     * 任务要求，比较扩展节点数，这里计算一下总和吧 ，节点数
     */
    int extendNum = 0;

    /**
     * A*算法中常用的open表和closed表  open表存储待访问节点
     */
    Queue<StatusNode> open;
    /**
     * A*算法中常用的open表和closed表 closed表存储访问过的
     */
    ArrayList<StatusNode> closed;
    /**
    *重写比较器的规则，这个优先权队列会使用到
     */
    static Comparator<StatusNode> cmp = new Comparator<StatusNode>() {
        @Override
        public int compare(StatusNode o1, StatusNode o2) {
            return o1.getfValue() - o2.getfValue();
        }
    };
    /**
    *构造函数
     * @param s0:初始状态
     * @param sg:目标状态
     */
    public AStarAlgorithm(StatusNode s0, StatusNode sg) {
        this.s0 = s0;
        this.sg = sg;

        this.extendNode = new StatusNode[4];    //扩展节点的数组

        open = new PriorityQueue<StatusNode>(cmp);
        closed = new ArrayList<StatusNode>();
    }
    /**
    全局择优算法：一般需要用到Open表和Closed表
    （1）初始节点s0放入Open表，并计算f(s0)=g(s0)+h(s0)
    (2)若Open为空，则问题无解，exit
    （3）取Open第一个节点放入Closed表,记为N;
    (4)考察N是否为目标节点，若是则success
    (5)若N不可扩展，转（2）
    （6）扩展N，生成子节点Ni，计算估值，并设置好parent，放入Open
    （7）根据估值，对Open表排序（重写排序规则是必要的）
    （8）转（2）
     * @return 解决的结果，true表示有解并解决
    */
    public boolean solveByAlgorithmA(int isBFS,int howToCalH){
        if(s0.isEqual(sg)) {
            System.out.println("s0=sg,problem is already solved!");
            return true;
        }
        if(!isResolvable()){
            System.out.println("proble is not resolvable!");
            return false;
        }
        System.out.println("using algorithm A to solve eight digital problem..........");

//        int isBFS = 1;
//        int howToCalH = 0;

        StatusNode N;
        open.add(this.s0);   //步骤（1）
//        open.push(this.s0);   //初始状态入栈
        while(!open.isEmpty()){  //步骤（2）Open表空，结束

//            N = open.pop();
            N = open.remove();        //取出估值最小的
            closed.add(N);//步骤（3）   存放已经扩展的节点
            //步骤（4）  判断是否是目标状态
            if(isOver(N)){
                System.out.println("problem is solved! Now print route.......");
                getRoute(N);
                System.out.println("扩展节点的情况是："+nextNumArr);
                System.out.println("生成节点的情况是："+extendNum);
                return true;
            }
            //否则步骤（5），判断是否可扩展  遍历四种方向上的移动
            if(!isExtended(N,isBFS,howToCalH)){
                continue;   // 不可扩展，继续，转步骤（2）
            }
            System.out.println("nextNum="+nextNum);
//            for(StatusNode s:extendNode){
//                s.printStatus();
//            }
            //否则，转步骤（6），这里需要获得扩展节点啊
            for(int i=0;i<nextNum;i++){
//                open.push(node);    //扩展节点存入open
                open.add(extendNode[i]);
            }
            //步骤（7）排序，这里需要自定义排序规则，用比较器吧
            //直接使用优先权队列，加入时就会自动排序了
            //转步骤（2）
        }
        System.out.println("fail, because it desn't return before!");

        return false;
    }

    /**
    *这里是一种估计h值的方法,g值一般就是节点所在层数，在扩展节点时就可以更新了
    *不同A*算法的区别就是h值的区别，这里给出使用曼哈顿距离的
     * @param current:需要求h值的节点
     */
    public void calculateHValueByMH(StatusNode current){
        //估计当前节点 字节点ni 到目标节点的h   P（n）:各数码与其目标位置之间的距离之和   上下左右移动，忽略障碍
        //就是计算曼哈顿距离吧  SUM(|Xi(a) - Xi(b)|   i=1,...,n
        for(int i=0;i<StatusNode.NUM;i++){
            for(int j=0;j<StatusNode.NUM;j++){
                //跳过0
                if(current.getStatus(i,j) != 0){
                    //需要两个元素的位置，其实也就是目标状态sg中 当前元素应该所处的位置
                    String position = this.sg.getXPosition(current.getStatus(i,j));
                    int index_i = position.charAt(1) - '0';
                    int index_j = position.charAt(3) - '0';
                    int distance = Math.abs(index_i - i) + Math.abs(index_j - j);
                    current.sethValue( current.gethValue() + distance );
                }
            }
        }
    }
    /**
     * 使用欧氏距离计算节点h值
     * @param current :待求的节点
     */
    public void calculateHValueByEular(StatusNode current){
        //计算公式，sqrt((xa-xb)^2+(ya-yb)^2)
        for(int i=0;i<StatusNode.NUM;i++){
            for(int j=0;j<StatusNode.NUM;j++){
                //跳过0
                if(current.getStatus(i,j) != 0){
                    //需要两个元素的位置，其实也就是目标状态sg中 当前元素应该所处的位置
                    String position = this.sg.getXPosition(current.getStatus(i,j));
                    int index_i = position.charAt(1) - '0';
                    int index_j = position.charAt(3) - '0';
                    int distance = (int)Math.sqrt(Math.pow(index_i - i,2))
                            + (int)Math.sqrt(Math.pow(index_j - j,2));    //实际上计算出的是整数，所以取整好了，省的麻烦去修改h值的属性
    //                    System.out.println("欧氏距离="+distance);
                    current.sethValue( current.gethValue() + distance );
                }
            }
        }
    }

    /**
     * 使用A算法“不在其位”的估价方式
     * @param current:待求节点
     */
    public void calculateHValueByA(StatusNode current){
        current.sethValue(0);
        for(int i=0;i<StatusNode.NUM;i++){
            for(int j=0;j<StatusNode.NUM;j++){
                if(current.getStatus(i,j) != 0) {
                    if (current.getStatus(i, j) != sg.getStatus(i, j))   //不在其位的    A算法
                        current.sethValue(current.gethValue() + 1);
                }
            }
        }
    }

    /**
     * 根据输入的标志控制更新f值的方法
     * @param current：待求节点
     * @param isBFS  是否使用BFS的估计方法 0否 1是
     * @param howToCalH 如何求解h的估值，0 曼哈顿  1 欧拉  2 A算法
     */
    public void updateValues(StatusNode current, int isBFS, int howToCalH){
        //g值每一次扩展就可以自动计算，这里就不用计算，下面更新f值
        switch (isBFS){
            case 0:
                switch (howToCalH){
                    case 0:
                        //使用曼哈顿距离计算
                        calculateHValueByMH(current);
                        break;
                    case 1:
                        calculateHValueByEular(current);    //4 2 1 2
                        break;
                    case 2:
                        calculateHValueByA(current);
                        break;
                }
                current.setfValue(current.getgValue() + current.gethValue());
                break;
            case 1:
                current.setfValue(current.getgValue() + 0);
        }

    }

    /**
     * 判断节点n是否可扩展，并完成扩展操作
     * @param n 当前节点，open表中取出的节点
     * @param isBFS 是否使用BFS的估计方法 0否 1是
     * @param howToCalH 如何求解h的估值，0 曼哈顿  1 欧拉  2 A算法
     * @return true表示可扩展，并已经完成扩展
     */
    public boolean isExtended(StatusNode n,int isBFS,int howToCalH){
        //扩展节点，更新估价函数
        nextNum = 0;   //全局变量
        n.setZeroPosition();  //更新一下0的位置,同时也会更新可移动状态
//        n.printStatus();
//        System.out.println(n.isUpMovAble()+" "+n.isDownMovable()+" "+n.isLeftMovable()+" "+n.isRightMovable());
        if(n.isUpMovAble()){   //上移动
            StatusNode up = (StatusNode) n.clone();  //得到克隆状态
            System.out.print("原节点的cost:"+n.getfValue()+" "+n.getgValue()+" "+n.gethValue()+" ");

            //System.out.println(up+" "+n);  //com.yemuc.StatusNode@4554617c com.yemuc.StatusNode@74a14482
            up.move("up");//将n中0的位置上，产生一个新的状态
//            System.out.println("相山移动");
//            up.printStatus();
            //ok，产生了一个新的状态，有必要检查这个状态是否出现过closed
            if(!isEverArrived(up)){   //若没有出现过
                //下面更新parent children信息
                up.setParent(n);
//            n.setChildren(up);    //这个没意义，因为每一次多个扩展，但是这个只能指向一个，删除children变量吧
                //需要将扩展的节点存储数组，这才是全局择优的算法
                this.extendNode[nextNum] = up;
                nextNum ++;
                //计算估值,g值可以直接+1   计算子节点的估计值
                up.setgValue(n.getgValue() + 1);    //g值也就是深度了
                //h值怎么算呢？
                updateValues(up,isBFS,howToCalH);

                System.out.println("up"+up.getfValue()+" "+up.getgValue()+" "+ up.gethValue());
            }
        }
        if(n.isDownMovable()){
            StatusNode down = (StatusNode) n.clone();  //得到克隆状态
            System.out.print("原节点的cost:"+n.getfValue()+" "+n.getgValue()+" "+n.gethValue()+" ");
//            System.out.println("down:");
//            down.printStatus();    //这里出错了，克隆的是up的信息
            down.move("down");//将n中0的位置上，产生一个新的状态

            if(!isEverArrived(down)) {
                //下面更新parent children信息
                down.setParent(n);
                //            n.setChildren(down);
                this.extendNode[nextNum] = down;
                nextNum++;
                down.setgValue(n.getgValue() + 1);
                updateValues(down,isBFS,howToCalH);
                System.out.println("down"+down.getfValue() + " " + down.getgValue() + " " + down.gethValue());
            }
        }
        if(n.isLeftMovable()){
            StatusNode left = (StatusNode) n.clone();  //得到克隆状态
            System.out.print("原节点的cost:"+n.getfValue()+" "+n.getgValue()+" "+n.gethValue()+" ");
//            System.out.println("left");
//            left.printStatus();
            left.move("left");//将n中0的位置上，产生一个新的状态
            if(!isEverArrived(left)) {   //若没有出现过
                //下面更新parent children信息
                left.setParent(n);
                //            n.setChildren(left);
                this.extendNode[nextNum] = left;
                nextNum++;
                left.setgValue(n.getgValue() + 1);
                updateValues(left,isBFS,howToCalH);
                System.out.println("left"+left.getfValue() + " " + left.getgValue() + " " + left.gethValue());
            }
        }
        if (n.isRightMovable()){
            StatusNode right = (StatusNode) n.clone();  //得到克隆状态
            System.out.print("原节点的cost:"+n.getfValue()+" "+n.getgValue()+" "+n.gethValue()+" ");
            right.move("right");//将n中0的位置上，产生一个新的状态
            if(!isEverArrived(right)) {
                //下面更新parent children信息
                right.setParent(n);
                //            n.setChildren(right);
                this.extendNode[nextNum] = right;
                nextNum++;
                right.setgValue(n.getgValue() + 1);
                updateValues(right,isBFS,howToCalH);
                System.out.println("right"+right.getfValue() + " " + right.getgValue() + " " + right.gethValue());
            }
        }
        //更新nextNumArr
        nextNumArr.add(nextNum);
        extendNum += nextNum;
        return nextNum != 0;
    }

    /**
     * 判断当前节点是否出现过，若出现过，则不加入extendNode数组
     * @param current  当前的节点
     * @return true表示出现过
     */
    public boolean isEverArrived(StatusNode current){
        for(int i=0;i<closed.size();i++){
            if(closed.get(i).isEqual(current) ){
                return true;
            }
        }
        return false;
    }

    /**
     * 从底向上得到路径
     * @param n  当前节点
     */
    private void getRoute(StatusNode n) {
        //从当前节点向上，得到路径
        StatusNode tmp = n;
        int step = 0;
        while(tmp != null){
            tmp.printStatus();
            tmp = tmp.getParent();
            step++;
        }
        step --;
        System.out.println("steps = "+step);
    }

    /**
     * 根据逆序数，判断问题是否可解
     * @return true表示可解，问题可以继续求
     */
    public boolean isResolvable(){
        //通过逆序数是否相等来判断
        //首先转化为一维数字，字符串
        String s1 = arrToStr(s0.getStatus());
        String s2 = arrToStr(sg.getStatus());

        int a = getInverseNumber(s1) % 2;  //同奇同偶才可解
        int b = getInverseNumber(s2) % 2;

        return a == b;
    }

    /**
     * 私有函数，求解矩阵的逆序数
     * @param s 二维矩阵转化为一维数组，因为数值都只有一位，所以可以用字符串存储
     * @return int 逆序数
     */
    int getInverseNumber(String s){
        int result = 0;

        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '0')
                continue;
            for(int j=0;j<i;j++){
                if(s.charAt(i) < s.charAt(j)) {  //因为都是数字，直接比较ASCII码就好了
                    //System.out.println(s.charAt(i)+" "+s.charAt(j));
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * 二维数组变一维字符串
     * @param arr 二维数组
     * @return 字符串
     */
    String arrToStr(int[][] arr){
        String s = "";
        for(int[] line:arr)
            for(int e:line)
                s += e;
//        System.out.println(s);
        return s;
    }

    /**
     * 判断求解是否结束，比较当前状态和目标状态
     * @param current
     * @return
     */
    public boolean isOver(StatusNode current){
        //比较当前节点的状态信息和目标节点的状态信息
        return current.isEqual(sg);
    }

}
