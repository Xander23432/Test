package com.realnet.icon.response;

import java.util.List;

import com.realnet.fnd.response.PageResponse;
import com.realnet.icon.entity.FuturisticEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FuturisticResponse extends PageResponse {

	@ApiModelProperty(required = true , value="")
	List <FuturisticEntity> futuristic;
 }
