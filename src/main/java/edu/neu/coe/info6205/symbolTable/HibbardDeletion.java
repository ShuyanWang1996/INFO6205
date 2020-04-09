package edu.neu.coe.info6205.symbolTable;

import java.util.Random;

public class HibbardDeletion {

    public static void main(String[] args) {
        HibbardDeletion hibbardDeletion = new HibbardDeletion();
        Random random = new Random();
        BSTSimple<Integer,Integer> bstSimple = new BSTSimple<>();
//         N: size of nodes
        int N = 40;
        // M: times of insertion and deletion
        hibbardDeletion.test(N,50,random);
        hibbardDeletion.test(N,55,random);
        hibbardDeletion.test(N,60,random);
        hibbardDeletion.test(N,65,random);
        hibbardDeletion.test(N,70,random);
        hibbardDeletion.test(N,75,random);
        hibbardDeletion.test(N,80,random);
    }


    public void test(int N, int M, Random random){
        BSTSimple<Integer,Integer> bstSimple = new BSTSimple<>();
        Integer[] keys = new Integer[N];
        Integer[] values = new Integer[N];
        for (int i = 0; i < N ; i ++){
            keys[i] = random.nextInt(N);
            values[i] = random.nextInt(N);
            bstSimple.put(keys[i],values[i]);
        }
        for (int i = 0; i < M ; i ++){
            Integer rand1 = random.nextInt(N*10);
            Integer rand2 = random.nextInt(N*10);
            if (HibbardDeletion.insertOrDelete(random)){
                bstSimple.put(rand1,rand2);
            }else{
                bstSimple.delete(rand1);
            }
        }
        System.out.println("N = "+N+", M = "+M);
        System.out.println("Size of BST = "+bstSimple.size());
        System.out.println("Depth of BST = "+bstSimple.depth());
        double proportion = bstSimple.depth()/Math.sqrt((double) bstSimple.size());
//        System.out.println(proportion);
        System.out.println("Depth is "+proportion+" proportional to Square of Size");
        System.out.println("-------------------------------------");
    }

    static boolean insertOrDelete(Random random){
        return (random.nextBoolean());
    }

}
