package com.realnet.icon.response;

import java.util.List;

import com.realnet.fnd.response.PageResponse;
import com.realnet.icon.entity.ArchivedEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArchivedResponse  extends PageResponse{

	@ApiModelProperty(required = true, value="")
	private List<ArchivedEntity> archivedEntity;
}
