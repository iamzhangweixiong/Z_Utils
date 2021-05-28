package com.zhangwx.z_utils.struct;

import com.google.common.collect.EvictingQueue;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class EvictingQueueTest {

    public static void main(String[] args) {
        EvictingQueue<String> mRecentTime = EvictingQueue.create(13);

        mRecentTime.add("11:00");
        mRecentTime.add("21:00");
        mRecentTime.add("31:00");
        mRecentTime.add("51:00");
        mRecentTime.add("61:00");
        mRecentTime.add("71:00");
        mRecentTime.add("81:00");
        mRecentTime.add("91:00");
        mRecentTime.add("101:00");

        System.out.println(mRecentTime);

        EvictingArrayQueue<String> mRecentTimes = EvictingArrayQueue.create(13);
        mRecentTimes.add("1:00");
        mRecentTimes.add("2:00");
        mRecentTimes.add("3:00");
        mRecentTimes.add("4:00");

        mRecentTimes.addAll(mRecentTime);
        mRecentTimes.addAll(mRecentTime);

        System.out.println(mRecentTimes);
    }
}

class EvictingArrayQueue<E> extends ArrayDeque<E> {
    final int mMaxSize;

    public  EvictingArrayQueue(int maxSize) {
        mMaxSize = maxSize;
    }

    public static <E> EvictingArrayQueue<E> create(int maxSize) {
        return new EvictingArrayQueue<E>(maxSize);
    }

    @Override
    public boolean add(E e) {
        if (mMaxSize == 0) {
            return true;
        }
        if (size() == mMaxSize) {
            remove();
        }
        return super.add(e);
    }
}

class Iterables {

    public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof Collection) {
            Collection<? extends T> c = cast(elementsToAdd);
            return addTo.addAll(c);
        }
        if (elementsToAdd == null) {
            throw new NullPointerException();
        }
        return Iterators.addAll(addTo, elementsToAdd.iterator());
    }

    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
        if (iterable == null) {
            throw new NullPointerException();
        }
        if (numberToSkip < 0) {
            throw new IllegalArgumentException("number to skip cannot be negative");
        }
        return () -> {
            if (iterable instanceof List) {
                final List<T> list = (List<T>) iterable;
                int toSkip = Math.min(list.size(), numberToSkip);
                return list.subList(toSkip, list.size()).iterator();
            }
            final Iterator<T> iterator = iterable.iterator();

            Iterators.advance(iterator, numberToSkip);

            /*
             * We can't just return the iterator because an immediate call to its
             * remove() method would remove one of the skipped elements instead of
             * throwing an IllegalStateException.
             */
            return new Iterator<T>() {
                boolean atStart = true;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public T next() {
                    T result = iterator.next();
                    atStart = false; // not called if next() fails
                    return result;
                }

                @Override
                public void remove() {
                    if (atStart) {
                        throw new IllegalStateException("no calls to next() since the last call to remove()");
                    }
                    iterator.remove();
                }
            };
        };
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection<T>) iterable;
    }
}

class Iterators {

    public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add(iterator.next());
        }
        return wasModified;
    }

    public static int advance(Iterator<?> iterator, int numberToAdvance) {
        if (iterator == null) {
            throw new NullPointerException();
        }
        if (numberToAdvance < 0) {
            throw new IllegalArgumentException("numberToAdvance must be nonnegative");
        }

        int i;
        for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
            iterator.next();
        }
        return i;
    }
}
