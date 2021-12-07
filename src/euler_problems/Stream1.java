package euler_problems;

import java.util.stream.IntStream;

public class Stream1 {
    public static void main(String[] args) {
        int answer = IntStream.range(0, 1000)
                .filter(x -> Math.min(x % 3, x % 5) == 0)
                .sum();

        System.out.println(answer);
    }
}
