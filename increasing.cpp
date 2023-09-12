#include <iostream>
#include <algorithm>
#include <map>
#include <stdio.h>

using namespace std;

/*
Solution Path: We will count the ways from left to right and store the number of
ways to pick a given number of students to our left with all their heights
less than a given value.

Solution efficiency: O(n*k*log(n)) (note that k is really small)

We will need fast range queries and point updates, so I have chosen to use a Fenwick
Tree to do this. An excellent explanation of how Fenwick Trees work is available
here:
https://www.youtube.com/watch?v=kPaJfAUwViY&t=2552s
*/

//since all heights are unique, we can compress them into the integers from 0 to n-1
//so that they are easier to work with
#define MAX_N 100001
#define FT_SIZE 200000
#define MOD 1000000007
int n, k;

//we need 10 fenwick trees, one for each height
int fts[10][FT_SIZE];

void mod(int &i) {
   i=(i%MOD+MOD)%MOD;
}
void mod(long long &i) {
   i=(i%MOD+MOD)%MOD;
}

//returns the sum of everything before and equal to this index
long long ftRangeSum(int* ft, int index) {
   index++;   //Fenwick Trees are 1-index based
   long long total=0;
   while (index>0) {
      total+=ft[index];
      mod(total);
      index-=(index&-index);
   }
   return total;
}

//updates the index by the specified amount
void ftPointUpdate(int* ft, int index, int by) {
   index++;
   while (index<FT_SIZE) {
      ft[index]+=by;
      mod(ft[index]);
      index+=(index&-index);
   }
}

void compressHeights(int* heights) {
   int heightsClone[MAX_N];
   for (int i=0; i<n; i++)
      heightsClone[i]=heights[i];
   
   sort(heightsClone, heightsClone+n);
   
   map<int, int> compress;
   for (int i=0; i<n; i++)
      compress[heightsClone[i]]=i;   
   
   for (int i=0; i<n; i++)
      heights[i]=compress[heights[i]];
}

int main() {
   // Read in the number of classes
   int classes;
   cin>>classes;

   // Loop over the classes
   for (int c=1; c<=classes; c++) {
      // Read in the values for "n" and "k"
      cin>>n>>k;

      // Read in the heights
      int heights[MAX_N];
      for (int i=0; i<n; i++)
         scanf("%d", &heights[i]);
   
      // "Compress" the heights 
      compressHeights(heights);
   
      // Initialize our array of Fenwick trees
      for (int ft=0; ft<10; ft++)
         for (int i=0; i<FT_SIZE; i++)
            fts[ft][i]=0;
      
      // Loop over the people
      for (int person=0; person<n; person++) {
         //for each person, we want to count how many ways they can be in each position in the photo
         for (int max=9; max>0; max--) {
            //sum the number of ways to get k-1 people with the last person being shorter than me
            int waysBeforeMe=ftRangeSum(fts[max-1], heights[person]-1);
            //you can end that number of pictures with my height by ending with me
            ftPointUpdate(fts[max], heights[person], waysBeforeMe);
         }
         //Also, I could start the picture and be the only one in it so far
         ftPointUpdate(fts[0], heights[person], 1);
      }
      
      //after we have gone through all the people, we want to know the number of ways of picking k, ending in any height
      long long ans=ftRangeSum(fts[k-1], n);
      cout<<"Class #"<<c<<": "<<ans<<endl;
   }
   return 0;
}
