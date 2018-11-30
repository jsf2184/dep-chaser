package com.jeff.fischman.play;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class DependencyMap {
    Map<String, Entry> _map;

    public DependencyMap() {
        _map = new HashMap<>();
    }

    public void addEntry(Entry entry) {
        // Here we need to consider the possiblity that we received more than one entry
        // for the same variableName. If so, we merge the dependencies for that variable.
        //
        String variableName = entry.getVariableName();
        Entry mapEntry = _map.get(variableName);
        if (mapEntry == null) {
            _map.put(variableName, entry);
        } else {
            mapEntry.merge(entry);
        }
    }

    public Entry get(String key) {
        return _map.get(key);
    }

    public Set<String> keySet() {
        return _map.keySet();
    }

    public static DependencyMap create(Stream<String> inputStream) {
        DependencyMap res = new DependencyMap();
        inputStream.forEach(line -> {
            Entry entry = Entry.createEntry(line);
            res.addEntry(entry);
        });
        return res;
    }
}
