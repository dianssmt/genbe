package com.dian.genbe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dian.genbe.model.dto.Output1Dto;
import com.dian.genbe.model.dto.Output3Dto;
import com.dian.genbe.model.entity.Biodata;
import com.dian.genbe.model.entity.Pendidikan;
import com.dian.genbe.model.entity.Person;
import com.dian.genbe.repository.BiodataRepository;
import com.dian.genbe.repository.PendidikanRepository;
import com.dian.genbe.repository.PersonRepository;

@Service
@Transactional
public class DataServiceImpl implements DataService {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private BiodataRepository biodataRepository;
	@Autowired
	private PendidikanRepository pendidikanRepository;

	@Override
	public Output1Dto insertBiodataPerson(Output1Dto dto) {
		Person person = convertToEntityPerson(dto);
		personRepository.save(person);
		dto.setIdPerson(person.getIdPerson());

		Biodata biodata = convertToEntityBiodata(dto);
		biodata.setPerson(person);
		biodataRepository.save(biodata);
		return dto;
	}

	private Person convertToEntityPerson(Output1Dto dto) {
		Person person = new Person();
		person.setNik(dto.getNik());
		person.setNama(dto.getNama());
		person.setAlamat(dto.getAlamat());
		return person;
	}

	private Biodata convertToEntityBiodata(Output1Dto dto) {
//		Convert String to Date
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
//		Date date = Date.from(
//				LocalDate.parse(dto.getTanggalLahir(), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Person person = new Person();
		Biodata biodata = new Biodata();
		biodata.setPerson(person);
		biodata.setIdBio(dto.getIdBio());
		biodata.setNoHp(dto.getNoHp());
		biodata.setTanggalLahir(dto.getTanggalLahir());
		biodata.setTempatLahir(dto.getTempatLahir());
		return biodata;
	}

	@Override
	public List<Output3Dto> insertPendidikan(Integer idPerson, List<Output3Dto> dto) {
		for (Output3Dto output : dto) {
			Pendidikan pendidikan = convertToEntityPendidikan(output);
			if (personRepository.findById(idPerson).isPresent()) {
				Person person = personRepository.findById(idPerson).get();
				pendidikan.setPerson(person);
			}
			pendidikanRepository.save(pendidikan);
		}
		return dto;
	}

	private Pendidikan convertToEntityPendidikan(Output3Dto dto) {
		Pendidikan pendidikan = new Pendidikan();
		pendidikan.setIdPendidikan(dto.getIdPendidikan());
		pendidikan.setJenjang(dto.getJenjang());
		pendidikan.setInstitusi(dto.getInstitusi());
		pendidikan.setTahunMasuk(dto.getTahunMasuk());
		pendidikan.setTahunLulus(dto.getTahunLulus());
		return pendidikan;
	}
}
