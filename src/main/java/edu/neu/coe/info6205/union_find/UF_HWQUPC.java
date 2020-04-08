/**
 * Original code:
 * Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 * <p>
 * Modifications:
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Height-weighted Quick Union with Path Compression
 */
public class UF_HWQUPC implements UF {
    /**
     * Ensure that site p is connected to site q,
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     */
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param  n the number of sites
     * @param pathCompression whether to use path compression
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n, boolean pathCompression) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 1;
        }
        this.pathCompression = pathCompression;
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     * This data structure uses path compression
     *
     * @param  n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n) {
        this(n, true);
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], height[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int components() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param  p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        int root = p;
        // TO BE IMPLEMENTED ...
        while(root != getParent(root)) root = getParent(root);
         // ... END IMPLEMENTATION
        return root;
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
    }

    @Override
    public int size() {
        return parent.length;
    }

    /**
     * Used only by testing code
     * @param pathCompression true if you want path compression
     */
    public void setPathCompression(boolean pathCompression) {
        this.pathCompression = pathCompression;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("UF_HWQUPC:");
        stringBuilder.append("\n  count: ").append(count);
        stringBuilder.append("\n  path compression? ").append(pathCompression);
        stringBuilder.append("\n  parents: ").append(Arrays.toString(parent));
        stringBuilder.append("\n  heights: ").append(Arrays.toString(height));
        return stringBuilder.toString();
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    private void updateParent(int p, int x) {
        parent[p] = x;
    }

    private void updateHeight(int p, int x) {
        height[p] += height[x];
    }

    /**
     * Used only by testing code
     * @param i the component
     * @return the parent of the component
     */
    private int getParent(int i) {
        return parent[i];
    }

    private final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components
    private boolean pathCompression = false;

    private void mergeComponents(int i, int j) {
        // TO BE IMPLEMENTED make shorter root point to taller one
        int ri = find(i);
        int rj = find(j);
        if (height[ri] == height[rj]) {
            if (ri < rj){
                updateParent(rj,ri);
                updateHeight(rj,ri);
            }
            else{
                updateParent(ri,rj);
                updateHeight(ri,rj);
            }
        }
        else if (height[ri] < height[rj]) {
            updateParent(ri,rj);
            updateHeight(ri,rj);
        }
        else {
            updateParent(rj,ri);
            updateHeight(rj,ri);
        }
        if (pathCompression){
            for (int k = 0; k < size(); k++){
                doPathCompression(k);
            }
        }
        // ... END IMPLEMENTATION
    }

    /**
     * This implements the single-pass path-halving mechanism of path compression
     */
    private void doPathCompression(int i) {
        // TO BE IMPLEMENTED update parent to value of grandparent
        parent[i] = parent[parent[i]];
        // ... END IMPLEMENTATION
    }


    // Take an integer value n from the command line
    // Generate random pairs of integers between 0 and n-1
    // Calli connected() to determine if they are connected and union() if not
    // Loop until all sites are connected
    // Print the number of connections generated
    static public int count(int n){
        if (n <= 0){
            System.out.println("The param should be bigger than 0.");
            return 0;
        }

        int connections = 0;
        boolean isConnected = false;
        UF_HWQUPC uf_hwqupc = new UF_HWQUPC(n);
//        System.out.println(n+" sites are created.");
        while (!isConnected){
            if (uf_hwqupc.components() == 1) isConnected = true;
            int i,j;
            i = (int) ((Math.random() * n));
            j = (int) ((Math.random() * n));
            while(i == j) j = (int) ((Math.random() * n));
            uf_hwqupc.connect(i,j);
            //System.out.println("Connect: ( "+i+" , "+j+" ).");
            connections++;
        }
//        System.out.println("The number of connections is : "+connections);
        return connections;
    }

    public static void main(String[] args) {

        // With input n.
//        System.out.println("Please enter an Integer n (n > 0): ");
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        UF_HWQUPC.count(n);

        // Without input n.
//        int times = 0;
//        for (int i = 2000; times < 7 ; i *= 2){
//            UF_HWQUPC.count(i);
//            times++;
//        }

        // Hypothesis
//        double[] ns = new double[20];
//        double[] hs = new double[20];
//        int n = 100;
//        for (int i = 0; i < 20; i ++){
//            ns[i] = (double) UF_HWQUPC.count(n);
//            hs[i] = Math.log(n)*n/2;
//            System.out.println("Connections: "+ns[i]);
//            System.out.println("1/2 n ln n : "+hs[i]);
//            System.out.println("----------------- n : "+n+" -----------------");
//            n = n*2;
//        }
        int[] ns = new  int[10];
        double[] connections = new double[10];
        double[] lns = new double[10];
        double[] errors =  new double[10];
        for(int i = 0; i < 10; i++){
            ns[i] = (int) ((Math.random() * 10000));
            lns[i] = Math.log(ns[i])*ns[i]/2;
            connections[i] = 0;
            for (int j = 0; j < 500; j++){
                connections[i] += UF_HWQUPC.count(ns[i]);
//                System.out.println(connections[i]);
            }
            connections[i] /= 500;
            errors[i] = Math.abs(connections[i]-lns[i])/lns[i]*100;
            System.out.println("n : "+ns[i]);
            System.out.println("the average connections among 500 times : "+connections[i]);
            System.out.println("the hypothesis value 1/2*n*ln(n)        : "+lns[i]);
            System.out.println("the error percentage                    : "+errors[i]+" %");
            System.out.println("--------------------");
        }


    }
}
