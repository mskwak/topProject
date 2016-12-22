package com.daou.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.daou.lotto.Lotto;
import com.daou.service.MimeService;

@Controller
public class Toby130202 {
	private static final Logger logger = LoggerFactory.getLogger(Toby130202.class);

	@Autowired
	private MimeService mimeService;

/*
	@RequestMapping(value = "/login.do", method = { RequestMethod.GET })
	public String loginGet() {
		return "login";
	}

	@RequestMapping(value = "/login.do", method = { RequestMethod.POST })
	public String loginPost(Model model, @RequestParam("id") String id, @RequestParam("password") String password) {
		model.addAttribute("id", id);
		model.addAttribute("password", password);
		return "redirect:main.do";
	}

	@RequestMapping(value = "/main.do")
	public void main(Model model, @RequestParam("id") String id, @RequestParam("password") String password) {
		model.addAttribute("id", id);
		model.addAttribute("password", password);
		model.addAttribute("os", this.osName);
	}
*/

	@RequestMapping(value = "/login.do", method = { RequestMethod.GET })
	public String loginGet(HttpServletRequest request, HttpServletResponse response) {
		logger.info("called loginGet...");
		return "login";
	}

	@RequestMapping(value = "/login.do", method = { RequestMethod.POST })
	public String loginPost01(Model model, @RequestParam("id") String id, @RequestParam("password") String password) {
		// RedirectAttributes, RedirectView
		// Spring Redirect시에 데이타 숨겨서 클라이언트에 넘기는 방법: http://www.coolio.so/spring-redirect%EC%8B%9C%EC%97%90-%EB%8D%B0%EC%9D%B4%ED%83%80-%EC%88%A8%EA%B2%A8%EC%84%9C-%EB%84%98%EA%B8%B0%EB%8A%94-%EB%B0%A9%EB%B2%95/
		// 아래 2줄 주석을 제거하면 브라우저에서 http://localhost/env.do?id=mskw&password=ffff 와 같이 호출하게 된다.
		//model.addAttribute("id", id);
		//model.addAttribute("password", password);

		if(!"ffff".equals(password)) {
			return "login";
		}

		return "redirect:/background.do";
	}

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public String background01() {
		return "background";
	}

	@RequestMapping(value = "/background.do", method = { RequestMethod.GET })
	public String background02() {
		return "background";
	}

//	@RequestMapping(value = "/login.do", method = { RequestMethod.POST })
//	public String loginPost02(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("pppppppppppppppppppppppppppppppp");
//
//		Cookie[] cookies = request.getCookies();
//
//		for(Cookie cookie: cookies) {
//			System.out.println(cookie.getName());
//			System.out.println(cookie.getComment());
//			System.out.println(cookie.getDomain());
//			System.out.println(cookie.getMaxAge());
//			System.out.println(cookie.getPath());
//			System.out.println(cookie.getSecure());
//			System.out.println(cookie.getValue());
//			System.out.println(cookie.getVersion());
//		}
//
//		HttpSession httpSession = request.getSession();
//
//		System.out.println("p;" + httpSession.getId());
//
//		if(httpSession.isNew()) {
//			System.out.println("ppp...");
//		}
//
//		Enumeration<String> enumeration = httpSession.getAttributeNames();
//
//		while(enumeration.hasMoreElements()) {
//			System.out.println(enumeration.nextElement());
//		}
//
//		return "login";
//	}

	@RequestMapping(value = "/json.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView jsonMethod(Locale locale, Model model, HttpServletRequest request) {
		logger.info(request.getProtocol());
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		return new ModelAndView("jsonView", "json", list);
	}

