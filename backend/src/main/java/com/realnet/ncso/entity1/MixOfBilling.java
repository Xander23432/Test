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
public class MixOfBilling {
	private CreditNote creditNote;
	private Billing billing ;
	private List<BillingAttachment> billingAttachments;
	private List<BillingItem> billingItems;
	private List<Object> billingMarine;
	private List<BillingSource> billingSources;
	private List<Object> sordersGkey;
}
