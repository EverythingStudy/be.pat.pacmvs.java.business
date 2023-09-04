package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.domain.po.ProjectPo;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAddVO;
import cn.staitech.anno.exception.ReportException;
import cn.staitech.anno.mapper.ProjectExtMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.mapper.SpecialMapper;
import cn.staitech.anno.mapper.SysDictDataMapper;
import cn.staitech.anno.service.DiagnosticStatisticsService;
import cn.staitech.anno.service.ReportService;
import cn.staitech.anno.utils.PoiUtils;
import cn.staitech.anno.utils.date.DateUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mugw
 * @version 1.0
 * @description 报告服务
 * @date 2023/7/7 14:18:36
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

	//报告模板缓存
	public static Map<String,byte[]> TPL_CACHE = new HashMap<>();

	public static ExecutorService executorService =  Executors.newFixedThreadPool(10);

	@Value("${rpt.dir:../REPORT}")
	private String RPT_DIR;

	private final static String SUFFIX = ".docx";

	@Resource
	private SlideMapper slideMapper;
	@Resource
	private ProjectExtMapper projectExtMapper;
	@Resource
	private SpecialMapper specialMapper;
	@Resource
	private SysDictDataMapper sysDictDataMapper;
	
	@Resource
	private DiagnosticStatisticsService diagnosticStatisticsService;

	@Override
	public String createRpt(ReportRecordAddVO recordAddVO) throws Exception {
		long start = System.currentTimeMillis();
		String path = "";
		log.info("开始生成word报告，报告参数{}：",recordAddVO);
		Map params = new HashMap();
		Long userId = SecurityUtils.getUserId();
		params.put("currentUserId",userId);
		params.put("specialId",recordAddVO.getSpecialId());
		params.put("reasons",recordAddVO.getReasons());
		//报告类型（1单切片报告，2组间报告,3脏器病变报告
		Integer rptType = recordAddVO.getReportType();
		switch (rptType){
		case 2:
			path = diagnosticStatisticsService.createRpt(recordAddVO);
			break;
		case 3:
			path = createVisceraLesionRpt(params);
			break;
		}
		log.info("word报告创建完成，耗时{}：",System.currentTimeMillis()-start);
		return path;
//		return R.ok(path);

	}

	/**
	 * 脏器病变报告
	 * 附录1脏器病变表 - 给药期结束安乐死
	 * 附录2脏器病变表 - 恢复期结束安乐死
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String createVisceraLesionRpt(Map params) throws Exception {
		ZipSecureFile.setMinInflateRatio(0.001);
		Long specialId = MapUtils.getLong(params,"specialId");
		if (specialId==null){
			throw new ReportException("专题id为空！");
		}
		//查询专题
		Special special = specialMapper.selectById(specialId);
		//查询专题下项目
		QueryWrapper queryWrapper = Wrappers.query();
		queryWrapper.eq("special_id",specialId);
		queryWrapper.eq("del_flag",0);
		List<ProjectPo> projectPos = projectExtMapper.selectList(queryWrapper);
		//创建专题报告文件夹
		String basePath = RPT_DIR+File.separator+specialId+File.separator+MapUtils.getLong(params,"reasons");
		File rptDir = new File(basePath);
		if (!rptDir.exists()){
			rptDir.mkdirs();
		}
		//按专题下项目生成word报告
		String rptPath = basePath+File.separator+special.getSpecialNumber()+"_"+DateUtils.getDateToString(new Date(),"yyyy-MM-dd_hh_mm_ss")+SUFFIX;
		if (projectPos!=null&&!projectPos.isEmpty()){
			for (int i=0;i<projectPos.size();i++){
				ProjectPo p = projectPos.get(i);
				params.put("projectId",p.getProjectId());
				makeProjectRPT(params,basePath,special,p,0,i);
				makeProjectRPT(params,basePath,special,p,1,i);
			}
			// 文档生成方法
			XWPFDocument doc = new XWPFDocument();
			rebuildRPT(doc,params,basePath,special,projectPos,0);
			rebuildRPT(doc,params,basePath,special,projectPos,1);
			FileOutputStream out = null;
			try {
				File rpt = new File(rptPath);
				if (rpt.exists()){
					rpt.delete();
				}
				out = new FileOutputStream(new File(rptPath));
				doc.write(out);
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
		return rptPath;
	}

	/**
	 * 加载报表数据
	 * @param headerTable
	 * @param dataList
	 */
	public void loadData(XWPFTable headerTable,List<Map<String,Object>> dataList,List<Map<String,Integer>> mergeCellsParams)throws Exception{
		//分组合并标识
		boolean flag = false;
		String group = "";
		int groupRow = 0;

		XWPFTableRow row = null;
		for (int j=0;j<dataList.size();j++){
			Map<String,Object> data = dataList.get(j);
			log.debug("加载报表数据：{}",data);
			if ("".equals(group)){
				row = getRowTemplate(2);
				group = MapUtils.getString(data,"group");
				groupRow++;
			}else if((j+1)==dataList.size()){
				//上一行数据
				Map<String,Object> temp = dataList.get(j-1);
				row = getRowTemplate(3);
				if (MapUtils.getString(data,"group").equals(MapUtils.getString(temp,"group"))){
					flag = true;
					groupRow++;
				}
			}else if(group.equals(MapUtils.getString(data,"group"))){
				groupRow++;
				Map<String,Object> temp = dataList.get(j+1);
				//判断下一行是否同组
				if (!group.equals(MapUtils.getString(temp,"group"))){
					row = getRowTemplate(3);
					flag = true;
				}else{
					row = getRowTemplate(2);
				}
			}
			List<XWPFTableCell> cells = row.getTableCells();
			for (int i=0;i<cells.size();i++){
				XWPFTableCell cell = cells.get(i);
				//设置cell垂直居中
				cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
				switch (i){
				case 0:
					PoiUtils.updateCell(cell,MapUtils.getString(data,"group"),true);
					break;
				case 1:
					//PoiUtils.updateCell(cell,MapUtils.getString(data,"groupDesc"));
					PoiUtils.updateCell(cell,MapUtils.getString(data,"dosage"));
					break;
				case 2:
					PoiUtils.updateCell(cell,MapUtils.getString(data,"imageName"));
					break;
				case 3:
					PoiUtils.updateCell(cell,MapUtils.getString(data,"subImageName"));
					break;
				case 4:
					PoiUtils.updateCell(cell,MapUtils.getString(data,"diagnosis"));
					break;
				case 5:
					String remark = MapUtils.getString(data,"remark","");
					PoiUtils.updateCell(cell,MapUtils.getString(data,"remark","-"));
					break;
				}
			}
			headerTable.addRow(row);
			//构建合并单元格参数
			if (flag){
				Map<String,Integer> mergeCellsParam = new HashMap<>();
				mergeCellsParam.put("startRow",j-(groupRow-1)+3);
				mergeCellsParam.put("endRow",j+3);
				mergeCellsParams.add(mergeCellsParam);
				//重置组标识
				group = "";
				groupRow = 0;
				flag = false;
			}
		}
	}

	/**
	 *
	 * @param params
	 * @param basePath
	 * @param special
	 * @param prj
	 * @param gender
	 * @param index 项目序号
	 * @throws Exception
	 */
	private void makeProjectRPT(Map params,String basePath,Special special,ProjectPo prj,Integer gender,int index)throws Exception{
		params.put("gender",gender);
		long start = System.currentTimeMillis();
		List<Map<String,Object>> list = slideMapper.queryVisceraLesionRpt(params);
		long end = System.currentTimeMillis();
		log.info("/*********************开始数据查询,耗时：{}：{}*******************/",end-start,prj);
		if (list==null||list.isEmpty()){
			return;
		}
		String filePath = basePath+File.separator+prj.getProjectId()+gender+index+SUFFIX;
		File tempFile = new File(filePath);
		if (tempFile.exists()){
			tempFile.delete();
		}
		List<Map<String,Integer>> mergeCellsParams = new ArrayList<>();
		// 文档生成方法
		XWPFDocument doc = new XWPFDocument();
		XWPFParagraph paragraph = doc.createParagraph();
		//调整横向
		PoiUtils.changeOrientation(doc,paragraph, "landscape");
		//构建报告标题
		//createTitle(paragraph,getTitleTemplate(special,params));
		//第一个项目时，构建报告表头
		doc.createTable();
		//查询种属、试验类型、性别、脏器
		XWPFTable headerTable = getHeaderTable(ImmutableMap.of("species",special.getSpecies(),
				"trialType",special.getTrialType(),
				"viscera",getDict("sys_viscera_organization",String.valueOf(prj.getViscusCode())).getDictLabel(),
				"gender",gender==0?"雌性":"雄性"));
		//渲染数据
		loadData(headerTable,list,mergeCellsParams);
		doc.setTable(0, headerTable);
		byte[] bytes = PoiUtils.doc2Byte(doc);
		doc = PoiUtils.loadTPL(bytes);
		XWPFTable table = doc.getTables().get(0);
		//执行单元格合并
		List<Map<String,Integer>> imageCellsParams = PoiUtils.computeMergeCell(list,"imageName");
		List<Map<String,Integer>> subImageCellsParams = PoiUtils.computeMergeCell(list,"subImageName");
		Map<Integer,List<Map<String,Integer>>> mergeCellMap = new HashMap<>();
		mergeCellMap.put(0,mergeCellsParams);
		mergeCellMap.put(1,mergeCellsParams);
		mergeCellMap.put(2,imageCellsParams);
		mergeCellMap.put(3,subImageCellsParams);
		mergeCells(table,mergeCellMap);
		String docKey = basePath+File.separator+prj.getProjectId()+gender+SUFFIX;
		params.put(docKey,table);
	}

	/**
	 * 重新构建报告
	 * @param doc
	 * @param params
	 * @param basePath
	 * @param special
	 * @param projectPos
	 * @param gender
	 * @throws Exception
	 */
	public void rebuildRPT(XWPFDocument doc,Map params,String basePath,Special special,List<ProjectPo> projectPos,Integer gender)throws Exception{
		XWPFParagraph paragraph = doc.createParagraph();
		//调整横向
		PoiUtils.changeOrientation(doc,paragraph, "landscape");
		boolean isFirst = true;
		int prjCount = projectPos.size();
		for (int i=0;i<prjCount;i++){
			ProjectPo p = projectPos.get(i);
			params.put("projectId",p.getProjectId());
			String docKey = basePath+File.separator+p.getProjectId()+gender+SUFFIX;
			XWPFTable table = (XWPFTable) params.get(docKey);
			if (table==null){
				continue;
			}
			//首页添加标题
			if (isFirst){
				isFirst = false;
				//构建报告标题
				createTitle(paragraph,special,params);
			}
			doc.createTable();
			int index = doc.getTables().size();
			doc.setTable(index-1,table);
			doc.createParagraph().setPageBreak(true);
		}
	}

	/**
	 * 根据字典类型和字典值查询字典
	 * @param dictType
	 * @param dictValue
	 * @return
	 */
	public SysDictData getDict(String dictType,String dictValue){
		List<SysDictData> sysDictDataList = sysDictDataMapper.getSysDictDataListByParm(ImmutableMap.of("dictType",dictType,"dictValue",dictValue));
		if(sysDictDataList==null){
			return new SysDictData();
		}else{
			return sysDictDataList.get(0);
		}
	}

	/**
	 * 根据参数集合，合并单元格
	 * @param table
	 * @param
	 * @throws Exception
	 */
	public static void mergeCells(XWPFTable table,Map<Integer,List<Map<String,Integer>>> paramMap)throws Exception{
		paramMap.keySet().forEach(key->{
			List<Map<String,Integer>> mergeCellsParams = paramMap.get(key);
			for (Map<String, Integer> param : mergeCellsParams) {
				PoiUtils.mergeCellsVertically(table, key, MapUtils.getInteger(param,"startRow"), MapUtils.getInteger(param,"endRow"));
			}
		});
	}

	/********************************************模板加载********************************************************/

	/**
	 * 创建报告标题
	 * @param paragraph
	 * @param special
	 * @param params
	 */
	private void createTitle(XWPFParagraph paragraph,Special special,Map params){
		List<String> titles = new ArrayList<>();
		titles.add("生仝智能科技（北京）有限公司");
		titles.add("专题编号："+special.getSpecialNumber());
		titles.add(special.getSpecialName());
		titles.add("附录1 脏器病变表 - " + (MapUtils.getInteger(params,"reasons")==1?"给药期结束安乐死":"恢复期结束安乐死"));
		XWPFRun run = paragraph.createRun();
		int size = titles.size();
		for (int i=0;i<size;i++){
			run = paragraph.createRun();
			run.setBold(true);
			run.setFontSize(9);
			run.setText(titles.get(i));
			run.setFontFamily("等线");
			if (size!=(i+1)){
				run.addCarriageReturn();
			}
		}
	}

	/**
	 * 加载表格模板
	 * @return
	 * @throws Exception
	 *
	 */
	public XWPFTable getTPLTable(){
		byte[] bytes = TPL_CACHE.get("visceraLesionTPL");
		XWPFTable table = null;
		XWPFDocument doc = null;
		if (bytes == null){
			try {
				String path = ResourceUtils.getURL("classpath:template/visceraLesionTPL.docx").getPath();
                ClassPathResource classPathResource = new ClassPathResource("template/visceraLesionTPL.docx");
                InputStream inputStream =classPathResource.getInputStream();
                bytes = IOUtils.toByteArray(inputStream);
                //bytes = FileUtils.readFileToByteArray(new File(path));
				TPL_CACHE.put("visceraLesionTPL",bytes);
			}catch (FileNotFoundException e) {
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
		}finally {
			IOUtils.closeQuietly(is);
		}
		return table;
	}

	/**
	 * 构建报表头
	 * @return
	 * @throws Exception
	 */
	public XWPFTable getHeaderTable(Map<String, Object> params) throws Exception {
		XWPFTable table = getTPLTable();
		PoiUtils.convertTable(table,params);
		table.removeRow(5);
		table.removeRow(4);
		table.removeRow(3);
		return table;
	}

	/**
	 * 构建row
	 * @param pos 组内行号（编号不同取不同样式行）
	 * @return
	 * @throws Exception
	 */
	public XWPFTableRow getRowTemplate(int pos) throws Exception {
		XWPFTable table = getTPLTable();
		XWPFTableRow row = table.getRow(2+pos);
		return row;
	}


}
