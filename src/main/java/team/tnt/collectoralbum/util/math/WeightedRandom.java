package team.tnt.collectoralbum.util.math;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class WeightedRandom<T> implements Supplier<T> {

    private final Random random;
    private final T[] weightedEntries;
    private final ToIntFunction<T> weightProvider;
    private final int computedTotal;

    private WeightedRandom(Random random, T[] weightedEntries, ToIntFunction<T> weightProvider) {
        this.random = random;
        this.weightedEntries = weightedEntries;
        this.weightProvider = weightProvider;
        this.computedTotal = this.computeTotal();
        if (weightedEntries == null || weightedEntries.length == 0 || computedTotal < 1) {
            throw new IllegalArgumentException("Weighted random must contain atleast one entry with weight value over 0");
        }
    }

    public static <T> WeightedRandom<T> createWithSeed(long seed, T[] weightedEntries, ToIntFunction<T> weightProvider) {
        Random rng = new Random(seed);
        return new WeightedRandom<>(rng, weightedEntries, weightProvider);
    }

    public static <T extends IWeighted> WeightedRandom<T> createWithSeed(long seed, T[] entries) {
        return createWithSeed(seed, entries, IWeighted::getWeight);
    }

    public static <T> WeightedRandom<T> create(T[] weightedEntries, ToIntFunction<T> weightProvider) {
        Random rng = new Random();
        return new WeightedRandom<>(rng, weightedEntries, weightProvider);
    }

    public static <T extends IWeighted> WeightedRandom<T> create(T[] entries) {
        return create(entries, IWeighted::getWeight);
    }

    @Override
    public T get() {
        int buffer = random.nextInt(computedTotal);
        for (int i = weightedEntries.length - 1; i >= 0; i--) {
            T value = weightedEntries[i];
            buffer -= weightProvider.applyAsInt(value);
            if (buffer < 0) {
                return value;
            }
        }
        throw new IllegalStateException("Weighted value couldn't be selected for unknown reason");
    }

    private int computeTotal() {
        return Arrays.stream(weightedEntries)
                .mapToInt(weightProvider)
                .reduce(Integer::sum)
                .orElse(1);
    }

    @FunctionalInterface
    public interface IWeighted extends Supplier<Integer> {

        int getWeight();

        @Override
        default Integer get() {
            return this.getWeight();
        }
    }
}
