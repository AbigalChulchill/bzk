package net.bzk.flow.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.bzk.auth.JpaConstant;
import net.bzk.auth.model.JsonPojoConverter;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "saved_flow")
public class SavedFlow implements Serializable {
	@Id
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String uid;
	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_XLARGE_TEXT)
	@Convert(converter = FlowConvert.class)
	private Flow model;

	
	public static class FlowConvert extends JsonPojoConverter<Flow> {
		@Override
		public Class<Flow> getTClass() {
			return Flow.class;
		}
	}
}
