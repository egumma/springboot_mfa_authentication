package com.arch.ihcd.gateway.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.arch.ihcd.gateway.bean.VersionInfo;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("api/version")
public class VersionController {

	@Autowired
	BuildProperties buildProperties;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@GetMapping
	public ResponseEntity<List<VersionInfo>> getServiceVersion(
			@RequestHeader(name = "Authorization") String token, @RequestHeader(name = "login-userid") String userId) {
		log.info(">>>>>>VersionController getServiceVersion Start>>>>>");
		List<VersionInfo> versionList = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		String ipAddress = null;
		if (activeProfile.equals("dev")) {
			ipAddress = "http://3.228.64.120:8765/";
		} else if (activeProfile.equals("stage")) {
			ipAddress = "https://stage-backend.ihcdapp.com/";
		}else if (activeProfile.equals("prod")) {
			ipAddress = "https://prod-backend.ihcdapp.com/";
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.set("Authorization", token);
		headers.set("login-userid", userId);
		HttpEntity request = new HttpEntity<>(headers);

		// Master Data service version
		try {
			log.info(">>>>>>VersionController getServiceVersion Master Data>>>>>");
			String url = ipAddress + "ihcd-master-data/api/version";
			Map<String, String> masterVersionMap = new HashMap<>();
			ResponseEntity<Map> masterVersion = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
			if (masterVersion != null) {
				VersionInfo msterVersion = new VersionInfo();
				msterVersion.setArtifact(masterVersion.getBody().get("artifact").toString());
				msterVersion.setVersion(masterVersion.getBody().get("version").toString());				
				versionList.add(msterVersion);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Project manager service version
		try {
			log.info(">>>>>>VersionController getServiceVersion Project manager>>>>>");
			String url = ipAddress + "ihcd-project-manager/api/version";
			Map<String, String> projectVersionMap = new HashMap<>();
			ResponseEntity<Map> projectVersion = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
			if (projectVersion != null) {
				VersionInfo projVersion = new VersionInfo();
				projVersion.setArtifact(projectVersion.getBody().get("artifact").toString());	
				projVersion.setVersion(projectVersion.getBody().get("version").toString());						
				versionList.add(projVersion);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Search service version
		try {
			log.info(">>>>>>VersionController getServiceVersion Search service>>>>>");
			String url = ipAddress + "ihcd-search-service/api/version";
			Map<String, String> searchVersionMap = new HashMap<>();
			ResponseEntity<Map> searchVersion = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
			if (searchVersion != null) {
				VersionInfo searVersion = new VersionInfo();
				searVersion.setArtifact(searchVersion.getBody().get("artifact").toString());
				searVersion.setVersion(searchVersion.getBody().get("version").toString());				
				versionList.add(searVersion);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(">>>>>>VersionController getServiceVersion Zuul service>>>>>");
		// Zuul service version
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setArtifact(buildProperties.getArtifact());
		versionInfo.setVersion( buildProperties.getVersion());
		versionList.add(versionInfo);
		log.info(">>>>>>VersionController getServiceVersion End>>>>>");
		return new ResponseEntity<List<VersionInfo>>(versionList, HttpStatus.OK);
	}
}
