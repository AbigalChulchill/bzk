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
@Table(name = "payer")
@EntityListeners(CommService.class)
public class Payer implements CreateUpdateDate {
	
	public static enum State{
		Done,Zero
	}
	

	@Id
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String uid; 
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_LARGE_TEXT)
	private String name;
	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String accountOid;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_MEDIUM_TEXT)
	private State state;
	
	private Date updateAt;
	private Date createAt;
	
	public Payer() {
		 uid = RandomStringUtils.randomAlphanumeric(JpaConstant.COLUMN_DEFIN_UID_SIZE);
	}
	

}
