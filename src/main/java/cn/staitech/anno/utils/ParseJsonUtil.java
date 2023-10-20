package cn.staitech.anno.utils;

import cn.staitech.anno.domain.ParseJson;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @Author wudi
 * @Date 2023/10/18 11:13
 * @desc
 */
public class ParseJsonUtil {

    public static ParseJson parseJson(String url, int i) throws IOException {
        ParseJson resp = new ParseJson();
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createJsonParser(new File(url));
        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            throw new RemoteException("json type error！");
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            // move from field name to field value
            current = jp.nextToken();
            if ("image".equals(fieldName)) {
                JsonNode treeNode = jp.readValueAsTree();

                System.out.println(treeNode);
                String imageName = treeNode.get("image_name").asText();
                resp.setImageName(imageName);
                System.out.println(imageName);
            }
            if ("label_info".equals(fieldName)) {
                if (current == JsonToken.START_ARRAY) {
                    // For each of the records in the array
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        // read the record into a tree model,
                        // this moves the parsing position to the end of it
                        JsonNode node = jp.readValueAsTree();
                        int size = node.size();
                        System.out.println(size);
                        System.out.println("field1: " + node.get("label_name").asText());
                        i++;
                    }
                    resp.setLabels(i);
                } else {

                    System.out.println("Error: records should be an array: skipping.");
                    jp.skipChildren();
                }
            } else {
                System.out.println("Unprocessed property: " + fieldName);
                jp.skipChildren();
            }
        }
        return resp;
    }

    public static void main(String[] args) throws IOException {
        String url = "D:/2.0json/20230728112533212_2320.svs_1_116001-116111-116F00-116156-116008-11601E-11601F-116009-116155-11600C-116154_1696840876185.json";

        parseJson(url, 0);
    }
}