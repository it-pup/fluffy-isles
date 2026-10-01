import org.gradle.kotlin.dsl.get
import org.gradle.plugins.ide.idea.model.IdeaModel
import pet.itpuppy.tasks.CommandFileTask

plugins {
    java
    idea
}

val commandTask = tasks.register<CommandFileTask>("generateCommandFile") {
    maxDepth = 4
    packageName = "pet.itpuppy.utils.command.dsl"
}

extensions.configure<IdeaModel>("idea") {
    this.module.generatedSourceDirs.add(commandTask.flatMap {
        it.output.asFile
    }.get())
}

tasks.named("compileKotlin") {
    dependsOn(commandTask)
    mustRunAfter(commandTask)
}


sourceSets {
    main {
        extensions.configure<SourceDirectorySet>("kotlin") {
            this.srcDirs(commandTask.map {
                it.output
            })
        }
    }
}

