package conversion7.spashole.game.story.dialogs.convertor

import org.apache.commons.io.FileUtils

class DrawioXmlParser {

    static String XML_WARNING = 'This XML file does not appear to have any style information associated with it. The document tree is shown below.'

    static String read(File xmlFile) {
        def fix = XmlFix.fix(xmlFile.text.replaceAll(XML_WARNING, '').replaceAll('&nbsp;', ' '))
        if (fix.contains('<object')) {
            throw new RuntimeException("Unknown <object> tag, fix your diagram!");
        }
        FileUtils.write(new File(xmlFile.getPath() + '.fixed.xml'), fix)
        return fix

    }

}
