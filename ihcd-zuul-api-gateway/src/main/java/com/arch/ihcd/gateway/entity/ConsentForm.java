package com.arch.ihcd.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection= TableName.IHCD_CONSENT_FORM)
public class ConsentForm {
    @Id
    private String id;
    private String consentId;
    @NotEmpty(message="Instance id is required")
    private String instId;
    @NotEmpty(message="ConsentForm Name is required")
    private String cfName;
    @NotEmpty(message="ConsentForm Desc is required")
    private String cfDesc;
    private String imageLocation;

    private String createdby;
    private long createddtm;
    private String modifiedby;
    private long modifieddtm;
    private boolean active = true;
}
