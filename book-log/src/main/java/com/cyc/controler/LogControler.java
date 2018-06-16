package com.cyc.controler;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.base.BookResponse;
import com.cyc.common.base.ErrorCode;
import com.cyc.common.po.Book;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.file.NfsFileUtils;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeVo;
import com.cyc.common.vo.TodayCountVo;
import com.cyc.context.FaceAppContextUtils;
import com.cyc.service.ILogService;

@RestController
public class LogControler {

  private static final Logger log = LoggerFactory.getLogger(LogControler.class);
  @Autowired
  private ILogService logService;

  @Value("${server.port}")
  String port;

  @RequestMapping("/info")
  public String home() {
    return "测试 book-log" + ",port:" + port;
  }

  @RequestMapping("/indexHome")
  public IndexHomeVo indexHome(Integer pageNo, Integer pageSize) {
    IndexHomeVo indexHomeVo = new IndexHomeVo();
    try {
      HttpServletRequest request = FaceAppContextUtils.getCurrentRequest();
      TLog tLog = new TLog();
      PagedResult<TLog> pages = logService.selectLogPages(tLog, pageNo, pageSize);
      Long totalcount = logService.selectLogSumCount();
      TodayCountVo todayCount = logService.todayCount();
      String url = request.getRequestURI();
      pages.setUrl(url);
      indexHomeVo.setPages(pages);
      indexHomeVo.setTodayCount(todayCount);
      indexHomeVo.setTotalcount(totalcount);
      String resp = JSONObject.toJSONString(indexHomeVo);
      log.info(">>>>>>>>>indexHome resp:{}", resp);
    } catch (Exception e) {
      log.error("异常:{}", e);
      indexHomeVo.setErrorMsg(e.getMessage());
      indexHomeVo.setErrorCode(BookResponse.ERROR_CODE);
    }
    return indexHomeVo;
  }

  @RequestMapping("/")
  public String indexResume(Model model, HttpServletRequest request, ModelAndView modelAndView) {
    return "forward:/indexHome";
  }

  @RequestMapping("/resume")
  public String resume() {
    return "job/m2/resume";
  }

  @RequestMapping(value = "/indexHomeForIp", method = RequestMethod.GET)
  public String indexHomeForIp(String userIp, Model model, HttpServletRequest request, ModelAndView modelAndView,
    Integer pageNo, Integer pageSize) {
    // return "forward:/book/selectBookPages";
    PagedResult<TLog> pages = logService.selectLogPagesForIp(userIp, pageNo, pageSize);
    Long totalcount = logService.selectLogSumCount();
    String url = request.getRequestURI();
    pages.setStrWhere("userIp=" + userIp);
    pages.setUrl(url);
    model.addAttribute("pages", pages);
    model.addAttribute("totalcount", totalcount);
    return "ipLog";
  }

  @RequestMapping("/downloadResumeDocx")
  public String downloadResumeDocx(Model model, HttpServletResponse response, HttpServletRequest request)
    throws Exception {

    String fileName = "北京-Java开发工程师-陈超允.docx";
    fileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
    response.reset();// 去除空白行
    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 指定下载的文件名
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    InputStream bis = null;
    try {
      String nfsFileName = NfsFileUtils.getNfsUrl() + "resume/docx/chenchaoyun-resume.docx";
      log.info("nfsFileName:{}", nfsFileName);
      bis = NfsFileUtils.readNfsFile2Stream(nfsFileName);
      ServletOutputStream outputStream = response.getOutputStream();
      IOUtils.copy(bis, outputStream);
    } catch (Exception e) {
      log.error("要下载的文件不存在", e);
      request.setAttribute("msg", "要下载的文件不存在" + e.getMessage());
      return "forward:/error/msg.jsp";
    } finally {
    }
    return null;
  }

  @RequestMapping("/testImg")
  public String testImg(HttpServletRequest request, Model model) {
    String nfsFileName = NfsFileUtils.getNfsUrl() + "ad.jpg";
    String imageBase64Str = NfsFileUtils.getImageBase64Str(nfsFileName);
    Book book = new Book();
    book.setBookAuthor("chenchaoyun");
    book.setBookImg(imageBase64Str);
    model.addAttribute("book", book);
    return "testImg";
  }
}
