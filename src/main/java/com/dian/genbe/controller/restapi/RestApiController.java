package com.dian.genbe.controller.restapi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dian.genbe.model.dto.DataDto;
import com.dian.genbe.model.dto.Output1Dto;
import com.dian.genbe.model.dto.Output2Dto;
import com.dian.genbe.model.dto.Output3Dto;
import com.dian.genbe.model.dto.StatusDto;
import com.dian.genbe.service.DataService;
import com.dian.genbe.model.entity.Biodata;
import com.dian.genbe.model.entity.Person;
import com.dian.genbe.repository.BiodataRepository;
import com.dian.genbe.repository.PendidikanRepository;
import com.dian.genbe.repository.PersonRepository;

@RestController
@RequestMapping("/data") // localhost:9090/data
public class RestApiController {
	private final PersonRepository personRepository;
	private final BiodataRepository biodataRepository;
	private final PendidikanRepository pendidikanRepository;

	@Autowired
	public RestApiController(PersonRepository personRepository, BiodataRepository biodataRepository,
			PendidikanRepository pendidikanRepository) {
		this.personRepository = personRepository;
		this.biodataRepository = biodataRepository;
		this.pendidikanRepository = pendidikanRepository;
	}

	@Autowired
	private DataService dataService;

//	localhost:9090/data
//	transactional service
	@PostMapping
	public StatusDto insert(@RequestBody Output1Dto dto) {
		StatusDto statusDto = new StatusDto();

//		getAge
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
//		LocalDate birthdate = LocalDate.parse(dto.getTanggalLahir(), formatter);
		LocalDate birthdate = dto.getTanggalLahir().toLocalDate();
		LocalDate now = LocalDate.now();
		Period period = Period.between(birthdate, now);
		Integer age = period.getYears();

		if (age >= 30 && dto.getNik().length() == 16) {
			dataService.insertBiodataPerson(dto);
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

	@GetMapping("/getAllData")
	  public List<Output1Dto> getData() {
	    List<Output1Dto> dtoList = new ArrayList<>();
	    List<Person> person = personRepository.findAll();
	    for (Person persons : person) {
	      Output1Dto output1Dto = new Output1Dto();
	            Biodata biodata = biodataRepository.findByPersonIdPerson(persons.getIdPerson());
	            output1Dto.setIdBio(biodata.getIdBio());
	            output1Dto.setNoHp(biodata.getNoHp());
	            output1Dto.setTanggalLahir(biodata.getTanggalLahir());
	            output1Dto.setTempatLahir(biodata.getTempatLahir());
	            output1Dto.setNama(persons.getNama());
	            output1Dto.setAlamat(persons.getAlamat());
	            output1Dto.setIdPerson(persons.getIdPerson());
	            output1Dto.setNik(persons.getNik());
	            dtoList.add(output1Dto);
	        }
	    return dtoList;
	  }
	
//	localhost:9090/data/1234567890123456
	@GetMapping("/{nik}")
	public List<Object> get(@PathVariable String nik) {
		List<Object> output = new ArrayList<>();

		if (!personRepository.findByNik(nik).isEmpty() && nik.length() == 16) {
			Output2Dto output2Dto = new Output2Dto();
			Person person = personRepository.findByNik(nik).get(0);
			Integer idPerson = person.getIdPerson();
			Biodata biodata = biodataRepository.findAllByPersonIdPerson(idPerson);
			DataDto dataDto = convertToDto(person, biodata);

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
			statusDto.setStatus("false");
			statusDto.setMessage("data dengan nik " + nik + " tidak ditemukan");
			output.add(statusDto);
		}
		return output;
	}

	private DataDto convertToDto(Person person, Biodata biodata) {
//		convert Date to String
//		DateFormat formatter = new SimpleDateFormat("dd-MMMMMMMMM-yyyy");
//		String date = formatter.format(biodata.getTanggalLahir());

//		get age
		LocalDate birthdate = biodata.getTanggalLahir().toLocalDate();
		LocalDate now = LocalDate.now();
		Period period = Period.between(birthdate, now);
		Integer age = period.getYears();

		DataDto dto = new DataDto();
		Integer idPerson = person.getIdPerson();
		dto.setNik(person.getNik());
		dto.setNama(person.getNama());
		dto.setAlamat(person.getAlamat());
		dto.setNoHp(biodata.getNoHp());
		dto.setTanggalLahir(biodata.getTanggalLahir());
		dto.setTempatLahir(biodata.getTempatLahir());
		dto.setUmur(age);
		dto.setPendidikanTerakhir(pendidikanRepository.pendidikanTerakhir(idPerson));
		return dto;
	}

	//localhost:9090/data/pendidikan
	@PostMapping("/pendidikan")
	public StatusDto output3Dto(Integer idPerson, @RequestBody List<Output3Dto> dto) {
		StatusDto statusDto = new StatusDto();
		try {
			dataService.insertPendidikan(idPerson, dto);
			statusDto.setStatus("true");
			statusDto.setMessage("data berhasil masuk");
		} catch (Exception e) {
			statusDto.setStatus("false");
			statusDto.setMessage("data gagal masuk");
		}
		return statusDto;
	}
}
