package com.cico.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Message {
    private String name;
    private String text;
    private Date time;
}

