package com.jeff.fischman.play;

import java.util.HashSet;
import java.util.Set;

public class ChildrenDependencyChecker {

    // We are going to visit all the children originating from our _startingVariable
    private String _startingVariable;

    // _dependencyMap lets us find all the dependencies of any variable.
    DependencyMap _dependencyMap;

    // _preVerifiedVariables are variables handed to us for which we were told that there are no
    // cycles involving any of the variables in the set.
    //
    Set<String>   _preVerifiedVariables;

    // _visitedVariables will grow to be the sum total of all variables that we encounter
    // chasing dependencies from our _startingVariables
    //
    Set<String>   _visitedVariables;

    public ChildrenDependencyChecker(String startingVariable,
                                     DependencyMap dependencyMap,
                                     Set<String> preVerifiedVariables)
    {
        _startingVariable = startingVariable;
        _dependencyMap = dependencyMap;
        _preVerifiedVariables = preVerifiedVariables;
        _visitedVariables = new HashSet<>();
    }


    // If a cycle is detected, the run() method tnrows an Exception.
    // Otherwise, it returns a set of all the variables that were 'cleared' during
    // the course of chasing the children of our _startingVariable.
    //
    public Set<String> run() throws Exception {
        // Create a set of variables that contains the variables seen on a single path descent.
        // Its a 'scratchPad' that grows and shrinks as is appropriate
        //
        HashSet<String> pathVariables = new HashSet<>();
        process(_startingVariable, pathVariables);
        return _visitedVariables;
    }

    // process() is a recursive method that chases all of a variable's dependencies
    private void process(String variable,
                         HashSet<String> pathVariables) throws Exception
    {
        if (pathVariables.contains(variable)) {
            // this variable completes a cycle - bad news

            Exception exception = createException(pathVariables, variable);
            throw exception;
        }
        if (_preVerifiedVariables.contains(variable)) {
            // this variable is safe, so we need not bother with it.
            return;
        }
        // add ourself to the visitedVariables and the pathVariables
        _visitedVariables.add(variable);
        pathVariables.add(variable);

        // Now, lets get our dependencies
        Entry entry = _dependencyMap.get(variable);
        if (entry != null) {
            for (String dep : entry.getDependencies()) {
                process(dep, pathVariables);
            }
        }
        // If we got here, we "survived" the dependencies of our dependencies.
        // We can remove our entry from the pathVariables as we pop out of this call.
        // But, of course, we leave ourself on the visitedVariables.
        //
        pathVariables.remove(variable);
    }

    public static Exception createException(Set<String> set, String repeatedVariable) {
        StringBuilder sb = new StringBuilder();
        set.forEach(s -> {
            sb.append(s);
            sb.append(",");
        });
        sb.append(repeatedVariable);
        return new Exception(String.format("Detected a cycle among variables: %s", sb.toString()));

    }

}
