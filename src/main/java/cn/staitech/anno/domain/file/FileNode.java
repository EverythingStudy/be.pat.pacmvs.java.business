package cn.staitech.anno.domain.file;

/**
 * File节点
 * @author wangf
 */
public class FileNode {
    private String name;
    private String path;
    private String type;
    private Long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public FileNode(String name, String path, String type,Long size) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
    }
}
