package com.ddjf.image.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ddjf.image.util.DateUtil;
import com.ddjf.image.util.ImageConvertUtil;
import com.ddjf.image.util.PdfToHtmlUtil;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@RequestMapping
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView("home");
		return mv;
	}
	
	@RequestMapping(value = "/pdf2html")
	@ResponseBody
	public String pdfToHtml(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName){
		boolean flag = PdfToHtmlUtil.pdfToHtml(filePath, fileName);
		return flag + "==>" + DateUtil.formate(new Date());
	}
	
	@RequestMapping(value = "/heicToJgp")
	@ResponseBody
	public String heicToJgp(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName){
		boolean flag = ImageConvertUtil.heicToJgp(filePath, fileName);
		return flag + "==>" + DateUtil.formate(new Date());
	}
}
