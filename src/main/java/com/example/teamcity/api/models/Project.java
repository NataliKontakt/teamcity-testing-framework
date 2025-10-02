package com.example.teamcity.api.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseModel{
    private String id;
    private String name;
    @Builder.Default
    private String locator = "_Root";

}
