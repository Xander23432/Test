package com.realnet.ncso.entity1;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Embeddable
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderAttachmentCompositeKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "orderID", referencedColumnName = "orderID")
	private Long orderId;
	private Long attachmentId;
}
