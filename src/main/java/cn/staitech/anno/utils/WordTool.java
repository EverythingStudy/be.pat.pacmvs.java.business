package cn.staitech.anno.utils;

import cn.staitech.anno.vo.diagnosis.StatisticsHeadVo;
import cn.staitech.anno.vo.reportrecord.ReportRecordAddVO;
import cn.staitech.anno.vo.special.Special;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: WordTool
 * @Description:病理下载处理
 * @date 2023年7月24日
 */
@Slf4j
public class WordTool {

    /**
     * 报告模板缓存
     */
    public static Map<String, byte[]> TPL_CACHE = new HashMap<>();

    public static String generateWord(Special special, ReportRecordAddVO reportRecordAddVO, List<List<String>> list, List<Map<String, Object>> tableMapList, String rptPath) {
        // 1.创建一个新的Word文档
        XWPFDocument document = new XWPFDocument();
        FileOutputStream fos = null;
        try {
            // the body content
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            //			  run.setText("生仝智能科技（北京）有限公司专题号：R22-S277-RD-01-241301ZX2010重复皮下注射给予SD大鼠4周和恢复期4周的毒性试验表1组织病理学 – 病变组间分布 给药期结束安乐死");
            run.setText("生仝智能科技（北京）有限公司");
            run.addCarriageReturn();
            //			run.setText("专题号：R22-S277-RD-01-241301");
            run.setText("专题号：" + special.getSpecialNumber());
            run.addCarriageReturn();
            //			run.setText("ZX2010重复皮下注射给予SD大鼠4周和恢复期4周的毒性试验");
            run.setText(special.getSpecialName());
            run.addCarriageReturn();
            String reasonStr = "";
            //			1给药结束安乐死、2恢复期结束安乐死           表1组织病理学–病变组间分布 给药期结束安乐死  表2组织病理学–病变组间分布 恢复期结束安乐死
            if (reportRecordAddVO.getReasons() == 1) {
                reasonStr = "给药结束安乐死";
                run.setText("表1组织病理学 – 病变组间分布 " + reasonStr);
            } else if (reportRecordAddVO.getReasons() == 2) {
                reasonStr = "恢复期结束安乐死";
                run.setText("表1组织病理学 – 病变组间分布 " + reasonStr);
            }

            run.addCarriageReturn();
            // 粗体
            run.setBold(true);
            // 设置字体（如：微软雅黑,华文楷体,宋体）
            run.setFontFamily("等线");
            // 字体大小
            run.setFontSize(9);
            //设置水平居中
            paragraph.setAlignment(ParagraphAlignment.CENTER);

            //统计一共有杜少个组，然后计算需要生成多少个表格-然后进到表格里计算一共需要创建多少列
            for (int m = 0; m < list.size(); m++) {
                //			for(int m=0;m<1;m++){
                List<String> tableData = list.get(m);
                int row_hang = tableData.size();
                //int cell_lie = tableData.get(0).split("\\|\\|").length;
                // 2.创建一个新的表格，row_hang行cell_lie列
                XWPFTable t = document.createTable();

                XWPFTable table = getTPLTable();

                // 3.设置表格样式
                //				table.getCTTbl().addNewTblPr().addNewTblStyle().setVal("Table Grid");
                //				table.setWidth("100%"); // 设置表格宽度为100%

                // 4.填充表格内容
                //				XWPFTableCell cell = null;

                Map<String, Object> hisDataList = tableMapList.get(m);
                List<StatisticsHeadVo> manPlsList = (List<StatisticsHeadVo>) hisDataList.get("manPlsList");
                List<StatisticsHeadVo> womanPlsList = (List<StatisticsHeadVo>) hisDataList.get("womanPlsList");
                int manBlackPoint = 0;
                int womanBlackPoint = 0;
                if (CollectionUtils.isNotEmpty(manPlsList)) {
                    manBlackPoint = manPlsList.size();
                }
                if (CollectionUtils.isNotEmpty(womanPlsList)) {
                    womanBlackPoint = womanPlsList.size();
                }
                int maxCell = manBlackPoint + womanBlackPoint + 1;

                for (int row = 0; row < row_hang; row++) {
                    String rowStr = tableData.get(row);
                    String[] coleArray = rowStr.split("\\|\\|");
                    // 设置单元格内容
                    int currentRow = row + 1;
                    if (currentRow < 5) {
                        XWPFTableRow tempRow = table.getRow(currentRow - 1);
                        for (int col = 8; col > maxCell - 1; col--) {
                            tempRow.removeCell(col);
                            tempRow.getCtRow().removeTc(col);
                        }
                        // 循环遍历表格的列
                        for (int col = 0; col < coleArray.length; col++) {
                            // 获取当前单元格
                            XWPFTableCell cell = tempRow.getCell(col);
                            // 设置单元格内容垂直居中
                            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                            // 设置单元格内容水平居中(获取单元格中的第一个段落对象)
                            cell.getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
                            //获取 XWPFTableCell 的CTTc
                            CTTc ctTc = cell.getCTTc();
                            CTTcPr tcPr = ctTc.getTcPr();
                            CTTcBorders borders = tcPr.getTcBorders();

                            XWPFTableCell cell_1 = tempRow.getCell(1);
                            CTTc ctTc_1 = cell_1.getCTTc();
                            CTTcPr tcPr_1 = ctTc_1.getTcPr();
                            CTTcBorders borders_1 = tcPr_1.getTcBorders();
                            String colour1 = (String) borders_1.getLeft().getColor();


                            //右侧
                            if (col == coleArray.length - 1) {
                                setBorder(borders.getRight(), colour1);
                            }
                            //左侧
                            if (col == manBlackPoint + 1) {
                                setBorder2(borders.getLeft(), colour1);
                            }

                            //获取 CTP
                            CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);
                            // getParagraph(ctP) 获取 XWPFParagraph
                            XWPFParagraph par = cell.getParagraph(ctP);
                            // XWPFRun   设置格式
                            XWPFRun run2 = par.createRun();
                            // 设置字体（如：微软雅黑,华文楷体,宋体）
                            run2.setFontFamily("等线");
                            // 字体大小
                            run2.setFontSize(9);

                            int currentCol = col + 1;
                            if (currentCol == 1) {
                                String text = "";
                                if (currentRow < 4) {
                                    text = "移走原因：";
                                    XWPFRun run3 = par.createRun();
                                    run3.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
                                    run3.setFontSize(9);//字体大小
                                    run3.setText(reasonStr);
                                } else {
                                    text = "检查切片数量：";
                                    cell.getParagraphArray(0).setAlignment(ParagraphAlignment.RIGHT);
                                }
                                run2.setText(text);
                                run2.setBold(true);

                            } else {
                                run2.setText(coleArray[col]);
                                // 设置字体（如：微软雅黑,华文楷体,宋体）
                                run2.setFontFamily("等线");
                                // 字体大小
                                run2.setFontSize(9);
                                if (currentRow == 3) {
                                    // 设置换行
                                    run2.addBreak();
                                    XWPFRun run3 = par.createRun();
                                    // 设置字体（如：微软雅黑,华文楷体,宋体）
                                    run3.setFontFamily("等线");
                                    // 字体大小
                                    run3.setFontSize(9);
                                    run3.setText("mg");
                                }
                            }
                        }
                        t.addRow(tempRow);

                    } else if (currentRow == 5) {
                        //脏器开始-格式特殊
                        XWPFTableRow tempRow = null;
                        tempRow = table.getRow(4);
                        for (int col = 8; col > maxCell - 1; col--) {
                            tempRow.removeCell(col);
                            tempRow.getCtRow().removeTc(col);
                        }
                        // 循环遍历表格的列
                        for (int col = 0; col < manBlackPoint + womanBlackPoint + 1; col++) {
                            // 获取当前单元格
                            XWPFTableCell cell = tempRow.getCell(col);
                            // 设置单元格内容垂直居中
                            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                            // 设置单元格内容水平居中(获取单元格中的第一个段落对象)
                            cell.getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
                            //获取 XWPFTableCell 的CTTc
                            CTTc ctTc = cell.getCTTc();
                            //获取 CTP
                            CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);
                            CTTcPr tcPr = ctTc.getTcPr();
                            CTTcBorders borders = tcPr.getTcBorders();

                            XWPFTableCell cell_1 = tempRow.getCell(1);
                            CTTc ctTc_1 = cell_1.getCTTc();
                            CTTcPr tcPr_1 = ctTc_1.getTcPr();
                            CTTcBorders borders_1 = tcPr_1.getTcBorders();
                            String colour1 = (String) borders_1.getLeft().getColor();


                            //右侧
                            if (col == coleArray.length - 1) {
                                setBorder(borders.getRight(), colour1);
                            }
                            //左侧
                            if (col == manBlackPoint + 1) {
                                setBorder2(borders.getLeft(), colour1);
                            }

                            //getParagraph(ctP) 获取 XWPFParagraph
                            XWPFParagraph par = cell.getParagraph(ctP);
                            //XWPFRun   设置格式
                            XWPFRun run2 = par.createRun();
                            //设置字体（如：微软雅黑,华文楷体,宋体）
                            run2.setFontFamily("等线");
                            //字体大小
                            run2.setFontSize(9);


                            //viscera_
                            String value = "";
                            if (col < coleArray.length) {
                                value = coleArray[col];
                            }
                            if (StringUtils.isEmpty(value)) {
                                setBorder2(borders.getRight(), colour1);
                            }
                            if (value.startsWith("viscera_")) {
                                value = value.replaceAll("viscera_", "");
                                run2.setText(value);
                                //器官 靠左+黑体
                                run2.setBold(true);
                                cell.getParagraphArray(0).setAlignment(ParagraphAlignment.LEFT);
                            } else {
                                run2.setText(value);
                            }
                        }
                        t.addRow(tempRow);
                    } else {
                        XWPFTableRow tempRow = null;
                        CTRow ctrow = null;
                        //复制表头行的样式
                        if (row_hang != currentRow) {
                            ctrow = CTRow.Factory.parse(table.getRow(5).getCtRow().newInputStream());//重点行
                        } else {
                            ctrow = CTRow.Factory.parse(table.getRow(6).getCtRow().newInputStream());//重点行
                        }
                        //创建相同样式行
                        tempRow = new XWPFTableRow(ctrow, table);
                        for (int col = 8; col > maxCell - 1; col--) {
                            tempRow.removeCell(col);
                            tempRow.getCtRow().removeTc(col);
                        }

                        if (coleArray.length == 1) {
                            //							System.out.println("就想知道这里进来过嘛==========================================="+rowStr);
                            for (int col = 0; col < manBlackPoint + womanBlackPoint + 1; col++) { // 循环遍历表格的列
                                // 获取当前单元格
                                //							cell = table.getRow(row).getCell(col);
                                XWPFTableCell cell = tempRow.getCell(col);
                                // 设置单元格内容垂直居中
                                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                                // 设置单元格内容水平居中(获取单元格中的第一个段落对象)
                                cell.getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
                                //获取 XWPFTableCell 的CTTc
                                CTTc ctTc = cell.getCTTc();
                                //获取 CTP
                                CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);


                                CTTcPr tcPr = ctTc.getTcPr();
                                CTTcBorders borders = tcPr.getTcBorders();


                                XWPFTableCell cell_1 = tempRow.getCell(1);
                                CTTc ctTc_1 = cell_1.getCTTc();
                                CTTcPr tcPr_1 = ctTc_1.getTcPr();
                                CTTcBorders borders_1 = tcPr_1.getTcBorders();
                                String colour1 = (String) borders_1.getLeft().getColor();
                                // System.out.println("colour1=============="+colour1);

                                //右侧
                                if (col == coleArray.length - 1) {
                                    //									setBorder(borders.getRight(),"000000");
                                    setBorder(borders.getRight(), colour1);
                                }
                                //左侧
                                if (col == manBlackPoint + 1) {
                                    setBorder2(borders.getLeft(), colour1);
                                }

                                //getParagraph(ctP) 获取 XWPFParagraph
                                XWPFParagraph par = cell.getParagraph(ctP);
                                //XWPFRun   设置格式
                                XWPFRun run2 = par.createRun();
                                run2.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
                                run2.setFontSize(9);//字体大小


                                //								viscera_
                                String value = "";
                                if (col < coleArray.length) {
                                    value = coleArray[col];
                                }

                                if (StringUtils.isEmpty(value)) {
                                    setBorder2(borders.getRight(), colour1);
                                }
                                //								String value = coleArray[col];
                                if (value.startsWith("viscera_")) {
                                    value = value.replaceAll("viscera_", "");
                                    run2.setText(value);
                                    //器官 靠左+黑体
                                    run2.setBold(true);
                                    cell.getParagraphArray(0).setAlignment(ParagraphAlignment.LEFT);
                                } else {
                                    run2.setText(value);
                                }
                                if (col == 0) {
                                    cell.getParagraphArray(0).setAlignment(ParagraphAlignment.LEFT);
                                }
                            }

                        } else {
                            //							System.out.println("这里也有呀~~~");
                            //							for (int col = 0; col < coleArray.length; col++) { // 循环遍历表格的列
                            for (int col = 0; col < manBlackPoint + womanBlackPoint + 1; col++) { // 循环遍历表格的列
                                // 获取当前单元格
                                //							cell = table.getRow(row).getCell(col);
                                XWPFTableCell cell = tempRow.getCell(col);
                                // 设置单元格内容垂直居中
                                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                                // 设置单元格内容水平居中(获取单元格中的第一个段落对象)
                                cell.getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
                                //获取 XWPFTableCell 的CTTc
                                CTTc ctTc = cell.getCTTc();
                                //获取 CTP
                                CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);


                                CTTcPr tcPr = ctTc.getTcPr();
                                CTTcBorders borders = tcPr.getTcBorders();


                                XWPFTableCell cell_1 = tempRow.getCell(1);
                                CTTc ctTc_1 = cell_1.getCTTc();
                                CTTcPr tcPr_1 = ctTc_1.getTcPr();
                                CTTcBorders borders_1 = tcPr_1.getTcBorders();
                                String colour1 = (String) borders_1.getLeft().getColor();
                                // System.out.println("colour1=============="+colour1);

                                //右侧
                                if (col == coleArray.length - 1) {
                                    //									setBorder(borders.getRight(),"000000");
                                    setBorder(borders.getRight(), colour1);
                                }
                                //左侧
                                if (col == manBlackPoint + 1) {
                                    setBorder2(borders.getLeft(), colour1);
                                }

                                //getParagraph(ctP) 获取 XWPFParagraph
                                XWPFParagraph par = cell.getParagraph(ctP);
                                //XWPFRun   设置格式
                                XWPFRun run2 = par.createRun();
                                run2.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
                                run2.setFontSize(9);//字体大小


                                //								viscera_
                                String value = coleArray[col];
                                if (StringUtils.isEmpty(value)) {
                                    setBorder2(borders.getRight(), colour1);
                                }

                                if (value.startsWith("viscera_")) {
                                    value = value.replaceAll("viscera_", "");
                                    run2.setText(value);
                                    //器官 靠左+黑体
                                    run2.setBold(true);
                                } else {
                                    run2.setText(value);
                                }
                                if (col == 0) {
                                    cell.getParagraphArray(0).setAlignment(ParagraphAlignment.LEFT);
                                }
                            }
                        }
                        t.addRow(tempRow);

                    }

                }
                t.removeRow(0);
                document.setTable(m, t);
                byte[] bytes = PoiUtils.doc2Byte(document);
                ZipSecureFile.setMinInflateRatio(0.001);
                document = PoiUtils.loadTPL(bytes);
                t = document.getTables().get(m);


                int manSize = 0;
                if (CollectionUtils.isNotEmpty(manPlsList)) {
                    // 6.合并单元格方式二(推荐)
                    // 6.1 水平合并第1行的第2列到第4列
                    //					mergeCellsByHorizontal(table, 0, 1, 4);
                    //					mergeCellsByHorizontal(table, 0, 1, manPlsList.size());
                    mergeCellsByHorizontal(t, 0, 1, manPlsList.size());
                    manSize = manPlsList.size();
                }
                if (CollectionUtils.isNotEmpty(womanPlsList)) {
                    // 6.合并单元格方式二(推荐)
                    // 6.1 水平合并第1行的第2列到第4列
                    //					mergeCellsByHorizontal(table, 0, 5, 8);
                    //					mergeCellsByHorizontal(table, 0, manSize+1, manSize+womanPlsList.size());
                    mergeCellsByHorizontal(t, 0, manSize + 1, manSize + womanPlsList.size());
                }
                // 6.2 垂直合并第1列的第3行到第4行
                //				mergeCellsByVertically(table, 0, 0, 2);
                mergeCellsByVertically(t, 0, 0, 2);


                //				CTTblPr tblPr = t.getCTTbl().getTblPr();
                //				if(tblPr == null) tblPr = t.getCTTbl().addNewTblPr();
                //				CTTblBorders borders = tblPr.addNewTblBorders();
                ////		        t.getCTTbl().getTblPr().getTblBorders().getRight().setVal(STBorder.CELTIC_KNOTWORK);
                //				borders.addNewBottom().setVal(STBorder.Enum.forInt(STBorder.NONE.intValue()));  // 设置底部边框为空
                //				borders.addNewLeft().setVal(STBorder.Enum.forInt(STBorder.NONE.intValue()));  // 设置左侧边框为空
                //				borders.addNewRight().setVal(STBorder.Enum.forInt(STBorder.NONE.intValue())); // 设置右侧边框为空
                //				borders.addNewInsideH().setVal(STBorder.Enum.forInt(STBorder.SINGLE.intValue())); // 设置水平内部边框为虚线
                //				borders.addNewInsideV().setVal(STBorder.Enum.forInt(STBorder.SINGLE.intValue())); // 设置竖直内部边框为虚线
                //两个表分页隔开
                document.createParagraph().setPageBreak(true);
                ;
            }


            // create header-footer
            XWPFHeaderFooterPolicy headerFooterPolicy = document.getHeaderFooterPolicy();
            if (headerFooterPolicy == null) {
                headerFooterPolicy = document.createHeaderFooterPolicy();
            }

            // create header start
            XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);

            paragraph = header.getParagraphArray(0);
            if (paragraph == null) {
                paragraph = header.createParagraph();
            }
            paragraph.setAlignment(ParagraphAlignment.RIGHT);
            run = paragraph.createRun();
            run.setText("页码: ");
            // 粗体
            run.setBold(true);
            run.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
            run.setFontSize(9);//字体大小
            paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
            // 粗体
            run.setBold(true);
            run.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
            run.setFontSize(9);//字体大小
            run = paragraph.createRun();


            // 保存文档
            // 输出流，用于将文档写入磁盘
            //			fos = new FileOutputStream("C:\\Users\\86153\\Desktop\\nacas-本地\\test_"+System.currentTimeMillis()+"_1.docx");
            fos = new FileOutputStream(rptPath);
            // 将文档写入输出流
            document.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    // 关闭输出流
                    fos.close();
                    if (document != null) {
                        // 关闭Word文档
                        document.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("Word文档生成成功！");
        return "";
    }

    public static String generateWordOld(Special special, ReportRecordAddVO reportRecordAddVO, List<List<String>> list, List<Map<String, Object>> tableMapList, String rptPath) {
        // 1.创建一个新的Word文档
        XWPFDocument document = new XWPFDocument();
        FileOutputStream fos = null;
        try {
            // the body content
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            //			  run.setText("生仝智能科技（北京）有限公司专题号：R22-S277-RD-01-241301ZX2010重复皮下注射给予SD大鼠4周和恢复期4周的毒性试验表1组织病理学 – 病变组间分布 给药期结束安乐死");
            run.setText("生仝智能科技（北京）有限公司");
            run.addCarriageReturn();
            //			run.setText("专题号：R22-S277-RD-01-241301");
            run.setText("专题号：" + special.getSpecialNumber());
            run.addCarriageReturn();
            //			run.setText("ZX2010重复皮下注射给予SD大鼠4周和恢复期4周的毒性试验");
            run.setText(special.getSpecialName());
            run.addCarriageReturn();
            String reasonStr = "";
            //			1给药结束安乐死、2恢复期结束安乐死           表1组织病理学–病变组间分布 给药期结束安乐死  表2组织病理学–病变组间分布 恢复期结束安乐死
            if (reportRecordAddVO.getReasons() == 1) {
                reasonStr = "给药结束安乐死";
                run.setText("表1组织病理学 – 病变组间分布 " + reasonStr);
            } else if (reportRecordAddVO.getReasons() == 2) {
                reasonStr = "恢复期结束安乐死";
                run.setText("表1组织病理学 – 病变组间分布 " + reasonStr);
            }

            run.addCarriageReturn();
            // 粗体
            run.setBold(true);
            run.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
            run.setFontSize(9);//字体大小
            //设置水平居中
            paragraph.setAlignment(ParagraphAlignment.CENTER);

            //统计一共有杜少个组，然后计算需要生成多少个表格-然后进到表格里计算一共需要创建多少列
            for (int m = 0; m < list.size(); m++) {
                List<String> tableData = list.get(m);
                int row_hang = tableData.size();
                int cell_lie = tableData.get(0).split("\\|\\|").length;
                // 2.创建一个新的表格，row_hang行cell_lie列
                XWPFTable table = document.createTable(row_hang, cell_lie);
                // 3.设置表格样式
                table.getCTTbl().addNewTblPr().addNewTblStyle().setVal("Table Grid");
                table.setWidth("100%"); // 设置表格宽度为100%

                // 4.填充表格内容
                XWPFTableCell cell = null;
                for (int row = 0; row < row_hang; row++) { // 循环遍历表格的行
                    String rowStr = tableData.get(row);
                    String[] coleArray = rowStr.split("\\|\\|");
                    for (int col = 0; col < coleArray.length; col++) { // 循环遍历表格的列
                        // 获取当前单元格
                        cell = table.getRow(row).getCell(col);
                        // 设置单元格内容垂直居中
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                        // 设置单元格内容水平居中(获取单元格中的第一个段落对象)
                        cell.getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);

                        //获取 XWPFTableCell 的CTTc
                        CTTc ctTc = cell.getCTTc();
                        //获取 CTP
                        CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);
                        //getParagraph(ctP) 获取 XWPFParagraph
                        XWPFParagraph par = cell.getParagraph(ctP);
                        //XWPFRun   设置格式
                        XWPFRun run2 = par.createRun();
                        run2.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
                        run2.setFontSize(9);//字体大小


                        // 设置单元格内容
                        int currentRow = row + 1;
                        int currentCol = col + 1;

                        //						cell.setText(currentRow+","+currentCol);
                        //						cell.setText(coleArray[col]);
                        //						run2.setText(text);

                        if (currentRow == 1 && currentCol == 1) {
                            String text = "移走原因：";
                            run2.setText(text);
                            run2.setBold(true);
                            XWPFRun run3 = par.createRun();
                            run3.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
                            run3.setFontSize(9);//字体大小
                            run3.setText(reasonStr);
                        } else if (currentRow == 4 && currentCol == 1) {
                            //							String text = "检查切片数量：";
                            run2.setBold(true);
                            cell.getParagraphArray(0).setAlignment(ParagraphAlignment.RIGHT);
                            run2.setText(coleArray[col]);
                        } else {
                            //							cell.setText(coleArray[col]);
                            if (currentRow == 3 && currentCol > 1) {
                                run2.setText(coleArray[col]);
                                run2.addBreak();//设置换行
                                XWPFRun run3 = par.createRun();
                                run3.setFontFamily("等线");//设置字体（如：微软雅黑,华文楷体,宋体）
                                run3.setFontSize(9);//字体大小
                                run3.setText("mg/day");
                            } else {
                                //								viscera_
                                String value = coleArray[col];
                                if (value.startsWith("viscera_")) {
                                    value = value.replaceAll("viscera_", "");
                                    run2.setText(value);
                                    //器官 靠左+黑体
                                    run2.setBold(true);
                                    cell.getParagraphArray(0).setAlignment(ParagraphAlignment.LEFT);
                                } else {
                                    run2.setText(value);
                                }
                            }
                        }


                    }
                }

                Map<String, Object> hisDataList = tableMapList.get(m);
                List<StatisticsHeadVo> manPlsList = (List<StatisticsHeadVo>) hisDataList.get("manPlsList");
                List<StatisticsHeadVo> womanPlsList = (List<StatisticsHeadVo>) hisDataList.get("womanPlsList");
                //根据firstStr 查询雄性和雌性个数
                int manSize = 0;
                if (CollectionUtils.isNotEmpty(manPlsList)) {
                    // 6.合并单元格方式二(推荐)
                    // 6.1 水平合并第1行的第2列到第4列
                    //					mergeCellsByHorizontal(table, 0, 1, 4);
                    mergeCellsByHorizontal(table, 0, 1, manPlsList.size());
                    manSize = manPlsList.size();
                }
                if (CollectionUtils.isNotEmpty(womanPlsList)) {
                    // 6.合并单元格方式二(推荐)
                    // 6.1 水平合并第1行的第2列到第4列
                    //					mergeCellsByHorizontal(table, 0, 5, 8);
                    mergeCellsByHorizontal(table, 0, manSize + 1, manSize + womanPlsList.size());
                }
                // 6.2 垂直合并第1列的第3行到第4行
                mergeCellsByVertically(table, 0, 0, 2);

                //两个表分页隔开
                document.createParagraph().setPageBreak(true);
                ;
            }

			/*XWPFTable table2 = document.createTable(row_hang, cell_lie);

			// 3.设置表格样式
			table2.getCTTbl().addNewTblPr().addNewTblStyle().setVal("Table Grid");
			table2.setWidth("100%"); // 设置表格宽度为100%

			// 4.填充表格内容
			XWPFTableCell cell2 = null;
			for (int row = 0; row < row_hang; row++) { // 循环遍历表格的行
				for (int col = 0; col < cell_lie; col++) { // 循环遍历表格的列
					// 获取当前单元格
					cell2 = table2.getRow(row).getCell(col);
					// 设置单元格内容垂直居中
					cell2.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
					// 设置单元格内容水平居中(获取单元格中的第一个段落对象)
					cell2.getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
					// 设置单元格内容
					cell2.setText("行 " + (row + 1) + ", 列 " + (col + 1));
				}
			}

			// 6.合并单元格方式二(推荐)
			// 6.1 水平合并第1行的第2列到第4列
//			mergeCellsByHorizontal(table2, 0, 1, 3);
//			mergeCellsByHorizontal(table2, 0, 4, 6);
			mergeCellsByHorizontal(table2, 0, 1, 4);
			mergeCellsByHorizontal(table2, 0, 5, 8);
			// 6.2 垂直合并第1列的第3行到第4行
			mergeCellsByVertically(table2, 0, 0, 2);*/


            // create header-footer
            XWPFHeaderFooterPolicy headerFooterPolicy = document.getHeaderFooterPolicy();
            if (headerFooterPolicy == null) {
                headerFooterPolicy = document.createHeaderFooterPolicy();
            }

            // create header start
            XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);

            paragraph = header.getParagraphArray(0);
            if (paragraph == null) {
                paragraph = header.createParagraph();
            }
            paragraph.setAlignment(ParagraphAlignment.RIGHT);
            run = paragraph.createRun();
            run.setText("页码: ");
            // 粗体
            run.setBold(true);
            //设置字体（如：微软雅黑,华文楷体,宋体）
            run.setFontFamily("等线");
            //字体大小
            run.setFontSize(9);
            paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
            // 粗体
            run.setBold(true);
            //设置字体（如：微软雅黑,华文楷体,宋体）
            run.setFontFamily("等线");
            // 字体大小
            run.setFontSize(9);
            run = paragraph.createRun();


            // 保存文档
            // 输出流，用于将文档写入磁盘
            // fos = new FileOutputStream("C:\\Users\\86153\\Desktop\\nacas-本地\\test_"+System.currentTimeMillis()+"_1.docx");
            fos = new FileOutputStream(rptPath);
            // 将文档写入输出流
            document.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    // 关闭输出流
                    fos.close();
                    if (document != null) {
                        // 关闭Word文档
                        document.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("Word文档生成成功！");
        return "";
    }

    // ==================== private method ====================

    /**
     * <h5>描述:水平方向合并单元格<h5>
     *
     * @param table          表格
     * @param rowIndex       合并列所在的行下标(从0开始)
     * @param startCellIndex 开始合并的列下标(从0开始)
     * @param endCellIndex   结束合并的列下标(从0开始)
     */
    private static void mergeCellsByHorizontal(XWPFTable table, int rowIndex, int startCellIndex, int endCellIndex) {
        String str = "";
        for (int i = startCellIndex; i <= endCellIndex; i++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(i);
            str = "table.getRow(" + rowIndex + ").getCell(" + i + ")";
            //			System.out.println("str:"+str);
            if (i == startCellIndex) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
                //				 System.out.println(str + ".getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);");
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                //				 System.out.println(str + ".getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);");
            }
        }
    }

    /**
     * <h5>描述:垂直方向合并单元格<h5>
     *
     * @param table       表格
     * @param columnIndex 合并行所在列下标(从0开始)
     * @param startCell   开始合并的行下标(从0开始)
     * @param endCell     结束合并的行下标(从0开始)
     */
    private static void mergeCellsByVertically(XWPFTable table, int columnIndex, int startRowIndex, int endRowIndex) {
        String str = "";
        for (int i = startRowIndex; i <= endRowIndex; i++) {
            XWPFTableCell cell = table.getRow(i).getCell(columnIndex);
            str = "table.getRow(" + i + ").getCell(" + columnIndex + ")";
            if (i == startRowIndex) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                // System.out.println(str + ".getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);");
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
                // System.out.println(str + ".getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);");
            }
        }
    }


    /**
     * 加载表格模板
     *
     * @return
     * @throws Exception
     */
    public static XWPFTable getTPLTable() {
        //病理病变
        byte[] bytes = TPL_CACHE.get("pathologicalChanges");
        XWPFTable table = null;
        XWPFDocument doc = null;
        if (bytes == null) {
            try {
                String path = ResourceUtils.getURL("classpath:template/pathologicalChanges.docx").getPath();
                ClassPathResource classPathResource = new ClassPathResource("template/pathologicalChanges.docx");
                InputStream inputStream = classPathResource.getInputStream();
                bytes = IOUtils.toByteArray(inputStream);
                //bytes = FileUtils.readFileToByteArray(new File(path));
                TPL_CACHE.put("pathologicalChanges", bytes);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        //InputStream is = new FileInputStream(path);
        InputStream is = new ByteArrayInputStream(bytes);
        try {
            doc = new XWPFDocument(is);
            table = doc.getTables().get(0);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return table;
    }


    private static void setBorder(CTBorder boder, String colour) {
        boder.setVal(STBorder.SINGLE);
        boder.setColor(colour);
        boder.setSz(BigInteger.valueOf(8));
        boder.setSpace(new BigInteger("1"));
        boder.setThemeColor(STThemeColor.NONE);
    }


    private static void setBorder2(CTBorder boder, String colour) {
        boder.setVal(STBorder.SINGLE);
        boder.setColor(colour);
        boder.setSz(BigInteger.valueOf(8));
        boder.setSpace(new BigInteger("1"));
        boder.setThemeColor(STThemeColor.NONE);
    }
}