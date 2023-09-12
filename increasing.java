/*
Solution Idea:    If we know how many substrings of length k-1 end in a
            height smaller than the height of the student we're
            currently considering, then we can add our current student
            to all of those to make a substring of length k.
            Using a binary index tree, whenever we add new substrings,
            we can update the number of substrings smaller than every
            height bigger than the student we just added.
*/

import java.util.*;
public class increasing {
   public static void main(String[] args) {
      Scanner in=new Scanner(System.in);
      int c=in.nextInt();
      for(int loop=1;loop<=c;loop++) {
         int n=in.nextInt(),k=in.nextInt();
         
         //I call this "originialHeights" because they need to be changed
         //to numbers between 1 and n to fit in the binary index tree.
         int[] originalHeights=new int[n];
         ArrayList<Integer> heightsList=new ArrayList<Integer>();
         for(int i=0;i<n;i++) {
            originalHeights[i]=in.nextInt();
            heightsList.add(originalHeights[i]);
         }
         
         //heightsMap stores the index of each height in the sorted list.
         //This way, every height is between 1 and n, but relative heights
         //are kept the same.
         Collections.sort(heightsList);
         HashMap<Integer,Integer> heightsMap=new HashMap<Integer,Integer>();
         for(int i=0;i<n;i++) {
            if(!heightsMap.containsKey(heightsList.get(i)))
               heightsMap.put(heightsList.get(i), heightsMap.size()+1);
         }
         
         //newHeights stores the index where each height appears in the
         //sorted list. These are the heights that we'll actually use.
         int[] newHeights=new int[n+1];
         for(int i=0;i<n;i++) {
            newHeights[i]=heightsMap.get(originalHeights[i]);
         }
         
         //Each substring length between 1 and k needs a binary index
         //tree so that we can answer the question "how many substring
         //of length k-1 end in a height smaller than my own?"
         BIT[] dp=new BIT[k+1];
         for(int i=0;i<k+1;i++)
            dp[i]=new BIT(n+1);
         
         //We process everything for student i before considering student
         //i+1 so that we know everything in our binary index trees are
         //from students to the left of us.
         for(int i=0;i<n;i++) {
            //This adds a substring of length 1 that can be used by
            //everything bigger than our current student.
            dp[1].update(newHeights[i]+1, 1);
            for(int j=2;j<k+1;j++) {
               //substringsShorter stores the number of substrings of length
               //j-1 that are smaller than student i. We mod here to prevent
               //overflow.
               long substringsShorter=dp[j-1].read(newHeights[i])%1000000007;
               
               //This symbolizes adding student i onto the end of every substring
               //found by substringsShorter. It creates one new substring of length
               //j for every substring of length j-1 that ends in something smaller
               //than student i and adds that many substrings to the binary index
               //tree to be used by everything bigger than it.
               dp[j].update(newHeights[i]+1, substringsShorter);
            }
         }
         
         System.out.println("Class #"+loop+": "+dp[k].read(n+1)%1000000007);
      }
   }
   
   
   //This is a Binary Index Tree.  It's impossible to describe it here in just
   //a few comments so please research it online.
   static class BIT {
      int n;
      long[] tree;
      public BIT(int n) {
         this.n = n;
         tree = new long[n + 1];
      }
      long read(int i) {
         long sum = 0;
         while (i > 0) {
            sum += tree[i];
            i -= i & -i;
         }
         return sum;
      }
      void update(int i, long val) {
         while (i <= n) {
            tree[i] += val;
            i += i & -i;
         }
      }
   }
}
