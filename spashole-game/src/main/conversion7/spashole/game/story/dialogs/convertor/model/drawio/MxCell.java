
package conversion7.spashole.game.story.dialogs.convertor.model.drawio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MxCell {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("parent")
    @Expose
    public String parent;
    @SerializedName("vertex")
    @Expose
    public int vertex;
//    @SerializedName("mxGeometry")
//    @Expose
//    public MxGeometry mxGeometry;
    @SerializedName("style")
    @Expose
    public String style;
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("edge")
    @Expose
    public int edge;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("target")
    @Expose
    public String target;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
