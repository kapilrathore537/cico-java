package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Chapter;
import com.cico.model.ChapterContent;
import com.cico.service.IChapterService;

@RestController
@RequestMapping("/chapter")
@CrossOrigin("*")
public class ChapterController {

	@Autowired
	private IChapterService chapterService;

	@PostMapping("/addChapter")
	public ResponseEntity<?> addChapter(@RequestParam("subjectId") Integer subjectId,
			@RequestParam("chapterName") String chapterName,
			@RequestParam(name = "image", required = false) MultipartFile image)  {
		return chapterService.addChapter(subjectId, chapterName, image);
	}

	@PostMapping("/addChapterContent")
	public ResponseEntity<?> addContentToChapter(@RequestParam("chapterId") Integer chapterId,
			@RequestParam("title") String title, @RequestParam("subTitle") String subTitle,
			@RequestParam("content") String content) {
		return chapterService.addContentToChapter(chapterId, title, subTitle, content);

	}

	@PutMapping("/updateChapterContent")
	public ResponseEntity<ChapterContent> updateChapterContent(@RequestParam("title") String title,
			@RequestParam("subTitle") String subTitle, @RequestParam("content") String content,
			@RequestParam("contentId") Integer contentId) {
		ChapterContent chapterContent = chapterService.updateChapterContent(title, subTitle, content, contentId);
		return new ResponseEntity<ChapterContent>(chapterContent, HttpStatus.OK);
	}

	@GetMapping("/getChapterContent")
	public ResponseEntity<ChapterContent> getChapterContent(@RequestParam("chapterContentId") Integer chapterContentId)
			throws Exception {
		ChapterContent chapterContent = chapterService.getChapterContent(chapterContentId);
		return new ResponseEntity<ChapterContent>(chapterContent, HttpStatus.OK);
	}

	@GetMapping("/getChapterContentWithChapterIdForAdmin")
	public ResponseEntity<?> getChapterContentWithChapterId(@RequestParam("chapterId") Integer chapterId)
			throws Exception {
		return chapterService.getChapterContentWithChapterId(chapterId);
	}

	@PutMapping("/updateChapter")
	public ResponseEntity<?> updateChapter(@RequestParam("chapterId") Integer chapterId,
			@RequestParam("chapterName") String chapterName,@RequestParam("subjectId")Integer subjectId) {
		return chapterService.updateChapter(chapterId, chapterName,subjectId);
	}

	@GetMapping("/getChapterById")
	public ResponseEntity<?> getChapterById(@RequestParam("chapterId") Integer chapterId) {
		Map<String, Object> chapter = chapterService.getChapterById(chapterId);
		return ResponseEntity.ok(chapter);
	}

	@PutMapping("/deleteChapterContent")
	public ResponseEntity<?> deleteChapterContent(@RequestParam("contentId") Integer contentId) {
		chapterService.deleteChapterContent(contentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/deleteChapter")
	public ResponseEntity<?> deleteChapter(@RequestParam("chapterId") Integer chapterId) {
		return chapterService.deleteChapter(chapterId);

	}

	@PutMapping("/updateChapterStatus")
	public ResponseEntity<String> updateChapterStatus(@RequestParam("chapterId") Integer chapterId) {
		chapterService.updateChapterStatus(chapterId);
		return ResponseEntity.ok("Chapter Updated");
	}

	@GetMapping("/getAllChapters")
	public ResponseEntity<List<Chapter>> getAllChapters(@RequestParam("subjectId") Integer subjectId) {
		List<Chapter> chapters = chapterService.getAllChapters(subjectId);
		return ResponseEntity.ok(chapters);
	}

	@GetMapping("/getChaptersBySubject")
	public ResponseEntity<List<Chapter>> getChaptersBySubject(@RequestParam("subjectId") Integer subjectId) {
		List<Chapter> chapters = chapterService.getChaptersBySubject(subjectId);
		return ResponseEntity.ok(chapters);
	}

	@GetMapping("/getChapterExamQuestions")
	public ResponseEntity<?> getChaperExamQuestions(@RequestParam("chapterId") Integer chapterId) {
		 return  chapterService.getChaperExamQuestions(chapterId);
		
	}

}
