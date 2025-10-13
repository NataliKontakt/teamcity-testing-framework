package com.example.teamcity.ui;

import com.example.teamcity.BaseTest;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
@Test(groups = {"Regression"})
public class BaseUiTest extends BaseTest {
    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject(){
        // подготовка окружения
        step("Login as user" );

        // взаимодействие с UI
        step("Open create project page (http://localhost:8111/admin/createObjectMenu.html)" );
        step("Send all project parameters (repository url)" );
        step("Click Proceed" );
        step("Fix Project Name and Build Type name values" );
        step("Click Proceed" );

        // проверка состояния API
        //(корректность отправки данных с UI на API)
        step("Check that all entities (project, build type) was successfuly with correct data on API level " );
        // проверка состояния UI
        //(корректность считывания данных и отображения данных на UI)
        step("Check that project is visible on Projects Page (http://localhost:8111/favorite/projects)" );

    }
    @Test(description = "User should not be able to create project without wame", groups = {"Negative"})
    public void userCreatesProjectWithoutName(){
        // подготовка окружения
        step("Login as user" );
        step("Check number of projects");

        // взаимодействие с UI
        step("Open create project page (http://localhost:8111/admin/createObjectMenu.html)" );
        step("Send all project parameters (repository url)" );
        step("Click Proceed" );
        step("Set Project name value is empty" );
        step("Click Proceed" );
        // проверка состояния API
        //(корректность отправки данных с UI на API)
        step("Check that number of projects did not change" );


        // проверка состояния UI
        //(корректность считывания данных и отображения данных на UI)
        step("Check that error appears `Project name must not be empty`" );

    }
}
