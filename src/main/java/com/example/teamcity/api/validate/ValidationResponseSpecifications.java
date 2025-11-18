package com.example.teamcity.api.validate;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.hc.core5.http.HttpStatus;
import org.hamcrest.Matchers;

public class ValidationResponseSpecifications {
    public static ResponseSpecification checkBuildTypesWitIdAlreadyExist(String buildTypeId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody("errors[0].message", Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(buildTypeId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification insufficientPermissionsToEditProject(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_FORBIDDEN);
        responseSpecBuilder.expectBody("errors[0].message", Matchers.containsString("You do not have enough permissions to edit project with id: %s"
                .formatted(projectId)));
        return responseSpecBuilder.build();
    }


}
