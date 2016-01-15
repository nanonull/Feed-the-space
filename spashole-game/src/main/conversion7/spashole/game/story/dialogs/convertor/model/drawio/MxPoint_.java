
package conversion7.spashole.game.story.dialogs.convertor.model.drawio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MxPoint_ {

    @SerializedName("as")
    @Expose
    public String as;
    @SerializedName("y")
    @Expose
    public double y;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
