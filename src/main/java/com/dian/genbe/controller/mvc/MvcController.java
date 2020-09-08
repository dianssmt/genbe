package com.dian.genbe.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class MvcController {
	@GetMapping("biodata.html")
	public String biodata() {
		return "form/Biodata";
	}
	@GetMapping("nik.html")
	public String nik() {
		return "form/Nik";
	}
	@GetMapping("pendidikan.html")
	public String pendidikan() {
		return "form/Pendidikan";
	}
}