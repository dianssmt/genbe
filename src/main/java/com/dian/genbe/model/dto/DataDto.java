package com.dian.genbe.model.dto;

import java.sql.Date;

public class DataDto {
	private String nik;
	private String nama;
	private String alamat;
	private String noHp;
	private Date tanggalLahir;
	private String tempatLahir;
	private Integer umur;
	private String pendidikanTerakhir;

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getNoHp() {
		return noHp;
	}

	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}


	public Date getTanggalLahir() {
		return tanggalLahir;
	}

	public void setTanggalLahir(Date tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}

	public String getTempatLahir() {
		return tempatLahir;
	}

	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}

	public Integer getUmur() {
		return umur;
	}

	public void setUmur(Integer umur) {
		this.umur = umur;
	}

	public String getPendidikanTerakhir() {
		return pendidikanTerakhir;
	}

	public void setPendidikanTerakhir(String pendidikanTerakhir) {
		this.pendidikanTerakhir = pendidikanTerakhir;
	}

}
