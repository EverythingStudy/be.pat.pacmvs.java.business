package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FilesListVO;
import cn.staitech.anno.mapper.FilesMapper;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author wangf
 * @description 针对表【tb_files】的数据库操作Service实现
 * @createDate 2023-09-10 17:04:40
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
        implements FilesService {
    @Override
    public PageMaster<Files> selectList(FilesListVO vo) throws ExecutionException, InterruptedException {
        // 分页
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize()).setReasonable(true);

        Files files = new Files();
        BeanUtils.copyProperties(vo, files);
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>(files);

        // 异步查询图像列表
        CompletableFuture<List<Files>> listFuture = CompletableFuture.supplyAsync(() -> this.baseMapper.selectList(queryWrapper));
        List<Files> list = listFuture.get();

        PageMaster pageMaster = new PageMaster<>(list);
        pageMaster.setList(list);
        return pageMaster;
    }
}




