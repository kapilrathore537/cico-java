package com.cico.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.NewsEvents;
import com.cico.payload.PageResponse;
import com.cico.repository.NewsEventsRepository;
import com.cico.service.IFileService;
import com.cico.service.INewsEventsService;
import com.cico.util.AppConstants;

@Service
public class NewsEventsServiceImpl implements INewsEventsService {

	@Autowired
	private NewsEventsRepository newsEventsRepository;

	@Autowired
	private IFileService fileService;

	@Override
	public NewsEvents createNewsEvents(String shortDescription, String briefDescription, String title,
			MultipartFile file) {
		NewsEvents newsEvents = new NewsEvents();

		if (file != null && !file.isEmpty()) {
			newsEvents.setImage(fileService.uploadFileInFolder(file, AppConstants.NEWS_AND_EVENT_IMAGES));
		}
		newsEvents.setShortDescription(shortDescription);
		newsEvents.setBriefDescription(briefDescription);
		newsEvents.setTitle(title);
		newsEvents.setIsDeleted(false);
		newsEvents.setCreatedDate(LocalDateTime.now());
		newsEvents.setUpdatedDate(LocalDateTime.now());
		newsEvents.setIsActive(true);
		return newsEventsRepository.save(newsEvents);

	}

	@Override
	public NewsEvents getNewsEvents(Integer id) {
		return newsEventsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
	}

	@Override
	public PageResponse<NewsEvents> getAllNewsEvents(Integer page, Integer size) {
		Page<NewsEvents> pageData = newsEventsRepository.findAllByIsDeleted(false,
				PageRequest.of(page, size, Sort.by(Direction.DESC, "id")));
		return new PageResponse<>(pageData.getContent(), pageData.getNumber(), pageData.getSize(),
				pageData.getTotalElements(), pageData.getTotalPages(), pageData.isLast());
	}

	@Override
	public NewsEvents updateNewsEvents(Integer id, String shortDescription, String briefDescription, String title,
			MultipartFile file) {

		NewsEvents newsEvents = newsEventsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));

		if (shortDescription != null)
			newsEvents.setShortDescription(shortDescription);

		if (briefDescription != null)
			newsEvents.setBriefDescription(briefDescription);

		if (title != null)
			newsEvents.setTitle(title);

		if (file != null && !file.isEmpty()) {
			newsEvents.setImage(fileService.uploadFileInFolder(file, AppConstants.NEWS_AND_EVENT_IMAGES));
		} else {
			newsEvents.setImage("");
		}

		newsEvents.setUpdatedDate(LocalDateTime.now());
		return newsEventsRepository.save(newsEvents);
	}

	@Override
	public void deleteNewsEvents(Integer id) {
		NewsEvents newsEvents = newsEventsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		newsEvents.setIsDeleted(true);
		newsEvents.setUpdatedDate(LocalDateTime.now());
		newsEventsRepository.save(newsEvents);
	}

	@Override
	public PageResponse<NewsEvents> getAllNewsEventsIsActive(Integer page, Integer size) {
		Page<NewsEvents> pageData = newsEventsRepository.findAllByIsDeletedAndIsActive(false, true,
				PageRequest.of(page, size, Sort.by(Direction.DESC, "id")));
		return new PageResponse<>(pageData.getContent(), pageData.getNumber(), pageData.getSize(),
				pageData.getTotalElements(), pageData.getTotalPages(), pageData.isLast());
	}

	@Override
	public Boolean activeAndInActiveNewsAndEvent(Integer id) {
		NewsEvents newsEvents = newsEventsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));

		int check = newsEventsRepository.updateActiveAndInActiveNewsAndEvent(!newsEvents.getIsActive(), id);
		return check!=0?true:false;
	}

	@Override
	public PageResponse<NewsEvents> searchNewsAndEvents(String search, String role, Integer page, Integer size) {
		Page<NewsEvents> pageData = null;
		if (role.equalsIgnoreCase("Student"))
			pageData = newsEventsRepository.searchNewsAndEventForStudent(search,
					PageRequest.of(page, size, Sort.by(Direction.DESC, "createdDate")));
		else
			pageData = newsEventsRepository.searchNewsAndEventForAdmin(search,
					PageRequest.of(page, size, Sort.by(Direction.DESC, "createdDate")));

		return new PageResponse<>(pageData.getContent(), pageData.getNumber(), pageData.getSize(),
				pageData.getTotalElements(), pageData.getTotalPages(), pageData.isLast());
	}

}
