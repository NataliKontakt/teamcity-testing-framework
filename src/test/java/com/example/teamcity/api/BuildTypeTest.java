package com.example.teamcity.api;

import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest{
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest(){
        step("Create user");
        step("Create project by user");
        step("Create buildType for project");
        step("Check BuildType was created successfully wich correct data");
    }

    @Test(description = "User should not be able to create two build types with same id", groups = {"Negative", "CRUD"})
    public void userCreateTwosBuildTypesWithTheSameTest(){
        step("Create user");
        step("Create project by user");
        step("Create buildType1 for project");
        step("Create buildType2 with same id as buildType1 for project");
        step("Check BuildType2 was not created with bad request code");
    }
    @Test(description = "Project admin should be able to create build for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreateBuildTypeTest(){
        step("Create user");
        step("Create project");
        step("Grant user PROJECT_ADMIN role in project");

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