	@RequestMapping(value = "/json2.do", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<String> jsonMethod2(Locale locale, Model model, HttpServletRequest request) {
		logger.info(request.getProtocol());
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		return list;
	}

	/**
	 * ModelAndView는 메소드 파라미터에 설정될 수 있는 값(타입, 객체)이 아니다.
	 */
	@RequestMapping(value = "/mskw.do")
	public ModelAndView modelAndViewMethod() {
		Lotto lotto = new Lotto(5);
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("mskw");
		modelAndView.addObject("lottoAsMap", lotto.getLottoAsMap());
		modelAndView.addObject("lottoAsList", lotto.getLottoAsList());

		return modelAndView;
	}

	/*
		12:10:23.252 [http-bio-80-exec-7] DEBUG o.s.web.servlet.DispatcherServlet - DispatcherServlet with name 'myProject' processing GET request for [/model.do]
		12:10:23.252 [http-bio-80-exec-7] DEBUG o.s.w.s.m.m.a.RequestMappingHandlerMapping - Looking up handler method for path /model.do
		12:10:23.253 [http-bio-80-exec-7] DEBUG o.s.w.s.m.m.a.RequestMappingHandlerMapping - Returning handler method [public void com.daou.controller.Controller01.modelMethod(org.springframework.ui.Model)]
		12:10:23.253 [http-bio-80-exec-7] DEBUG o.s.b.f.s.DefaultListableBeanFactory - Returning cached instance of singleton bean 'controller01'
		12:10:23.253 [http-bio-80-exec-7] DEBUG o.s.web.servlet.DispatcherServlet - Last-Modified value for [/model.do] is: -1
		12:10:23.255 [http-bio-80-exec-7] DEBUG o.s.web.servlet.DispatcherServlet - Rendering view [org.springframework.web.servlet.view.JstlView: name 'model'; URL [/WEB-INF/jsp/model.jsp]] in DispatcherServlet with name 'myProject'
		12:10:23.255 [http-bio-80-exec-7] DEBUG o.s.web.servlet.view.JstlView - Added model object 'hashMap' of type [java.util.HashMap] to request in view with name 'model'
		12:10:23.255 [http-bio-80-exec-7] DEBUG o.s.web.servlet.view.JstlView - Added model object 'arrayListList' of type [java.util.ArrayList] to request in view with name 'model'
		12:10:23.255 [http-bio-80-exec-7] DEBUG o.s.web.servlet.view.JstlView - Added model object 'lottoAsMap' of type [java.util.HashMap] to request in view with name 'model'
		12:10:23.255 [http-bio-80-exec-7] DEBUG o.s.web.servlet.view.JstlView - Added model object 'lottoAsList' of type [java.util.ArrayList] to request in view with name 'model'
		12:10:23.255 [http-bio-80-exec-7] DEBUG o.s.web.servlet.view.JstlView - Forwarding to resource [/WEB-INF/jsp/model.jsp] in InternalResourceView 'model'
		12:10:23.261 [http-bio-80-exec-7] DEBUG o.s.web.servlet.DispatcherServlet - Successfully completed request
	 */

	/**
	 * Map, Model, ModelMap 이 메소드 파라미터로 있을 때에는 여기에 담긴 정보를 꺼내 쓰라고 있는 것이 아니다.
	 * 값을 담으라고 있는 것이다.
	 * Map, Model, ModelMap를 메소드 안에서 new 해서 사용하기 보단 파라미터에 설정해서 사용한다.
	 * 토비 p.1144 참조해라.
	 */
	@RequestMapping(value = "/model.do")
	public String modelMethod(Model model) {
		Lotto lotto = new Lotto(5);
		model.addAttribute(lotto.getLottoAsMap());
		model.addAttribute(lotto.getLottoAsList());
		model.addAttribute("lottoAsMap", lotto.getLottoAsMap());
//		model.addAttribute("lottoAsList", lotto.getLottoAsList());
		return "model";
	}

	@RequestMapping(value = "/modelmap.do")
	public String modelMapMethod(ModelMap modelMap) {
		Lotto lotto = new Lotto(5);
		modelMap.addAttribute(lotto.getLottoAsMap());
		modelMap.addAttribute(lotto.getLottoAsList());
		modelMap.addAttribute("lottoAsMap", lotto.getLottoAsMap());
		modelMap.addAttribute("lottoAsList", lotto.getLottoAsList());

		return "modelmap";
	}

	@RequestMapping(value = "/map.do")
	public void mapMethod(Map<String, Object> map) {
		Lotto lotto = new Lotto(5);
		map.put("lottoAsMap", lotto.getLottoAsMap());
		map.put("lottoAsList", lotto.getLottoAsList());
	}

	@RequestMapping(value = "/rcptauth.do", method = { RequestMethod.GET})
	public void rcptAuth() {
	}


	@RequestMapping(value = "/input.do", method = { RequestMethod.GET })
	public String inputGet() {
		return "input";
	}

	@RequestMapping(value = "/input.do", method = { RequestMethod.POST })
	@ResponseBody
	public String inputPost(MultipartHttpServletRequest multipartHttpServletRequest) throws MessagingException, IOException {
		return "input";
	}

	@RequestMapping(value = "/mapByDaum.do", method = { RequestMethod.GET })
	public String mapByDaumGet() {
		return "mapByDaum";
	}
}
