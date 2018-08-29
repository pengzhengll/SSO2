package com.myimooc.sso.www.a.com;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myimooc.sso.util.RespMessage;
import com.myimooc.sso.util.Cooks;
import com.myimooc.sso.util.HttpUtils;

/**
 * @version V1.0
 */
@Controller
@RequestMapping("/a")
public class DemoOneController {

	public Cooks cooks = new Cooks();

	/**
	 * 跳转到demo1的主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/demo1")
	public ModelAndView demo1(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		if (cooks.getCookieName() != null) {
			System.out.println("==============" + cooks.getCookieName() + "======================");
			if ("sso".equals(cooks.getCookieName())) {
				// 封装请求参数
				Map<String, String> param = new HashMap<String, String>();
				param.put("cookieName", cooks.getCookieName());
				param.put("cookieValue", cooks.getCookieValue());
				// 向校验服务器发送校验请求
				String url = "http://www.x.com/sso/checkCookie";
				mv.setViewName("demo1");
				return mv;
				// }
			}
		}
		 // 登录失败重新登录
        String path = request.getContextPath();
        mv.addObject("contextPath",path);
        mv.addObject("path","a");
        mv.addObject("gotoUrl", "http://www.a.com/a/demo1");
        mv.setViewName("login");
        return mv;

	}

	/**
	 * 用户登录
	 * 
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/doLogin")
	@ResponseBody
	public RespMessage doLogin(@RequestParam Map<String, String> param) {
		// 向校验服务器发送校验请求
		String url = "http://www.x.com/sso/doLogin";
		RespMessage respMessage = HttpUtils.doGet(url, param);
		System.out.println("SSO服务器响应消息：" + respMessage);
		return respMessage;
	}

	/**
	 * 想当前域添加cookie
	 * 
	 * @param cookieName
	 * @param cookieValue
	 * @param response
	 */
	@RequestMapping(value = "/addCookie")
	public void addCookie(String cookieName, String cookieValue, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setPath("/");
		// ----------------------------------//
		cooks.setCookieName(cookie.getName());
		cooks.setCookieValue(cookie.getValue());
		response.addCookie(cookie);
	}
}