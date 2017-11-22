package com.ai.routing.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDataStorage {

    private DataStorage storage;

    private final String BIN1 = "bin1";
    private final String BIN2 = "bin2";
    private final String BIN3 = "bin3";

    private final String PSP1 = "psp1";
    private final String PSP2 = "psp2";
    private final String PSP3 = "psp3";

    private final BinPsp BIN1_PSP1 = new BinPsp(BIN1, PSP1);

    @PostConstruct
    private void setUp() {
        storage = new DataStorage();
    }

    @Test
    public void testGetHistoryWhenThereIsNoHistory() {
        History result = storage.getHistory(new BinPsp(BIN1, PSP1));
        int successful = result.getSuccessCount();
        int unsuccessful = result.getFailCount();

        assertEquals(0, successful);
        assertEquals(0, unsuccessful);
    }

    @Test
    public void testGetAllBinsWhenThereIsNoHistory() {
        Set<String> bins = storage.getAllBins(PSP1);

        assertTrue(bins.isEmpty());
    }

    @Test
    public void testGetAllPspsWhenThereIsNoHistory() {
        Set<String> psps = storage.getAllPsps(BIN1);

        assertTrue(psps.isEmpty());
    }

    @Test
    public void testGetHistoryWhenSuccessfulObservationPosted() {
        storage.postResult(BIN1_PSP1, true);

        History result = storage.getHistory(BIN1_PSP1);
        int successful = result.getSuccessCount();
        int unsuccessful = result.getFailCount();

        assertEquals(1, successful);
        assertEquals(0, unsuccessful);
    }

    @Test
    public void testGetAllBinsWhenSuccessfulObservationPosted() {
        storage.postResult(BIN1_PSP1, true);

        Set<String> bins = storage.getAllBins(PSP1);
        assertTrue(bins.contains(BIN1));
    }

    @Test
    public void testGetAllPspsWhenSuccessfulObservationPosted() {
        storage.postResult(BIN1_PSP1, true);

        Set<String> psps = storage.getAllPsps(BIN1);
        assertTrue(psps.contains(PSP1));
    }

    @Test
    public void testGetHistoryWhenUnsuccessfulObservationPosted() {
        storage.postResult(BIN1_PSP1, false);

        History result = storage.getHistory(BIN1_PSP1);
        int successful = result.getSuccessCount();
        int unsuccessful = result.getFailCount();

        assertEquals(0, successful);
        assertEquals(1, unsuccessful);
    }

    @Test
    public void testGetAllBinsWhenUnsuccessfulObservationPosted() {
        storage.postResult(BIN1_PSP1, false);

        Set<String> bins = storage.getAllBins(PSP1);
        assertTrue(bins.contains(BIN1));
    }

    @Test
    public void testGetAllPspsWhenUnsuccessfulObservationPosted() {
        storage.postResult(BIN1_PSP1, false);

        Set<String> psps = storage.getAllPsps(BIN1);
        assertTrue(psps.contains(PSP1));
    }

    @Test
    public void testGetHistoryWhenMultipleObservationPostedForSingleCombination() {

        storage.postResult(BIN1_PSP1, true);
        storage.postResult(BIN1_PSP1, false);
        storage.postResult(BIN1_PSP1, true);
        storage.postResult(BIN1_PSP1, false);
        storage.postResult(BIN1_PSP1, true);
        storage.postResult(BIN1_PSP1, false);
        storage.postResult(BIN1_PSP1, false);

        History result = storage.getHistory(BIN1_PSP1);
        int successful = result.getSuccessCount();
        int unsuccessful = result.getFailCount();

        assertEquals(3, successful);
        assertEquals(4, unsuccessful);
    }

    @Test
    public void testGetHistoryWhenMultipleObservationPostedForDifferentPairs() {
        storage.postResult(BIN1_PSP1, true);
        storage.postResult(BIN1_PSP1, true);
        storage.postResult(new BinPsp(BIN2, PSP2), false);
        storage.postResult(new BinPsp(BIN2, PSP3), true);
        storage.postResult(new BinPsp(BIN3, PSP3), true);

        History result = storage.getHistory(new BinPsp(BIN2, PSP2));
        int successful = result.getSuccessCount();
        int unsuccessful = result.getFailCount();

        assertEquals(0, successful);
        assertEquals(1, unsuccessful);
    }

    @Test
    public void testGetAllBinsWhenMultipleObservationPostedForDifferentPairs() {
        storage.postResult(BIN1_PSP1, false);
        storage.postResult(new BinPsp(BIN2, PSP1), false);

        Set<String> bins = storage.getAllBins(PSP1);
        assertTrue(bins.contains(BIN1));
        assertTrue(bins.contains(BIN2));
    }

    @Test
    public void testGetAllPspsWhenMultipleObservationPostedForDifferentPairs(){
        DataStorage storage = new DataStorage();
        storage.postResult(BIN1_PSP1, false);
        storage.postResult(new BinPsp(BIN1, PSP2), false);
        storage.postResult(new BinPsp(BIN1, PSP3), false);

        Set<String> psps = storage.getAllPsps(BIN1);
        assertTrue(psps.contains(PSP1));
        assertTrue(psps.contains(PSP2));
        assertTrue(psps.contains(PSP3));
    }
}
