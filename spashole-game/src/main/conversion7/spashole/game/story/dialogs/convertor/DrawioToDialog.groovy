package conversion7.spashole.game.story.dialogs.convertor

import conversion7.spashole.game.story.dialogs.convertor.model.drawio.DrawioSrc
import conversion7.spashole.game.story.dialogs.convertor.model.drawio.MxCell
import conversion7.spashole.game.story.dialogs.convertor.model.drawio.Root
import conversion7.spashole.game.story.dialogs.convertor.model.mid.ClosureDef
import conversion7.spashole.game.story.dialogs.convertor.model.mid.InnerItemDef
import conversion7.spashole.game.story.dialogs.convertor.model.mid.ResourceDef
import conversion7.spashole.game.story.dialogs.convertor.utils.ConvUtils
import conversion7.spashole.game.utils.SpasholeUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.json.XML

import java.time.Instant

class DrawioToDialog {

    static String dialogName
    static File convertorFolder = new File(new File('').absoluteFile,
            '\\src\\main\\conversion7\\spashole\\game\\story\\dialogs\\convertor\\')

    public static void main(String[] args) {
        DrawioSrc drawioSrc = loadFromXml('1.xml')
        prepareGraph(drawioSrc)
        findDialogName(drawioSrc)
        buildClosures(drawioSrc)
        buildQuestSrc(drawioSrc)
        printWriteQuestSrcHistoryLog(dialogName, printQuestSrc())
        println '\n\n=============== \n'+dialogName + ' was generated!'
    }

    static def printWriteQuestSrcHistoryLog(String name, String text) {
        def histiryFolder = new File(convertorFolder, 'history')
        histiryFolder.mkdirs()
        FileUtils.write(new File(histiryFolder
                , ConvUtils.getSafeFileName(name + '_' + Instant.now().toString().replaceAll('\\..*$','')))
                , text)
    }

    static def prepareGraph(DrawioSrc drawioSrc) {
        drawioSrc.mxGraphModel.root.mxCell.each { cell ->
            if (cell.value)
                cell.value = ConvUtils.removeHtml(cell.value)
        }
    }

    static def buildClosures(DrawioSrc drawioSrc) {
        drawioSrc.mxGraphModel.root.mxCell.each { cell ->
            if (isSharedClosure(cell)) {
                println 'SharedClosure ' + cell.id
                closures.put(cell, new ClosureDef(cell))
            }
        }
    }

    static String printQuestSrc() {
        def builder = new StringBuilder()
        builder.append '\n===== RESOURCES:\n\n'
        resources.each { s ->
            builder.append s
            builder.append '\n'
        }

        builder.append '\n===== SCRIPT CLASS:\n\n'
        builder.append '    // RESOURCE KEYS:'
        builder.append '\n'
        resourceKeys.each { keyName ->
            builder.append 'static final ResKey ' + keyName + ' = new ResKey(\'' + getResName(keyName) + '\')'
            builder.append '\n'
        }
        builder.append '    // STATES:'
        builder.append '\n'
        statesDef.each { s ->
            builder.append s
            builder.append '\n'
        }
        builder.append '    // CLOSURES:'
        builder.append '\n'
        closures.each { cl ->
            builder.append cl.value.codeField
            builder.append '\n'
        }

        builder.append '\n    /** DIALOG STATES DEFINITION MAP */'
        builder.append '@Override\n' +
                '    protected Map<Object, List> getQuestStateMap() {\n' +
                '        return ['
        stateMapDef.each { s ->
            builder.append s
            builder.append '\n'
        }
        builder.append '        ]\n' +
                '    }'

        def string = builder.toString()
        println string
        return string
    }

    static def findDialogName(DrawioSrc drawioSrc) {
        def props = drawioSrc.mxGraphModel.root.mxCell.find { cell ->
            return isDiagramProps(cell)
        }
        assert props
        dialogName = ConvUtils.getConstName(props.value)
    }

    static DrawioSrc loadFromXml(String path) {
        def srcFolderFile = new File(convertorFolder, 'sources')
        def xmlFile = new File(srcFolderFile, path)
        def xml = DrawioXmlParser.read(xmlFile)
        def jSONObject = XML.toJSONObject(xml)
        def json = jSONObject.toString()
        FileUtils.write(new File(xmlFile.getPath() + '.json'), json)
        return SpasholeUtils.GSON.fromJson(json, DrawioSrc)
    }


    static List statesDef = []
    static List stateMapDef = []
    static Set<String> resourceKeys = []
    static Set resources = []
    static Map<MxCell, ClosureDef> closures = [:]

    static void buildQuestSrc(DrawioSrc drawioSrc) {
        drawioSrc.mxGraphModel.root.mxCell.each { cell ->
            if (cell.value
                    && !isDiagramProps(cell)
                    && !isSharedClosure(cell)
                    && !isInnerItem(cell)
                    && !isEndCellInBranch(cell, drawioSrc.mxGraphModel.root)) {
                if (!isInitialState(cell.value)) {
                    statesDef.add(sprintf('static final UUID %s = UUID.randomUUID()',
                            getStateName(cell.value)))
                }
                stateMapDef.add(getStateCode(cell, drawioSrc))

            } else {
                println 'Skip ' + cell.id
            }
        }
    }

