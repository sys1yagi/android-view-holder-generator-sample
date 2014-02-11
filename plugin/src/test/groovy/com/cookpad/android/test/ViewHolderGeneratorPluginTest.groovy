package com.cookpad.android.test

import com.cookpad.android.ViewHolderGenerator
import org.gradle.api.logging.Logger
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

public class ViewHolderGeneratorPluginTest {


    @Test
    public void generateClasses() {
        def projectDir = new File(".test")
        def project = ProjectBuilder.builder().withProjectDir(projectDir).build()

        def gen = new ViewHolderGenerator(project.logger, "com.example.android")

        assert true
    }
}