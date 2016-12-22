package com.daou.controller;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 토비의 스프링 3 p.871 참조해라.
 * 10.2.5 컨테이너가 자동으로 등록하는 빈
 */
@Controller
public class SystemEnvironmentAndSystemEnvironment {
	@Resource
	private Properties systemProperties;
	@Resource
	private Map<String, String> systemEnvironment;

//	@Value("#{systemProperties['os.name']}")
//	private String osName;

//	@Value("#{systemEnvironment['path]}")
//	private String path;

	@RequestMapping(value="/properties.do", method = { RequestMethod.GET })
	public void returnProperties(Model model) {

		Set<String> set = this.systemProperties.stringPropertyNames();

		for(String string: set) {
			String value = this.systemProperties.getProperty(string);
			System.out.println(string + ":" + value);
		}

		// model.addAttribute(this.systemProperties);
		model.addAttribute("systemProperties", this.systemProperties);
	}

	/**
	 * @param modelMap
	 * @RequestMapping URL의 value를 environment.do로 설정하면 안된다. environment는 내부적으로 어딘가에서 static 하게 쓰이는 듯 하다.
	 */
	@RequestMapping(value="/env.do", method={RequestMethod.GET})
	public void returnEnvironment(ModelMap modelMap) {
		modelMap.addAttribute("systemEnvironment", this.systemEnvironment);
	}
}