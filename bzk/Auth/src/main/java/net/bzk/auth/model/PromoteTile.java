package net.bzk.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.bzk.auth.JpaConstant;
import net.bzk.auth.service.CommService;

@Data
@Entity
@Table(name = "promote_tile")
@EntityListeners(CommService.class)
public class PromoteTile  implements CreateUpdateDate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_ID)
	private long id;
	
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String billOid;
	
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_SMALL_TEXT)
	private String title;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_SMALL_TEXT)
	private String subTitle;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_SMALL_TEXT)
	private String description;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_SMALL_TEXT)
	private String topText;
	
	private Date updateAt;
	private Date createAt;

}
