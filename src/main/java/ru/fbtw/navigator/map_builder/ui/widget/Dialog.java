package ru.fbtw.navigator.map_builder.ui.widget;

import javafx.scene.Parent;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Dialog {

    Parent getLayout();

    String getCssName();

    String getTitle();

}
