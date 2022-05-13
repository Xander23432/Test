package com.realnet.wfb.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class WireFrameEditDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Header header;
	private Line line;

}
