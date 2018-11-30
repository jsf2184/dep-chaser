package com.jeff.fischman.play;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntryTests {

    @Test
    public void testCreateExceptions() {
        verifyCreateException(null);
        verifyCreateException("");
        verifyCreateException("a:b:c");
    }

    @Test
    public void testGoodInput() {
        verifyCreation("a:b,c", "a", new String[] {"b", "c"});
        verifyCreation("a  :b  , c", "a", new String[] {"b", "c"});
        verifyCreation("a:", "a", new String[] {});
        verifyCreation("a", "a", new String[] {});
    }

    @Test
    public void testDupDepsEliminated() {
        verifyCreation("a:b,c,b,c", "a", new String[]{"b", "c"});
    }
        @Test
    public void testMerge(){
        Entry entry1 = Entry.createEntry("a:b,c,d");
        Entry entry2 = Entry.createEntry("a:c,d,e");
        entry1.merge(entry2);
        // Note that duplicates are eliminated in the merge entry.
        verifyEntry(entry1, "a",  new String[] {"b", "c", "d", "e"});
    }


    public void verifyCreation(String line, String expectedVariableName, String[] expectedDependencies) {
        Entry entry = Entry.createEntry(line);
        verifyEntry(entry, expectedVariableName, expectedDependencies);
    }

    public void verifyEntry(Entry entry, String expectedVariableName, String[] expectedDependencies) {
        Assert.assertEquals(expectedVariableName, entry.getVariableName());
        Set<String> depList = new HashSet<>(Arrays.asList(expectedDependencies));
        Assert.assertEquals(depList, entry.getDependencies());
    }


    public void verifyCreateException(String line) {
        boolean caught = false;
        try {
            Entry entry = Entry.createEntry(line);
        } catch (Exception e) {
            caught = true;
            System.err.println(e.getMessage());
        }
        Assert.assertTrue(caught);

    }
}
