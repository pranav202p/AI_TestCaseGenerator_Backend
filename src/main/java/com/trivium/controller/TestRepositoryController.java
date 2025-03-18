//package com.trivium.controller;
//import java.util.List;
//import com.trivium.service.TestRepositoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class TestRepositoryController {
//
//    @Autowired
//    TestRepositoryService testRepositoryService;
//
//
//
////    @GetMapping("/fetch-repo")
////    public String fetchRepository(@RequestParam String repoUrl) {
////        return testRepositoryService.processRepository(repoUrl);
////    }
//@GetMapping("/fetch-repo")
//public List<String> fetchRepository(@RequestParam String repoUrl) {
//    return testRepositoryService.processRepository(repoUrl);
//}
//}
//
//

//package com.trivium.controller;
//
//import com.trivium.service.TestRepositoryService;
//import com.trivium.service.OpenAiService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//public class TestRepositoryController {
//    private final TestRepositoryService testRepositoryService;
//    private final OpenAiService openAiService;
//
//    public TestRepositoryController(TestRepositoryService testRepositoryService, OpenAiService openAiService) {
//        this.testRepositoryService = testRepositoryService;
//        this.openAiService = openAiService;
//    }
//
//    // ✅ Fix: Add a GET endpoint for fetch-repo
//    @GetMapping("/fetch-repo")
//    public Map<String, Object> fetchRepo(@RequestParam String repoUrl) {
//        // Step 1: Fetch .java file URLs
//        List<String> javaFileUrls = testRepositoryService.processRepository(repoUrl);
//
//        if (javaFileUrls.isEmpty()) {
//            return Map.of("error", "No Java files found in the repository.");
//        }
//
//        // Step 2: Return file URLs as JSON response
//        return Map.of("java_files", javaFileUrls);
//    }
//
//    @PostMapping("/generate-tests")
//    public Map<String, String> generateTests(@RequestBody Map<String, String> request) {
//        String repoUrl = request.get("repo_url");
//
//        // Step 1: Fetch .java file URLs
//        List<String> javaFileUrls = testRepositoryService.processRepository(repoUrl);
//
//        if (javaFileUrls.isEmpty()) {
//            return Map.of("error", "No Java files found in the repository.");
//        }
//
//        // Step 2: Fetch Java code from the first file
//        String javaCode = testRepositoryService.fetchJavaCode(javaFileUrls.get(0));
//
//        if (javaCode.startsWith("Failed")) {
//            return Map.of("error", javaCode);
//        }
//
//        // Step 3: Generate JUnit test cases using GPT-4
//        String testCases = openAiService.generateTestCases(javaCode);
//
//        return Map.of("test_cases", testCases);
//    }
//
//
//
//}


package com.trivium.controller;
import java.util.*;
import com.trivium.service.TestRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.trivium.service.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.IOException;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:4200") // ✅ Allow only frontend
public class TestRepositoryController {
    private final TestRepositoryService testRepositoryService;
    private final OpenAiService openAiService;

    @Autowired
    public TestRepositoryController(TestRepositoryService testRepositoryService, OpenAiService openAiService) {
        this.testRepositoryService = testRepositoryService;
        this.openAiService = openAiService;
    }

    @GetMapping("/fetch-repo")
    public ResponseEntity<Map<String, Object>> fetchRepo(@RequestParam String repoUrl) {
        List<String> javaFileUrls = testRepositoryService.processRepository(repoUrl);

        if (javaFileUrls.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No Java files found in the repository."));
        }

        return ResponseEntity.ok(Map.of("java_files", javaFileUrls));
    }
    @PostMapping("/upload-zip")
    public ResponseEntity<Map<String, Object>> uploadZip(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty."));
        }

        // Process the uploaded ZIP file
        List<String> extractedJavaFiles = testRepositoryService.processZipFile(file);

        if (extractedJavaFiles.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No Java files found in the ZIP."));
        }

        return ResponseEntity.ok(Map.of("java_files", extractedJavaFiles));
    }

}


