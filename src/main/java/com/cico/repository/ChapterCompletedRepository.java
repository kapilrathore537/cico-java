package com.cico.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.ChapterCompleted;

@Repository
public interface ChapterCompletedRepository extends JpaRepository<ChapterCompleted, Integer> {

	@Query("SELECT COUNT(DISTINCT s) FROM ChapterCompleted s WHERE s.subjectId = :subjectId AND s.studentId = :studentId")
	Long countBySubjectIdAndStudentId(@Param("subjectId") Integer subjectId, @Param("studentId") Integer studentId);

	@Query("SELECT c FROM ChapterCompleted c WHERE c.chapterId =:chapterId AND c.studentId =:studentId")
	ChapterCompleted findByChapterAndStudent(@Param("chapterId") Integer chapter,@Param("studentId") Integer student);

}
