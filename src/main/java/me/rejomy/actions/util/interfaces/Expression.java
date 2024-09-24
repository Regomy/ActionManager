package me.rejomy.actions.util.interfaces;

public interface Expression<First, Second> {
    boolean test(First first, Second second);
}
