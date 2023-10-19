package cn.staitech.anno;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @Author wudi
 * @Date 2023/10/18 11:02
 * @desc
 */
@Slf4j
@SpringBootTest
public class StatisticJson {


    public static void main(String[] args) throws Exception {
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createJsonParser(new File("D:/2.0json/20230728112533212_2320.svs_1_116001-116111-116F00-116156-116008-11601E-11601F-116009-116155-11600C-116154_1696840876185.json"));
        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            System.out.println("Error: root should be object: quiting.");
            return;
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            //开始之后元素
            current = jp.nextToken();
            if ("image".equals(fieldName)) {
                TreeNode treeNode = jp.readValueAsTree();
                System.out.println(treeNode);
            }
            if ("label_info".equals(fieldName)) {
                if (current == JsonToken.START_ARRAY) {
                    //标签数组
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        //单个数组元素
                        JsonNode node = jp.readValueAsTree();

                        System.out.println("field1: " + node.get("label_name").asText());

                    }
                } else {
                    //没有数组数据则跳过
                    System.out.println("Error: records should be an array: skipping.");
                    jp.skipChildren();
                }
            } else {
                //其他标签跳过
                System.out.println("Unprocessed property: " + fieldName);
                jp.skipChildren();
            }
        }
    }
}

