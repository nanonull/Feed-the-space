package conversion7.spashole.game.story.dialogs.convertor.model.mid

import conversion7.spashole.game.story.dialogs.convertor.Translit
import conversion7.spashole.game.story.dialogs.convertor.utils.ConvUtils

class ResourceDef {
    String resourceGetter
    String keyName
    String resourceProperty
    String resourceField
    static final int NAME_LIMIT = 54

    ResourceDef(String resPrefix, String resFullText) {
        def resKey = Translit.toTranslit(resFullText)
        if (resKey.length() > NAME_LIMIT) {
            resKey = resKey.substring(0, NAME_LIMIT)
        }
        keyName = ConvUtils.getConstName(resKey)
        def resName = ConvUtils.getResName(resPrefix + ' ' + keyName)
        resourceProperty = resName + '=' + resFullText
        resourceField = 'public static final ResKey ' + keyName + ' = new ResKey("' + resName + '");'
        if (resFullText.contains('%s')) {
            resourceGetter = 'SpasholeAssets.textResources.get(' + keyName + ', formatArgs)'
        } else {
            resourceGetter = 'SpasholeAssets.textResources.get(' + keyName + ')'
        }
    }

    @Override
    String toString() {
        return keyName + '\n' +
                resourceProperty + '\n' +
                resourceField + '\n' +
                resourceGetter
    }
}
