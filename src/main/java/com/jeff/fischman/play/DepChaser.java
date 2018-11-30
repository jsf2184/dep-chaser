package com.jeff.fischman.play;

import java.util.HashSet;
import java.util.Set;

public class DepChaser {
    private DependencyMap _dependencyMap;
    private ChildrenDependencyCheckerFactory _dependencyCheckerFactory;

    private Set<String> _verifiedVariables;

    public DepChaser(DependencyMap dependencyMap) {
        this(dependencyMap, new ChildrenDependencyCheckerFactory());
    }

    public DepChaser(DependencyMap dependencyMap,
                     ChildrenDependencyCheckerFactory dependencyCheckerFactory)
    {
        _dependencyMap = dependencyMap;
        _dependencyCheckerFactory = dependencyCheckerFactory;
        _verifiedVariables = new HashSet<>();
    }

    // verify that there are no cycles.
    public boolean validateNoDependencies() {

        String startVar;
        // Verify all the entries in our DependencyMap. Note, however, that in general, many will be verified
        // during a single run.
        //
        while ((startVar = getUnprocessedVariable()) != null) {

            // create a DependencyChecker slated to start its work from 'startVar'
            ChildrenDependencyChecker dependencyChecker = _dependencyCheckerFactory.create(startVar,
                                                                                           _dependencyMap,
                                                                                           _verifiedVariables);
            try {
                // run with dependencyChecker and capture all the variables that were verified during the run.
                Set<String> visitedVariables = dependencyChecker.run();
                // Add the visitedVariables to our set of verifiedVariables
                _verifiedVariables.addAll(visitedVariables);
            } catch (Exception e) {
                // whoops, the dependencyChecker found a cycle in its run with that startingVariable
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }



    public Set<String> getVerifiedVariables() {
        return _verifiedVariables;
    }

    private String getUnprocessedVariable() {
        Set<String> variableNames = _dependencyMap.keySet();
        for (String v : variableNames) {
            if (_verifiedVariables.contains(v)) {
                continue; // this variable already processed
            }
            return v;
        }
        return null;
    }
}
