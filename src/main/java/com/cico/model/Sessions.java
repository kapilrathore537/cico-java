package com.cico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Sessions {
	@Id
	private Integer id;
	@ElementCollection
	@CollectionTable
	private List<String> session = new ArrayList<>();
}
