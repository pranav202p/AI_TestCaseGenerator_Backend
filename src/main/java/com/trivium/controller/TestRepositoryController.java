package com.trivium.controller;

import com.trivium.service.TestRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestRepositoryController {

    @Autowired
    TestRepositoryService testRepositoryService;



    @GetMapping("/fetch-repo")
    public String fetchRepository(@RequestParam String repoUrl) {
        return testRepositoryService.processRepository(repoUrl);
    }
}