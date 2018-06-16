package com.cyc.bookweb.contrller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.bookweb.feignclient.IBookLogService;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeVo;
import com.cyc.common.vo.TodayCountVo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexHomeController {

  @Autowired
  IBookLogService bookLogService;

  @RequestMapping(value = "/indexHome", method = RequestMethod.GET)
  public String indexHome(Model model, HttpServletRequest request, ModelAndView modelAndView, Integer pageNo,
    Integer pageSize) {
    IndexHomeVo indexHomeVo = bookLogService.sayHiFromClientOne(pageNo, pageSize);
    PagedResult<TLog> pages = indexHomeVo.getPages();
    TodayCountVo todayCount = indexHomeVo.getTodayCount();
    Long totalcount = indexHomeVo.getTotalcount();
    //
    String url = request.getRequestURI();
    pages.setUrl(url);
    model.addAttribute("pages", pages);
    model.addAttribute("totalcount", totalcount);
    model.addAttribute("todayCount", todayCount);
    log.info(">>>>>>>>>pages getTotal:{}", JSONObject.toJSON(pages.getTotal()));
    return "ipLog";
  }
}