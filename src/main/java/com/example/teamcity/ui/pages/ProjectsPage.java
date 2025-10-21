package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.elements.ProjectElement;
import lombok.Getter;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

public class ProjectsPage extends BasePage{
    private static final String PROJECT_URL = "/favorite/projects";
    private ElementsCollection projectElements = $$("div[class*=`Subproject-module__container`]");
    public static ProjectsPage open(){
        return Selenide.open(PROJECT_URL, ProjectsPage.class);
    }

    public List<ProjectElement> getProjects(){
        return generatePageElements(projectElements, ProjectElement::new);
    }
}
