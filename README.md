# Radix sort

[Radix sort](https://en.wikipedia.org/wiki/Radix_sort) is an interesting sorting algorithm.

It avoids the use of comparisons, rather it uses the digits as indicies to an array (referred to as buckets).

Radix sort works by first looking at the least significant digit and then working its way to the most significant digit. [See examples](#examples-of-radix-sort-in-action)

This implementation works by using binary instead of base 10, because it's blazingly fast. Bitshift operations can be used to reduce CPU cycles. I tested the time it took to sort large arrays of unique digits (for example digits from 0 to 100,000,000). This solution is currently limited to only working with signed integers. However, I am considering making a floating point radix sorter as well.

The time complexity of the radix sort algorithm is often referred to as `O(m*n)`, where `m` is the amount of digits in the largest integer, and `n` is the amount of elements in the unsorted array. My solution asssumes the most digits a signed integer can have is 32, so therefore `m` is a constant. This simplifies the `O(m*n)` time complexity to simply `O(n)`. Similarly, the space complexity for the bucekts is `O(m*n)` where `m` is the amount of buckets, and `n` is the amount of digits. Since I am using 2 buckets (due to the nature of binary) `m` simplifies to the constant 2, therefore, making the final space complexity `O(n)`.

# Examples of Radix sort in action

Let's assume the following array

```py
arr = [102, 37, 55, 8, 69, 420, 22]
```

The first step to sorting this array using radix sort (assuming 10 buckets) is to look at the least significant digit in base 10.

```py
buckets = {
  0: [420],
  1: [],
  2: [102, 22],
  3: [],
  4: [],
  5: [55],
  6: [],
  7: [37],
  8: [8],
  9: [69]
}
```

Then repeat for the second least significant digit, maintaining the order of the buckets from the last step.

```py
buckets = {
  0: [102, 8],
  1: [],
  2: [420, 22],
  3: [37],
  4: [],
  5: [55],
  6: [69],
  7: [],
  8: [],
  9: []
}
```

And lastly repeat for the most significant digit.

```py
buckets = {
  0: [8, 22, 37, 55, 69],
  1: [102],
  2: [],
  3: [],
  4: [420],
  5: [],
  6: [],
  7: [],
  8: [],
  9: []
}
```

Lastly, simply take each bucket and concatinate them together to get the final sorted array.

```py
sorted_arr = [8, 22, 37, 55, 69, 102, 420]
```
