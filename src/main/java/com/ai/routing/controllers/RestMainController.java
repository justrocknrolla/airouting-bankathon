package com.ai.routing.controllers;

import com.ai.routing.model.Point;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestMainController {

    // TODO Implement
    @RequestMapping(value = "/suggestPSP", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String get(String bin) {
        return null;
    }

    @RequestMapping(value = "/postResult", method = RequestMethod.POST, produces = "text/html")
    @ResponseBody
    public String postResult(@RequestParam String bin, @RequestParam String psp, @RequestParam Boolean result) {
        // TODO Implement post functionality
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/chart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Point> getChartData(@RequestParam String bin,
                                    @RequestParam String psp,
                                    @RequestParam(defaultValue = "100") int n) {
        return null;
    }

}
