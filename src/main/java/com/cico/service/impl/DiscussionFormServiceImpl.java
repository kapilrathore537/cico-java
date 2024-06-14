//package com.cico.service.impl;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.cico.config.CommentResponseConfig;
//import com.cico.config.LikeResponseForum;
//import com.cico.config.RemoveComment;
//import com.cico.model.CommentReply;
//import com.cico.model.DiscussionFormComment;
//import com.cico.model.DiscusssionForm;
//import com.cico.model.Likes;
//import com.cico.model.Student;
//import com.cico.payload.CommentReplyResponse;
//import com.cico.payload.CommentResponse;
//import com.cico.payload.DiscussionFormResponse;
//import com.cico.payload.LikeResponse;
//import com.cico.repository.CommentReplyRepo;
//import com.cico.repository.DiscussionFormCommentRepo;
//import com.cico.repository.DiscussionFormRepo;
//import com.cico.repository.LikeRepo;
//import com.cico.repository.StudentRepository;
//import com.cico.service.IFileService;
//import com.cico.service.IdiscussionForm;
//import com.cico.util.AppConstants;
//import com.cico.util.DiscussionFormEnum;
//
//@Service
//public class DiscussionFormServiceImpl implements IdiscussionForm {
//
//	@Autowired
//	private DiscussionFormRepo discussionFormRepo;
//	@Autowired
//	private IFileService fileService;
//
//	@Autowired
//	private StudentRepository studentRepository;;
//
////	@Value("${discussionFromFile}")
////	private String FILE_UPLAOD_DIR;
//
//	@Autowired
//	private DiscussionFormCommentRepo discussionFormCommentRepo;
//	@Autowired
//	private LikeRepo likeRepo;
//
//	@Autowired
//	private CommentReplyRepo commentReplyRepo;
//
//	@Autowired
//	private SimpMessageSendingOperations messageSendingOperations;
//
////	@Override
////	public ResponseEntity<?> createDiscussionForm(Integer studentId, MultipartFile file, String content,
////			MultipartFile audioFile) {
////		if (Objects.isNull(content)) {
////			return new ResponseEntity<>("Message can not be empty!! ", HttpStatus.BAD_REQUEST);
////		}
////		Student student = studentRepository.findById(studentId).get();
////		if (Objects.nonNull(student)) {
////			DiscusssionForm discusssionForm = new DiscusssionForm();
////			discusssionForm.setCreatedDate(LocalDateTime.now());
////			discusssionForm.setContent(content);
////			discusssionForm.setStudent(student);
////
////			if (Objects.nonNull(file) && !file.isEmpty()) {
////				String savedFile = fileService.uploadFileInFolder(file, AppConstants.DISCUSSION_FORUM_IMAGES);
////				discusssionForm.setFile(savedFile);
////			}
////			if (Objects.nonNull(audioFile) && !audioFile.isEmpty()) {
////				String savedFile = fileService.uploadFileInFolder(audioFile, AppConstants.DISCUSSION_FORUM_IMAGES);
////				discusssionForm.setAudioFile(savedFile);
////			}
////
////			/// sending response to socket /////////////////////////////////
////			DiscusssionForm save = discussionFormRepo.save(discusssionForm);
////			DiscussionFormResponse response = new DiscussionFormResponse();
////			response.setAudioFile(save.getAudioFile());
////			response.setComments(
////					save.getComments().stream().map(obj -> getCommentFilter(obj)).collect(Collectors.toList()));
////			response.setContent(content);
////			response.setCreatedDate(save.getCreatedDate());
////			response.setFile(save.getFile());
////			response.setIsCommented(
////					save.getComments().stream().anyMatch(obj -> obj.getStudent().getStudentId() == studentId));
////			response.setId(save.getId());
////			response.setType(DiscussionFormEnum.createDiscussionForm);
////			response.setStudentProfilePic(save.getStudent().getProfilePic());
////			response.setStudentName(save.getStudent().getFullName());
////			response.setAudioFile(save.getAudioFile());
////			response.setLikes(new ArrayList<>());
////
////			sendMessageManually(response.toString());
////
////			// sending back to requested //////////////
////			DiscussionFormResponse discussionFormFilter = discussionFormFilter(save);
////
////			return new ResponseEntity<>(discussionFormFilter, HttpStatus.OK);
////		} else {
////			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////		}
////	}
////
////	@Override
////	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId,
////			MultipartFile file) {
////		Student student = studentRepository.findById(studentId).get();
////		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
////		if (Objects.nonNull(student) && !content.equals("")) {
////			if (discussionForm.get().getComments().stream()
////					.anyMatch(obj -> obj.getStudent().getStudentId() == studentId)) {
////				return new ResponseEntity<>("ALREADY_COMMENTED", HttpStatus.OK);
////			}
////			DiscussionFormComment comment = new DiscussionFormComment();
////			comment.setCreatedDate(LocalDateTime.now());
////			comment.setContent(content);
////			comment.setStudent(student);
////			if (Objects.nonNull(file) && !file.isEmpty()) {
////				String savedFile = fileService.uploadFileInFolder(file, AppConstants.DISCUSSION_FORUM_IMAGES);
////				comment.setFile(savedFile);
////			}
////			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
////			if (Objects.nonNull(discussionForm)) {
////				List<DiscussionFormComment> comments = discussionForm.get().getComments();
////				comments.add(savedComment);
////				discussionForm.get().setComments(comments);
////				discussionFormRepo.save(discussionForm.get());
////			}
////
////			// sending response to the socket
////			CommentResponseConfig res = new CommentResponseConfig();
////			res.setCreatedDate(savedComment.getCreatedDate());
////			res.setContent(savedComment.getContent());
////			res.setDiscussionFormId(discussionFormId);
////			res.setFile(savedComment.getFile());
////			res.setType("commentResponse");
////			res.setStudentProfilePic(savedComment.getStudent().getProfilePic());
////			res.setStudentId(savedComment.getStudent().getStudentId());
////			res.setId(savedComment.getId());
////			res.setStudentName(savedComment.getStudent().getFullName());
////			sendMessageManually(res.toString());
////			return new ResponseEntity<>(getCommentFilter(savedComment), HttpStatus.OK);
////		} else {
////			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////		}
////	}
////	// Method to send a message manually
////
//////	@Override
//////	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId,
//////			MultipartFile file) {
//////		Student student = studentRepository.findById(studentId).get();
//////		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
//////		if (Objects.nonNull(student) && !content.equals("")) {
//////			if (discussionForm.get().getComments().stream()
//////					.anyMatch(obj -> obj.getStudent().getStudentId() == studentId)) {
//////				return new ResponseEntity<>("ALREADY_COMMENTED", HttpStatus.OK);
//////			
//////			DiscussionFormComment comment = new DiscussionFormComment();
//////			comment.setCreatedDate(LocalDateTime.now());
//////			comment.setContent(content);
//////			comment.setStudent(student);
//////			if (Objects.nonNull(file) && !file.isEmpty()) {
//////				String savedFile = fileService.uploadFileInFolder(file, FILE_UPLAOD_DIR);
//////				comment.setFile(savedFile);
//////			}
//////			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
//////			if (Objects.nonNull(discussionForm)) {
//////				List<DiscussionFormComment> comments = discussionForm.get().getComments();
//////				comments.add(savedComment);
//////				discussionForm.get().setComments(comments);
//////				discussionFormRepo.save(discussionForm.get());
//////			}
//////
//////			// sending message to socket
//////			
//////			/// sending message to api request ////
//////			return new ResponseEntity<>(getCommentFilter(savedComment), HttpStatus.OK);
//////		} else {
//////			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//////		}
//////	
//////	}
////
////	@Override
////	public ResponseEntity<?> getAllDiscussionForm(Integer studentId) {
////		List<DiscusssionForm> list = discussionFormRepo.findAll();
////		Map<String, Object> response = new HashMap<>();
////		if (list.isEmpty()) {
////			response.put("response", list);
////			return new ResponseEntity<>(response, HttpStatus.OK);
////		} else {
////			List<DiscussionFormResponse> response1 = new ArrayList<>();
////			if (Objects.nonNull(studentId)) {
////				list.forEach(obj -> {
////					DiscussionFormResponse obj1 = discussionFormFilter(obj);
////					obj1.setIsLike(obj1.getLikes().stream().anyMatch(e -> Objects.equals(e.getStudentId(), studentId)));
////					response1.add(obj1);
////					obj1.setIsCommented(obj1.getComments().stream()
////							.anyMatch(obj2 -> Objects.equals(obj2.getStudentId(), studentId)));
////				});
////
////			} else {
////				list.forEach(obj -> {
////					DiscussionFormResponse obj1 = discussionFormFilter(obj);
////					response1.add(obj1);
////				});
////			}
////
////			Collections.reverse(response1);
////			List<DiscussionFormResponse> collect = response1.stream().filter(obj -> {
////				Collections.reverse(obj.getComments());
////				return obj != null;
////			}).collect(Collectors.toList());
////
////			response.put("response", collect);
////			return new ResponseEntity<>(response, HttpStatus.OK);
////		}
////	}
////
////	@Override
////	public ResponseEntity<?> getDiscussionFormById(Integer id, Integer studentId) {
////		Optional<DiscusssionForm> object = discussionFormRepo.findById(id);
////		if (Objects.isNull(object)) {
////			return new ResponseEntity<>("DISCUSSION_FORM_NOT_FOUND", HttpStatus.OK);
////		} else {
////			DiscussionFormResponse res = discussionFormFilter(object.get());
////			res.likes.forEach(obj -> {
////				res.setIsLike(res.getLikes().stream().anyMatch(e -> Objects.equals(e.getStudentId(), studentId)));
////			});
////			res.setIsCommented(
////					res.getComments().stream().anyMatch(obj2 -> Objects.equals(obj2.getStudentId(), studentId)));
////			return new ResponseEntity<>(res, HttpStatus.OK);
////		}
////	}
////
////	@Override
////	public ResponseEntity<?> addOrRemoveLike(Integer studentId, Integer discussionFormId) {
////		Optional<Student> student1 = studentRepository.findById(studentId);
////		Optional<DiscusssionForm> discusssionForm = discussionFormRepo.findById(discussionFormId);
////		if (student1.isPresent() && discusssionForm.isPresent()) {
////			DiscusssionForm form = discusssionForm.get();
////			Student student = student1.get();
////			List<Likes> likes = form.getLikes();
////			Likes like = likes.stream().filter(obj -> obj.getStudent().getStudentId() == studentId).findFirst()
////					.orElse(null);
////			if (Objects.isNull(like)) {
////				Likes obj = new Likes();
////				obj.setCreatedDate(LocalDateTime.now());
////				obj.setStudent(student);
////				Likes like1 = likeRepo.save(obj);
////				likes.add(like1);
////				form.setLikes(likes);
////				DiscusssionForm save = discussionFormRepo.save(form);
////				DiscussionFormResponse obj1 = discussionFormFilter(save);
////				obj1.setIsLike(true);
////				obj1.setType(DiscussionFormEnum.likeResponse);
////
////				// sending to socket response ///
////				LikeResponseForum res = new LikeResponseForum();
////				res.setCreatedDate(LocalDateTime.now());
////				res.setDiscussionFormId(discussionFormId);
////				res.setLikeId(like1.getId());
////				res.setLike(true);
////				res.setStudentId(studentId);
////				res.setType(DiscussionFormEnum.likeResponse);
////				sendMessageManually(res.toString());
////				//////////////// end /////////////		
////
////				return new ResponseEntity<>(obj1, HttpStatus.OK);
////			} else {
////				form.setLikes(likes.parallelStream().filter(obj -> obj.getStudent().getStudentId() != studentId)
////						.collect(Collectors.toList()));
////				DiscusssionForm form2 = discussionFormRepo.save(form);
////				likeRepo.delete(like);
////				DiscussionFormResponse obj1 = discussionFormFilter(form2);
////				obj1.setIsLike(false);
////				obj1.setType(DiscussionFormEnum.removeLike);
////
////				// sending to socket response ///
////				LikeResponseForum res = new LikeResponseForum();
////				res.setCreatedDate(LocalDateTime.now());
////				res.setDiscussionFormId(discussionFormId);
////				res.setLikeId(like.getId());
////				res.setLike(false);
////				res.setType(DiscussionFormEnum.removeLike);
////				res.setStudentId(studentId);
////				sendMessageManually(res.toString());
////				//////////////// end //////
////				return new ResponseEntity<>(obj1, HttpStatus.OK);
////			}
////		}
////		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////	}
////
////	public DiscussionFormResponse discussionFormFilter(DiscusssionForm obj) {
////		List<CommentResponse> comments = new ArrayList<>();
////		List<LikeResponse> likes = new ArrayList<>();
////
////		DiscussionFormResponse object = new DiscussionFormResponse();
////		object.setCreatedDate(obj.getCreatedDate());
////		object.setContent(obj.getContent());
////		object.setStudentId(obj.getStudent().getStudentId());
////		object.setStudentName(obj.getStudent().getFullName());
////		object.setStudentProfilePic(obj.getStudent().getProfilePic());
////		object.setId(obj.getId());
////		object.setFile(obj.getFile());
////		object.setCourseName(obj.getStudent().getApplyForCourse());
////		object.setAudioFile(obj.getAudioFile());
////		object.setIsCommented(false);
////
////		if (Objects.nonNull(obj.getLikes())) {
////			obj.getLikes().forEach(obj1 -> {
////				LikeResponse likeResponse = new LikeResponse();
////				likeResponse.setCreatedDate(obj1.getCreatedDate());
////				likeResponse.setStudentName(obj1.getStudent().getFullName());
////				likeResponse.setStudentProfilePic(obj1.getStudent().getProfilePic());
////				likeResponse.setId(obj1.getId());
////				likeResponse.setStudentId(obj1.getStudent().getStudentId());
////				likes.add(likeResponse);
////			});
////		}
////		if (Objects.nonNull(obj.getComments())) {
////
////			obj.getComments().forEach(obj2 -> {
////				List<CommentReplyResponse> commentReplyResponses = new ArrayList<>();
////				CommentResponse commentResponse = new CommentResponse();
////				commentResponse.setCreatedDate(obj2.getCreatedDate());
////				commentResponse.setStudentName(obj2.getStudent().getFullName());
////				commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
////				commentResponse.setId(obj2.getId());
////				commentResponse.setContent(obj2.getContent());
////				commentResponse.setStudentId(obj2.getStudent().getStudentId());
////				commentResponse.setFile(obj2.getFile());
////				obj2.getCommentReply().forEach(reply -> {
////					commentReplyResponses.add(getCommentReplyFilter(reply));
////				});
////				Collections.reverse(commentReplyResponses);
////				commentResponse.setCommentReplyResponses(commentReplyResponses);
////				comments.add(commentResponse);
////			});
////
////		}
////		object.setLikes(likes);
////		object.setComments(comments);
////
////		return object;
////	}
////
////	@Override
////	public ResponseEntity<?> removeComment(Integer discussionFormId, Integer commentsId) {
////
////		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
////		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
////
////		if (discussionForm.isPresent() && commentsForm.isPresent()) {
////			DiscusssionForm discusssionForm1 = discussionForm.get();
////			DiscussionFormComment comment1 = commentsForm.get();
////
////			List<DiscussionFormComment> comments = discusssionForm1.getComments();
////			DiscussionFormComment discussionFormComment = comments.parallelStream()
////					.filter(obj -> obj.getId() == commentsId).findFirst().orElse(null);
////			discusssionForm1.setComments(
////					comments.parallelStream().filter(obj -> obj.getId() != commentsId).collect(Collectors.toList()));
////			DiscusssionForm form2 = discussionFormRepo.save(discusssionForm1);
////			discussionFormCommentRepo.delete(discussionFormComment);
////
////			RemoveComment res = new RemoveComment();
////			res.setDiscussionFormId(discussionFormId);
////			res.setType("removeComment");
////			res.setCommentId(commentsId);
////			sendMessageManually(res.toString());
////			return new ResponseEntity<>(discussionFormFilter(form2), HttpStatus.OK);
////		}
////		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////
////	}
////
////	@Override
////	public ResponseEntity<?> removeCommentReply(Integer commentReplyId, Integer commentsId) {
////
////		Optional<CommentReply> commentReply = commentReplyRepo.findById(commentReplyId);
////		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
////		Map<String, Object> response = new HashMap<>();
////		if (commentReply.isPresent() && commentsForm.isPresent()) {
////			DiscussionFormComment comment1 = commentsForm.get();
////			List<CommentReply> commentReply2 = comment1.getCommentReply();
////			CommentReply cmReply = commentReply2.parallelStream().filter(obj -> obj.getId() == commentReplyId)
////					.findFirst().orElse(null);
////			commentsForm.get().setCommentReply(commentReply2.parallelStream()
////					.filter(obj -> obj.getId() != commentReplyId).collect(Collectors.toList()));
////			DiscussionFormComment save = discussionFormCommentRepo.save(comment1);
////			commentReplyRepo.delete(cmReply);
////			response.put("response", "deleted");
////			return new ResponseEntity<>(response, HttpStatus.OK);
////		} else {
////			response.put("response", "not_found");
////			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
////		}
////
////	}
////
////	public CommentResponse getCommentFilter(DiscussionFormComment obj2) {
////		CommentResponse commentResponse = new CommentResponse();
////		commentResponse.setCreatedDate(obj2.getCreatedDate());
////		commentResponse.setStudentName(obj2.getStudent().getFullName());
////		commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
////		commentResponse.setId(obj2.getId());
////		commentResponse.setContent(obj2.getContent());
////		commentResponse.setStudentId(obj2.getStudent().getStudentId());
////		commentResponse.setFile(obj2.getFile());
////		return commentResponse;
////
////	}
////
////	@Override
////	public ResponseEntity<?> deletePost() {
////		List<DiscusssionForm> list = discussionFormRepo.findAll();
////		list.stream().filter(obj -> {
////			obj.setLikes(new ArrayList<>());
////			obj.setComments(new ArrayList<>());
////			discussionFormRepo.delete(obj);
////			return obj != null;
////		}).collect(Collectors.toList());
////		return new ResponseEntity<>(HttpStatus.OK);
////	}
////
////	@Override
////	public ResponseEntity<?> addCommentReply(Integer studentId, Integer commentsId, String content,
////			MultipartFile file) {
////		Optional<Student> student = studentRepository.findById(studentId);
////		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
////		if (Objects.nonNull(commentsForm) && Objects.nonNull(student)) {
////			CommentReply obj = new CommentReply();
////			obj.setContent(content);
////			obj.setCreatedDate(LocalDateTime.now());
////			obj.setStudent(student.get());
////			if (Objects.nonNull(file) && !file.isEmpty()) {
////				String savedFile = fileService.uploadFileInFolder(file, AppConstants.DISCUSSION_FORUM_IMAGES);
////				obj.setFile(savedFile);
////			}
////			CommentReply save2 = commentReplyRepo.save(obj);
////			commentsForm.get().getCommentReply().add(save2);
////			DiscussionFormComment save = discussionFormCommentRepo.save(commentsForm.get());
////			return new ResponseEntity<>(getCommentReplyFilter(save2), HttpStatus.OK);
////		} else {
////			return new ResponseEntity<>(HttpStatus.OK);
////		}
////	}
////
////	public CommentReplyResponse getCommentReplyFilter(CommentReply commentReply) {
////
////		CommentReplyResponse obj = new CommentReplyResponse();
////		obj.setCreatedDate(commentReply.getCreatedDate());
////		obj.setStudentName(commentReply.getStudent().getFullName());
////		obj.setStudentProfilePic(commentReply.getStudent().getProfilePic());
////		obj.setId(commentReply.getId());
////		obj.setContent(commentReply.getContent());
////		obj.setStudentId(commentReply.getStudent().getStudentId());
////		obj.setFile(commentReply.getFile());
////		return obj;
////	}
////
////	public void sendMessageManually(String message) {
////		System.err.println("message send" + message);
////		messageSendingOperations.convertAndSend("/queue/Chatmessages", message);
////	}
//
//	@Override
//	public ResponseEntity<?> createDiscussionForm(Integer studentId, MultipartFile file, String content,
//			MultipartFile audioFile) {
//		if (Objects.isNull(content)) {
//			return new ResponseEntity<>("Message can not be empty!! ", HttpStatus.BAD_REQUEST);
//		}
//		Student student = studentRepository.findById(studentId).get();
//		if (Objects.nonNull(student)) {
//			DiscusssionForm discusssionForm = new DiscusssionForm();
//			discusssionForm.setCreatedDate(LocalDateTime.now());
//			discusssionForm.setContent(content);
//			discusssionForm.setStudent(student);
//
//			if (Objects.nonNull(file) && !file.isEmpty()) {
//				String savedFile = fileService.uploadFileInFolder(file, "");
//				discusssionForm.setFile(savedFile);
//			}
//			if (Objects.nonNull(audioFile) && !audioFile.isEmpty()) {
//				String savedFile = fileService.uploadFileInFolder(audioFile, "");
//				discusssionForm.setAudioFile(savedFile);
//			}
//
//			/// sending response to socket /////////////////////////////////
//			DiscusssionForm save = discussionFormRepo.save(discusssionForm);
//			DiscussionFormResponse response = new DiscussionFormResponse();
//			response.setAudioFile(save.getAudioFile());
//			response.setComments(
//					save.getComments().stream().map(obj -> getCommentFilter(obj)).collect(Collectors.toList()));
//			response.setContent(content);
//			response.setCreatedDate(save.getCreatedDate());
//			response.setFile(save.getFile());
//			response.setIsCommented(
//					save.getComments().stream().anyMatch(obj -> obj.getStudent().getStudentId() == studentId));
//			response.setId(save.getId());
//			//response.setType("createDiscussionForm");
//			response.setStudentProfilePic(save.getStudent().getProfilePic());
//			response.setStudentName(save.getStudent().getFullName());
//			response.setAudioFile(save.getAudioFile());
//			response.setLikes(new ArrayList<>());
//
//			sendMessageManually(response.toString());
//
//			// sending back to requested //////////////
//			DiscussionFormResponse discussionFormFilter = discussionFormFilter(save);
//
//			return new ResponseEntity<>(discussionFormFilter, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@Override
//	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId,
//			MultipartFile file) {
//		Student student = studentRepository.findById(studentId).get();
//		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
//		if (Objects.nonNull(student) && !content.equals("")) {
//			if (discussionForm.get().getComments().stream()
//					.anyMatch(obj -> obj.getStudent().getStudentId() == studentId)) {
//				return new ResponseEntity<>("ALREADY_COMMENTED", HttpStatus.OK);
//			}
//			DiscussionFormComment comment = new DiscussionFormComment();
//			comment.setCreatedDate(LocalDateTime.now());
//			comment.setContent(content);
//			comment.setStudent(student);
//			if (Objects.nonNull(file) && !file.isEmpty()) {
//				String savedFile = fileService.uploadFileInFolder(file, "");
//				comment.setFile(savedFile);
//			}
//			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
//			if (Objects.nonNull(discussionForm)) {
//				List<DiscussionFormComment> comments = discussionForm.get().getComments();
//				comments.add(savedComment);
//				discussionForm.get().setComments(comments);
//				discussionFormRepo.save(discussionForm.get());
//			}
//
//			// sending response to the socket
//			CommentResponseConfig res = new CommentResponseConfig();
//			res.setCreatedDate(savedComment.getCreatedDate());
//			res.setContent(savedComment.getContent());
//			res.setDiscussionFormId(discussionFormId);
//			res.setFile(savedComment.getFile());
//			res.setType("commentResponse");
//			res.setStudentProfilePic(savedComment.getStudent().getProfilePic());
//			res.setStudentId(savedComment.getStudent().getStudentId());
//			res.setId(savedComment.getId());
//			res.setStudentName(savedComment.getStudent().getFullName());
//			sendMessageManually(res.toString());
//			return new ResponseEntity<>(getCommentFilter(savedComment), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//	// Method to send a message manually
//
////	@Override
////	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId,
////			MultipartFile file) {
////		Student student = studentRepository.findById(studentId).get();
////		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
////		if (Objects.nonNull(student) && !content.equals("")) {
////			if (discussionForm.get().getComments().stream()
////					.anyMatch(obj -> obj.getStudent().getStudentId() == studentId)) {
////				return new ResponseEntity<>("ALREADY_COMMENTED", HttpStatus.OK);
////			
////			DiscussionFormComment comment = new DiscussionFormComment();
////			comment.setCreatedDate(LocalDateTime.now());
////			comment.setContent(content);
////			comment.setStudent(student);
////			if (Objects.nonNull(file) && !file.isEmpty()) {
////				String savedFile = fileService.uploadFileInFolder(file, FILE_UPLAOD_DIR);
////				comment.setFile(savedFile);
////			}
////			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
////			if (Objects.nonNull(discussionForm)) {
////				List<DiscussionFormComment> comments = discussionForm.get().getComments();
////				comments.add(savedComment);
////				discussionForm.get().setComments(comments);
////				discussionFormRepo.save(discussionForm.get());
////			}
////
////			// sending message to socket
////			
////			/// sending message to api request ////
////			return new ResponseEntity<>(getCommentFilter(savedComment), HttpStatus.OK);
////		} else {
////			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////		}
////	
////	}
//
//	@Override
//	public ResponseEntity<?> getAllDiscussionForm(Integer studentId) {
//		List<DiscusssionForm> list = discussionFormRepo.findAll();
//		Map<String, Object> response = new HashMap<>();
//		if (list.isEmpty()) {
//			response.put("response", list);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		} else {
//			List<DiscussionFormResponse> response1 = new ArrayList<>();
//			if (Objects.nonNull(studentId)) {
//				list.forEach(obj -> {
//					DiscussionFormResponse obj1 = discussionFormFilter(obj);
//					obj1.setIsLike(obj1.getLikes().stream().anyMatch(e -> Objects.equals(e.getStudentId(), studentId)));
//					response1.add(obj1);
//					obj1.setIsCommented(obj1.getComments().stream()
//							.anyMatch(obj2 -> Objects.equals(obj2.getStudentId(), studentId)));
//				});
//
//			} else {
//				list.forEach(obj -> {
//					DiscussionFormResponse obj1 = discussionFormFilter(obj);
//					response1.add(obj1);
//				});
//			}
//
//			Collections.reverse(response1);
//			List<DiscussionFormResponse> collect = response1.stream().filter(obj -> {
//				Collections.reverse(obj.getComments());
//				return obj != null;
//			}).collect(Collectors.toList());
//
//			response.put("response", collect);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}
//	}
//
//	@Override
//	public ResponseEntity<?> getDiscussionFormById(Integer id, Integer studentId) {
//		Optional<DiscusssionForm> object = discussionFormRepo.findById(id)
//;
//		if (Objects.isNull(object)) {
//			return new ResponseEntity<>("DISCUSSION_FORM_NOT_FOUND", HttpStatus.OK);
//		} else {
//			DiscussionFormResponse res = discussionFormFilter(object.get());
//			res.likes.forEach(obj -> {
//				res.setIsLike(res.getLikes().stream().anyMatch(e -> Objects.equals(e.getStudentId(), studentId)));
//			});
//			res.setIsCommented(
//					res.getComments().stream().anyMatch(obj2 -> Objects.equals(obj2.getStudentId(), studentId)));
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//	}
//
//	@Override
//	public ResponseEntity<?> addOrRemoveLike(Integer studentId, Integer discussionFormId) {
//		Optional<Student> student1 = studentRepository.findById(studentId);
//		Optional<DiscusssionForm> discusssionForm = discussionFormRepo.findById(discussionFormId);
//		if (student1.isPresent() && discusssionForm.isPresent()) {
//			DiscusssionForm form = discusssionForm.get();
//			Student student = student1.get();
//			List<Likes> likes = form.getLikes();
//			Likes like = likes.stream().filter(obj -> obj.getStudent().getStudentId() == studentId).findFirst()
//					.orElse(null);
//			if (Objects.isNull(like)) {
//				Likes obj = new Likes();
//				obj.setCreatedDate(LocalDateTime.now());
//				obj.setStudent(student);
//				Likes like1 = likeRepo.save(obj);
//				likes.add(like1);
//				form.setLikes(likes);
//				DiscusssionForm save = discussionFormRepo.save(form);
//				DiscussionFormResponse obj1 = discussionFormFilter(save);
//				obj1.setIsLike(true);
//				
//				// sending to socket response ///
//				LikeResponseForum res = new LikeResponseForum();
//				res.setCreatedDate(LocalDateTime.now());
//				res.setDiscussionFormId(discussionFormId);
//				res.setLikeId(like1.getId());
//				res.setLike(true);
//				//res.setType("likeResponse");
//				res.setStudentId(studentId);
//				sendMessageManually(res.toString());
//				////////////////end   //////
//				
//				return new ResponseEntity<>(obj1, HttpStatus.OK);
//			} else {
//				form.setLikes(likes.parallelStream().filter(obj -> obj.getStudent().getStudentId() != studentId)
//						.collect(Collectors.toList()));
//				DiscusssionForm form2 = discussionFormRepo.save(form);
//				likeRepo.delete(like)
//;
//				DiscussionFormResponse obj1 = discussionFormFilter(form2);
//				obj1.setIsLike(false);
//
//				
//				// sending to socket response ///
//				LikeResponseForum res = new LikeResponseForum();
//				res.setCreatedDate(LocalDateTime.now());
//				res.setDiscussionFormId(discussionFormId);
//				res.setLikeId(like.getId());
//				res.setLike(false);
//			//	res.setType("removeLike");
//				res.setStudentId(studentId);
//				sendMessageManually(res.toString());
//				////////////////  end   //////
//				return new ResponseEntity<>(obj1, HttpStatus.OK);
//			}
//		}
//		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}
//
//	public DiscussionFormResponse discussionFormFilter(DiscusssionForm obj) {
//		List<CommentResponse> comments = new ArrayList<>();
//		List<LikeResponse> likes = new ArrayList<>();
//
//		DiscussionFormResponse object = new DiscussionFormResponse();
//		object.setCreatedDate(obj.getCreatedDate());
//		object.setContent(obj.getContent());
//		object.setStudentId(obj.getStudent().getStudentId());
//		object.setStudentName(obj.getStudent().getFullName());
//		object.setStudentProfilePic(obj.getStudent().getProfilePic());
//		object.setId(obj.getId());
//		object.setFile(obj.getFile());
//		object.setCourseName(obj.getStudent().getApplyForCourse());
//		object.setAudioFile(obj.getAudioFile());
//		object.setIsCommented(false);
//
//		if (Objects.nonNull(obj.getLikes())) {
//			obj.getLikes().forEach(obj1 -> {
//				LikeResponse likeResponse = new LikeResponse();
//				likeResponse.setCreatedDate(obj1.getCreatedDate());
//				likeResponse.setStudentName(obj1.getStudent().getFullName());
//				likeResponse.setStudentProfilePic(obj1.getStudent().getProfilePic());
//				likeResponse.setId(obj1.getId());
//				likeResponse.setStudentId(obj1.getStudent().getStudentId());
//				likes.add(likeResponse);
//			});
//		}
//		if (Objects.nonNull(obj.getComments())) {
//
//			obj.getComments().forEach(obj2 -> {
//				List<CommentReplyResponse> commentReplyResponses = new ArrayList<>();
//				CommentResponse commentResponse = new CommentResponse();
//				commentResponse.setCreatedDate(obj2.getCreatedDate());
//				commentResponse.setStudentName(obj2.getStudent().getFullName());
//				commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
//				commentResponse.setId(obj2.getId());
//				commentResponse.setContent(obj2.getContent());
//				commentResponse.setStudentId(obj2.getStudent().getStudentId());
//				commentResponse.setFile(obj2.getFile());
//				obj2.getCommentReply().forEach(reply -> {
//					commentReplyResponses.add(getCommentReplyFilter(reply));
//				});
//				Collections.reverse(commentReplyResponses);
//				commentResponse.setCommentReplyResponses(commentReplyResponses);
//				comments.add(commentResponse);
//			});
//
//		}
//		object.setLikes(likes);
//		object.setComments(comments);
//
//		return object;
//	}
//
//	@Override
//	public ResponseEntity<?> removeComment(Integer discussionFormId, Integer commentsId) {
//
//		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
//		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
//
//		if (discussionForm.isPresent() && commentsForm.isPresent()) {
//			DiscusssionForm discusssionForm1 = discussionForm.get();
//			DiscussionFormComment comment1 = commentsForm.get();
//
//			List<DiscussionFormComment> comments = discusssionForm1.getComments();
//			DiscussionFormComment discussionFormComment = comments.parallelStream()
//					.filter(obj -> obj.getId() == commentsId).findFirst().orElse(null);
//
//			discusssionForm1.setComments(
//					comments.parallelStream().filter(obj -> obj.getId() != commentsId).collect(Collectors.toList()));
//			DiscusssionForm form2 = discussionFormRepo.save(discusssionForm1);
//			discussionFormCommentRepo.delete(discussionFormComment);
//
//			RemoveComment res = new RemoveComment();
//			res.setDiscussionFormId(discussionFormId);
//			res.setType("removeComment");
//			res.setCommentId(commentsId);
//			sendMessageManually(res.toString());
//			return new ResponseEntity<>(discussionFormFilter(form2), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//	}
//
//	@Override
//	public ResponseEntity<?> removeCommentReply(Integer commentReplyId, Integer commentsId) {
//
//		Optional<CommentReply> commentReply = commentReplyRepo.findById(commentReplyId);
//		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
//		Map<String, Object> response = new HashMap<>();
//		if (commentReply.isPresent() && commentsForm.isPresent()) {
//			DiscussionFormComment comment1 = commentsForm.get();
//			List<CommentReply> commentReply2 = comment1.getCommentReply();
//			CommentReply cmReply = commentReply2.parallelStream().filter(obj -> obj.getId() == commentReplyId)
//					.findFirst().orElse(null);
//			commentsForm.get().setCommentReply(commentReply2.parallelStream()
//					.filter(obj -> obj.getId() != commentReplyId).collect(Collectors.toList()));
//			DiscussionFormComment save = discussionFormCommentRepo.save(comment1);
//			commentReplyRepo.delete(cmReply);
//			response.put("response", "deleted");
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		} else {
//			response.put("response", "not_found");
//			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//		}
//
//	}
//
//	public CommentResponse getCommentFilter(DiscussionFormComment obj2) {
//		CommentResponse commentResponse = new CommentResponse();
//		commentResponse.setCreatedDate(obj2.getCreatedDate());
//		commentResponse.setStudentName(obj2.getStudent().getFullName());
//		commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
//		commentResponse.setId(obj2.getId());
//		commentResponse.setContent(obj2.getContent());
//		commentResponse.setStudentId(obj2.getStudent().getStudentId());
//		commentResponse.setFile(obj2.getFile());
//		return commentResponse;
//
//	}
//
//	@Override
//	public ResponseEntity<?> deletePost() {
//		List<DiscusssionForm> list = discussionFormRepo.findAll();
//		list.stream().filter(obj -> {
//			obj.setLikes(new ArrayList<>());
//			obj.setComments(new ArrayList<>());
//			discussionFormRepo.delete(obj);
//			return obj != null;
//		}).collect(Collectors.toList());
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<?> addCommentReply(Integer studentId, Integer commentsId, String content,
//			MultipartFile file) {
//		Optional<Student> student = studentRepository.findById(studentId);
//		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
//		if (Objects.nonNull(commentsForm) && Objects.nonNull(student)) {
//			CommentReply obj = new CommentReply();
//			obj.setContent(content);
//			obj.setCreatedDate(LocalDateTime.now());
//			obj.setStudent(student.get());
//			if (Objects.nonNull(file) && !file.isEmpty()) {
//				String savedFile = fileService.uploadFileInFolder(file, "");
//				obj.setFile(savedFile);
//			}
//			CommentReply save2 = commentReplyRepo.save(obj);
//			commentsForm.get().getCommentReply().add(save2);
//			DiscussionFormComment save = discussionFormCommentRepo.save(commentsForm.get());
//			return new ResponseEntity<>(getCommentReplyFilter(save2), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
//	}
//
//	public CommentReplyResponse getCommentReplyFilter(CommentReply commentReply) {
//
//		CommentReplyResponse obj = new CommentReplyResponse();
//		obj.setCreatedDate(commentReply.getCreatedDate());
//		obj.setStudentName(commentReply.getStudent().getFullName());
//		obj.setStudentProfilePic(commentReply.getStudent().getProfilePic());
//		obj.setId(commentReply.getId());
//		obj.setContent(commentReply.getContent());
//		obj.setStudentId(commentReply.getStudent().getStudentId());
//		obj.setFile(commentReply.getFile());
//		return obj;
//	}
//
//	public void sendMessageManually(String message) {
//		System.err.println("message send" + message);
//		messageSendingOperations.convertAndSend("/queue/Chatmessages", message);
//	}
//
//	@Override
//	public ResponseEntity<?> searchingDiscussionForm(String search) {
//		List<DiscusssionForm> searching = discussionFormRepo.searching(search.trim());
//		List<DiscussionFormResponse> collect = searching.stream().map(obj -> discussionFormFilter(obj))
//				.collect(Collectors.toList());
//		return new ResponseEntity<>(collect, HttpStatus.OK);
//	}
//}

package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.config.CommentResponseConfig;
import com.cico.config.LikeResponseForum;
import com.cico.config.RemoveComment;
import com.cico.model.CommentReply;
import com.cico.model.DiscussionFormComment;
import com.cico.model.DiscusssionForm;
import com.cico.model.Likes;
import com.cico.model.Student;
import com.cico.payload.CommentReplyResponse;
import com.cico.payload.CommentResponse;
import com.cico.payload.DiscussionFormResponse;
import com.cico.payload.LikeResponse;
import com.cico.repository.CommentReplyRepo;
import com.cico.repository.DiscussionFormCommentRepo;
import com.cico.repository.DiscussionFormRepo;
import com.cico.repository.LikeRepo;
import com.cico.repository.StudentRepository;
import com.cico.service.IFileService;
import com.cico.service.IdiscussionForm;
import com.cico.util.AppConstants;
import com.cico.util.DiscussionFormEnum;

@Service
public class DiscussionFormServiceImpl implements IdiscussionForm {

	@Autowired
	private DiscussionFormRepo discussionFormRepo;
	@Autowired
	private IFileService fileService;

	@Autowired
	private StudentRepository studentRepository;;


	@Autowired
	private DiscussionFormCommentRepo discussionFormCommentRepo;
	@Autowired
	private LikeRepo likeRepo;

	@Autowired
	private CommentReplyRepo commentReplyRepo;

	@Autowired
	private SimpMessageSendingOperations messageSendingOperations;

	@Override
	public ResponseEntity<?> createDiscussionForm(Integer studentId, MultipartFile file, String content,
			MultipartFile audioFile) {
		if (Objects.isNull(content)) {
			return new ResponseEntity<>("Message can not be empty!! ", HttpStatus.BAD_REQUEST);
		}
		Student student = studentRepository.findById(studentId).get();
		if (Objects.nonNull(student)) {
			DiscusssionForm discusssionForm = new DiscusssionForm();
			discusssionForm.setCreatedDate(LocalDateTime.now());
			discusssionForm.setContent(content);
			discusssionForm.setStudent(student);

			if (Objects.nonNull(file) && !file.isEmpty()) {
				String savedFile = fileService.uploadFileInFolder(file, AppConstants.DISCUSSION_FORUM_IMAGES);
				discusssionForm.setFile(savedFile);
			}
			if (Objects.nonNull(audioFile) && !audioFile.isEmpty()) {
				String savedFile = fileService.uploadFileInFolder(audioFile, AppConstants.DISCUSSION_FORUM_IMAGES);
				discusssionForm.setAudioFile(savedFile);
			}

			/// sending response to socket /////////////////////////////////
			DiscusssionForm save = discussionFormRepo.save(discusssionForm);
			DiscussionFormResponse response = new DiscussionFormResponse();
			response.setAudioFile(save.getAudioFile());
			response.setComments(
					save.getComments().stream().map(obj -> getCommentFilter(obj)).collect(Collectors.toList()));
			response.setContent(content);
			response.setCreatedDate(save.getCreatedDate());
			response.setFile(save.getFile());
			response.setIsCommented(
					save.getComments().stream().anyMatch(obj -> obj.getStudent().getStudentId() == studentId));
			response.setId(save.getId());
		//	response.setType("createDiscussionForm");
			response.setStudentProfilePic(save.getStudent().getProfilePic());
			response.setStudentName(save.getStudent().getFullName());
			response.setAudioFile(save.getAudioFile());
			response.setLikes(new ArrayList<>());

			sendMessageManually(response.toString());

			// sending back to requested //////////////
			DiscussionFormResponse discussionFormFilter = discussionFormFilter(save);

			return new ResponseEntity<>(discussionFormFilter, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId,
			MultipartFile file) {
		Student student = studentRepository.findById(studentId).get();
		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
		if (Objects.nonNull(student) && !content.equals("")) {
			if (discussionForm.get().getComments().stream()
					.anyMatch(obj -> obj.getStudent().getStudentId() == studentId)) {
				return new ResponseEntity<>("ALREADY_COMMENTED", HttpStatus.OK);
			}
			DiscussionFormComment comment = new DiscussionFormComment();
			comment.setCreatedDate(LocalDateTime.now());
			comment.setContent(content);
			comment.setStudent(student);
			if (Objects.nonNull(file) && !file.isEmpty()) {
				String savedFile = fileService.uploadFileInFolder(file, AppConstants.DISCUSSION_FORUM_IMAGES);
				comment.setFile(savedFile);
			}
			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
			if (Objects.nonNull(discussionForm)) {
				List<DiscussionFormComment> comments = discussionForm.get().getComments();
				comments.add(savedComment);
				discussionForm.get().setComments(comments);
				discussionFormRepo.save(discussionForm.get());
			}

			// sending response to the socket
			CommentResponseConfig res = new CommentResponseConfig();
			res.setCreatedDate(savedComment.getCreatedDate());
			res.setContent(savedComment.getContent());
			res.setDiscussionFormId(discussionFormId);
			res.setFile(savedComment.getFile());
			res.setType("commentResponse");
			res.setStudentProfilePic(savedComment.getStudent().getProfilePic());
			res.setStudentId(savedComment.getStudent().getStudentId());
			res.setId(savedComment.getId());
			res.setStudentName(savedComment.getStudent().getFullName());
			sendMessageManually(res.toString());
			return new ResponseEntity<>(getCommentFilter(savedComment), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	// Method to send a message manually

//	@Override
//	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId,
//			MultipartFile file) {
//		Student student = studentRepository.findById(studentId).get();
//		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
//		if (Objects.nonNull(student) && !content.equals("")) {
//			if (discussionForm.get().getComments().stream()
//					.anyMatch(obj -> obj.getStudent().getStudentId() == studentId)) {
//				return new ResponseEntity<>("ALREADY_COMMENTED", HttpStatus.OK);
//			
//			DiscussionFormComment comment = new DiscussionFormComment();
//			comment.setCreatedDate(LocalDateTime.now());
//			comment.setContent(content);
//			comment.setStudent(student);
//			if (Objects.nonNull(file) && !file.isEmpty()) {
//				String savedFile = fileService.uploadFileInFolder(file, FILE_UPLAOD_DIR);
//				comment.setFile(savedFile);
//			}
//			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
//			if (Objects.nonNull(discussionForm)) {
//				List<DiscussionFormComment> comments = discussionForm.get().getComments();
//				comments.add(savedComment);
//				discussionForm.get().setComments(comments);
//				discussionFormRepo.save(discussionForm.get());
//			}
//
//			// sending message to socket
//			
//			/// sending message to api request ////
//			return new ResponseEntity<>(getCommentFilter(savedComment), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	
//	}

	@Override
	public ResponseEntity<?> getAllDiscussionForm(Integer studentId) {
		List<DiscusssionForm> list = discussionFormRepo.findAll();
		Map<String, Object> response = new HashMap<>();
		if (list.isEmpty()) {
			response.put("response", list);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			List<DiscussionFormResponse> response1 = new ArrayList<>();
			if (Objects.nonNull(studentId)) {
				list.forEach(obj -> {
					DiscussionFormResponse obj1 = discussionFormFilter(obj);
					obj1.setIsLike(obj1.getLikes().stream().anyMatch(e -> Objects.equals(e.getStudentId(), studentId)));
					response1.add(obj1);
					obj1.setIsCommented(obj1.getComments().stream()
							.anyMatch(obj2 -> Objects.equals(obj2.getStudentId(), studentId)));
				});

			} else {
				list.forEach(obj -> {
					DiscussionFormResponse obj1 = discussionFormFilter(obj);
					response1.add(obj1);
				});
			}

			Collections.reverse(response1);
			List<DiscussionFormResponse> collect = response1.stream().filter(obj -> {
				Collections.reverse(obj.getComments());
				return obj != null;
			}).collect(Collectors.toList());

			response.put("response", collect);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getDiscussionFormById(Integer id, Integer studentId) {
		Optional<DiscusssionForm> object = discussionFormRepo.findById(id);
		if (Objects.isNull(object)) {
			return new ResponseEntity<>("DISCUSSION_FORM_NOT_FOUND", HttpStatus.OK);
		} else {
			DiscussionFormResponse res = discussionFormFilter(object.get());
			res.likes.forEach(obj -> {
				res.setIsLike(res.getLikes().stream().anyMatch(e -> Objects.equals(e.getStudentId(), studentId)));
			});
			res.setIsCommented(
					res.getComments().stream().anyMatch(obj2 -> Objects.equals(obj2.getStudentId(), studentId)));
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> addOrRemoveLike(Integer studentId, Integer discussionFormId) {
		Optional<Student> student1 = studentRepository.findById(studentId);
		Optional<DiscusssionForm> discusssionForm = discussionFormRepo.findById(discussionFormId);
		if (student1.isPresent() && discusssionForm.isPresent()) {
			DiscusssionForm form = discusssionForm.get();
			Student student = student1.get();
			List<Likes> likes = form.getLikes();
			Likes like = likes.stream().filter(obj -> obj.getStudent().getStudentId() == studentId).findFirst()
					.orElse(null);
			if (Objects.isNull(like)) {
				Likes obj = new Likes();
				obj.setCreatedDate(LocalDateTime.now());
				obj.setStudent(student);
				Likes like1 = likeRepo.save(obj);
				likes.add(like1);
				form.setLikes(likes);
				DiscusssionForm save = discussionFormRepo.save(form);
				DiscussionFormResponse obj1 = discussionFormFilter(save);
				obj1.setIsLike(true);

				// sending to socket response ///
				LikeResponseForum res = new LikeResponseForum();
				res.setCreatedDate(LocalDateTime.now());
				res.setDiscussionFormId(discussionFormId);
				res.setLikeId(like1.getId());
				res.setLike(true);
				res.setType(DiscussionFormEnum.likeResponse);
				res.setStudentId(studentId);
				sendMessageManually(res.toString());
				//////////////// end //////

				return new ResponseEntity<>(obj1, HttpStatus.OK);
			} else {
				form.setLikes(likes.parallelStream().filter(obj -> obj.getStudent().getStudentId() != studentId)
						.collect(Collectors.toList()));
				DiscusssionForm form2 = discussionFormRepo.save(form);
				likeRepo.delete(like);
				DiscussionFormResponse obj1 = discussionFormFilter(form2);
				obj1.setIsLike(false);

				// sending to socket response ///
				LikeResponseForum res = new LikeResponseForum();
				res.setCreatedDate(LocalDateTime.now());
				res.setDiscussionFormId(discussionFormId);
				res.setLikeId(like.getId());
				res.setLike(false);
				res.setType(DiscussionFormEnum.removeLike);
				res.setStudentId(studentId);
				sendMessageManually(res.toString());
				//////////////// end //////
				return new ResponseEntity<>(obj1, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	public DiscussionFormResponse discussionFormFilter(DiscusssionForm obj) {
		List<CommentResponse> comments = new ArrayList<>();
		List<LikeResponse> likes = new ArrayList<>();

		DiscussionFormResponse object = new DiscussionFormResponse();
		object.setCreatedDate(obj.getCreatedDate());
		object.setContent(obj.getContent());
		object.setStudentId(obj.getStudent().getStudentId());
		object.setStudentName(obj.getStudent().getFullName());
		object.setStudentProfilePic(obj.getStudent().getProfilePic());
		object.setId(obj.getId());
		object.setFile(obj.getFile());
		object.setCourseName(obj.getStudent().getApplyForCourse());
		object.setAudioFile(obj.getAudioFile());
		object.setIsCommented(false);

		if (Objects.nonNull(obj.getLikes())) {
			obj.getLikes().forEach(obj1 -> {
				LikeResponse likeResponse = new LikeResponse();
				likeResponse.setCreatedDate(obj1.getCreatedDate());
				likeResponse.setStudentName(obj1.getStudent().getFullName());
				likeResponse.setStudentProfilePic(obj1.getStudent().getProfilePic());
				likeResponse.setId(obj1.getId());
				likeResponse.setStudentId(obj1.getStudent().getStudentId());
				likes.add(likeResponse);
			});
		}
		if (Objects.nonNull(obj.getComments())) {

			obj.getComments().forEach(obj2 -> {
				List<CommentReplyResponse> commentReplyResponses = new ArrayList<>();
				CommentResponse commentResponse = new CommentResponse();
				commentResponse.setCreatedDate(obj2.getCreatedDate());
				commentResponse.setStudentName(obj2.getStudent().getFullName());
				commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
				commentResponse.setId(obj2.getId());
				commentResponse.setContent(obj2.getContent());
				commentResponse.setStudentId(obj2.getStudent().getStudentId());
				commentResponse.setFile(obj2.getFile());
				obj2.getCommentReply().forEach(reply -> {
					commentReplyResponses.add(getCommentReplyFilter(reply));
				});
				Collections.reverse(commentReplyResponses);
				commentResponse.setCommentReplyResponses(commentReplyResponses);
				comments.add(commentResponse);
			});

		}
		object.setLikes(likes);
		object.setComments(comments);

		return object;
	}

	@Override
	public ResponseEntity<?> removeComment(Integer discussionFormId, Integer commentsId) {

		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);

		if (discussionForm.isPresent() && commentsForm.isPresent()) {
			DiscusssionForm discusssionForm1 = discussionForm.get();
			DiscussionFormComment comment1 = commentsForm.get();

			List<DiscussionFormComment> comments = discusssionForm1.getComments();
			DiscussionFormComment discussionFormComment = comments.parallelStream()
					.filter(obj -> obj.getId() == commentsId).findFirst().orElse(null);

			discusssionForm1.setComments(
					comments.parallelStream().filter(obj -> obj.getId() != commentsId).collect(Collectors.toList()));
			DiscusssionForm form2 = discussionFormRepo.save(discusssionForm1);
			discussionFormCommentRepo.delete(discussionFormComment);

			RemoveComment res = new RemoveComment();
			res.setDiscussionFormId(discussionFormId);
			res.setType("removeComment");
			res.setCommentId(commentsId);
			sendMessageManually(res.toString());
			return new ResponseEntity<>(discussionFormFilter(form2), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<?> removeCommentReply(Integer commentReplyId, Integer commentsId) {

		Optional<CommentReply> commentReply = commentReplyRepo.findById(commentReplyId);
		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
		Map<String, Object> response = new HashMap<>();
		if (commentReply.isPresent() && commentsForm.isPresent()) {
			DiscussionFormComment comment1 = commentsForm.get();
			List<CommentReply> commentReply2 = comment1.getCommentReply();
			CommentReply cmReply = commentReply2.parallelStream().filter(obj -> obj.getId() == commentReplyId)
					.findFirst().orElse(null);
			commentsForm.get().setCommentReply(commentReply2.parallelStream()
					.filter(obj -> obj.getId() != commentReplyId).collect(Collectors.toList()));
			DiscussionFormComment save = discussionFormCommentRepo.save(comment1);
			commentReplyRepo.delete(cmReply);
			response.put("response", "deleted");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put("response", "not_found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	public CommentResponse getCommentFilter(DiscussionFormComment obj2) {
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setCreatedDate(obj2.getCreatedDate());
		commentResponse.setStudentName(obj2.getStudent().getFullName());
		commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
		commentResponse.setId(obj2.getId());
		commentResponse.setContent(obj2.getContent());
		commentResponse.setStudentId(obj2.getStudent().getStudentId());
		commentResponse.setFile(obj2.getFile());
		return commentResponse;

	}

	@Override
	public ResponseEntity<?> deletePost() {
		List<DiscusssionForm> list = discussionFormRepo.findAll();
		list.stream().filter(obj -> {
			obj.setLikes(new ArrayList<>());
			obj.setComments(new ArrayList<>());
			discussionFormRepo.delete(obj);
			return obj != null;
		}).collect(Collectors.toList());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addCommentReply(Integer studentId, Integer commentsId, String content,
			MultipartFile file) {
		Optional<Student> student = studentRepository.findById(studentId);
		Optional<DiscussionFormComment> commentsForm = discussionFormCommentRepo.findById(commentsId);
		if (Objects.nonNull(commentsForm) && Objects.nonNull(student)) {
			CommentReply obj = new CommentReply();
			obj.setContent(content);
			obj.setCreatedDate(LocalDateTime.now());
			obj.setStudent(student.get());
			if (Objects.nonNull(file) && !file.isEmpty()) {
				String savedFile = fileService.uploadFileInFolder(file, AppConstants.DISCUSSION_FORUM_IMAGES);
				obj.setFile(savedFile);
			}
			CommentReply save2 = commentReplyRepo.save(obj);
			commentsForm.get().getCommentReply().add(save2);
			DiscussionFormComment save = discussionFormCommentRepo.save(commentsForm.get());
			return new ResponseEntity<>(getCommentReplyFilter(save2), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	public CommentReplyResponse getCommentReplyFilter(CommentReply commentReply) {

		CommentReplyResponse obj = new CommentReplyResponse();
		obj.setCreatedDate(commentReply.getCreatedDate());
		obj.setStudentName(commentReply.getStudent().getFullName());
		obj.setStudentProfilePic(commentReply.getStudent().getProfilePic());
		obj.setId(commentReply.getId());
		obj.setContent(commentReply.getContent());
		obj.setStudentId(commentReply.getStudent().getStudentId());
		obj.setFile(commentReply.getFile());
		return obj;
	}

	public void sendMessageManually(String message) {
		System.err.println("message send" + message);
		messageSendingOperations.convertAndSend("/queue/Chatmessages", message);
	}

	@Override
	public ResponseEntity<?> searchingDiscussionForm(String search) {
		List<DiscusssionForm> searching = discussionFormRepo.searching(search.trim());
		List<DiscussionFormResponse> collect = searching.stream().map(obj -> discussionFormFilter(obj))
				.collect(Collectors.toList());
		return new ResponseEntity<>(collect, HttpStatus.OK);
	}
}

