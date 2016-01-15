package conversion7.spashole.game.story.dialogs.convertor.model.mid

import conversion7.spashole.game.story.dialogs.convertor.DrawioToDialog
import conversion7.spashole.game.story.dialogs.convertor.model.drawio.MxCell
import conversion7.spashole.game.story.dialogs.convertor.model.drawio.Root

class InnerItemDef {
    private MxCell fromCell
    private MxCell parentCell
    String code
    def transition

    def InnerItemDef(MxCell itemCell, MxCell parentCell, Root root) {
        this.parentCell = parentCell
        this.fromCell = itemCell

        int typesDetected = 0
        transition = DrawioToDialog.getTransitionFrom(itemCell, root)
        if (transition) {
            // closure
            typesDetected++
            def targetCell = DrawioToDialog.getTransitionTarget(transition, root)
            code = sprintf '%s.call()\n', DrawioToDialog.closures.get(targetCell).name
        }

        if ('speakOn' == itemCell.value) {
            typesDetected++
            code = 'enableSpeaker()\n'
        } else if ('speakOff' == itemCell.value) {
            typesDetected++
            code = 'disableSpeaker()\n'
        } else if (itemCell.value?.startsWith('customCode')) {
            typesDetected++
            def split = itemCell.value.split(':')
            if (split.length > 1) {
                code = split[1] + '\n'
            } else {
                code = 'customCode\n'
            }
        }

        if (typesDetected > 1) {
            throw new RuntimeException("Detected more than 1 type of inner item on: " + itemCell.id);
        } else if (typesDetected == 0) {
            // just text
            def link = DrawioToDialog.resourceLink(itemCell.value)
            code = 'text(' + link + ')\n'
        }
    }

}
