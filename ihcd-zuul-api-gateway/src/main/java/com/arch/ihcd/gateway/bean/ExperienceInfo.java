package com.arch.ihcd.gateway.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceInfo {
    @NotEmpty(message = "Industry Type is required")
    private String industryTypeId;
    @NotEmpty(message = "Role is required")
    private String roleId;
    private String expWithIhcd;
    private String teamExpWithIhcd;
    private String projectSchedule;
}
