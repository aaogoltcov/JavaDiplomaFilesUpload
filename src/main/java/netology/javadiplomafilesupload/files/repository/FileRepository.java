package netology.javadiplomafilesupload.files.repository;


import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<FileEntity, Integer>, JpaSpecificationExecutor<FileEntity> {
    @Query("""
            select distinct f
            from FileEntity f
            order by f.fileName
            limit :limit
            """)
    Collection<FileEntity> findAllWithLimit(int limit);

    Optional<FileEntity> getFirstByFileName(String fileName);
}
