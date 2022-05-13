package com.realnet.fnd.entity1;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="SEC_MENU_DET")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuDet {
	@Id
	private Long menuItemId;
	private Long itemSeq;
	private String menuItemDesc;
	private String status;
	private Long menuId;
	private String moduleName;
	private String createby;
	private Date createdate;
	private String updateby;
	private Date updatedate;
}
