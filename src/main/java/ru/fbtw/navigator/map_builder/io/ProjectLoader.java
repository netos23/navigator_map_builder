package ru.fbtw.navigator.map_builder.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.holder.*;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.core.Level;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.fbtw.navigator.map_builder.io.HolderSerializeType.*;

public class ProjectLoader {

    private JsonObject root;

    public ProjectLoader(File file) throws FileNotFoundException {
        Reader reader = new FileReader(file);
        root = JsonParser.parseReader(reader)
                .getAsJsonObject();
    }

    public ProjectLoader(String json) {
        root = JsonParser.parseString(json)
                .getAsJsonObject();
    }

    public Project load() throws Exception {
        JsonArray levelsJson = root.getAsJsonArray("levels");
        List<Level> levels = parseLevels(levelsJson);
        Project project = new Project();
        project.setLevels(levels);
        return project;
    }

    private List<Level> parseLevels(JsonArray levelsJson) throws Exception {
        List<Level> levels = new ArrayList<>();
        int id = 0;
        for (JsonElement data : levelsJson) {
            JsonObject levelRoot = data.getAsJsonObject();

            String imageBase64 = levelRoot.get("image").getAsString();
            Image backGround = ImageUtils.fromBase64(imageBase64);

            JsonArray probesJson = levelRoot.get("probes").getAsJsonArray();
            Map<String, Probe> probes = parseProbes(probesJson);

            JsonArray holdersJson = levelRoot.get("holders").getAsJsonArray();
            List<Holder> holders = parseHolders(holdersJson, probes);

            Level level = new Level(id, CanvasProperties.DEFAULT_PROPERTIES);
            level.setBackground(backGround);
            level.getController().setProbes(probes.values());
            level.getController().setHolders(holders);
            levels.add(level);
            id++;
        }

        return levels;
    }

    private List<Holder> parseHolders(JsonArray holdersJson, Map<String, Probe> probes)
            throws Exception {
        List<Holder> holders = new ArrayList<>();
        for (JsonElement data : holdersJson) {
            JsonObject holderJson = data.getAsJsonObject();
            int type = holderJson.get("type").getAsInt();

            switch (type) {
                case LINE:
                    holders.add(parseLine(holderJson, probes));
                    break;
                case RECTANGLE:
                    holders.add(parseRectangle(holderJson, probes));
                    break;
                case CIRCLE:
                    holders.add(parseCircle(holderJson, probes));
                    break;
                case ELLIPSE:
                    holders.add(parseEllipse(holderJson, probes));
                    break;
                default:
                    throw new Exception("Unsupported type");
            }
        }
        return holders;
    }

    private Holder parseEllipse(JsonObject holderJson, Map<String, Probe> probes) throws Exception {
        double x = holderJson.get("x").getAsDouble();
        double y = holderJson.get("y").getAsDouble();

        double rx = holderJson.get("rX").getAsDouble();
        double ry = holderJson.get("rY").getAsDouble();

        Ellipse shape = new Ellipse(x, y, rx, ry);

        setupPaint(holderJson, shape);
        setupFill(holderJson, shape);

        JsonArray attachedProbesJson = holderJson.get("probes").getAsJsonArray();
        Probe[] attachedProbes = getAttachedProbes(attachedProbesJson, probes);

        if (attachedProbes.length != 1) {
            throw new Exception("Wrong or broken data");
        }

        return new EllipseHolder(shape, attachedProbes[0]);
    }

    private void setupFill(JsonObject holderJson, Shape shape) {
        String fill = holderJson.get("fill").getAsString();
        Color fillColor = Color.valueOf(fill);
        shape.setFill(fillColor);
    }

    private void setupPaint(JsonObject holderJson, Shape shape) {
        double strokeWidth = holderJson.get("strokeWidth").getAsDouble();
        String stroke = holderJson.get("stroke").getAsString();

        Color strokeColor = Color.valueOf(stroke);

        shape.setStroke(strokeColor);
        shape.setStrokeWidth(strokeWidth);
    }

    private Probe[] getAttachedProbes(JsonArray attachedProbesJson, Map<String, Probe> probes) {
        Probe[] result = new Probe[attachedProbesJson.size()];
        for (int i = 0; i < attachedProbesJson.size(); i++) {
            String id = attachedProbesJson.get(i).getAsString();
            result[i] = probes.get(id);
        }

        return result;
    }

    private Holder parseCircle(JsonObject holderJson, Map<String, Probe> probes) throws Exception {
        double x = holderJson.get("x").getAsDouble();
        double y = holderJson.get("y").getAsDouble();
        double r = holderJson.get("r").getAsDouble();

        Circle shape = new Circle(x, y, r);

        setupFill(holderJson,shape);
        setupPaint(holderJson,shape);

        JsonArray attachedProbesJson = holderJson.get("probes").getAsJsonArray();
        Probe[] attachedProbes = getAttachedProbes(attachedProbesJson, probes);

        if (attachedProbes.length != 1) {
            throw new Exception("Wrong or broken data");
        }
        // custom constructor
        return new CircleHolder(shape, attachedProbes[0]);
    }

    private Holder parseRectangle(JsonObject holderJson, Map<String, Probe> probes) throws Exception {
        double x = holderJson.get("x").getAsDouble();
        double y = holderJson.get("y").getAsDouble();

        double width = holderJson.get("width").getAsDouble();
        double height = holderJson.get("height").getAsDouble();

        Rectangle shape = new Rectangle(x,y,width,height);

        setupPaint(holderJson,shape);
        setupFill(holderJson,shape);

        JsonArray attachedProbesJson = holderJson.get("probes").getAsJsonArray();
        Probe[] attachedProbes = getAttachedProbes(attachedProbesJson, probes);

        if (attachedProbes.length != 4) {
            throw new Exception("Wrong or broken data");
        }
        return new RectangleHolder(shape,attachedProbes);
    }

    private Holder parseLine(JsonObject holderJson, Map<String, Probe> probes) throws Exception {
        double startX = holderJson.get("startX").getAsDouble();
        double startY = holderJson.get("startY").getAsDouble();
        double endX = holderJson.get("endX").getAsDouble();
        double endY = holderJson.get("endY").getAsDouble();

        Line shape = new Line(startX,startY,endX,endY);
        setupPaint(holderJson,shape);

        JsonArray attachedProbesJson = holderJson.get("probes").getAsJsonArray();
        Probe[] attachedProbes = getAttachedProbes(attachedProbesJson, probes);

        if (attachedProbes.length != 2) {
            throw new Exception("Wrong or broken data");
        }
        return new LineHolder(shape,attachedProbes[0],attachedProbes[1]);
    }

    private Map<String, Probe> parseProbes(JsonArray probesJson) {
        Map<String, Probe> res = new HashMap<>();
        for (JsonElement data : probesJson) {
            JsonObject probeJson = data.getAsJsonObject();

            int x = probeJson.get("x").getAsInt();
            int y = probeJson.get("y").getAsInt();
            String id = probeJson.get("id").getAsString();

            res.put(id, new Probe(x, y, id));
        }

        return res;
    }

}