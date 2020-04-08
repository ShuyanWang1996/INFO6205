package edu.neu.coe.info6205.sort.simple;

import java.util.Iterator;

public class InsertionSort<X extends Comparable<X>> implements Sort<X> {

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        this.helper = helper;
    }

    public InsertionSort() {
        this(new Helper<>("InsertionSort"));
    }

    @Override
    public void sort(X[] xs, int from, int to) {

        for (int i = from; i < to; i++) {
            // Invariant 1: elements xs[from..i] are in order
            // TO BE IMPLEMENTED ...
            int j = i;
            while (j>=from && j<= to-1 && xs[j].compareTo(xs[j+1])>0){
                helper.swap(xs,from,to,j+1,j);
                j--;
            }
            // ... END IMPLEMENTATION
        }
    }

    //Author: Shuyan Wang
    @Override
    public X[] sort(X[] xs) {
        sort(xs,0,xs.length-1);
        return xs;
    }

    @Override
    public String toString() {
        return helper.toString();
    }

    @Override
    public Helper<X> getHelper() {
        return helper;
    }

    private final Helper<X> helper;
}
