package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        int N = 8000000;
//        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        System.out.println("Degree of parallelism: " + ParSort.forkJoinPool.getParallelism());
        System.out.println("Array size: " + N);
        Random random = new Random();
        int[] array = new int[N];
        ParSort.cutoff = N;
        ArrayList<Long> timeList = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            ParSort.cutoff = ParSort.cutoff/2;
//            ParSort.cutoff = 10000 * (j + 2);
            // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
            long time;
            long startTime = System.currentTimeMillis();
            for (int t = 0; t < 10; t++) {
                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                ParSort.sort(array, 0, array.length);
            }
            long endTime = System.currentTimeMillis();
            time = (endTime - startTime);
            timeList.add(time);
            //print time consuming result
            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10 times Time:" + time + "ms\t\tAverage Time:"+time/10+"ms");

        }
        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        String[] temp = new  String[2];
        temp[0] = "-P";
        temp[1] = "";
        while (xs.length > 0){
            if (xs[0].startsWith("-")) {
                xs = processArg(xs);
                break;
            }
            //to end up the dead loop
            else {
                System.out.println("The input format do not match");
                processArg(temp);
                break;
            }
        }
    }

    private static String[] processArg(String[] xs) {
//        System.out.println("processArg : "+xs);
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
//        System.out.println("processCommand : "+x+", "+y);
//        System.out.println("the compare result:"+x.equalsIgnoreCase("N"));
        if (x.equalsIgnoreCase("-N")) {
            setConfig(x, Integer.parseInt(y));
        }
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("-P")){ //noinspection ResultOfMethodCallIgnored
                ParSort.forkJoinPool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism());
                int depth = (int)(Math.log(ForkJoinPool.getCommonPoolParallelism()) / Math.log(2));
                int leaf = 0;
                for (int k = 1; k <= depth; k++){
                    leaf += k*2;
                }
                ParSort.boundary = leaf;
            }
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
//        ForkJoinPool.commonParallelism.parallelism = i;
//        System.out.println("main param: "+x+i);
        ParSort.forkJoinPool = new ForkJoinPool(i);
        int depth = (int)(Math.log(i)/ Math.log(2));
        int leaf = 0;
        for (int k = 1; k <= depth; k++){
            leaf += k*2;
        }
        ParSort.boundary = leaf;
//        System.out.println("t = "+i);
//        System.out.println("lg t = "+depth);
//        System.out.println("leaf nodes = "+leaf);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
