package ge.freeuni.jack.language.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import ge.freeuni.jack.language.JackIcons
import javax.swing.Icon

class JackColorSettingsPage : ColorSettingsPage {

    companion object {
        val DESCRIPTORS = arrayOf(
            AttributesDescriptor("KEYWORD", JackSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("IDENTIFIER", JackSyntaxHighlighter.IDENTIFIER),
        )

    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Jack"
    }

    override fun getIcon(): Icon {
        return JackIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return JackSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return "field int nino;"
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? {
        return null
    }
}