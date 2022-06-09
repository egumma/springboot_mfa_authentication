package com.arch.ihcd.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection= TableName.INST_USER_ROLE_XREF)
public class InstanceUserRolesXRef {

    @Id
    private String id;
    private String instId;
    private String userId;
    private String roleId;
    private boolean status = true;

    private String createdby;
    private long createddtm;
    private String modifiedby;
    private long modifieddtm;
}
