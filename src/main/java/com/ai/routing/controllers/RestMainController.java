package com.ai.routing.controllers;

import com.ai.routing.model.BinPsp;
import com.ai.routing.model.Stats;
import com.ai.routing.services.AIRoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
public class RestMainController {

    private AIRoutingService aiRoutingService;

    @Autowired
    RestMainController(AIRoutingService aiRoutingService) {
        this.aiRoutingService = aiRoutingService;
    }

    @RequestMapping(value = "/suggestPSP", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String get(String bin) {
        return aiRoutingService.suggestPSP(bin);
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

}
