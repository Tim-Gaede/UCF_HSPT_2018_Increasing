
#The value we need to take the modulus of the answer by
mod = 1000000007


#A simple implementation of a Binary Indexed Tree
class bit:
    def __init__(self, n):
        sz = 1
        while n >= sz:
            sz *= 2
        self.size = sz
        self.data = [0] * sz

    def sum1(self, i):
        s = 0
        while i > 0:
            s += self.data[i]
            s %= mod
            i -= i & -i
        return s

    def sum2(self, low, high):
        s = self.sum1(high) - self.sum1(low - 1)
        if s < 0: s += mod
        if s >= mod: s %= mod
        return s

    def add(self, i, x):
        while i < self.size:
            self.data[i] += x
            if self.data[i] >= mod: self.data[i] %= mod
            i += i & -i


#Number of test cases
t = int(input())


#loop through each test case
for tt in range(1, t + 1):
    #number of students (n) and the size of the photo (k) respectively
    n, k = (int(i) for i in input().split(" "))

    #the heights of the students
    a = [int(i) for i in input().split(" ")]

    #copy of the heights to sort by increasing order
    s = [i for i in a]
    s.sort()

    #now we make a map, in order to co-ordinate compress (ie. if the values are 5, 6, and 7, then that yields the same results as if the heights were 1, 2, 3)
    #so basically we just store the relative heights for each value so that we can store them in our Binary Indexed Tree
    mp = {}
    for i in s: mp[i] = len(mp) + 1

    #replace the original values of "a" with the relative values
    for i in range(n): a[i] = mp[a[i]]

    #make an array of Binary Indexed Trees "bi", where bi[x].sum2(1, y) represents the number of ways to have a strictly increasing subsequence of size x from the indecies 1 to y
    bi = [bit(n + 1) for i in range(k + 1)]

    #let's loop for each height in the array
    for i in range(n):
        #for each person, we see how they affect each of the sizes
        #we'll start from the highest size value and decrement
        for j in range(k, 1, -1):
            #the number of ways to make a photo with "j" strictly increasing people by inserting person "i" is equal to
            #the number of ways to make a photo with "j-1" strictly increasing people where the height of the tallest person is shorter than the i'th person
            bi[j].add(a[i], bi[j - 1].sum2(1, a[i]))
        #we can always make a sequence of 1 strictly increasing person (just have them by themselves)
        bi[1].add(a[i], 1)

    #print the answer!
    print("Class #" + str(tt) + ":", bi[k].sum2(1, n))
