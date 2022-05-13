package com.realnet.ncso.entity1;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MixOfAll {
	private OrderDetails orderDetails;
	private List<OrderItemEntity> orderItemEntity;
	private List<OrderAttachment> orderAttachment;
	private Billing billing;
	private CreditNote crediteNote;
}
