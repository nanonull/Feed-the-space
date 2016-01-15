
package conversion7.spashole.game.story.dialogs.convertor.model.drawio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MxGeometry {

//    @SerializedName("Array")
//    @Expose
//    public conversion7.spashole.game.story.quests.convertor.model.Array Array;
    @SerializedName("as")
    @Expose
    public String as;
    @SerializedName("x")
    @Expose
    public double x;
    @SerializedName("y")
    @Expose
    public double y;
//    @SerializedName("mxPoint")
//    @Expose
//    public MxPoint_ mxPoint;
    @SerializedName("relative")
    @Expose
    public int relative;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
