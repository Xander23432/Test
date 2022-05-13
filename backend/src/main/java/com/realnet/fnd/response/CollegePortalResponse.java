package com.realnet.fnd.response;
import java.util.List;
import com.realnet.comm.entity.CollegeStudentEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Data
@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
public class CollegePortalResponse extends PageResponse {
	
	  @ApiModelProperty(required = true, value = "")
	  private List<CollegeStudentEntity> CollegeStudentEntity;
	
	
	
	
}