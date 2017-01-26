package com.hangugi.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hangugi.service.MimeService;

@Controller
public class UploadBySomething {
	private static final Logger logger = LoggerFactory.getLogger(UploadBySomething.class);

	@Autowired
	private MimeService mimeService;

	@RequestMapping(value = "/upload.do", method = { RequestMethod.GET })
	public String fileUploadGet() {
		return "upload";
	}

	@RequestMapping(value = "/upload.do", method = { RequestMethod.POST })
	@ResponseBody
	public String fileUploadPost(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
//		Map<String, MultipartFile> map = multipartHttpServletRequest.getFileMap();

//		Iterator<String> iterator = map.keySet().iterator();
//		while(iterator.hasNext()) {
//			System.out.println("String: " + iterator.next());
//		}
//
//		Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
//		while(fileNames.hasNext()) {
//			System.out.println("String: " + fileNames.next());
//		}

//		"upload"는 <input type="file" name="upload"> 에서 name="upload" 다.
//		아래 코드는 대용량 메일에 대한 처리를 고려하지 않았다.
		List<MultipartFile> multipartFiles = multipartHttpServletRequest.getFiles("upload");

		for(MultipartFile multipartFile: multipartFiles) {
			System.out.println("Filename: " + multipartFile.getOriginalFilename());
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File("/" + multipartFile.getOriginalFilename()));
		}

		return "upload";
	}

	@RequestMapping(value = "/uploadByIframe.do", method = { RequestMethod.GET })
	public String uploadByIframeGet() {
		return "uploadByIframe";
	}

//	"upload"는 <input type="file" name="upload"> 에서 name="upload" 다.
//	아래 코드는 대용량 메일에 대한 처리를 고려하지 않았다.
	@RequestMapping(value = "/uploadByIframe.do", method = { RequestMethod.POST })
	@ResponseBody
	public String uploadByIframePost(MultipartHttpServletRequest multipartHttpServletRequest) throws MessagingException, IOException {
		List<MultipartFile> multiPartFiles = multipartHttpServletRequest.getFiles("upload");
		StringBuilder stringBuilder = new StringBuilder();

		for(MultipartFile multipartFile: multiPartFiles) {
			String fileName = multipartFile.getOriginalFilename();
			String bound = this.mimeService.getMimeStructure(multipartFile.getInputStream());
			stringBuilder.append(fileName);
			stringBuilder.append("<br>");
			stringBuilder.append(bound);
		}

		return stringBuilder.toString();
	}

	@RequestMapping(value = "/uploadByAjax.do", method = { RequestMethod.GET })
	public String uploadByAjaxGet() {
		return "uploadByAjax";
	}

	@RequestMapping(value = "/uploadByAjax.do", method = { RequestMethod.POST })
	@ResponseBody
	public String uploadByAjaxPost(MultipartHttpServletRequest multipartHttpServletRequest) throws MessagingException, IOException {
		List<MultipartFile> multiPartFiles = multipartHttpServletRequest.getFiles("upload");
		StringBuilder stringBuilder = new StringBuilder();

		for(MultipartFile multipartFile: multiPartFiles) {
			String fileName = multipartFile.getOriginalFilename();
			String bound = this.mimeService.getMimeStructure(multipartFile.getInputStream());
			stringBuilder.append(fileName);
			stringBuilder.append("<br>");
			stringBuilder.append(bound);
		}

		return stringBuilder.toString();
	}

	@RequestMapping(value = "/uploadByAjaxP.do", method = { RequestMethod.GET })
	public String uploadByAjaxPGet() {
		return "uploadByAjaxP";
	}

	@RequestMapping(value = "/uploadByAjaxP.do", method = { RequestMethod.POST })
	@ResponseBody
	public String uploadByAjaxPPost(MultipartHttpServletRequest multipartHttpServletRequest) throws MessagingException, IOException {
		List<MultipartFile> multiPartFiles = multipartHttpServletRequest.getFiles("upload");
		StringBuilder stringBuilder = new StringBuilder();

		for(MultipartFile multipartFile: multiPartFiles) {
			String fileName = multipartFile.getOriginalFilename();
			String bound = this.mimeService.getMimeStructure(multipartFile.getInputStream());
			stringBuilder.append(fileName);
			stringBuilder.append("<br>");
			stringBuilder.append(bound);
		}

		return stringBuilder.toString();
	}
	
	@RequestMapping(value="fileUploadByHttpTag.do", method = { RequestMethod.GET })
	public String fileUploadByHttpTag() {
		return "fileUploadByHttpTag";
	}

//	업로드된 하나의 파일에 대해서만 처리 가능하다.
//	@RequestMapping(value="fileUploadByHttpTag.do", method = { RequestMethod.POST })
//	public String fileUploadByHttpTag1(@RequestParam("upload") final MultipartFile multipartFile) {
//		System.out.println(multipartFile.getContentType());
//		System.out.println(multipartFile.getSize());
//		System.out.println(multipartFile.getName());
//		System.out.println(multipartFile.getOriginalFilename());
//		//multipartFile.transferTo(new File("/" + multipartFile.getName()));
//		return "index";
//	}

//	MultipartHttpServletRequest 사용법을 몰랐을 때의 코드
//	@RequestMapping(value="fileUploadByHttpTag.do", method = { RequestMethod.POST })
//	public void fileUploadByHttpTag2(@RequestParam("upload") MultipartHttpServletRequest multipartHttpServletRequest) {
//		Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
//
//		while(fileNames.hasNext()) {
//			System.out.println(fileNames.next());
//		}
//	}


	// 사용법은 토비의 스프링 3 p.1115
	@RequestMapping(value="fileUploadByHttpTag.do", method = { RequestMethod.POST })
	public void fileUploadByHttpTag2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;

//		Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
//		while(fileNames.hasNext()) {
//			System.out.println(fileNames.next());
//		}


//		Map<String, MultipartFile> map = multipartHttpServletRequest.getFileMap();
//		Collection<MultipartFile> multipartFiles = map.values();
//		Iterator<MultipartFile> muIterator = multipartFiles.iterator();
//
//		while(muIterator.hasNext()) {
//			MultipartFile multipartFile = muIterator.next();
//			System.out.println(multipartFile.getOriginalFilename());
//		}

		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

		while(iterator.hasNext()) {
			String fileName = iterator.next();
			MultipartFile multipartFile = multipartHttpServletRequest.getFile(fileName);
			System.out.println(multipartFile.getOriginalFilename());
		}
	}
}
