package ru.fbtw.navigator.map_builder.canvas;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.node.NodeConnectTool;
import ru.fbtw.navigator.map_builder.canvas.node.NodeHolder;
import ru.fbtw.navigator.map_builder.canvas.node.NodeHolderManager;
import ru.fbtw.navigator.map_builder.canvas.node.NodeTool;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.canvas.tools.*;
import ru.fbtw.navigator.map_builder.core.navigation.Node;
import ru.fbtw.navigator.map_builder.utils.StringUtils;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CanvasController {
    public static String[] drawingToolsNames = new String[]{
            StringUtils.toolToString(LineTool.class),
            StringUtils.toolToString(RectangleTool.class),
            StringUtils.toolToString(EllipseTool.class),
            StringUtils.toolToString(CircleTool.class)
    };
    public static String[] settingsToolNames = new String[]{
            StringUtils.toolToString(RemoveTool.class),
            StringUtils.toolToString(ResizeTool.class),
            StringUtils.toolToString(ReplaceTool.class),
            StringUtils.toolToString(StrokeTool.class),
            StringUtils.toolToString(WidthTool.class),
            StringUtils.toolToString(FillTool.class),
            StringUtils.toolToString(InfoTool.class),
    };

    public static String[] nodeToolNames = new String[]{
            StringUtils.toolToString(NodeTool.class),
            StringUtils.toolToString(NodeConnectTool.class)
    };

    public Tool[] tools;
    private Pane[] layers;
    private Pane inputLayer;
    private ProbeManager probeManager;
    private CanvasProperties properties;

    private HolderManager holderManager;

    public CanvasController(CanvasProperties properties, ArrayList<Node> nodeSystem) {
        this.properties = properties;
        this.properties.setSource(this);


        layers = new Pane[5];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Pane();
        }
        inputLayer = layers[LayersName.INPUT_LAYER];

        probeManager = new ProbeManager(this);
        NodeHolderManager nodeHolderManager = new NodeHolderManager(nodeSystem);
        holderManager = new HolderManager(probeManager, properties, nodeHolderManager);

        tools = new Tool[]{
                new LineTool(holderManager),
                new RectangleTool(holderManager),
                new EllipseTool(holderManager),
                new CircleTool(holderManager),
                new RemoveTool(holderManager),
                new ResizeTool(holderManager),
                new ReplaceTool(holderManager),
                new StrokeTool(holderManager),
                new WidthTool(holderManager),
                new FillTool(holderManager),
                new InfoTool(holderManager),
                new NodeTool(holderManager),
                new NodeConnectTool(holderManager)
        };


        setOnClicks();
    }

    private void setOnClicks() {

        inputLayer.setOnMousePressed(event ->
                tools[properties.getTool()].onPressed(event.getX(), event.getY())
        );

        inputLayer.setOnMouseDragged(event -> {
            Vector2 curPos = probeManager.getPosOfExistingTempPoint(event);
            tools[properties.getTool()].onDragged(curPos.getX(), curPos.getY());

        });

        inputLayer.setOnMouseReleased(event ->
                tools[properties.getTool()].onReleased(event.getX(), event.getY())
        );


    }


    public void setBackground(Image bg) {
        enableBackground(true);

        ImageView imageView = new ImageView(bg);
        layers[0].getChildren().add(imageView);
        for (Pane layer : layers) {
            layer.setMinSize(imageView.getX(), imageView.getY());
        }
    }

    public void enableBackground(boolean value) {
        layers[0].setVisible(value);
    }

    public Pane[] getLayers() {
        return layers;
    }


    @Deprecated
    public ProbeManager getProbeManager() {
        return probeManager;
    }

    public List<Holder> getHolders() {
        return holderManager.getHolders();
    }

    public List<Probe> getProbes() {
        return probeManager.getProbes();
    }

    public void setProbes(Iterable<Probe> probes) {
        probes.forEach(probe -> probeManager.push(probe));
    }

    public void setHolders(Iterable<Holder> holders) {
        holders.forEach(holder -> {
            holderManager.push(holder);
            layers[LayersName.DECORATION].getChildren().add(holder.getShape());
        });
    }

    public void setNodes(Map<String, Node> nodeMap, Map<String, String> connections) {
        NodeHolderManager nodeManager = holderManager.getNodeManager();

        nodeMap.values().forEach(node -> {
            NodeHolder holder = new NodeHolder(node);
            nodeManager.push(holder);
            holder.splitLayers(layers);
        });

        NodeConnectTool connectTool = (NodeConnectTool) tools[12];
        ReplaceTool replaceTool = (ReplaceTool) tools[6];
        // setup node connections
        for (Map.Entry<String, String> entry : connections.entrySet()) {
            String nameA = entry.getKey();
            String nameB = entry.getValue();

            Node a = nodeMap.get(nameA);
            Node b = nodeMap.get(nameB);

            connectTool.onPressed(a.getX(), a.getY());
            connectTool.onReleased(b.getX(), b.getY());

            replaceTool.onPressed(a.getX(), a.getY());
            replaceTool.onReleased(a.getX(), a.getY());

            replaceTool.onPressed(b.getX(), b.getY());
            replaceTool.onReleased(b.getX(), b.getY());
        }
    }
}
