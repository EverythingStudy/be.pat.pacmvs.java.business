package cn.staitech.anno;

import cn.staitech.anno.constant.Container;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class ImageConstantTest {

    @Test
    public void put() {
        for (int i = 0; i < 3; i++) {
            System.out.println(i + ":" + Container.IMAGE_PROCESS_MAP.get(i));
        }

//      Container.IMAGE_PROCESS_MAP.put(1,"aaa");


    }


    public void page() {

//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//        Page page = new Page(pageNum,pageSize);
//        subImageService.page(page);
//        PageMaster<SubImage> pageMaster = PageMaster.of(page.getRecords());
//        pageMaster.setPageSize(pageSize);
//        pageMaster.setPageNum(pageNum);
//        return R.ok(pageMaster);
    }

    @Test
    public void longMax() {
        // Long.MAX_VALUE = 9223372036854775807
        System.out.println("Long.MAX_VALUE = " + Long.MAX_VALUE);
    }
}
