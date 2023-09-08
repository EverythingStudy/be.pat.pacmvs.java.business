package cn.staitech.anno.domain.file;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author wangf
 */
@Data
@Accessors(chain = true)
public class Chunk implements Serializable {
    /**
     * 当前文件块，从0开始
     */
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    private Long chunkSize;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 切片总块数
     */
    private Integer totalChunks;

    /**
     * 专题id
     */
    private Long specialId;

    /**
     * 二进制文件(分片文件)
     */
    private MultipartFile file;

}
