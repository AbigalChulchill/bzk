package net.bzk.flow.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;
import net.bzk.auth.model.CreateUpdateDate;
import net.bzk.auth.model.JsonPojoConverter;
import net.bzk.auth.service.CommService;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;

@SuppressWarnings("serial")
@Data
@Entity
@EntityListeners(CommService.class)
@Table(indexes = {
        @Index(columnList = "createAt"),
})
public class ArchiveRun implements Serializable, CreateUpdateDate {
    @Id
    @Column(nullable = false)
    private String flowRunUid;

    private String flowUid;

    private Date updateAt;
    private Date createAt;
    @Column(nullable = true, columnDefinition = "TEXT")
    @Convert(converter = RunInfoConverter.class)
    private RunInfo info;
    @Enumerated(EnumType.STRING)
    private FlowRuner.State state;

    public static class RunInfoConverter extends JsonPojoConverter<RunInfo> {

        @Override
        public Class<RunInfo> getTClass() {
            return RunInfo.class;
        }

    }


}
