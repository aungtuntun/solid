package solid.converters;

import java.util.Iterator;

import solid.stream.Stream;

public class Integers extends Stream<Integer> {

    private int[] integers;

    public static Stream<Integer> integers(int[] integers) {
        return new Integers(integers);
    }

    public Integers(int[] integers) {
        this.integers = integers;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new ReadOnlyIterator<Integer>() {

            int i;

            @Override
            public boolean hasNext() {
                return i < integers.length;
            }

            @Override
            public Integer next() {
                return integers[i++];
            }
        };
    }
}
