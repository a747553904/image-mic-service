package com.ddjf.image.mapper;

import java.util.List;

import com.ddjf.image.model.Country;
import com.ddjf.image.util.MyMapper;

public interface CountryMapper extends MyMapper<Country> {
	
	List<Country> findList(Country country);
	
}