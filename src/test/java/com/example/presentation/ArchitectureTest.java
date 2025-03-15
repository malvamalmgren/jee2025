package com.example.presentation;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {
    static JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example");

    @Test
    void presentationLayerShouldOnlyDependOnBusinessLayer() {

        ArchRule rule = noClasses().that().resideInAPackage("..presentation..")
                .should().dependOnClassesThat().resideInAPackage("..persistence..")
                .because("Presentation should only depend on business layer");

        rule.check(importedClasses);
    }

    @Test
    void businessLayerShouldOnlyDependOnPersistenceLayer() {
        ArchRule rule = noClasses().that().resideInAPackage("..business..")
                .should().dependOnClassesThat().resideInAPackage("..presentation..")
                .because("Business should only depend on presentation layer");

        rule.check(importedClasses);
    }

    @Test
    void onlyClassesNamedRepositoryAllowedInPersistencePackage() {
        ArchRule rule =
                classes().that()
                        .haveSimpleNameEndingWith("Repository")
                        .should().resideInAPackage("..persistence..")
                        .because("repository belongs to persistence layer");

        rule.check(importedClasses);
    }
}
