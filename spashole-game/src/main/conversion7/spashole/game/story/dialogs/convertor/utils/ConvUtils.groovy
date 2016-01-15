package conversion7.spashole.game.story.dialogs.convertor.utils

import com.google.common.base.CaseFormat
import conversion7.spashole.game.story.dialogs.convertor.DrawioToDialog
import org.jsoup.Jsoup

class ConvUtils {

    static String getSafeFileName(String name) {
        return name.replaceAll('[^A-Za-z0-9_.]', '_')
    }
    static String getResName(String keyName) {
        return getConstName(keyName)
    }
    static def getConstName(String s) {
        def val = removeHtml(
                CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.UPPER_UNDERSCORE, DrawioToDialog.toCamelCase(s)
                )
        )
        if (val == 'NULL') {
            throw new RuntimeException("Invalid key! Maybe encoding broken! Initial text: " + s);
        }
        return val
    }

    static String removeHtml(String s) {
        return Jsoup.parse(s).text()
    }
}
