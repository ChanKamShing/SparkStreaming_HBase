import java.util.Arrays;

public class PrimeCount {
//==========================第一版============================
    /**
     * 统计[2,n)之间有多少个素数
     * @param n
     * @return
     */
    int countPrimes(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 判断num是否是素数
     * @param num
     * @return
     */
    private boolean isPrime(int num) {
        for (int i = 2; i < num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
//==========================第二版============================
//    换种思路，使用一个包括n个元素的数组进行标记
    /**
     * 统计[2,n)之间有多少个素数
     * @param n
     * @return
     */
    int countPrimes2(int n) {
        boolean[] arr = new boolean[n];
        Arrays.fill(arr, true);

        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                for (int j = i * i; j < n; j += i) { //j相当于2*i，3*i，4*i，...
//                    素数的倍数都被标记为false
                    arr[j] = false;
                }
            }
        }
//        遍历数组，统计素数
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (arr[i]) {
                count++;
            }
        }

        return count;
    }

    /**
     * 判断num是否是素数
     * @param num
     * @return
     */
    private boolean isPrime2(int num) {
        for (int i = 2; i*i < num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
