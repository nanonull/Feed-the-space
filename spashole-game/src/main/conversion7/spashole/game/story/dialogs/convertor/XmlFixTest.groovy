package conversion7.spashole.game.story.dialogs.convertor

class XmlFixTest extends GroovyTestCase {

    void testMain(){
        def line = '<mxCell id="1015ed106430be7f-4" value="<div>Мы самая миролюбивая раса в галактике! <span style="line-height: 1.2">Наша физиология - это феномен, загадка и источник проблем...</span></div><div>При одной мысли о нанесении вреда другому живому существу люмен испытывает неимоверную боль вплоть до смерти от болевого шока.<br></div>" style="text;html=1;spacingLeft=4;spacingRight=4;whiteSpace=wrap;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;plain-green" vertex="1" parent="1015ed106430be7f-3">\n' +
                '<mxGeometry y="26" width="270" height="124" as="geometry"/>'

       assert XmlFix.fix(line) == '<mxCell id="1015ed106430be7f-4" value="Мы самая миролюбивая раса в галактике! Наша физиология - это феномен, загадка и источник проблем...При одной мысли о нанесении вреда другому живому существу люмен испытывает неимоверную боль вплоть до смерти от болевого шока." style="text;html=1;spacingLeft=4;spacingRight=4;whiteSpace=wrap;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;plain-green" vertex="1" parent="1015ed106430be7f-3">\n' +
               '<mxGeometry y="26" width="270" height="124" as="geometry"/>'
    }
    void test1(){
        def line = '<mxCell id="left">'
        assert XmlFix.fix(line) == '<mxCell id="left">'
    }

    void test2(){
        def line = '<mxCell id="<remove>left">'
        assert XmlFix.fix(line) == '<mxCell id="left">'
    }

    void test3(){
        def line = '<mxCell id="<remove="remove">left">'
        assert XmlFix.fix(line) == '<mxCell id="left">'
    }
    
}
