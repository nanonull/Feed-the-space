
package conversion7.spashole.game.story.dialogs.convertor.model.drawio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Root {

    @SerializedName("mxCell")
    @Expose
    public List<MxCell> mxCell = new ArrayList<MxCell>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
