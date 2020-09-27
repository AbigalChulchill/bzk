package net.bzk.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import net.bzk.data.JpaConstant;
import net.bzk.data.service.CommService;

@SuppressWarnings("serial")
@Entity
@Table(name = "config_row")
@EntityListeners(CommService.class)
public class ConfigRow implements Serializable, CreateUpdateDate {

	private String uid;
	private String name;
	private Date updateAt;
	private Date createAt;

	private String stringVal;
	private int intVal;
	private float floatVal;
	private boolean boolVal;

	@Id
	@Column(nullable = false, unique = true, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_LARGE_TEXT)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_DATE)
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	@Override
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_DATE)
	public Date getCreateAt() {
		return createAt;
	}

	@Override
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_XLARGE_TEXT)
	public String getStringVal() {
		return stringVal;
	}

	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_INT)
	public int getIntVal() {
		return intVal;
	}

	public void setIntVal(int intVal) {
		this.intVal = intVal;
	}

	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_DOUBLE)
	public float getFloatVal() {
		return floatVal;
	}

	public void setFloatVal(float floatVal) {
		this.floatVal = floatVal;
	}

	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_BOOLEAN)
	public boolean isBoolVal() {
		return boolVal;
	}

	public void setBoolVal(boolean boolVal) {
		this.boolVal = boolVal;
	}

}
