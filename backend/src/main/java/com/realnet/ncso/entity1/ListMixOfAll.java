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
public class ListMixOfAll {
	private List<OrderDetails> orderDetails;
	private List<CreditNote> creditNote;
	private List<Billing> billings;
}
