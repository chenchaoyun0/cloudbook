package com.cyc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.po.Book;
import com.cyc.common.po.TImg;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.pages.BeanUtil;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.mapper.BookMapper;
import com.cyc.service.IBookService;
import com.cyc.service.IImgService;
import com.github.pagehelper.PageHelper;

@Service("bookService")
public class BookServiceImpl implements IBookService {
  private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private IImgService imgService;

    public int insertSelective(Book book) {

        try {
            return bookMapper.insertSelective(book);
        } catch (UserException e) {
            throw new UserException("操作失败");
        }
    }

    public Book selectByPrimaryKey(String bookId) {
        Book book = bookMapper.selectByPrimaryKey(bookId);
        return book;
    }

    public PagedResult<Book> selectBookPages(Book book, Integer pageNo, Integer pageSize) {
        /**
         * 默认是12条
         */
        pageNo = pageNo == null ? (Integer) 1 : pageNo;
        pageSize = pageSize == null ? (Integer) 8 : pageSize;
        PageHelper.startPage(pageNo, pageSize);// 告诉插件开始分页

        List<Book> bookList = bookMapper.selectBookPages(book);
        PagedResult<Book> bookPagedResult = BeanUtil.toPagedResult(bookList);
        log.info("bookList:{}", bookList.toString());
        List<Book> arrayList = new ArrayList<Book>();
        List<Book> dataList = bookPagedResult.getDataList();
        for (Book book2 : dataList) {
            TImg tImg = new TImg();
            tImg.setLinkId(book2.getBookId());
            log.info("查询图书图片begin...");
            List<TImg> imgList = imgService.selectList(tImg);
            log.info("查询图书图片end...imgList:{}", JSONObject.toJSON(imgList));
            TImg tImg2 = imgList.get(0);
            String imgPath = tImg2.getImgPath();
            book2.setBookImg(imgPath);
            arrayList.add(book2);
        }
        bookPagedResult.setDataList(arrayList);
        bookPagedResult.setPageNo(pageNo);
        bookPagedResult.setPageSize(pageSize);

        return bookPagedResult;
    }

    public int updateByPrimaryKeySelective(Book book) {
        return bookMapper.updateByPrimaryKeySelective(book);
    }

    public int unmountBook(String bookId) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookStatus(-1);
        return bookMapper.updateByPrimaryKeySelective(book);
    }

    public int mountBook(String bookId) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookStatus(1);
        return bookMapper.updateByPrimaryKeySelective(book);
    }

    public int countByBookId(String bookId) {
        return bookMapper.countByBookId(bookId);
    }

}
