package team.tnt.collectoralbum.data.boosts;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class SimpleBoostContext implements IBoostContext {

    public static final String PLAYER = "player";
    private final Map<String, Object> paramMap = new HashMap<>();

    public SimpleBoostContext(PlayerEntity player) {
        set(PLAYER, player);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String paramName, Class<T> tClass) {
        Object obj = paramMap.get(paramName);
        if (obj != null && tClass.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }
        return null;
    }

    protected void set(String paramName, Object obj) {
        this.paramMap.put(paramName, obj);
    }
}
