package com.github.zzehring.intellijjsonnet.settings

import com.intellij.openapi.project.Project
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.jetbrains.annotations.NotNull
import org.wso2.lsp4intellij.IntellijLanguageClient

class LSPSettingsSync(val project: Project) {


    @NotNull
    fun getExtCodeAsMap(extCode: String): Map<String, String> {
        return         extCode
            .split(',')
            .associateBy(
                keySelector = { s: String -> s.split('=')[0] },
                valueTransform = {
                        s: String -> s.split('=')
                    .getOrElse(1, defaultValue = { "" }) },
            )
    }

    fun sync() {
        val didChangeConfigurationParams = DidChangeConfigurationParams()
        didChangeConfigurationParams.settings = hashMapOf(
            "jpath" to JLSSettingsStateComponent.instance.state.jPaths,
            "show_docstring_in_completion" to true,
            "ext_code" to getExtCodeAsMap(JLSSettingsStateComponent.instance.state.extCode)
        )

        IntellijLanguageClient.didChangeConfiguration(
            didChangeConfigurationParams,
            project
        )
    }
}