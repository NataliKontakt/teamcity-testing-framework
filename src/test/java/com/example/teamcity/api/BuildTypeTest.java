package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.validate.ValidationResponseSpecifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());

        softy.assertEquals(createdBuildType, testData.getBuildType());
/*        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(),
                "Build type name is not correct");*/
    }

    @Test(description = "User should not be able to create two build types with same id", groups = {"Negative", "CRUD"})
    public void userCreateTwosBuildTypesWithTheSameTest() {
        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then().spec(ValidationResponseSpecifications
                        .checkBuildTypesWitIdAlreadyExist(testData.getBuildType().getId()));

    }

    @Test(description = "Project admin should be able to create build for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreateBuildTypeTest() {
        var user = generate(User.class, "PROJECT_ADMIN");
        var requesterUser = new CheckedBase<User>(Specifications.superUserSpec(), USERS);
        requesterUser.create(user);

        var project = generate(Project.class);
        AtomicReference<String> projectId = new AtomicReference<>("");

        var requesterProject = new CheckedBase<Project>(Specifications.authSpec(user), Endpoint.PROJECTS);
        projectId.set(requesterProject.create(project).getId());

        var buildType = generate(BuildType.class);
        buildType.setProject(Project.builder().id(projectId.get()).locator(null).build());

        var requesterBuildType = new CheckedBase<BuildType>(Specifications.authSpec(user), Endpoint.BUILD_TYPES);
        AtomicReference<String> buildTypeId = new AtomicReference<>("");

        buildTypeId.set(requesterBuildType.create(buildType).getId());

        var createdBuildType = requesterBuildType.read(buildTypeId.get());
        softy.assertEquals(createdBuildType, testData.getBuildType());

        //softy.assertEquals(buildType.getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "Project admin should not be able to create build for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreateBuildTypeForAnotherUserProjectTest() {
        var user1 = generate(User.class, "PROJECT_ADMIN");
        String password = user1.getPassword();
        var requesterUser1 = new CheckedBase<User>(Specifications.superUserSpec(), USERS);
        //сохраняем полученный объект, чтобы потом достать id и поменять поле scope
        user1 = requesterUser1.create(user1);
        //сохраняем пароль, т.к. в ответе он приходит = null
        user1.setPassword(password);
        System.out.println(user1.toString());
        //сохраняем в финальную переменную, т.к. authSpec не принимает просто user1
        final var projectAdmin1 = user1;

        var project1 = generate(Project.class);
        AtomicReference<String> project1Id = new AtomicReference<>("");

        var requesterProject1 = new CheckedBase<Project>(Specifications.authSpec(projectAdmin1), Endpoint.PROJECTS);
        project1Id.set(requesterProject1.create(project1).getId());


        projectAdmin1.getRoles().getRole().get(0).setScope("p:" + project1Id.get());
        requesterUser1.update(projectAdmin1.getId(), projectAdmin1);

        var user2 = generate(User.class, "PROJECT_ADMIN");
        String password2 = user2.getPassword();
        var requesterUser2 = new CheckedBase<User>(Specifications.superUserSpec(), USERS);
        user2 = requesterUser2.create(user2);
        user2.setPassword(password2);
        System.out.println(user2.toString());
        final var projectAdmin2 = user2;

        var project2 = generate(Project.class);
        AtomicReference<String> project2Id = new AtomicReference<>("");
        var requesterProject2 = new CheckedBase<Project>(Specifications.authSpec(projectAdmin2), Endpoint.PROJECTS);
        project2Id.set(requesterProject2.create(project2).getId());

        projectAdmin2.getRoles().getRole().get(0).setScope("p:" + project2Id.get());
        requesterUser2.update(projectAdmin2.getId(), projectAdmin2);

        var buildType = generate(BuildType.class);
        buildType.setProject(Project.builder().id(project1Id.get()).locator(null).build());

        new CheckedBase<BuildType>(Specifications.authSpec(projectAdmin2), Endpoint.BUILD_TYPES);
        new UncheckedBase(Specifications.authSpec(projectAdmin2), BUILD_TYPES)
                .create(buildType)
                .then().spec(ValidationResponseSpecifications
                        .insufficientPermissionsToEditProject(project1Id.get()));

    }

}
