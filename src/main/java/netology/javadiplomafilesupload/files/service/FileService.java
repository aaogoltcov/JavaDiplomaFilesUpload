package netology.javadiplomafilesupload.files.service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import netology.javadiplomafilesupload.files.dto.FileResponse;
import netology.javadiplomafilesupload.files.dto.FileUpdate;
import netology.javadiplomafilesupload.files.exception.FileErrors;
import netology.javadiplomafilesupload.files.exception.FileSaveException;
import netology.javadiplomafilesupload.files.repository.FileEntity;
import netology.javadiplomafilesupload.files.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public List<FileResponse> getFilesWithLimit(int limit) {
        return fileRepository.findAllWithLimit(limit).stream().map(file -> {
            var fileResponse = new FileResponse();

            fileResponse.setFileName(file.getFileName());
            fileResponse.setEditedAt(file.getEditedAt());
            fileResponse.setSize(file.getSize());

            return fileResponse;
        }).toList();
    }

    public FileEntity getFileByFileName(String fileName) throws FileNotFoundException {
        return fileRepository.getFirstByFileName(fileName).orElseThrow(() -> new FileNotFoundException(FileErrors.FILE_NOT_FOUND));
    }

    public void uploadFile(String fileName, MultipartFile fileData) {
        try {
            FileEntity file = new FileEntity();

            file.setFileName(fileName);
            file.setFileData(fileData.getBytes());
            file.setEditedAt(LocalDateTime.now());
            file.setSize(fileData.getSize());
            fileRepository.save(file);

        } catch (Exception e) {
            throw new FileSaveException(FileErrors.FILE_NOT_STORED, e);
        }
    }

    public void updateFileName(String fileName, FileUpdate file) throws FileNotFoundException {
        Optional<FileEntity> findFile = fileRepository.getFirstByFileName(fileName);

        if (findFile.isEmpty()) {
            throw new FileNotFoundException(FileErrors.FILE_NOT_FOUND);
        }

        findFile.ifPresent(fileEntity -> {
            fileEntity.setFileName(file.getFileName());

            fileRepository.save(fileEntity);
        });
    }

    public void deleteFile(String fileName) throws FileNotFoundException {
        Optional<FileEntity> findFile = fileRepository.getFirstByFileName(fileName);

        if (findFile.isEmpty()) {
            throw new FileNotFoundException(FileErrors.FILE_NOT_FOUND);
        }

        findFile.ifPresent(fileRepository::delete);
    }
}
