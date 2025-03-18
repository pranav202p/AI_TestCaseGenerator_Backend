package com.trivium.service;
import java.util.List;
import java.util.Map;

//public interface TestRepositoryService {
////    public String processRepository(String repoUrl);
//public List<String> processRepository(String repoUrl);
//    public String fetchJavaCode(String javaFileUrl);
//
//}






//public interface TestRepositoryService {
//
//    List<String> processRepository(String repoUrl);
//
//
//    String fetchJavaCode(String javaFileUrl);
//}


import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface TestRepositoryService {
    List<String> processRepository(String repoUrl);
    String fetchJavaCode(String fileUrl);
    public List<String> extractJavaFilesFromZip(MultipartFile zipFile) throws IOException;
    List<String> processZipFile(MultipartFile zipFile) ;
}



