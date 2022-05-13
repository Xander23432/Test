package com.realnet.department.response;
import java.util.List;
import com.realnet.department.entity.Departmententity;
import com.realnet.fnd.response.PageResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Data
@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
public class Departmentresponse extends PageResponse {
	@ApiModelProperty(required = true, value = "")
	  private List<Departmententity> departmententity;
}
