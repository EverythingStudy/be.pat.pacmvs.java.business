/*
package cn.staitech.anno;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.service.ImageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.file.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootTest
class StaiTechAnnoApplicationTests {
    @Autowired
    private ImageService imageService;

    @SneakyThrows
    @Test
    void test01() {
        Long imageId = 1L;
        Image image = imageService.selectImageById(imageId);

        String imagePath = image.getImagePath();
        String thumbUrl = image.getThumbUrl();
        String macroUrl = image.getMacroUrl();
        String labelUrl = image.getLabelUrl();
        System.out.println(imagePath);

       */
/* if (StringUtils.isNotBlank(imagePath)) {
            deleteFile(imagePath);
        }
        if (StringUtils.isNotBlank(thumbUrl)) {
            String thumbUrlFolder = thumbUrl.substring(0, thumbUrl.lastIndexOf("/" + imageId + "/")) + "/" + imageId;
            deleteFile(thumbUrlFolder);
        }
        if (StringUtils.isNotBlank(macroUrl)) {
            String macroUrlFolder = macroUrl.substring(0, macroUrl.lastIndexOf("/" + imageId + "/")) + "/" + imageId;
            deleteFile(macroUrlFolder);
        }
        if (StringUtils.isNotBlank(labelUrl)) {
            String labelUrlFolder = labelUrl.substring(0, labelUrl.lastIndexOf("/" + imageId + "/")) + "/" + imageId;
            deleteFile(labelUrlFolder);
        }
*//*


    }

    private void deleteFile(String path) throws IOException {
        if (StringUtils.isNotBlank(path)) {

            File file = new File(path);
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
                log.info(path + " 已经成功删除");
            }
        }
    }
}
*/
