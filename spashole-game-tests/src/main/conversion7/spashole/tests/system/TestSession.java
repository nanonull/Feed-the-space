package conversion7.spashole.tests.system;

import conversion7.gdxg.core.interfaces.MapInterfaceExt;
import conversion7.gdxg.core.utils.Utils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TestSession implements MapInterfaceExt<String, Object> {
    private static final Logger LOG = Utils.getLoggerForClass();
    private Map<String, Object> storage = new HashMap<>();

    @Override
    public Object get(String key) {
        Object o = storage.get(key);
        LOG.info("get [{}] == [{}]", key, String.valueOf(o));
        return o;
    }

    @Override
    public void put(String key, Object value) {
        LOG.info("put [{}] == [{}]", key, String.valueOf(value));
        storage.put(key, value);
    }

    @Override
    public void remove(String key) {
        Object o = storage.remove(key);
        LOG.info("remove [{}] == [{}]", key, String.valueOf(o));
    }

}
