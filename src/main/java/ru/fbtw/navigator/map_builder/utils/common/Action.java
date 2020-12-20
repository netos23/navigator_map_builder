package ru.fbtw.navigator.map_builder.utils.common;

@FunctionalInterface
public interface Action {
    void execute();

    default void andThen(Action action){ }
}
