package com.ddjf.image.mapper;

import java.util.List;
import java.util.Map;

import com.ddjf.image.model.EcmPage;
import com.ddjf.image.util.MyMapper;

public interface EcmPageMapper extends MyMapper<EcmPage> {
	
	List<EcmPage> findList(EcmPage ecmPage);
	
	List<Map<String, Object>> getImageList(List<String> list);
	
	List<Map<String, Object>> getEcmPageList(EcmPage ecmPage);
	
	EcmPage getBySerialno(String serialno);
	
	Integer insertEcmPage(EcmPage ecmPage);
	
	Integer updateEcmPage(EcmPage ecmPage);

	Integer updateEcmPageRemarks(EcmPage ecmPage);

	Integer deleteEcmPage(String serialno);
	
	Integer getMaxPageNum(EcmPage ecmPage);
	
	Integer getMaxSortNo(EcmPage ecmPage);
	
	List<Map<String, String>> getEcmPageTypeList(String productType);

}