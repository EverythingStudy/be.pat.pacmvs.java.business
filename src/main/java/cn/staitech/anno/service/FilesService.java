package cn.staitech.anno.service;

import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FilesListVO;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.ExecutionException;


/**
 * @author wangf
 * @description 针对表【tb_files】的数据库操作Service
 * @createDate 2023-09-10 17:04:40
 */
public interface FilesService extends IService<Files> {

    PageMaster<Files> selectList(FilesListVO filesListVO) throws ExecutionException, InterruptedException;
}
