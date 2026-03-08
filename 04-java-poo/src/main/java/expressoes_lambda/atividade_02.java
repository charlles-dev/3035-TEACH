package Expressões_Lambdas;

import java.util.function.Predicate;
import java.util.stream.IntStream;

public class atividade_02 {
    public static void main(String[] args) {
        // Abordagem puramente declarativa e funcional usando IntStream
        Predicate<Integer> isPrime = n -> n > 1 && IntStream
                .rangeClosed(2, (int) Math.sqrt(n))
                .noneMatch(i -> n % i == 0);

        System.out.println("2 é primo? " + isPrime.test(2));
        System.out.println("4 é primo? " + isPrime.test(4));
        System.out.println("7 é primo? " + isPrime.test(7));
        System.out.println("10 é primo? " + isPrime.test(10));
    }
}
