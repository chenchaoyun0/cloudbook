package com.cyc.bookweb.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cyc.common.po.Book;
import com.cyc.common.po.TImg;
import com.cyc.common.vo.SelectBookDetailReq;
import com.cyc.common.vo.SelectBookDetailResp;
import com.cyc.common.vo.SelectBookPagesReq;
import com.cyc.common.vo.SelectBookPagesResp;
import com.cyc.common.vo.UploadBookSubmitReq;
import com.cyc.common.vo.UploadBookSubmitResp;

@FeignClient(value = "book-book")
public interface IBookBookClient {
  @RequestMapping(value = "/uploadBookSubmit", method = RequestMethod.POST)
  public UploadBookSubmitResp uploadBookSubmit(@RequestParam("req") UploadBookSubmitReq req,
    @RequestParam("bookFile") MultipartFile[] bookFile);
  
  @RequestMapping(value = "/selectBookPages", method = {RequestMethod.POST})
  public SelectBookPagesResp selectBookPages(@RequestBody SelectBookPagesReq req);
  
  @RequestMapping(value = "/selectBookDetail", method = {RequestMethod.POST})
  public SelectBookDetailResp selectBookDetail(@RequestBody SelectBookDetailReq req);

  @RequestMapping(value = "selectByPrimaryKey/{bookId}", method = {RequestMethod.GET})
  public Book selectByPrimaryKey(@PathVariable("bookId") String bookId);

  @RequestMapping(value = "selectList", method = {RequestMethod.GET})
  public List<TImg> selectList(@RequestBody TImg tImg);

  @RequestMapping(value = "updateByPrimaryKeySelective", method = {RequestMethod.POST})
  public void updateByPrimaryKeySelective(@RequestBody Book book);

  @RequestMapping(value = "unmountBook/{bookId}", method = {RequestMethod.POST})
  public @ResponseBody int unmountBook(@PathVariable("bookId") String bookId);

  @RequestMapping(value = "mountBook/{bookId}", method = {RequestMethod.POST})
  public @ResponseBody int mountBook(@PathVariable("bookId") String bookId);
}
