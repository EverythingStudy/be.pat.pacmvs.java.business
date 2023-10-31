package cn.staitech.anno.utils;

import cn.staitech.anno.domain.special.SpecialResVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageSerializable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 分页类
 *
 * @param <T>
 */
@Api(value = "分页类", tags = "分页类")
public class SpecialPageMaster<T> extends PageSerializable<T> {

    public static final SpecialPageMaster EMPTY = new SpecialPageMaster(Collections.emptyList(), 0, 0l, new SpecialResVo());

    @ApiModelProperty(value = "页码")
    private int pageNum;

    @ApiModelProperty(value = "每页记录数")
    private int pageSize;

    @ApiModelProperty(value = "总页数")
    private int pages;

    @ApiModelProperty(value = "交付状态")
    private Long deliveryStatus;

    @ApiModelProperty(value = "交付状态")
    private SpecialResVo specialResVo;


    public SpecialPageMaster(List<T> list, Long deliveryStatus, SpecialResVo specialResVo) {
        this(list, 8, deliveryStatus, specialResVo);
    }

    public SpecialPageMaster(List<T> list, int navigatePages, Long deliveryStatus, SpecialResVo specialResVo) {
        super(list);
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.pages = page.getPages();

        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();
            this.pages = this.pageSize > 0 ? 1 : 0;
        }
        this.deliveryStatus = deliveryStatus;
        this.specialResVo = specialResVo;
    }

    public static <T> SpecialPageMaster<T> of(List<T> list, Long deliveryStatus, SpecialResVo specialResVo) {
        return new SpecialPageMaster(list, deliveryStatus, specialResVo);
    }

    public static <T> SpecialPageMaster<T> of(List<T> list, int navigatePages, Long deliveryStatus, SpecialResVo specialResVo) {
        return new SpecialPageMaster(list, navigatePages, deliveryStatus, specialResVo);
    }

    public static <T> SpecialPageMaster<T> delivery(List<T> list, Long deliveryStatus, SpecialResVo specialResVo) {
        return new SpecialPageMaster(list, deliveryStatus, specialResVo);
    }

    public static <T> SpecialPageMaster<T> emptyPageMaster() {
        return EMPTY;
    }


    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public int getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }


    public Long getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public void setDeliveryStatus(Long deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }


    public SpecialResVo getSpecialResVo() {
        return specialResVo;
    }

    public void setSpecialResVo(SpecialResVo specialResVo) {
        this.specialResVo = specialResVo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PageMaster{");
        sb.append("pageNum=").append(this.pageNum);
        sb.append(", pageSize=").append(this.pageSize);
        sb.append(", total=").append(this.total);
        sb.append(", pages=").append(this.pages);
        sb.append(", rows=").append(this.list);

        return sb.toString();
    }

}
