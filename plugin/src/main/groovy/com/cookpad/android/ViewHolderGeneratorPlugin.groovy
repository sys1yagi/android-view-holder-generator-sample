package com.cookpad.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class ViewHolderGeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        project.logger.trace("Applying ${this.class} into $project")
        project.configure(project) {
            tasks.whenTaskAdded { task ->
                configureTask(project, task)
            }
        }
    }

    private void configureTask(final Project project, final Task task) {
        def projectFlavorNames = project.android.productFlavors.collect { it.name }
        if (projectFlavorNames.size() == 0) {
            projectFlavorNames.add("")
        }
        project.android.buildTypes.all { build ->
            def buildName = build.name // e.g. "Debug", "Release"
            projectFlavorNames.each { flavorName ->
                def taskAffix = flavorName != "" ? "${flavorName.capitalize()}${buildName.capitalize()}" : buildName.capitalize()
                if (task.name == "generate${taskAffix}Sources") {
                    //def flavorPath = flavorName != "" ? "${flavorName}/${buildName}" : buildName
                    project.logger.trace "Register generate${taskAffix}ViewHolders"

                    def generateViewHoldersTask = project.task("generate${taskAffix}ViewHolders") {
                        description = 'Generates ViewHolder classes from layout XMLs'

                        doLast {
                            generateViewHolders(project)
                        }
                    }
                    task.dependsOn(generateViewHoldersTask)
                }
            }
        }
    }

    public static void generateViewHolders(final Project project) {
        final t0 = System.currentTimeMillis()

        // TODO: do not depend on the magic string "src/main"
        def src = "src/main"

        def xmlFiles = project.fileTree(dir: src, include: "**/layout*/*.xml")

        def manifestXml = new XmlSlurper().parse(project.file("$src/AndroidManifest.xml"))

        def pkg = manifestXml.('@package').toString()

        def targetSourceDir = project.file("${project.buildDir}/source/gen/")
        targetSourceDir.mkdirs()
        project.android.sourceSets.main.java.srcDir(targetSourceDir)

        def gen = new ViewHolderGenerator(project, pkg);
        gen.generate(xmlFiles);
        gen.getCodeModel().build(targetSourceDir)

        println("[${this.getSimpleName()}] generateViewHolders in ${System.currentTimeMillis() - t0} ms.")
    }
}
