package com.realnet.ncso.entity1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pager {
	private int from;
	private int to;
	private int size;
	private int current;
}	
