package com.cico.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService detailService;

	@Autowired
	private AuthenticationEntryPoint entryPoint;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private SecurityFilter filter;

	@Bean
	public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder);
		provider.setUserDetailsService(detailService);
		return provider;
	}

	String studentPath[] = { "/student/studentCheckInCheckOutApi", "/student/studentDashboardApi",
			"/student/studentMispunchRequestApi", "/student/studentEarlyCheckoutRequestApi",
			"/student/getStudentCheckInCheckOutHistory", "/student/getStudentProfileApi",
			"/student/studentChangePasswordApi", "/student/updateStudentProfileApi",
			"/student/studentAttendanceMonthFilter", "/student/updateFcmId", "/qr/getLinkedDevice",
			"/qr/getLinkedDeviceByUuid", "/qr/webLogout", "/leave/getLeavesType", "/leave/addStudentLeave",
			"/leave/getStudentLeaves", "/leave/getStudentLeavesById", "/leave/deleteStudentLeave",
			"/leave/retractStudentLeave", "/leave/studentLeaveMonthFilterById", "/leave/studentLeaveMonthFilter" };
	String adminPaths[] = { "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/v3/api-docs",
			"/v2/api-docs", "/webjars/**", "/announcement/publishAnnouncement", "/announcement/getAllAnnouncement",
			"/assignment/createAssignment", "/assignment/addAssignment", "/assignment/addQuestionInAssignment",
			"/assignment/getAllAssignments", "/assignment/deleteTaskQuestion", "/assignment/getAllSubmitedAssginments",
			"/assignment/updateSubmitedAssignmentStatus", "/assignment/getAllSubmissionAssignmentTaskStatus",
			"/assignment/getOverAllAssignmentTaskStatus",
			"/assignment/getAllSubmissionAssignmentTaskStatusByCourseIdFilter", "/assignment/updateAssignmentQuestion",
			"/assignment/activateAssignment", "/assignment/getAllSubmittedAssignmentTask",
			"/assignment/deleteAttachment", "/assignment/addAttachment", "/assignment/addAttachment",
			"/batch/createBatch", "/batch/updateBatch", "/batch/deleteBatch/{batchId}", "/batch/getBatchById/{batchId}",
			"/batch/updateBatchStatus/{batchId}", "/chapter/addChapter", "/chapter/addChapterContent",
			"/chapter/updateChapterContent", "/chapter/updateChapter", "/chapter/deleteChapterContent",
			"/chapter/deleteChapter", "/chapter/updateChapterStatus", "/course/addCourseApi", "/course/updateCourseApi",
			"/course/deleteCourseByIdApi", "/course/getAllNonStarterCourses", "/course/studentUpgradeCourse",
			"/course/getCoureWithBatchesAndSubjects", "/exam/addChapterExam",
			"/exam/getALLChapterExamResultesByChapterIdApi", "/exam/addSubjectExam", "/exam/deleteSubjectExam",
			"/exam/getALLSubjectExamResultesBySubjectId", "/exam/updateSubjectExam", "/exam/changeSubjectExamStatus",
			"/exam/changeChapterExamStatus", "/exam/setSubjectExamStartStatus", "/exam/setChapterExamStartStatus",
			"/fees/createStudentFees", "/fees/feesListApi", "/fees/findByFeesId", "/fees/searchByName",
			"/fees/findFeesByDates", "/fees/feesCompletedList", "/fees/feesPay", "/fees/feesPendingList",
			"/fees/feesPayList", "/fees/findByPayId", "/fees/updateFeesApi", "/fees/getFeesCollectionMonthAndYearWise",
			"/fees/getTotalFeesCollection", "/fees/updateFeesPay", "/fees/searchByNameInFeesPayList",
			"/fees/searchByMonthInFeesPayList", "/technologyStack/createTechnologyStackApi",
			"/technologyStack/updateTechnologyStackApi", "/technologyStack/deleteTechnologyStackApi/{id}",
			"/student/todayAttendanceCountsForAdmin", "/student/getMonthwiseAdmissionCountForYear",

			"/student/getAllStudentData", "/task/addQuestionInTask", "/task/addTaskAttachment",
			"/task/deleteTaskQuestion", "/task/getAllSubmitedTask", "/task/updateSubmitedAssignmentStatus",
			"/getAllSubmissionTaskStatusByCourseIdAndSubjectId", "/task/updateTaskQuestion", "/task/deleteAttachement",
			"/task/activateTask", "/task/createTask",

			"/subject/getAllSubjects", "/subject/addSubject", "/subject/addChapterToSubject", "/subject/updateSubject",
			"/subject/deleteSubject", "/subject/updateSubjectStatus", "/subject/deleteSubjectById",

			"/question/addQuestionToChapter", "/question/addQuestionToSubject", "/question/updateQuestionById",
			"/question/deleteQuestionById", "/question/updateQuestionStatus",

			"/newsEventscreateNewsEvents", "/newsEvents/updateNewsEvents", "/newsEvents/deleteNewsEvents",
			"/newsEvents/activeAndInActiveNewsAndEvent", "/newsEvents/searchNewsAndEvents",

			"/job/createJobApi", "/job/searchJobApi", "/job/activeJobApi", "/job/updateAlertJobApi",
			"/job/deleteJobApi", "/admin/**"

	};

//	String apiPaths[] = { "/admin/adminLoginApi", "/student/studentLoginApi", "/qr/qrGenerator",
//			"/qr/qrlogin/{qrKey}/{token}", "/qr/updateWebLoginStatus", "/socket", "/queue/**", "/file/**",
//			"/resources/**", "/discussionForm/**"
////			"/student/**","/file/**","/leave/**","/job/**","/technologyStack/**","/assignment/**","/course/**",
////			"/newsEvents/**","/qr/**","/resources/**","/socket/**","/queue/**","/batch/**","/fees/**","/subject/**","/chapter/**","/question/**",
////			"/exam/**","/task/**","/discussionForm/**","/announcement/**","*"
//	};
	
	String apiPaths[] = {
			"/file/**", "/resources/**", "/qr/qrGenerator", "/socket/**", "/queue/**", "/student/studentLoginApi",
			"/admin/adminLoginApi", "/qr/qrlogin/{qrKey}/{token}"};

	@Bean
	SecurityFilterChain chain(HttpSecurity security) throws Exception {
		security.csrf().disable().authorizeRequests().antMatchers(apiPaths)
		 .permitAll().antMatchers(adminPaths).hasAuthority("ADMIN").antMatchers(studentPath).hasAuthority("STUDENT")

				.and().exceptionHandling().authenticationEntryPoint(entryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//				.addFilterBefore(corsFilter(), CorsFilter.class)
//				.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		return security.build();
	}

	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		this.registerPublicOrigins(source);
		return source;
	}

	private void registerPublicOrigins(UrlBasedCorsConfigurationSource source) {
		CorsConfiguration publicCorsConfig = new CorsConfiguration();
		publicCorsConfig.addAllowedOrigin("*");
		publicCorsConfig.setAllowedMethods(Arrays.asList("*"));
		publicCorsConfig.setAllowedHeaders(Arrays.asList("Authorization"));
		publicCorsConfig.setAllowCredentials(false);
		source.registerCorsConfiguration("/**", publicCorsConfig);
	}

}
