package cn.staitech.anno.controller;

import cn.staitech.anno.domain.UserManual;
import cn.staitech.anno.service.UserManualService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.redis.service.RedisService;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: UserManualController
 * @Description:用户手册
 * @date 2024年1月4日
 */
@Api(value = "用户手册", tags = "用户手册")
@RestController
@RequestMapping("/userManual")
public class UserManualController {


    @Resource
    private UserManualService uerManualService;

    @Resource
    private RedisService redisService;


    /**
     * 用户手册 .
     */
    @ApiOperation(value = "获取用户手册", notes = "获取用户手册")
    @GetMapping("/getUserManual")
    public R<UserManual> getUserManual() {
        //获取语言
        String language = "";
        if (SecurityUtils.getLoginUser().getLanguage() != null) {
            language = SecurityUtils.getLoginUser().getLanguage();
        }
        int messagesType = 1;
        if ("en-us".equals(language)) {
            messagesType = 2;
        }

        //获取机构id
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        String cacheKey = "userManual_" + organizationId + "_" + messagesType;
        UserManual userManual = redisService.getCacheObject(cacheKey);
        if (null == userManual) {
            QueryWrapper<UserManual> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("organization_id", organizationId);
            queryWrapper.eq("messages_type", messagesType);
            queryWrapper.eq("del_flag", 0);
            userManual = uerManualService.getOne(queryWrapper);
            redisService.setCacheObject(cacheKey, userManual, 24 * 31L, TimeUnit.HOURS);
        }

        return R.ok(userManual);
    }
    
    @GetMapping("/getServerInfo")
    public R getServerInfo() {
    	File[] files = File.listRoots();
    	for (File file : files) {
    		String total = new DecimalFormat("#.#").format(file.getTotalSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
    		String free = new DecimalFormat("#.#").format(file.getFreeSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
    		String un = new DecimalFormat("#.#").format(file.getUsableSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
    		String path = file.getPath();
    		System.out.println(path + "总:" + total + ",可用空间:" + un + ",空闲空间:" + free);
    		System.out.println("=============================================");
    	}

    	String pathAll = "";
    	String allTotal = execBash(pathAll);
    	System.out.println("allTotal："+allTotal);
    	
    	String onlyPath = "/home/data_nfs";
    	String onlyPathTotal = execBash(onlyPath);
    	System.out.println("onlyPathTotal："+onlyPathTotal);
    	return R.ok();
    }
    
    private String execBash(String path){
    	String result = "";
    	String cmd = "df -h ";
    	if(StringUtils.isNotEmpty(path)){
    		cmd = cmd +path;
    	}
    	StringBuffer sb = new StringBuffer();
    	try {
    		Process process;
    		process = Runtime.getRuntime().exec(cmd);

    		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    		String line;
    		while ((line = br.readLine()) != null) {
    			sb.append(line).append("\n");
    		}
    		 result = sb.toString();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return result;
    }

}
