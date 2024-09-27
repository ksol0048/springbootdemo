package kroryi.demo.controller;

import io.swagger.annotations.ApiOperation;
import kroryi.demo.dto.UploadFileDTO;
import kroryi.demo.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
        log.info(uploadFileDTO);
        log.info("upload->{}", uploadPath);

        if (uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().isEmpty()) {

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(file -> {
                String originalFilename = file.getOriginalFilename();
//                log.info("/upload->{}", file.getOriginalFilename());
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFilename);
                log.info(uuid);
                log.info(originalFilename);
                log.info(uploadPath);

                boolean image = false;

                try {
                    file.transferTo(savePath);
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;

                        File thumFile = new File(uploadPath, "s_" + uuid + "_" + originalFilename);

                        Thumbnailator.createThumbnail(savePath.toFile(), thumFile, 200, 200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalFilename)
                        .img(image)
                        .build()
                );

            });
            return list;
        }

        return null;
    }

    @ApiOperation(value = "view파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> view(@PathVariable String filename) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + filename);

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value = "Delete 파일", notes = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{filename}")
    public Map<String, Boolean> remove(@PathVariable String filename) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + filename);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if (contentType.startsWith("image")) {
                File thumbailFile = new File(uploadPath + File.separator + "s_" + filename);
                thumbailFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        resultMap.put("result", removed);

        return resultMap;
    }
}
