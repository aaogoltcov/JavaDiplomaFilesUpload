package netology.javadiplomafilesupload.files.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import netology.javadiplomafilesupload.files.dto.FileResponse;
import netology.javadiplomafilesupload.files.dto.FileUpdate;
import netology.javadiplomafilesupload.files.exception.FileErrors;
import netology.javadiplomafilesupload.files.repository.FileEntity;
import netology.javadiplomafilesupload.files.service.FileService;
import netology.javadiplomafilesupload.log.LogMarker;

@RestController
@RequestMapping("/")
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("list")
    public ResponseEntity<List<FileResponse>> getFilesWithLimit(@Valid int limit) {
        List<FileResponse> files = fileService.getFilesWithLimit(limit).stream().map(file -> {
            var fileResponse = new FileResponse();

            fileResponse.setFileName(file.getFileName());
            fileResponse.setEditedAt(file.getEditedAt());
            fileResponse.setSize(file.getSize());

            return fileResponse;
        }).toList();

        log.info(LogMarker.READ, "Get files with limit {}", limit);

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("file")
    public ResponseEntity<FileEntity> getFileByFileName(@Valid @RequestParam("filename") String fileName) throws FileNotFoundException {
        FileEntity file = fileService.getFileByFileName(fileName).orElseThrow(() -> new FileNotFoundException(FileErrors.FILE_NOT_FOUND));

        log.info(LogMarker.READ, "Get file by file name {}", fileName);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping("file")
    public ResponseEntity<HttpStatus> deleteFileByFileName(@Valid @RequestParam("filename") String fileName) throws FileNotFoundException {
        fileService.deleteFile(fileName);

        log.info(LogMarker.DELETE, "Delete file by file name {}", fileName);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("file")
    public ResponseEntity<HttpStatus> uploadFile(
            @Valid @RequestParam("filename") String fileName,
            @RequestParam("file") MultipartFile fileData
    ) {
        fileService.uploadFile(fileName, fileData);

        log.info(LogMarker.CREATE, "Upload file with file name {}", fileName);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("file")
    public ResponseEntity<HttpStatus> updateFile(
            @Valid @RequestParam("filename") String fileName,
            @RequestBody FileUpdate file
    ) throws FileNotFoundException {
        fileService.updateFileName(fileName, file);

        log.info(LogMarker.UPDATE, "Update file by file name {}", fileName);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
