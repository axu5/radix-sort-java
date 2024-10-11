public class Radix {
  private int[] arr;
  private int length;

  /**
   * A class to sort an array with radix sort
   * 
   * @param arr The array which should be sorted
   */
  public Radix(int[] arr) {
    this.arr = arr;
    this.length = arr.length;
  }

  public Radix() {

  }

  public int[] getArr() {
    return arr;
  }

  public int calculateBinDigits(int num) {
    /*
     * Using the change of base formula for logs
     * log[b](c) = log[a](c)/log[a](b)
     * we get how many digits need to be sorted in base 2
     * 1.44269504089 = 1 / Math.log(2)
     */
    return ((int) (Math.log(num) * 1.44269504089)) + 1;
  }

  public int fastBinDigits(int num) {
    /*
     * An alternative solution could be bitshifting
     * left until a 1 is detected, this is how many
     * zeroes are used until the first digit is used
     * in a 32 bit number, and hence a subtraction
     * of this result by 32 would yield the correct
     * answer?
     * 
     * 2^32 = 4294967296
     * However Java uses signed integers and therefore the first
     * bit is reserved, leaving us with a range of values:
     * [-2^31+1, 2^31-1]
     * and therefore the maximum bit value is
     * 2^30 = 1073741824
     */
    int j = 1073741824;
    int k = 0;
    for (int i = 32; i >= 1; --i) {
      int lastDig = num & j;
      if (lastDig == j) {
        k = i;
        break;
      }
      num = num << 1;
    }
    return k - 1;
  }

  /**
   * Use radix sort to sort this.arr
   */
  public void sort() {
    // Find largest number in the unsorted array
    int largest = arr[0];
    int digits = fastBinDigits(largest);
    // 2d array in 1 array: Memory complexity is O(2n) -> O(n)
    int[] buckets = new int[2 * length];
    // keep track of indices
    int[] bucketCounter = new int[2];

    /*
     * Complexity: O(m*n), where
     * m is the number of digits the largest integer has,
     * n is the amount of numbers in the unsorted array
     * -> O(n)
     */
    int i = 0;
    while (i < digits) { // m iterations
      for (int j = 0; j < length; ++j) { // n iterations
        int number = arr[j];
        // Divide by the next power of two (same as >> i), and see which bucket
        // it should fall in (& 1)
        int bucketKey = (number >> i) & 1;
        // Get a free index in the "2d" array, and update bucket counter
        // so program knows this space is allocated
        int columnIdx = bucketCounter[bucketKey]++;

        // Compute index in "2d" array where bucketKey is the row
        // and columnIdx is the column
        buckets[bucketKey * length + columnIdx] = number;

        if (i == 0 && number > largest) {
          largest = number;
          digits = fastBinDigits(number);
        }
      }

      // Copy buckets into original array
      System.arraycopy(buckets, 0, arr, 0, bucketCounter[0]);
      System.arraycopy(buckets, length, arr, bucketCounter[0], bucketCounter[1]);

      // Empty buckets (in this case just overwrite indices)
      bucketCounter[0] = 0;
      bucketCounter[1] = 0;
      i++;
    }
  }

  /**
   * @return if the array is sorted or not
   */
  public boolean isSorted() {
    int previous = this.arr[0];
    for (int i = 1; i < this.length; ++i) {
      if (previous > this.arr[i]) {
        return false;
      }

      previous = this.arr[i];
    }

    return true;
  }

  public static void main(String[] args) {
    testSort();
    // testLog();
  }

  public static void testLog() {
    Radix radix = new Radix();
    for (int i = 2; i < Math.pow(2, 29); i *= 2) {
      int r1 = radix.calculateBinDigits(i);
      int r2 = radix.fastBinDigits(i);
      if (r1 != r2) {
        System.out.println("BROKE AT " + i + "\tr1 " + r1 + "\tr2 " + r2);
        break;
      }
    }
  }

  public static void testSort() {
    int items = 100_000_000;
    int iterations = 10;

    int sum = 0;

    System.out.println("\tItems:\t\t" + items);
    System.out.println("\tIterations:\t" + iterations);
    for (int _i = 0; _i < iterations; ++_i) {
      int[] unsorted = new int[items];
      for (int i = 0; i < items; ++i) {
        unsorted[i] = i;
      }

      for (int i = 0; i < unsorted.length; ++i) {
        int j = (int) Math.random() * items;
        int tmp = unsorted[i];
        unsorted[i] = unsorted[j];
        unsorted[j] = tmp;
      }

      Radix sorter = new Radix(unsorted);
      long start = System.currentTimeMillis();
      sorter.sort();
      long finish = System.currentTimeMillis();

      // Print out the result
      // printNumbers(sorter.getArr());

      // Final logs
      long timeElapsed = (finish - start);
      System.out.println(_i + 1 + "\tTime elapsed:\t" + timeElapsed + "ms");
      // System.out.println("Is validated:\t" + sorter.isSorted());
      if (!sorter.isSorted()) {
        System.out.println("SORTER BROKE");
        return;
      }
      sum += timeElapsed;
    }

    System.out.println("---------------------");
    System.out.println("Time average:\t\t" + (sum / iterations) + "ms");
  }

  /**
   * Should only be used for small arrays
   * 
   * @param arr the array which should be printed to the terminal
   */
  public static void printNumbers(int[] arr) {
    for (int i = 0; i < arr.length; ++i) {
      System.out.printf("%d, ", arr[i]);
    }

    System.out.print("\n");
  }
}