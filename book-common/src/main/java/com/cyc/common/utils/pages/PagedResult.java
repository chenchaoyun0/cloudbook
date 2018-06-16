package com.cyc.common.utils.pages;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;

import lombok.Data;
@Data
public class PagedResult<T> extends BaseEntity {

    /* serialVersionUID */
    private static final long serialVersionUID = 1L;
    private int pageNo;// 当前页

    private int pageSize;// 一页条数

    private long total;// 总条数

    private long pages;// 总页数
    private int pageOffset;
    private List<T> dataList;// 数据
    private String url;
    private String strWhere;
    
    @Override
    public int getNumberOfPages() {
      // TODO Auto-generated method stub
       return 0;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
      // TODO Auto-generated method stub
       return null;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
      // TODO Auto-generated method stub
       return null;
    }

}
