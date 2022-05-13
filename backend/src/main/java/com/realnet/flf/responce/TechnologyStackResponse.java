package com.realnet.flf.responce;

import java.util.List;


import com.realnet.flf.entity.Rn_Bcf_Technology_Stack;
import com.realnet.fnd.response.PageResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TechnologyStackResponse extends PageResponse {
  @ApiModelProperty(required = true, value = "")
  private List<Rn_Bcf_Technology_Stack> items;
}
