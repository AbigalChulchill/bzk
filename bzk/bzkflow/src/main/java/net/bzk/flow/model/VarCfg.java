package net.bzk.flow.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.bzk.auth.model.CreateUpdateDate;
import net.bzk.auth.service.CommService;
import net.bzk.flow.dto.ConvertInfra.VarMapConvert;
import net.bzk.flow.model.var.VarMap;

@SuppressWarnings("serial")
@Data
@Entity
@EntityListeners(CommService.class)
@Table
public class VarCfg implements Serializable, CreateUpdateDate {
	@Id
	@Column(nullable = false)
	private String name;
	private String description;
	private String sha256;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	@Convert(converter = VarMapConvert.class)
	private VarMap content;
	
	private Date updateAt;
	private Date createAt;

}
