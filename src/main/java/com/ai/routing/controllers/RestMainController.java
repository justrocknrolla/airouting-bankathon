package com.ai.routing.controllers;

import org.springframework.web.bind.annotation.*;

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

}
