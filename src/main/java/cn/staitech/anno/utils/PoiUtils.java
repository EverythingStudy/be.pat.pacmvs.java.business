package cn.staitech.anno.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description poi操作工具类
 * @date 2023/7/13 09:42:09
 */
@Slf4j
public class PoiUtils {
    /**
     * word doc转字节数组
     *
     * @param doc
     * @return
     */
    public static byte[] doc2Byte(XWPFDocument doc) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doc.write(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
        byte[] bytes = out.toByteArray();
        return bytes;
    }

    /**
     * 加载模板
     *
     * @return
     */
    public static XWPFDocument loadTPL(byte[] bytes) {
        InputStream is = null;
        XWPFDocument doc = null;
        try {
            is = new ByteArrayInputStream(bytes);
            doc = new XWPFDocument(is);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return doc;
    }


    /**
     * 加载模板
     *
     * @return
     */
    public static XWPFDocument loadTPL() {
        String path = "";
        InputStream is = null;
        XWPFDocument doc = null;
        try {
            path = ResourceUtils.getURL("classpath:template/visceraLesionTPL.docx").getPath();
            is = new FileInputStream(path);
            doc = new XWPFDocument(is);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return doc;
    }

    /**
     * word,遍历table，替换模板内容
     *
     * @param table
     */
    public static void convertTable(XWPFTable table, Map<String, Object> params) {
        if (table != null) {
            table.getRows().forEach(row -> {
                row.getTableCells().forEach(cell -> {
                    cell.getParagraphs().forEach(p -> {
                        p.getRuns().forEach(run -> {
                            String text = run.getText(0);
                            int start = text.indexOf("${");
                            int end = text.indexOf("}");
                            if (start != -1) {
                                String key = text.substring(start + 2, end);
                                String str = params.get(key).toString();
                                log.debug("/******************************替换表头模板内容：{};;key:{};;{}***********************/", text, key, str);
                                run.setText(str, 0);
                            }
                        });
                    });
                });
            });
        }
    }

    /**
     * word单元格行合并
     *
     * @param table    表格
     * @param col      合并行所在列
     * @param startRow 开始行
     * @param endRow   结束行
     * @date 2020年4月8日 下午4:46:18
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int startRow, int endRow) {
        for (int i = startRow; i <= endRow; i++) {
            XWPFTableCell cell = table.getRow(i).getCell(col);
            if (i == startRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 设置页边距、横向布局
     *
     * @param document
     * @param orientation
     */
    public static void changeOrientation(XWPFDocument document, XWPFParagraph paragraph, String orientation) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        CTDocument1 doc = document.getDocument();
        CTBody body = doc.getBody();
        CTSectPr section = body.isSetSectPr() ? body.getSectPr() : body.addNewSectPr();
        //设置页边距
        CTPageMar pageMar = section.isSetPgMar() ? section.getPgMar() : section.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(500));
        pageMar.setRight(BigInteger.valueOf(500));
        pageMar.setTop(BigInteger.valueOf(500));
        pageMar.setBottom(BigInteger.valueOf(500));
        CTPageSz pageSize = section.isSetPgSz() ? section.getPgSz() : section.addNewPgSz();
        //设置横向布局
        if (orientation.equals("landscape")) {
            pageSize.setOrient(STPageOrientation.LANDSCAPE);
            pageSize.setH(BigInteger.valueOf(595 * 20));
            pageSize.setW(BigInteger.valueOf(842 * 20));
        } else {
            pageSize.setOrient(STPageOrientation.PORTRAIT);
            pageSize.setH(BigInteger.valueOf(842 * 20));
            pageSize.setW(BigInteger.valueOf(595 * 20));
        }
    }

    /**
     * 计算表格合并参数
     *
     * @param dataList  数据
     * @param fieldName 合并字段名
     * @throws Exception
     */
    public static List<Map<String, Integer>> computeMergeCell(List<Map<String, Object>> dataList, String fieldName) {
        List<Map<String, Integer>> mergeCellsParams = new ArrayList<>();
        //分组合并标识
        boolean flag = false;
        //分组列
        String fieldValue = "";
        //当前组数据序号
        int groupRow = 0;

        for (int j = 0; j < dataList.size(); j++) {
            Map<String, Object> data = dataList.get(j);
            log.debug("计算合并cell报表数据：{}", data);
            //组内第一行
            if ("".equals(fieldValue)) {
                fieldValue = MapUtils.getString(data, fieldName);
                groupRow++;
                //数据最后一行
            } else if ((j + 1) == dataList.size()) {
                Map<String, Object> temp = dataList.get(j-1);
                //判断上一行是否相同
                if (MapUtils.getString(data, fieldName).equals(MapUtils.getString(temp, fieldName))) {
                    flag = true;
                    groupRow++;
                }
                //组内数据
            } else if (fieldValue.equals(MapUtils.getString(data, fieldName))) {
                groupRow++;
                Map<String, Object> temp = dataList.get(j + 1);
                //判断下一行是否同组
                if (!fieldValue.equals(MapUtils.getString(temp, fieldName))) {
                    flag = true;
                }
            }
            //构建合并单元格参数
            if (flag) {
                Map<String, Integer> mergeCellsParam = new HashMap<>();
                //表头3行
                mergeCellsParam.put("startRow", j - (groupRow - 1) + 3);
                mergeCellsParam.put("endRow", j + 3);
                mergeCellsParams.add(mergeCellsParam);
                //重置组标识
                flag = false;
                fieldValue = "";
                groupRow = 0;
            }
        }
        return mergeCellsParams;
    }

    /**
     * 更新cell样式
     *
     * @param cell
     * @param value
     */
    public static void updateCell(XWPFTableCell cell, Object value) {
        updateCell(cell, value, false);
    }

    /**
     * 更新cell样式
     *
     * @param cell
     * @param value
     */
    public static void updateCell(XWPFTableCell cell, Object value, Boolean bold) {
        XWPFParagraph paragraph = cell.getParagraphArray(0);
        XWPFRun xwpfRun = null;
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs.isEmpty()) {
            xwpfRun = paragraph.createRun();
        } else {
            xwpfRun = runs.get(0);
        }
        xwpfRun.setFontSize(9);
        xwpfRun.setFontFamily("等线");
        if (bold) {
            xwpfRun.setBold(true);
        }
        xwpfRun.setText(value == null ? "" : value.toString(), 0);
    }

}
