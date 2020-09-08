package com.dian.genbe.service;

import java.util.List;

import com.dian.genbe.model.dto.Output1Dto;
import com.dian.genbe.model.dto.Output3Dto;

public interface DataService {
	Output1Dto insertBiodataPerson(Output1Dto dto);
	List<Output3Dto> insertPendidikan(Integer idPerson, List<Output3Dto> dto);
}