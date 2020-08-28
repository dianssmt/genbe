package com.dian.genbe.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dian.genbe.model.dto.Output1Dto;
import com.dian.genbe.model.dto.Output2Dto;
import com.dian.genbe.model.dto.DataDto;
import com.dian.genbe.model.dto.Output3Dto;
import com.dian.genbe.model.dto.StatusDto;
import com.dian.genbe.model.entity.Biodata;
import com.dian.genbe.model.entity.Pendidikan;
import com.dian.genbe.model.entity.Person;
import com.dian.genbe.repository.BiodataRepository;
import com.dian.genbe.repository.PendidikanRepository;
import com.dian.genbe.repository.PersonRepository;

@RestController
@RequestMapping("/data") // localhost:9090/data
public class Controller {
	private final PersonRepository personRepository;
	private final BiodataRepository biodataRepository;
	private final PendidikanRepository pendidikanRepository;

	@Autowired
	public Controller(PersonRepository personRepository, BiodataRepository biodataRepository,
			PendidikanRepository pendidikanRepository) {
		this.personRepository = personRepository;
		this.biodataRepository = biodataRepository;
		this.pendidikanRepository = pendidikanRepository;
	}

//	localhost:9090/data
	@PostMapping
	public StatusDto insert(@RequestBody Output1Dto dto) {
		StatusDto statusDto = new StatusDto();

//		getAge
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate birthdate = LocalDate.parse(dto.getTgl(), formatter);
		LocalDate now = LocalDate.now();
		Period period = Period.between(birthdate, now);
		Integer age = period.getYears();

		if (age >= 30 && dto.getNik().length() == 16) {
			Person person = convertToEntityPerson(dto);
			personRepository.save(person);

			Biodata biodata = convertToEntityBiodata(dto);
			biodata.setPerson(person);
			biodataRepository.save(biodata);

			statusDto.setStatus("true");
			statusDto.setMessage("data berhasil masuk");
		} else if (age < 30 && dto.getNik().length() != 16) {
			statusDto.setStatus("false");
			statusDto.setMessage(
					"data gagal masuk, umur kurang dari 30 tahun dan jumlah digit nik tidak sama dengan 16");
		} else if (age < 30) {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk, umur kurang dari 30 tahun");
		} else {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk, jumlah digit nik tidak sama dengan 16");
		}
		return statusDto;
	}

	private Person convertToEntityPerson(Output1Dto dto) {
		Person person = new Person();
		person.setIdPerson(dto.getIdPerson());
		person.setNik(dto.getNik());
		person.setNama(dto.getName());
		person.setAlamat(dto.getAddress());
		return person;
	}

	private Biodata convertToEntityBiodata(Output1Dto dto) {
//		convert String to Date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMMMMMMM-yyyy");
		Date date = Date
				.from(LocalDate.parse(dto.getTgl(), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Biodata biodata = new Biodata();
		biodata.setIdBio(dto.getIdBio());
		biodata.setNoHp(dto.getHp());
		biodata.setTanggalLahir(date);
		biodata.setTempatLahir(dto.getTempatLahir());
		return biodata;
	}

//	localhost:9090/data/1234567890123456
	@GetMapping("/{nik}")
	public List<Object> get(@PathVariable String nik) {
		List<Object> output = new ArrayList<>();

		if (!personRepository.findByNik(nik).isEmpty() && nik.length() == 16) {
			Output2Dto output2Dto = new Output2Dto();
			DataDto dataDto = new DataDto();
			Person person = personRepository.findByNik(nik).get(0);
			Integer idPerson = person.getIdPerson();
			Biodata biodata = biodataRepository.findAllByPersonIdPerson(idPerson);

//			convert Date to String
			DateFormat formatter = new SimpleDateFormat("dd-MMMMMMMMM-yyyy");
			String date = formatter.format(biodata.getTanggalLahir());

//			get age
			LocalDate birthdate = biodata.getTanggalLahir().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate now = LocalDate.now();
			Period period = Period.between(birthdate, now);
			Integer age = period.getYears();

			dataDto.setNik(nik);
			dataDto.setName(person.getNama());
			dataDto.setAddress(person.getAlamat());
			dataDto.setHp(biodata.getNoHp());
			dataDto.setTgl(date);
			dataDto.setTempatLahir(biodata.getTempatLahir());
			dataDto.setUmur(age);
			dataDto.setPendidikanTerakhir(pendidikanRepository.pendidikanTerakhir(idPerson));

			output2Dto.setMassage("success");
			output2Dto.setStatus("true");
			output2Dto.setData(dataDto);
			output.add(output2Dto);

		} else if (nik.length() != 16) {
			StatusDto statusDto = new StatusDto();
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk, jumlah digit nik tidak sama dengan 16");
			output.add(statusDto);
		} else {
			StatusDto statusDto = new StatusDto();
			statusDto.setStatus("true");
			statusDto.setMessage("data dengan nik " + nik + " tidak ditemukan");
			output.add(statusDto);
		}
		return output;
	}

//	localhost:9090/data/pendidikan
	@PostMapping("/pendidikan")
	public StatusDto output3Dto(@RequestParam Integer idPerson, @RequestBody List<Output3Dto> output3Dto) {
		StatusDto statusDto = new StatusDto();
		
		try {
			for (Output3Dto output : output3Dto) {
				Pendidikan pendidikan = new Pendidikan();
				pendidikan.setIdPendidikan(output.getIdPendidikan());
				pendidikan.setJenjang(output.getJenjang());
				pendidikan.setInstitusi(output.getInstitusi());
				pendidikan.setTahunMasuk(output.getMasuk());
				pendidikan.setTahunLulus(output.getLulus());
				
				if (personRepository.findById(idPerson).isPresent()) {
					Person person = personRepository.findById(idPerson).get();
					pendidikan.setPerson(person);
				}
				pendidikanRepository.save(pendidikan);
			}
			statusDto.setStatus("true");
			statusDto.setMessage("data berhasil masuk");
		} catch (Exception e) {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk");
		}
		return statusDto;
	}
}
