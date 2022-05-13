package com.realnet.invoicetyperule.response;
import java.util.List;
import com.realnet.fnd.response.PageResponse;
import com.realnet.invoicetyperule.entity.invoicetyperuleentity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Data
@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
public class invoicetyperuleresponse extends PageResponse {
	@ApiModelProperty(required = true, value = "")
	  private List<invoicetyperuleentity> invoiceentity;
}
