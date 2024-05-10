package com.cico.config;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class AppUtils {
  
	public static Page<?> convertListToPage(List<?> dataList, Pageable pageable) {
		int pageSize = pageable.getPageSize(); 
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<?> pageList;

		if (dataList.size() < startItem) {
			pageList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, dataList.size());
			pageList = dataList.subList(startItem, toIndex);
		}

		return new PageImpl<>(pageList, pageable, dataList.size());
	}

}
