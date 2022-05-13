package com.realnet.comm.response;

import java.util.List;

import com.realnet.comm.entity.OrderInfo;
import com.realnet.fnd.response.PageResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class OrderInfoResponse extends PageResponse {
  @ApiModelProperty(required = true, value = "")
  private List<OrderInfo> items;
}
