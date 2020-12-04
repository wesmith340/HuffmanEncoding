/*
	HW4:         Huffman Coding
	Author:      Weston Smith
	Class:       CS 316
	Date:        12/4/2020
 */

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * This class takes a string input from a user and creates a table of Huffman codes
 */
public class Huffman {
    // Declarations
    StringBuilder code = new StringBuilder();
    ArrayList<TableEntry> table = new ArrayList<>();

    /**
     * This class is one node in a Huffman tree
     */
    class Node implements Comparable<Node>{
        int num;
        char value;
        Node left, right;

        /**
         * No-Arg Constructor
         */
        public Node() {
            num = 0;
            value = 'a';
        }

        /**
         * Constructor
         * @param value
         */
        public Node(char value) {
            num = 1;
            this.value = value;
        }

        /**
         * Constructor
         * @param num
         */
        public Node(int num) {
            this.num = num;
            this.value = 'a';
        }

        /**
         * Increment num
         */
        public void increment()
        {   num++;      }

        /**
         * This method compares two nodes
         * @param node
         * @return
         */
        @Override
        public int compareTo(Node node) {
            return this.num - node.num;
        }
    }

    /**
     * This class is for one table entry in the Huffman table
     */
    class TableEntry {
        char value;
        String code;

        /**
         * Constructor
         * @param value
         * @param code
         */
        public TableEntry(char value, String code){
            this.value = value;
            this.code = code;
        }
    }

    /**
     * No-Arg Constructor
     */
    public Huffman () {
        // Get user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your string");
        String input = scanner.nextLine();

        // Create list of different characters used
        ArrayList<Node> list = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            boolean found = false;
            for (int k = 0; k < list.size()&&!found; k++){
                if (list.get(k).value == input.charAt(i)) {
                    found = true;
                    list.get(k).increment();
                }
            }
            if (!found) {
                list.add(new Node(input.charAt(i)));
            }
        }

        // Use priority queue to create a Huffman tree
        PriorityQueue<Node> minHeap = new PriorityQueue<>(list);
        while (minHeap.size() > 1) {
            Node a = minHeap.poll();
            Node b = minHeap.poll();
            Node parent = new Node(a.num+b.num);
            parent.left = a;
            parent.right = b;
            minHeap.add(parent);
        }

        // Make the Huffman table
        Node root = minHeap.poll();
        makeTable(root);

        // Check how much space you saved
        int fileSize = input.length()*8;
        int compressedSize = 0;
        for (int i = 0; i < input.length(); i++) {
            for (TableEntry t: table) {
                if (t.value == input.charAt(i)){
                    compressedSize+=t.code.length();
                }
            }
        }
        double ratio = (double)compressedSize/(double)fileSize;

        // Output
        for (TableEntry t: table) {
            System.out.println(t.value+": "+t.code);
        }
        System.out.println(String.format(
                "Compression ratio = %d/%d=%.5f",compressedSize,fileSize,ratio));
    }

    /**
     * Recursive method to create Huffman codes
     * @param n
     */
    void makeTable(Node n) {
        if (n.left == null && n.right == null){
            table.add(new TableEntry(n.value, code.toString()));
        } else {
            code.append(0);
            makeTable(n.left);
            code.append(1);
            makeTable(n.right);
        }
        if (code.length()>0)
            code.deleteCharAt(code.length()-1);
    }

    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {
        Huffman huff = new Huffman();
    }

}
