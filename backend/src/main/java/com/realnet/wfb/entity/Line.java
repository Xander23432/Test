package com.realnet.wfb.entity;

import java.util.List;

import lombok.Data;

@Data
public class Line {
	private List<Section> section;
	private List<Button> button;
}
