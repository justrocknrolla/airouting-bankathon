package com.ai.routing.controllers;

import com.ai.routing.model.BinPsp;
import com.ai.routing.model.Stats;
import com.ai.routing.services.AIRoutingService;
import com.ai.routing.services.HistoryImporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
public class RestMainController {

    private final AIRoutingService aiRoutingService;
    private final HistoryImporterService historyImporterService;

    @Autowired
    RestMainController(AIRoutingService aiRoutingService, HistoryImporterService historyImporterService) {
        this.aiRoutingService = aiRoutingService;
        this.historyImporterService = historyImporterService;
    }

    @RequestMapping(value = "/suggestPSP", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map get(String bin) {
        return Collections.singletonMap("suggested", aiRoutingService.suggestPSP(bin));
    }

    @RequestMapping(value = "/postResult", method = RequestMethod.POST, produces = "text/html")
    @ResponseBody
    public String postResult(@RequestParam String bin, @RequestParam String psp, @RequestParam Boolean result) {
        aiRoutingService.postResult(new BinPsp(bin, psp), result);
        return "success";
    }

    @RequestMapping(value = "/postBulkResult", method = RequestMethod.POST, produces = "text/html")
    @ResponseBody
    public String postBulkResult(@RequestParam String bin, @RequestParam String psp, @RequestParam Boolean result) {
        IntStream.rangeClosed(1, 8)
                .forEach(i ->
                        aiRoutingService.postResult(new BinPsp(bin, psp), result)
                );
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/chart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map getChartData(@RequestParam String bin,
                            @RequestParam String psp,
                            @RequestParam(defaultValue = "100") int n) {
        Map<String,Object> result = new HashMap<>();
        result.put("points", aiRoutingService.getChartData(new BinPsp(bin, psp), n));
        result.put("stats", aiRoutingService.getStats(new BinPsp(bin, psp)));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Stats getStatsData(@RequestParam String bin,
                              @RequestParam String psp) {
        return aiRoutingService.getStats(new BinPsp(bin, psp));
    }

    @ResponseBody
    @RequestMapping(value = "/train", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map train() {
        historyImporterService.importDataDespacito(getClass().getResourceAsStream("/trained_data.txt"));
        return Collections.emptyMap();
    }

    @ResponseBody
    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map reset() {
        aiRoutingService.flushStorage();
        historyImporterService.importData(getClass().getResourceAsStream("/initial_data.txt"));
        return Collections.emptyMap();
    }
}
