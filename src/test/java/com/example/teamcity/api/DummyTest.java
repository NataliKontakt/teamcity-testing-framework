package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class DummyTest extends BaseApiTest{
    @Test
    public void userShouldBeAbleGetAllProjects() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .spec(Specifications.getSpec().authSpec(User.builder()
                        .user("administrator")
                        .password("administrator")
                        .build()))
                .get("/app/rest/projects");

    }
}
