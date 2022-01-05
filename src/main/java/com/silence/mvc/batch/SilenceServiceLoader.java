package com.silence.mvc.batch;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SilenceServiceLoader {

    private static final Map<Class<?>, Collection<Class<?>>> SERVICES = new ConcurrentHashMap<Class<?>, Collection<Class<?>>>();


    public static <T> Collection<T> load(final Class<T> service) {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();


        if (SERVICES.containsKey(service)) {
            Collection<T> collection = new LinkedList<>();
            for (Class<?> each : SERVICES.get(service)) {
                collection.add((T)each);
            }
            return collection;
        }

        Collection<T> result = new LinkedList<>();
        for (T each : ServiceLoader.load(service, cl)) {
            result.add(each);
            if (!SERVICES.containsKey(service)) {
                SERVICES.put(service, new LinkedHashSet<Class<?>>());
            }
            SERVICES.get(service).add(each.getClass());
        }
        return result;
    }


}
