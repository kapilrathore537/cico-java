package com.cico.payload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCalenderResponse {
	List<Integer> present = new ArrayList<>();
	List<Integer> leaves = new ArrayList<>();
	List<Integer> absent = new ArrayList<>();
	List<Integer>earlyCheckOut = new ArrayList<>();
	List<Integer>mispunch = new ArrayList<>();
}
