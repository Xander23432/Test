package com.realnet.fnd.response;
import java.util.List;


import com.realnet.comm.entity.UniversityEntity;
import com.realnet.fnd.response.PageResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UniversityResponse extends PageResponse {
	@ApiModelProperty(required = true, value = "")
	  private List<UniversityEntity> author;


}
