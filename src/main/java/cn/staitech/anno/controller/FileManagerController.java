package cn.staitech.anno.controller;

import cn.staitech.anno.domain.file.FileNode;
import cn.staitech.anno.domain.file.PathVO;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Slf4j
@Api(value = "文件管理器", tags = "文件管理器")
@RestController
@RequestMapping("/filemanager")
public class FileManagerController {

    private String baseDir = "/home/uploadPath/big";

    /**
     * 查询目录下的文件夹和文件列表
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/list")
    public R<List<FileNode>> list(@RequestBody PathVO vo) {
        String path = vo.getPath();

        if (!path.startsWith(baseDir)) {
            path = baseDir;
        }

        File file = new File(path);

        // 如果传入的参数不存在或是文件返回提示
        if (!file.exists()) {
            return R.fail("路径不存在");
        }

        if (file.isFile()) {
            return R.fail("是文件，不是文件夹");
        }

        List<FileNode> fileNodeList = new ArrayList<>();

        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            for (File f : fileArray) {
                String type = f.isDirectory() ? "dir" : "file";
                FileNode node = new FileNode(f.getName(), f.getAbsolutePath(), type, f.length());
                fileNodeList.add(node);
            }
        }

        // 排序
        List<FileNode> nodes = fileNodeList.stream().
                sorted(Comparator.comparing(FileNode::getType).
                        thenComparing(FileNode::getType, Comparator.reverseOrder())).collect(Collectors.toList());

        return R.ok(nodes, "成功");
    }
}
