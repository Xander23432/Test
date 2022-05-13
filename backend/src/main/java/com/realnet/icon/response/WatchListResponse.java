package com.realnet.icon.response;

import java.util.List;

import com.realnet.fnd.response.PageResponse;
import com.realnet.icon.entity.WatchListEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WatchListResponse extends PageResponse {
	
	@ApiModelProperty(required = true, value = "")
	 private List<WatchListEntity> star;

}
