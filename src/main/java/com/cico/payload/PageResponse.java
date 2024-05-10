package com.cico.payload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
	
	private List<T> response;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean last;

	
	public List<T> getResponse() {
		return response == null ? null : new ArrayList<>(this.response);
	}
	
	public void setResponse(List<T> response) {
		if(response == null) {
			this.response = null;
		}else {
			this.response = response;
		}
	}
	
	
}