package com.dian.genbe.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dian.genbe.model.dto.Output1Dto;
import com.dian.genbe.model.entity.Biodata;
import com.dian.genbe.model.entity.Person;
import com.dian.genbe.repository.BiodataRepository;
import com.dian.genbe.repository.PersonRepository;

@Service
@Transactional
public class DataServiceImpl implements DataService {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private BiodataRepository biodataRepository;

	@Override
	public Output1Dto insertBiodataPerson(Output1Dto dto) {
		Person person = new Person();
		person.setNik(dto.getNik());
		person.setNama(dto.getName());
		person.setAlamat(dto.getAddress());
		personRepository.save(person);
		dto.setIdPerson(person.getIdPerson());
		
//		convert String to Date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		Date date = Date
				.from(LocalDate.parse(dto.getTgl(), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Biodata biodata = new Biodata();
		biodata.setPerson(person);
		biodata.setIdBio(dto.getIdBio());
		biodata.setNoHp(dto.getHp());
		biodata.setTanggalLahir(date);
		biodata.setTempatLahir(dto.getTempatLahir());
		biodataRepository.save(biodata);
		return dto;
	}

}
