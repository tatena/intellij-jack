package ge.freeuni.jack.editor.snippets

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class JackTemplateContext: TemplateContextType("JACK_STATEMENT", "Jack") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        
        return templateActionContext.file.name.endsWith(".jack")
    }
}