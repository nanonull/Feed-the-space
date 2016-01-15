package conversion7.spashole.game;

import conversion7.gdxg.core.utils.Utils;
import conversion7.gdxg.core.utils.stream_gobler.StreamGobbler;
import conversion7.gdxg.core.utils.stream_gobler.StreamGobblerResponseHandler;
import conversion7.spashole.game.story.dialogs.convertor.model.mid.ResourceDef;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

class Repl extends StreamGobbler {
    private static final Logger LOG = Utils.getLoggerForClass();
    private static final String RES_CMD = "r ";
    private static List<ResourceDef> collectedResources = new ArrayList<>();
    private static final String RR_CMD = "rr";

    Repl() {
        super(System.in, "REPL");
    }

    public static void main(String[] args) {
        Repl repl = new Repl();
        repl.setResponseHandler(new StreamGobblerResponseHandler() {

            @Override
            public void run(String line) {
                if (line.equals("test")) {
                    handled(repl.type + ": It works");

                } else if (line.startsWith(RES_CMD)) {
                    // resource
                    handled(printResource(line.replaceAll("^" + RES_CMD, "")));

                } else if (line.equals(RR_CMD)) {
                    // reprint && resource
                    if (repl.isMultiLineMode()) {
                        repl.setMultiLineMode(false);
                        handled(reprintResource(repl.flushMultiLinesAsString(true)));
                    } else {
                        repl.setMultiLineMode(true);
                        handled("Input your multi-line resource and repeat '" + RR_CMD + "' cmd:");
                    }

                } else if (line.equals("cr") || line.equals("nr")) {
                    // clear resources || new class resources
                    collectedResources.clear();
                    handled("Resources cleared");

                } else if (line.equals("pcr")) {
                    // print collected resources && clear resources
                    StringBuilder resourceProps = new StringBuilder();
                    StringBuilder resourceFields = new StringBuilder();
                    StringBuilder resourceGetters = new StringBuilder();
                    for (ResourceDef resourceDef : collectedResources) {
                        resourceProps.append(resourceDef.getResourceProperty()).append("\n");
                        resourceFields.append(resourceDef.getResourceField()).append("\n");
                        resourceGetters.append(resourceDef.getResourceGetter()).append("\n");
                    }

                    handled(
                            "\n" + resourceProps.toString() +
                                    "\n" + resourceFields.toString() +
                                    "\n" + resourceGetters.toString() + "\n"
                    );
                    collectedResources.clear();
                }
            }

            @Override
            public void handled(String respMsg) {
                super.handled(respMsg);
                LOG.info(repl.type + ": " + respMsg);
            }

            @Override
            public void unhandled(String line) {
                LOG.error("Unhandled line: {}", line);
            }
        });
        repl.setDaemon(false);
        repl.start();
    }

    private static String printResource(String value) {
        ResourceDef resourceDef = new ResourceDef("", value);
        collectedResources.add(resourceDef);
        return String.valueOf("\n" + String.valueOf(resourceDef));
    }

    private static String reprintResource(String value) {
        String reformVal = "";
        for (String vline : value.split("\n")) {
            reformVal += vline.replaceAll("^\\s*[\"|']?", "")
                    .replaceAll("[\"|']?\\s*\\+?$", "");
        }
        return printResource(reformVal.replaceAll("\\\\n$", ""));
    }
}
