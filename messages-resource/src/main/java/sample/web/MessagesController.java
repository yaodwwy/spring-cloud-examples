/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@Controller
public class MessagesController {

	@GetMapping("/")
	public String index() {
		return "redirect:/version";
	}
	@ResponseBody
	@GetMapping("/messages")
	public String[] getMessages() {
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}

	@GetMapping("/token")
	public String tokenInfo(@RequestParam String code) throws UnsupportedEncodingException {
		//获取token
		Map tokenMap = getAccessToken(code);
		String accessToken = (String)tokenMap.get("access_token");

		return accessToken;
	}

	/**
	 * 获取token
	 */
	public Map getAccessToken(String code) throws UnsupportedEncodingException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		byte[] authorization = ("pm-client:pm-secret").getBytes("UTF-8");
		Base64.Encoder encoder = Base64.getEncoder();
		String result = encoder.encodeToString(authorization);

		headers.add("Authorization", "Basic " + result);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("code", code);
		param.add("grant_type", "authorization_code");
		param.add("redirect_uri", "http://localhost:5173/redirect-uri");
		param.add("scope", "profile");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
		ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:9090/oauth2/token", request , Map.class);
		return response.getBody();
	}
}
