package com.realnet.fnd.response;

import java.util.List;

import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Header;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ActionBuilderResponse extends PageResponse {
	@ApiModelProperty(required = true, value = "")
	private List<Rn_cff_ActionBuilder_Header> items;
}
