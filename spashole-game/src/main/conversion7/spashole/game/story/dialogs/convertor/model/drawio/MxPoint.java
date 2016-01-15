
package conversion7.spashole.game.story.dialogs.convertor.model.drawio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MxPoint {

    @SerializedName("x")
    @Expose
    public int x;
    @SerializedName("y")
    @Expose
    public int y;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
