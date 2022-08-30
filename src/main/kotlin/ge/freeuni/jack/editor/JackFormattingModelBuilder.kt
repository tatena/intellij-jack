package ge.freeuni.jack.editor

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import ge.freeuni.jack.language.JackLanguage
import ge.freeuni.jack.language.psi.JackTypes


class JackFormattingModelBuilder: FormattingModelBuilder {
    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, JackLanguage.INSTANCE)
            .before(JackTypes.LBRACE).spaceIf(true)
            .after(JackTypes.LBRACE).lineBreakInCode()
            .around(JackTypes.RBRACE).lineBreakInCode()
            .after(JackTypes.SEMICOLON).lineBreakInCode()
            .before(JackTypes.SEMICOLON).spaceIf(false)
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                formattingContext.containingFile,
                JackFormattingBlock(
                    formattingContext.node,
                    mySpacingBuilder = createSpaceBuilder(codeStyleSettings)
                ),
                codeStyleSettings
            )
    }
}