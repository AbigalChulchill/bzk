package net.bzk.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import net.bzk.auth.JpaConstant;

@Data
@Entity
@Table(name = "end_user")
public class EndUser {
	

	@Id
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String uid;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID , unique =  true)
	private String accountOid;
	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_SMALL_TEXT )
	private String displayName;
	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_LARGE_TEXT)
    private String avatarUrl;
	
	public EndUser() {
		 uid = RandomStringUtils.randomAlphanumeric(JpaConstant.COLUMN_DEFIN_UID_SIZE);
	}

}
