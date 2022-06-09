package com.arch.ihcd.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//ToDo: move to master-data-service
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection= TableName.INSTANCES_REF)
public class InstancesRef {

    @Id
    private String id;
    private String instId;
    private String instName;
    private String instDesc;
    private String instPremiumType = "Free"; //Free or Paid ..etc todo
    private int instTotalLicenses = 25;
    private int instLoadedLicenses = 0;
    private boolean status = true;

    private String createdby;
    private long createddtm;
    private String modifiedby;
    private long modifieddtm;
}
