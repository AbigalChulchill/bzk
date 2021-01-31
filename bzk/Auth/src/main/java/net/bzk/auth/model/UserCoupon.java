package net.bzk.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import net.bzk.auth.JpaConstant;
import net.bzk.auth.service.CommService;

@Data
@Entity
@Table(name = "user_coupon")
@EntityListeners(CommService.class)
public class UserCoupon implements CreateUpdateDate {
	
	public static enum State{
		Idle,Vaild,Used,
	}
	
	@Id
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String uid;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_MEDIUM_TEXT)
	private State state;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String userOid;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String couponOid;
	private Date updateAt;
	private Date createAt;
	
	public UserCoupon() {
		uid =  RandomStringUtils.randomAlphanumeric(JpaConstant.COLUMN_DEFIN_UID_SIZE);
	}

}
