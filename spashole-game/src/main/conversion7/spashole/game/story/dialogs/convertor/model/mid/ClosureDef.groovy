package conversion7.spashole.game.story.dialogs.convertor.model.mid

import conversion7.spashole.game.story.dialogs.convertor.DrawioToDialog
import conversion7.spashole.game.story.dialogs.convertor.Translit
import conversion7.spashole.game.story.dialogs.convertor.model.drawio.MxCell

class ClosureDef {
    String name
    String body
    String codeField

    def ClosureDef(MxCell cell) {
        buildClosureName(cell)
        buildClosureBody(cell)
        codeField = sprintf('Closure %s = \n' +
                '{\n' +
                '       %s\n' +
                '    }\n'
                , name
                , body)
    }

    def buildClosureName(MxCell cell) {
        String rawText = cell.value
        if (rawText.length() > 50) {
            rawText = rawText.substring(0, 50)
        }
        name = DrawioToDialog.toCamelCase(Translit.toTranslit(rawText))
    }

    def buildClosureBody(MxCell cell) {
        body = 'owner.text(' + DrawioToDialog.resourceLink(cell.value) + ')\n'
    }
}
