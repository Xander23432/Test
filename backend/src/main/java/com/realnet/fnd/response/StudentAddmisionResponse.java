package com.realnet.fnd.response;
import java.util.List;

import com.realnet.comm.entity.StudentAddmisionEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Data
@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
public class StudentAddmisionResponse extends PageResponse {
	 @ApiModelProperty(required = true, value = "")
	  private List<StudentAddmisionEntity> StudentAddmisionEntity;
}
