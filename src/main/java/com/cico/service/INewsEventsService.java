package com.cico.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.NewsEvents;
import com.cico.payload.PageResponse;



public interface INewsEventsService {

	NewsEvents createNewsEvents(String shortDescription, String briefDescription, String title, MultipartFile file);

	NewsEvents getNewsEvents(Integer id);

	PageResponse<NewsEvents> getAllNewsEvents(Integer page,Integer size);

	NewsEvents updateNewsEvents(Integer id, String shortDescription, String briefDescription, String title,
			MultipartFile file);

	void deleteNewsEvents(Integer id);

	PageResponse<NewsEvents> getAllNewsEventsIsActive(Integer page, Integer size);

	Boolean activeAndInActiveNewsAndEvent(Integer id);

	PageResponse<NewsEvents> searchNewsAndEvents(String search,String role,Integer page,Integer size);
}
