package com.jeff.fischman.play;

import java.util.Set;

public class ChildrenDependencyCheckerFactory {

    public ChildrenDependencyCheckerFactory() {
    }

    public ChildrenDependencyChecker create(String startingVariable,
                                            DependencyMap dependencyMap,
                                            Set<String> preVerifiedVariables)
    {
        return new ChildrenDependencyChecker(startingVariable, dependencyMap, preVerifiedVariables);
    }
}
