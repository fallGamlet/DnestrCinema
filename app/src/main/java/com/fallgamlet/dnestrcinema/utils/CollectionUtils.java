package com.fallgamlet.dnestrcinema.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static int getSize(Collection collection) {
        return collection == null? 0: collection.size();
    }

    @NonNull
    public static <T> List<T> getNonNull(List<T> list) {
        return list != null? list: new ArrayList<>();
    }

    @NonNull
    public static <T> Set<T> getNonNull(Set<T> list) {
        return list != null? list: new HashSet<>();
    }

    @NonNull
    public static <T> List<T> getList(Set<T> set) {
        return set == null?
                new ArrayList<>():
                new ArrayList<>(set);
    }

    @NonNull
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    @NonNull
    public static <T, R> List<R> mapItems(Collection<T> items, @NonNull Converter<T, R> converter) {
        if (isEmpty(items)) {
            return emptyList();
        }

        List<R> resultItems = new ArrayList<>(items.size());

        for (T item : items) {
            R resItem = converter.convert(item);
            if (item != null) {
                resultItems.add(resItem);
            }
        }

        return resultItems;
    }

    @NonNull
    public static <T> void forEach(Collection<T> items, @NonNull Each<T> function) {
        if (isEmpty(items)) {
            return;
        }

        for (T item : items) {
            function.run(item);
        }
    }


    public interface Converter<T, R> {
        R convert(T item);
    }

    public interface Each<T> {
        void run(T item);
    }
}
