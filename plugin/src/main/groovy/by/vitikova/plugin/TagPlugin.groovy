package by.vitikova.plugin

import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.constant.PushExtension
import by.vitikova.plugin.service.GitService
import org.gradle.api.Plugin
import org.gradle.api.Project

class TagPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create(Constant.PUSH_TAG, PushExtension)
        project.tasks.register(Constant.PUSH_TAG, GitService) {
            group = Constant.GIT
            doFirst {
                logger.warn "GO!"
            }
        }
    }
}