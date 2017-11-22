package com.ai.routing.services;

import com.ai.routing.model.BinPsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Scanner;

@Service
public class HistoryImporterService {

    private final AIRoutingService aiRoutingService;

    @Autowired
    public HistoryImporterService(AIRoutingService aiRoutingService) {
        this.aiRoutingService = aiRoutingService;
    }

    public void importData(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] parts = s.split(",");
            String psp = parts[0];
            String bin = parts[1];
            boolean success = "1".equals(parts[2]);
            aiRoutingService.postResult(new BinPsp(bin, psp), success);
        }
    }
}
