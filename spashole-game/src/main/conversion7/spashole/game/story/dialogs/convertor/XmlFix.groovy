package conversion7.spashole.game.story.dialogs.convertor

class XmlFix {

    static String fix(String line) {
        boolean braketOpened = false
        boolean leftScobaOpened = false
        def fixed = ''
        line.each { s ->
            if (braketOpened) {
                if (s == '"') {
                    if (!leftScobaOpened) {
                        braketOpened = !braketOpened
                    }
                }

                if (s == '<') {
                    leftScobaOpened = true
                }

                if (s == '>') {
                    leftScobaOpened = false
                    return
                }

                if (leftScobaOpened) {
                    return
                }

                fixed += s

            } else {
                if (s == '"') {
                    braketOpened = !braketOpened
                }
                fixed += s
            }

        }
        return fixed
    }

}
