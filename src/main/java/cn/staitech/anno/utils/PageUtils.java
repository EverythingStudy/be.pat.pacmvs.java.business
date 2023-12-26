package cn.staitech.anno.utils;


import java.util.List;

public class PageUtils<T> {
    //页码
    private int pageNum;
    //页面大小
    private int pageSize;
    //总记录条数
    private long total;
    //总页数
    private int pages;
    //每页的开始
    private int start;
    //当前页面记录集
    private List<T> results;

    public void doPage(List<T> lists){
        int count = lists.size();
        setTotal(count);
        setPages(count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        setStart(getPageNum() * getPageSize());
        setResults(lists.subList(getStart(),
                (count - getStart()) > getPageSize() ? getStart() + getPageSize() : count));
    }
    public PageUtils(){
        super();
    }
    public PageUtils(int pageNum, int pageSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
