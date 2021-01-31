package net.bzk.auth.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.auth.JpaConstant;

@Data
@Entity
@Table(name = "end_user_each_map")
public class EndUserEachMap implements CreateUpdateDate {

	@EmbeddedId
	private EndUserEachMapId id;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_BOOLEAN)
	private boolean introduced;
	
	private Date updateAt;
	private Date createAt;

	@SuppressWarnings("serial")
	@Data
	@EqualsAndHashCode
	@Embeddable
	@AllArgsConstructor
	public static class EndUserEachMapId implements Serializable {
		@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
		private String ownUid;
		@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
		private String toMapUid;

	}

}
