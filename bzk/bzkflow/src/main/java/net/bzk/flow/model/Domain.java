package net.bzk.flow.model;

import lombok.Data;
import net.bzk.flow.model.var.VarMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "domain")
public class Domain {

    @Id
    private long id;

    @Indexed(unique = true)
    private String domain;

    private boolean displayAds;


    private VarMap flowVar;




}