package com.jeff.fischman.play;

import java.util.*;

public class Entry implements Iterable<String> {
    String _variableName;
    Set<String> _dependencies;

    public Entry(String variableName) {
        _variableName = variableName;
        _dependencies = new HashSet<>();
    }

    public Entry(String variableName, Set<String> dependencies) {
        _variableName = variableName;
        _dependencies = dependencies;
    }

    public void merge(Entry other) {
        if (!_variableName.equals(other._variableName) ){
            throw new RuntimeException("Entry.merge() Error: variableNames must match");
        }
        _dependencies.addAll(other._dependencies);
    }

    public String getVariableName() {
        return _variableName;
    }

    public Set<String> getDependencies() {
        return _dependencies;
    }

    @Override
    public Iterator<String> iterator() {
        return _dependencies.iterator();
    }

    public static Entry createEntry(String line) {
        // We expect input of the form
        //   a:b,c,d,e
        // where 'a' is the variableName and 'b', 'c', 'd', 'e' are the dependencies

        if (line == null || line.length() == 0) {
            throw new RuntimeException("Entry.createEntry(): ERROR - null or empty input");
        }

        // remove any extraneous spaces
        line = line.replaceAll("\\s", "");

        String[] halves = line.split(":");
        Entry res;
        if (halves.length == 1) {
            // Just a variable name, no dependencies
            res = new Entry(halves[0]);
            return res;
        }
        if (halves.length != 2) {
            throw new RuntimeException("Entry.createEntry(): ERROR - invalid input");
        }
        String variableName = halves[0];
        String[] depsArray = halves[1].split(",");
        res = new Entry(variableName, new HashSet<>(Arrays.asList(depsArray)));
        return res;
    }


}

