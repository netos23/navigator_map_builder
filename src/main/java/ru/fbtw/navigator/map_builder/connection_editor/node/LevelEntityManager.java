package ru.fbtw.navigator.map_builder.connection_editor.node;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.connection_editor.ConnectionEditorProperties;
import ru.fbtw.navigator.map_builder.connection_editor.LayersName;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.navigation.LevelConnection;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;

import java.util.ArrayList;
import java.util.HashSet;

public class LevelEntityManager {
    private static final int offset = 200;
    private static final double WIDTH = 10;

    private ArrayList<LevelEntity> levelEntities;
    private HashSet<LevelConnectionEntity> levelConnections;
    private ConnectionEditorProperties properties;
    private Pane[] layers;

    private Project project;
    private HashSet<LevelConnection> connectionsManager;

    public LevelEntityManager(
            Project project,
            ConnectionEditorProperties properties
    ) {
        this.project = project;
        this.properties = properties;

        levelEntities = new ArrayList<>();
        levelConnections = new HashSet<>();
        layers = properties.getSource().getLayers();
        connectionsManager = project.getConnections();

        initLevelEntities();
    }

    private void initLevelEntities() {
        ArrayList<LevelNode> nodeSystem = project.getNodeSystem();
        for (LevelNode level : nodeSystem) {
            levelEntities.add(new LevelEntity(level));
        }

        setDefaultPositions();
        try {
            reBuildConnections();
        } catch (Exception e) {
            e.printStackTrace();
            //todo лог об ошибке
        }
    }

    private LevelEntity getByLevelNode(LevelNode node) {
        return levelEntities.stream()
                .filter(levelEntity -> levelEntity.getNode().equals(node))
                .findFirst()
                .orElse(null);
    }

    private void reBuildConnections() throws Exception {
        for (LevelConnection connection : project.getConnections()) {
            LevelEntity nodeA = getByLevelNode(connection.getNodeA());
            LevelEntity nodeB = getByLevelNode(connection.getNodeB());

            if (nodeA == null || nodeB == null) {
                throw new Exception("Level rebuild error");
            }

            String socketIdA = connection.getSocketA().getHashName();
            String socketIdB = connection.getSocketB().getHashName();

            Line line = new Line(
                    nodeA.getSocketByName(socketIdA).getCenterX(),
                    nodeA.getSocketByName(socketIdA).getCenterY(),
                    nodeB.getSocketByName(socketIdB).getCenterX(),
                    nodeB.getSocketByName(socketIdB).getCenterY()
            );
            line.setStrokeWidth(WIDTH);

            layers[LayersName.CONNECTIONS].getChildren().add(line);


            LevelConnectionEntity levelConnectionEntity = new LevelConnectionEntity(
                    line, nodeA, socketIdA,
                    nodeB, socketIdB,
                    connection
            );
            levelConnections.add(levelConnectionEntity);

            nodeA.addConnection(levelConnectionEntity);
            nodeB.addConnection(levelConnectionEntity);
        }
    }

    private void setDefaultPositions() {
        final int size = levelEntities.size();
        int count = (int) Math.round(Math.sqrt(size));

        for (int i = 0; i < size; i++) {
            LevelEntity node = levelEntities.get(i);

            final double baseOffset = offset + node.getR();

            double x = (i % count == 0)
                    ? baseOffset
                    : levelEntities.get(i - 1).getX() + baseOffset + levelEntities.get(i - 1).getR();

            double y = (i < count)
                    ? baseOffset
                    : levelEntities.get(i - count).getY() + baseOffset + levelEntities.get(i - count).getR();

            node.setPosition(x, y);
            node.splitLayers(layers);
        }
    }


    public Pane[] getLayers() {
        return layers;
    }

    public LevelEntity select(double x, double y) {
        return levelEntities.stream()
                .filter(lvl -> lvl.contains(x, y))
                .findFirst()
                .orElse(null);
    }

    public LevelConnectionEntity selectConnection(double x, double y) {
        return levelConnections.stream()
                .filter(connection -> connection.contains(x, y))
                .findFirst()
                .orElse(null);
    }


    public void push(LevelConnectionEntity connection) {
        levelConnections.add(connection);
    }

    public void remove(LevelConnectionEntity connection) {
        levelConnections.remove(connection);
    }

    public HashSet<LevelConnection> getConnectionsManager() {
        return connectionsManager;
    }
}
