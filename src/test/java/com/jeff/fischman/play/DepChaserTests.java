package com.jeff.fischman.play;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class DepChaserTests {

    DependencyMap _dependencyMap;
    ChildrenDependencyCheckerFactory _dependencyCheckerFactory;

    // These tests generally mock DepChaser's java dependencies. For tests involving DepChaser using 'real'
    // components, see EndToEndTests.
    //

    @Test
    public void testVerifyLoopInvokesDependenciesProperlyWhenNoCycles() throws Exception {
        // ----------------------------------------------------
        // Add more mocking detail to our dependencies first.
        // ----------------------------------------------------

        DepChaser sut = createSut();

        // First mock our dependencyMap to know about variables a thru f
        when(_dependencyMap.keySet()).thenReturn(new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f")));

        // ----------------------------------------------------------------------------------
        // we're going to make it so it takes 2 iterations in DepChaser to handle a thru f
        // ----------------------------------------------------------------------------------

        // The 1st time, our mocked checker is going to checkout no cycles in a, b, c variables
        ChildrenDependencyChecker checker1 = mockChildrenDependencyChecker(Arrays.asList("a", "b", "c"));
        // The 2nd time, our mocked checker is going to checkout no cycles in d, e, f variables
        ChildrenDependencyChecker checker2 = mockChildrenDependencyChecker(Arrays.asList("d", "e", "f"));

        when(_dependencyCheckerFactory.create("a", _dependencyMap, new HashSet<>())).thenReturn(checker1);
        when(_dependencyCheckerFactory.create("d", _dependencyMap, new HashSet<>(Arrays.asList("a", "b", "c")))).thenReturn(checker2);

        Assert.assertTrue(sut.validateNoDependencyCycles());
        verify(checker1, times(1)).run();
        verify(checker2, times(1)).run();

        // Verify all variables were verified
        Assert.assertEquals(_dependencyMap.keySet(), sut.getVerifiedVariables());
    }

    @Test
    public void testVerifyLoopTerminatesWhenCycleFound() throws Exception {
        // ----------------------------------------------------
        // Add more mocking detail to our dependencies first.
        // ----------------------------------------------------

        DepChaser sut = createSut();

        // First mock our dependencyMap to know about variables a thru f
        when(_dependencyMap.keySet()).thenReturn(new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f")));

        // ----------------------------------------------------------------------------------
        // we're going to make it so it takes 2 iterations in DepChaser to handle a thru f
        // ----------------------------------------------------------------------------------

        // The 1st time, our mocked checker is going to checkout no cycles in a, b, c variables
        ChildrenDependencyChecker checker1 = mock(ChildrenDependencyChecker.class);
        Exception exception = new Exception("fake exception");
        when(checker1.run()).thenThrow(exception);

        when(_dependencyCheckerFactory.create("a", _dependencyMap, new HashSet<>())).thenReturn(checker1);

        Assert.assertFalse(sut.validateNoDependencyCycles());
        verify(checker1, times(1)).run();

        // Verify no variables were verified
        Assert.assertEquals(new HashSet<>(), sut.getVerifiedVariables());
        // Verify the _dependencyCheckerFactory was only called once.
        verify(_dependencyCheckerFactory, times(1)).create("a", _dependencyMap, new HashSet<>());
        verifyNoMoreInteractions(_dependencyCheckerFactory);
    }


    DepChaser createSut() {
        _dependencyMap = mock(DependencyMap.class);
        _dependencyCheckerFactory = mock(ChildrenDependencyCheckerFactory.class);
        return new DepChaser(_dependencyMap, _dependencyCheckerFactory);
    }

    ChildrenDependencyChecker mockChildrenDependencyChecker(List<String> visitedVariables) throws Exception {
        ChildrenDependencyChecker res = mock(ChildrenDependencyChecker.class);
        when(res.run()).thenReturn(new HashSet<>(visitedVariables));
        return res;
    }
}
