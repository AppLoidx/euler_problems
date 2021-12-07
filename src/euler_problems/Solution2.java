package euler_problems;

import java.util.stream.IntStream;

public class Solution2 {
    private static int fifthPowerDigitSum(int x) {
        int sum = 0;
        while (x != 0) {
            int y = x % 10;
            sum += y * y * y * y * y;
            x /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        int sum = 0;
        for (int i = 2; i < 1000000; i++) {
            if (i == fifthPowerDigitSum(i))
                sum += i;
        }
        System.out.println(sum);

        int res = IntStream.range(2, 1000000)
                .filter(x -> x == fifthPowerDigitSum(x))
                .sum();

        IntStream.range(2, 1000000)
                .filter(x -> x == fifthPowerDigitSum(x))
                .sum();

        System.out.println(res);
    }

}
