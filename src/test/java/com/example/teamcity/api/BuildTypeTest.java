package com.example.teamcity.api;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest{
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest(){
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(),
                "Build type name is not correct");
    }

    @Test(description = "User should not be able to create two build types with same id", groups = {"Negative", "CRUD"})
    public void userCreateTwosBuildTypesWithTheSameTest(){
        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()),BuildType.class, testData.getBuildType().getId());

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());



        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("errors[0].message", Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));
    }
    @Test(description = "Project admin should be able to create build for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreateBuildTypeTest(){
        /*
        Документация
        https://www.jetbrains.com/help/teamcity/rest/serverauthsettings.html#perProjectPermissions
        {
  "emailVerification" : false,
  "collapseLoginForm" : false,
  "guestUsername" : "guest",
  "perProjectPermissions" : true,
  "welcomeText" : "welcomeText",
  "allowGuest" : false,
  "modules" : {
    "module" : [ {
      "name" : "name",
      "properties" : {
        "count" : 1,
        "property" : [ {
          "inherited" : true,
          "name" : "name",
          "type" : "type...",
          "value" : "value"
        } ],
        "href" : "href"
      }
    } ]
  },
  "buildAuthenticationMode" : "strict"
}
        http://localhost:8111/admin/action.html
        POST
        -ufd-teamcity-ui-role
Project administrator
role
PROJECT_ADMIN
projectId
CloudStorage
roleScope
perProject
_replaceRoles
assignRoles
Assign
tc-csrf-token
60d295b7-c303-44c9-b978-6538767acec6
rolesHolderId
71
         */

        step("Create user PROJECT_ADMIN");
        //superUserCheckRequests.getRequest(USERS).create(testData.getUser(), User.class, testData.getUser().getRoles().getRole().add("PROJECT_ADMIN"););
        step("Create project by user (PROJECT_ADMIN)");
        //step("Grant user PROJECT_ADMIN role in project");

        step("Create buildType for project by user (PROJECT_ADMIN)");
        step("Check BuildType was created successfully");
    }

    @Test(description = "Project admin should not be able to create build for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreateBuildTypeForAnotherUserProjectTest(){
        step("Create user1");
        step("Create project1");
        step("Grant user1 PROJECT_ADMIN role in project1");

        step("Create user1");
        step("Create project1");
        step("Grant user1 PROJECT_ADMIN role in project1");


        step("Create buildType for project1 by user2");
        step("check BuildType2 was not created with forbidden code");
    }


}
