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
import netology.javadiplomafilesupload.files.dto.FileResponse;
import netology.javadiplomafilesupload.files.dto.FileUpdate;
import netology.javadiplomafilesupload.files.repository.FileEntity;
import netology.javadiplomafilesupload.files.service.FileService;

@RestController
@RequestMapping("/")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("list")
    public ResponseEntity<List<FileResponse>> getFilesWithLimit(@Valid int limit) {
        List<FileResponse> files = fileService.getFilesWithLimit(limit);

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("file")
    public ResponseEntity<FileEntity> getFileByFileName(@Valid @RequestParam("filename") String fileName) throws FileNotFoundException {
        FileEntity file = fileService.getFileByFileName(fileName);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping("file")
    public ResponseEntity<HttpStatus> deleteFileByFileName(@Valid @RequestParam("filename") String fileName) throws FileNotFoundException {
        fileService.deleteFile(fileName);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("file")
    public ResponseEntity<HttpStatus> uploadFile(
            @Valid @RequestParam("filename") String fileName,
            @RequestParam("file") MultipartFile fileData
    ) {
        fileService.uploadFile(fileName, fileData);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("file")
    public ResponseEntity<HttpStatus> updateFile(
            @Valid @RequestParam("filename") String fileName,
            @RequestBody FileUpdate file
    ) throws FileNotFoundException {
        fileService.updateFileName(fileName, file);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
