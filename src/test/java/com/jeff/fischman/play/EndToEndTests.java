package com.jeff.fischman.play;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class EndToEndTests {

    @Test
    public void testSimpleNonCycle() {
        List<String> input = Arrays.asList(
                "a:b,c",
                "b:d",
                "c:e",
                "d:",
                "e:"
        );
        verifyResult(input, true);
    }

    @Test
    public void testTwoUnrelatedBranchesWithNoCycles() {
        List<String> input = Arrays.asList(
                // We have an 'a' branch that includes a thru e
                "a:b,c",
                "b:d",
                "c:e",
                "d:",
                "e:",
                // And an 'f' branch that includes f thru i
                "f:g",
                "g:h",
                "h:i"
        );
        verifyResult(input, true);
    }

    @Test
    public void testSecondBranchRefersToFirstWithNoCycles() {
        List<String> input = Arrays.asList(
                // We have an 'a' branch that includes a thru e
                "a:b,c",
                "b:d",
                "c:e",
                "d:",
                "e:",
                // And an 'f' branch that includes f,g and refers back to 'a'
                "f:g",
                "g:h",
                "h:a"
        );
        verifyResult(input, true);
    }

    @Test
    public void testSimpleCycle() {
        List<String> input = Arrays.asList(
                "a:b,c",
                "b:d",
                "c:e",
                "d:",
                "e:a"  // Here is our cycle
        );
        verifyResult(input, false);
    }

    @Test
    public void testSecondCycleHasBranch() {
        List<String> input = Arrays.asList(
                // We have an 'a' branch that includes a thru e
                "a:b,c",
                "b:d",
                "c:e",
                "d:",
                "e:",
                // And an 'f' branch that includes f thru i
                "f:e,g",
                "g:h",
                "h:f"
        );
        verifyResult(input, false);
    }

    void verifyResult(List<String> input, boolean success) {
        DependencyMap dependencyMap = DependencyMap.create(input.stream());
        DepChaser sut = new DepChaser(dependencyMap);
        Assert.assertEquals(success, sut.validateNoDependencies());
    }

}
