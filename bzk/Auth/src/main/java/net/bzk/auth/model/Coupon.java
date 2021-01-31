package net.bzk.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import net.bzk.auth.JpaConstant;
import net.bzk.auth.service.CommService;

@Data
@Entity
@Table(name = "coupon")
@EntityListeners(CommService.class)
public class Coupon implements CreateUpdateDate {

	@Id
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String uid;
	
	//State 直接用 Shopline 的 State
	
	private Date updateAt;
	private Date createAt;
	
	public Coupon() {
		uid = RandomStringUtils.randomAlphanumeric(JpaConstant.COLUMN_DEFIN_UID_SIZE);
	}
	
}
