package eu.glitch.mywarehouse.utils

import java.util.Locale

object LanguageUtils {

    private val CZECH = "cs"
    private val SLOVAK = "sk"

    fun isLanguageCzech(): Boolean {
        val locale = Locale.getDefault().language
        return locale.equals(CZECH) || locale.equals(SLOVAK)
    }

}
