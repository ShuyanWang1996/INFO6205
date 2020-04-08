package edu.neu.coe.info6205.sort.par;

import edu.neu.coe.info6205.sort.simple.Helper;
import edu.neu.coe.info6205.sort.simple.MergeSortBasic;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;

    public static ForkJoinPool forkJoinPool;
    public static int count = 0;
    public static int boundary;

    public static void sort(int[] array, int from, int to) {
        if (to - from < cutoff) Arrays.sort(array, from, to);
        else {
            //to stop recursion
            if (count >= boundary) Arrays.sort(array,from,to);
            count ++;
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2); // TO IMPLEMENT
            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to); // TO IMPLEMENT
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                        int[] result = new int[xs1.length + xs2.length];
                        // TO BE IMPLEMENTED ...
                        System.arraycopy(xs1,0,result,0,xs1.length);
                        System.arraycopy(xs2,0,result,xs1.length,xs2.length);
                        int[] aux = new int[xs1.length + xs2.length];
                        System.arraycopy(result,0,aux,0,result.length);
                        //merge implementation copy from MergeSortBasic.merge() to make it available for int type
                        int i = 0;
                        int j = xs1.length;
                        for (int k = 0; k < result.length; k++) if (i >= xs1.length) result[k] = aux[j++];
                        else if (j >= result.length) result[k] = aux[i++];
                        else if (aux[j] < aux[i]) result[k] = aux[j++];
                        else result[k] = aux[i++];
                        // ... END IMPLEMENTATION
                        return result;
                    });
            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
//            System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int[] result = new int[to - from];

                    // TO BE IMPLEMENTED ...
                    if (to - from > cutoff) sort(array,from,to);
                    Arrays.sort(array,from,to);
                    System.arraycopy(array,from,result,0,to-from);
                    // ... END IMPLEMENTATION
                    return result;
                },forkJoinPool
        );
    }
}