    static boolean isSharedClosure(MxCell mxCell) {
        return mxCell.style?.contains('shape=process')
    }

    static String getStateName(String s) {
        ConvUtils.getConstName(Translit.toTranslit(s + '_STATE'))
    }

    static def isDiagramProps(MxCell mxCell) {
        return mxCell.style?.contains('Gear')
    }

    static boolean isInnerItem(MxCell mxCell) {
        return !(mxCell.parent in ['0', '1'])
    }

    static boolean isEndCellInBranch(MxCell cell, Root root) {
        MxCell cellWithTrans = root.mxCell.find { c ->
            if (c.source == cell.id) return true
        }
        return !cellWithTrans
    }

    static def getStateCode(MxCell startCell, DrawioSrc drawioSrc) {

        String optionsDef = (stateMapDef.isEmpty() ? '' : ', ') +
                '(' +
                (isInitialState(startCell.value)
                        ? 'QUEST_INIT_STATE'
                        : getStateName(startCell.value)) +
                '):\n' +
                '   [\n' +
                '   {\n'

        optionsDef += callInnerItems(startCell, drawioSrc.mxGraphModel.root)
        optionsDef += callStateTransitions(startCell, drawioSrc.mxGraphModel.root)

        optionsDef += '    }\n' +
                '    ]\n\n'
        return optionsDef
    }


    static String callInnerItems(MxCell onCell, Root root) {
        def innerCode = ''
        findInnerRows(onCell, root).each { inner ->
            innerCode += inner.code
        }

        return innerCode
    }

    static def isInitialState(String startName) {
        return 'start'.equalsIgnoreCase(startName)
    }

    static String callStateTransitions(MxCell startCell, Root root) {
        List<MxCell> transitionArrowCells = []
        root.mxCell.each { c ->
            if (c.source == startCell.id) transitionArrowCells.add(c)
        }

        def text = ''
        transitionArrowCells.each { transitionArrowCell ->
            def transitionToCell = root.mxCell.find { c2 ->
                if (c2.id == transitionArrowCell.target) return true
            }

            if (transitionArrowCell.value) {
                text += 'if (condition) {\n'
            }

            if ('end'.equalsIgnoreCase(transitionToCell.value)) {
                text += 'option(QUEST_OPTION_EXIT)\n'
            } else {
                def optResLink = resourceLink(ConvUtils.removeHtml(transitionToCell.value))
                if (isEndCellInBranch(transitionToCell, root)) {
                    def innerCode = ''
                    findInnerRows(transitionToCell, root).each { inner ->
//                        if (!inner.transition) { // filter closures
                        innerCode += inner.code
//                        }
                    }

                    text += sprintf('option(' + optResLink + ', {\n' +
                            '   skipComputeState()\n' +
                            innerCode +
                            '          })\n'
                    )
                } else {
                    def stateTarg = getStateName(transitionToCell.value)
                    text += sprintf('option(%s, {\n' +
                            '    newState(%s)\n' +
                            '    })\n'
                            , optResLink, stateTarg)
                }
            }

            if (transitionArrowCell.value) {
                text += '}\n'
            }
        }

        return text
    }

    static def resourceLink(String resFullText) {
        def resourceDef = new ResourceDef(dialogName, resFullText)
        if (resourceKeys.add(resourceDef.keyName)) {
            resources.add(resourceDef.resourceProperty)
        }
        return resourceDef.resourceGetter
    }

    static String getResName(String keyName) {
        return ConvUtils.getConstName(dialogName + ' ' + keyName)
    }

    static def findInnerRows(MxCell parentCell, Root root) {
        List<InnerItemDef> inner = []
        root.mxCell.each { other ->
            if (other.parent == parentCell.id) inner.add(new InnerItemDef(other, parentCell, root))
        }
        return inner
    }


    static String getStateHeader(MxCell cell, Root root) {
        String header = ''
        root.mxCell.each { other ->
            if (cell.id == other.parent) {
                def string = resourceLink(ConvUtils.removeHtml(other.value))
                header += 'text(' + string + ')\n'
            }
        }
        return header
    }


    public static String toCamelCase(final String inText) {
        String text = inText;
        if (text == null) {
            return "null";
        }
        // convert bad symbols to spaces
        text = text.replaceAll("[^a-zA-Z0-9]", " ");
        // no numbers on start
        text = text.replaceAll("^\\d*", "");
        // split by upper-case symbols to do not break existing camel-case
        text = text.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");

        String validatedText = text.trim();
        if (validatedText.isEmpty()) {
            return "null";
        }
        String[] parts = validatedText.split(" ");
        StringBuilder camelCaseBuilder = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                String proceedPart = StringUtils.lowerCase(part);
                if (camelCaseBuilder.length() > 0) {
                    proceedPart = StringUtils.capitalize(proceedPart);
                }
                camelCaseBuilder.append(proceedPart);
            }
        }
        return camelCaseBuilder.toString();
    }

    static MxCell getTransitionFrom(MxCell from, Root root) {
        return root.mxCell.find({ other ->
            return (from.id == other.source)
        })
    }

    static MxCell getTransitionTarget(MxCell arrowItem, Root root) {
        return root.mxCell.find { o ->
            return arrowItem.target == o.id
        }
    }
}
