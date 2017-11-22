package com.ai.routing.controllers;

import com.ai.routing.model.Point;
import com.ai.routing.services.AIRoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        aiRoutingService.postPSP(bin, psp, result);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/chart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Point> getChartData(@RequestParam String bin,
                                    @RequestParam String psp,
                                    @RequestParam(defaultValue = "100") int n) {
        return aiRoutingService.getChartData(bin, psp, n);
    }

}
