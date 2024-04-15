package cn.staitech.anno.service;

import cn.staitech.anno.domain.BlurImage;
import cn.staitech.anno.vo.blurimage.in.ImageVagueQueryIn;
import cn.staitech.anno.vo.blurimage.out.ImageVagueListOutVO;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wmy
 * @since 2024-04-10
 */
public interface BlurImageService extends IService<BlurImage> {

    PageResponse<ImageVagueListOutVO> getImageVagueList(ImageVagueQueryIn req) throws ParseException;
}
