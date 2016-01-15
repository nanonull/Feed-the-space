
package conversion7.spashole.game.story.dialogs.convertor.model.drawio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MxGraphModel {

    @SerializedName("pageScale")
    @Expose
    public int pageScale;
    @SerializedName("arrows")
    @Expose
    public int arrows;
    @SerializedName("pageHeight")
    @Expose
    public int pageHeight;
    @SerializedName("pageWidth")
    @Expose
    public int pageWidth;
    @SerializedName("fold")
    @Expose
    public int fold;
    @SerializedName("guides")
    @Expose
    public int guides;
    @SerializedName("gridSize")
    @Expose
    public int gridSize;
    @SerializedName("dx")
    @Expose
    public int dx;
    @SerializedName("dy")
    @Expose
    public int dy;
    @SerializedName("grid")
    @Expose
    public int grid;
    @SerializedName("background")
    @Expose
    public String background;
    @SerializedName("root")
    @Expose
    public Root root;
    @SerializedName("page")
    @Expose
    public int page;
    @SerializedName("math")
    @Expose
    public int math;
    @SerializedName("tooltips")
    @Expose
    public int tooltips;
    @SerializedName("connect")
    @Expose
    public int connect;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
