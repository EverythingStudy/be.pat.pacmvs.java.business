package cn.staitech.anno.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.json.JSONUtil;
import cn.staitech.anno.domain.AlgorithmModel;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.mapper.SlidePredictionMapper;
import cn.staitech.anno.service.AlgorithmModelService;
import cn.staitech.anno.service.AlgorithmPredictionService;
import cn.staitech.anno.service.SlidePredictionService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.predictionInfo.in.PreExecData;
import cn.staitech.anno.vo.predictionInfo.in.PredictionDataIn;
import cn.staitech.anno.vo.predictionInfo.in.PredictionInfo;
import cn.staitech.anno.vo.predictionInfo.in.SlidePredictionIn;
import cn.staitech.anno.vo.predictionInfo.in.SlidePredictionQuery;
import cn.staitech.anno.vo.predictionInfo.in.StartPredictionIn;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionInfo;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionOut;
import cn.staitech.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: AlgorithmPredictionServiceImpl
 * @Description:服务实现类
 * @author wanglibei
 * @date 2023年11月2日
 * @version V1.0
 */
@Service
@Slf4j
public class AlgorithmPredictionServiceImpl implements AlgorithmPredictionService {


	@Resource
	private AlgorithmModelService algorithmModelService;

	@Resource
	private SlidePredictionMapper slidePredictionMapper;
	
	@Resource
	private SlidePredictionService slidePredictionService;

	@Autowired
	private RestTemplate restTemplate ;

	@Value("${algorithmPredictionPath}")
	private String algorithmPredictionPath;

	@SuppressWarnings("rawtypes")
	@Override
	public R startPrediction(StartPredictionIn req, cn.staitech.anno.domain.Project project) {
		//请求算法类型 0：启动算法 1：重算失败数据
		int type = req.getType();
		//算法模型id
		Long modelId = project.getModelId();
		AlgorithmModel algorithmModel = algorithmModelService.getById(modelId);
		//算法需要的数据
		PredictionDataIn predictionData = new PredictionDataIn();
		predictionData.setProjectId(project.getProjectId());
		predictionData.setModelName(algorithmModel.getModelName());

		ImageCsvGetVO request = new ImageCsvGetVO();
		request.setProjectId(project.getProjectId());
		//AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败'
		if(type == 0){
			request.setAiAnalyzed(0);
		}else if(type == 1){
			request.setAiAnalyzed(3);
		}
		//碎片状态（默认为0校验通过，1校验不通过）
		request.setEyeMent("0");  
		//查询需要的数据
		List<ImageCsvListVO> list = slidePredictionMapper.getImageCsvListVOList(request);
		List<PreExecData> slideList = new ArrayList<PreExecData>();
		List<PredictionInfo> pInfoList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(list)){
			for(ImageCsvListVO vo:list){
				PreExecData ped = new PreExecData();
				BeanUtils.copyProperties(vo, ped);
				//根据slideId 查询SlidePrediction信息
				SlidePredictionQuery spQuery = new SlidePredictionQuery();
					spQuery.setSlideId(vo.getSlideId());
					spQuery.setEyeMent("0");
				List<SlidePredictionInfo> spList = slidePredictionMapper.getOriginalSlideList(spQuery);
				if(CollectionUtils.isNotEmpty(spList)){
					for(SlidePredictionInfo sInfo:spList){
						PredictionInfo pInfo = new PredictionInfo();
						BeanUtils.copyProperties(sInfo,pInfo);
						pInfoList.add(pInfo);
					}
				}
//				ped.setSlidePredictionList(spList);
				ped.setPredictionInfoList(pInfoList);
				slideList.add(ped);
			}
			predictionData.setSlideList(slideList);
			log.info("请求数据：{}",JSONUtil.toJsonStr(predictionData));
			//TODO 请求算法接口
			try{
				ResponseEntity<String> resp =  restTemplate.postForEntity(algorithmPredictionPath, predictionData, String.class);
				String body = resp.getBody();
				log.info("标注请求算法数据返回{},内容是{}",JSONUtil.toJsonStr(resp),body);
			}catch(Exception e){
				e.printStackTrace();
			}finally {

			}

		}
		return R.ok();
	}


	@Override
	public SlidePredictionOut getOriginalSlideList(SlidePredictionIn req) {
		SlidePredictionQuery query = new SlidePredictionQuery();
		BeanUtils.copyProperties(req, query);
		query.setEyeMent("0");
		List<SlidePredictionInfo>  list  =  slidePredictionMapper.getOriginalSlideList(query);
		SlidePredictionOut spo = new SlidePredictionOut();
		spo.setList(list);
		
		int alreadyMainImage = 0;
		//查询是否已经有主图了
		QueryWrapper<SlidePrediction> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("slide_id", req.getSlideId()).eq("del_flag", "0").eq("main_image", "1");
		List<SlidePrediction> spList = slidePredictionService.list(queryWrapper);
		if(CollectionUtils.isNotEmpty(spList)){
			alreadyMainImage = 1;
		}
		spo.setAlreadyMainImage(alreadyMainImage);
		return spo;
	}

}
