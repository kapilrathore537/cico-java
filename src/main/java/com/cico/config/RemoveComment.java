package com.cico.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RemoveComment {
	private Integer id;
	private String type;
	private  Integer discussionFormId;
    private Integer commentId;
	 @Override
	    public String toString() {
	        return "{\"id\":" + id + ", "
	                + "\"type\":\"" + type + "\", "
	                + "\"type\":\"" + type + "\", "
	                +"\"commentId\":" +commentId  +", "
	                + "\"discussionFormId\":" + discussionFormId + "}";
	    }


}
