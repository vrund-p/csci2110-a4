import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import java.io.PrintWriter;
import java.util.*;

public class Huffman {

    /**
     * Code
     * provided from previous version and modified for 2020.
     */
    public static void encode() throws IOException {
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);
        ArrayList<Pair> pairs = new ArrayList<Pair>();

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/encode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\z");
        String text = input.next();

        // close input file
        input.close();

        // initialize Array to hold frequencies (indices correspond to
        // ASCII values)
        int[] freq = new int[256];
        // concatenate/sanitize text String and create character Array
        // nice that \\s also consumes \n and \r
        // we can add the whitespace back in during the encoding phase

        char[] chars = text.replaceAll("\\s", "").toCharArray();

        // count character frequencies
        for (char c : chars)
            freq[c]++;


        //Your work starts here************************************8


        for (int i = 0; i < 256; i++) {
            if (freq[i] != 0) {
                // this method of rounding is good enough
                Pair p = new Pair((char) i, Math.round(freq[i] * 10000d / chars.length) / 10000d);
                pairs.add(p);
            }
        }


        //Apply the huffman algorithm here and build the tree ************************************

        // two queues for storing list
        Collections.sort(pairs);
        Queue<BinaryTree<Pair>> listS = new Queue<BinaryTree<Pair>>();
        Queue<BinaryTree<Pair>> listT = new Queue<BinaryTree<Pair>>();

        // for-each loop to add elements in S
        for (Pair p : pairs) {
            BinaryTree<Pair> temp = new BinaryTree<Pair>();
            temp.makeRoot(p);
            listS.enqueue(temp);

        }

        // two trees of type Binary tree
        BinaryTree<Pair> A = null;
        BinaryTree<Pair> B = null;

        double sumOfProb = 0;

        while (!listS.isEmpty()) {

            //If T is empty, A and B are respectively the front and next to front entries of S, adding them to S
            if (listT.isEmpty()) {
                A = listS.dequeue();
                B = listS.dequeue();

//                sumOfProb = A.getData().getProb() + B.getData().getProb();
//                Pair pair = new Pair('⁂', sumOfProb);
//                P.makeRoot(pair);


            //If T is not empty, finding the smaller weight tree of the trees in front of S and in front of T.
            } else {
                if (listT.peek().getData().compareTo(listS.peek().getData()) < 0) {
                    A = listS.dequeue();
                } else {
                    A = listT.dequeue();
                }

                if (listT.peek().getData().compareTo(listS.peek().getData()) < 0) {
                    B = listS.dequeue();
                } else {
                    B = listT.dequeue();
                }
            }

            // creating a new tree and attaching A and B as subtrees
            BinaryTree<Pair> P = new BinaryTree<Pair>();

            // The weight of the root is the combined weights of the roots of A and B.
            sumOfProb = A.getData().getProb() + B.getData().getProb();
            Pair pair = new Pair('⁂', sumOfProb);
            P.makeRoot(pair);

            P.attachLeft(A);
            P.attachRight(B);
            listT.enqueue(P);
        }

        // If the number of elements in queue T is greater than 1, dequeuing two nodes at a time,
        while (listT.size() > 1) {
            sumOfProb = 0.0;
            BinaryTree<Pair> P = new BinaryTree<>();

            A = listT.dequeue();
            B = listT.dequeue();

            Pair pair = new Pair('⁂', sumOfProb);
            P.makeRoot(pair);
            P.attachLeft(A);
            P.attachRight(B);
            listT.enqueue(P);
        }

        //can be used to get the codes
        //String[] codes = findEncoding(HuffmanTree);
        String[] codes = findEncoding(listT.dequeue());

        PrintWriter output = new PrintWriter("Huffman.txt");
        output.println("Symbol" + "\t" + "Prob." + "\t" + "Huffman Code" + "\n");
        for (Pair p : pairs) {
            output.println(p.getValue() + "\t" + p.getProb() + "\t" + codes[p.getValue()]);
        }
        output.close();

        String encodedValue = "";
        char[] stringCh = chars;
        for (char c : stringCh) {
            encodedValue += codes[c];
        }
        output = new PrintWriter("Encoded.txt");
        output.println(encodedValue);
        output.close();

    }


    public static void decode() throws IOException {
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/decode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\Z");
        String text = input.next();
        // ensure all text is consumed, avoiding false positive end of
        // input String
        input.useDelimiter("\\z");
        text += input.next();


        // close input file
        input.close();

        // capture file information from user and read file
        System.out.print("Enter the filename of document containing Huffman codes: ");
        f = sc.nextLine();

        // create File object and build text String
        file = new File(f);
        input = new Scanner(file).useDelimiter("\\Z");
        String codes = input.next();

        // close input file
        input.close();

        //Your work starts here********************************************


        Scanner ls = new Scanner(codes);

        //initialize a String variable to reference the text captured from the encoded file
        ArrayList<Character> chart = new ArrayList<Character>();
        ArrayList<String> code = new ArrayList<String>();

        ls.nextLine();
        ls.nextLine();
        while (ls.hasNextLine()) {
            char c = ls.next().charAt(0);
            ls.next();
            String s = ls.next();
            chart.add(c);
            code.add(s);

        }

        PrintWriter output = new PrintWriter("Decoded.txt");
        String str = "";

        for (char t : text.toCharArray()) {
            str += t;
            if (code.contains(str)) {
                output.print(chart.get(code.indexOf(str)));
                str = "";

            }
        }
        output.close();
    }

    // the findEncoding helper method returns a String Array containing
    // Huffman codes for all characters in the Huffman Tree (characters not
    // present are represented by nulls)
    // this method was provided by Srini (Dr. Srini Sampalli). Two versions are below, one for Pairtree and one for BinaryTree


    private static String[] findEncoding(BinaryTree<Pair> bt) {
        String[] result = new String[256];
        findEncoding(bt, result, "");
        return result;
    }


    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix) {
        // test is node/tree is a leaf
        if (bt.getLeft() == null && bt.getRight() == null) {
            a[bt.getData().getValue()] = prefix;
        }
        // recursive calls
        else {
            findEncoding(bt.getLeft(), a, prefix + "0");
            findEncoding(bt.getRight(), a, prefix + "1");
        }
    }


    private static String[] findEncoding(Peartree pt) {
        // initialize String array with indices corresponding to ASCII values
        String[] result = new String[256];
        // first call from wrapper
        findEncoding(pt, result, "");
        return result;
    }

    private static void findEncoding(Peartree pt, String[] a, String prefix) {
        // test is node/tree is a leaf
        if (pt.getLeft() == null && pt.getRight() == null) {
            a[pt.getValue()] = prefix;
        }
        // recursive calls
        else {
            findEncoding(pt.getLeft(), a, prefix + "0");
            findEncoding(pt.getRight(), a, prefix + "1");
        }
    }


}
