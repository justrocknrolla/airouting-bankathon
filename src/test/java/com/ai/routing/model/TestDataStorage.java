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

    @PostConstruct
    private void setUp() {
        storage = new DataStorage();
    }

    @Test
    public void testGetHistoryWhenThereIsNoHistory() {
        History result = storage.getHistory(new BinPsp("bin1", "psp1"));
        int successful = result.getSuccessCount();
        int unsuccessful = result.getFailCount();

        assertEquals(0, successful);
        assertEquals(0, unsuccessful);
    }

    @Test
    public void testGetAllBinsWhenThereIsNoHistory() {
        Set<String> bins = storage.getAllBins("psp1");

        assertTrue(bins.isEmpty());
    }

    @Test
    public void testGetAllPspsWhenThereIsNoHistory() {
        Set<String> psps = storage.getAllPsps("bin1");

        assertTrue(psps.isEmpty());
    }
}
