package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.interfaces.Expression;
import org.bukkit.entity.Player;

public class ExpressionPermission implements Expression<Object, String> {

    @Override
    public boolean test(Object player, String string2) {
        return ((Player) player).hasPermission(string2);
    }
}
