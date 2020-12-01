package ru.fbtw.navigator.map_builder.canvas.node;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.tools.Tool;
import ru.fbtw.navigator.map_builder.core.navigation.Node;

public class NodeConnectTool extends Tool {

	private NodeHolder targetA;
	private Line line;
	private Pane[] layers;

	public NodeConnectTool(HolderManager manager) {
		super(manager);
		layers = manager.getCanvasProperties()
				.getSource()
				.getLayers();
	}

	@Override
	public void onPressed(double x, double y) {
		targetA = manager.getNodeManager()
				.selectHolder(x, y);

		if(targetA != null){
			line = new Line(targetA.getX(),targetA.getY(),targetA.getX(),targetA.getY());
			line.setStrokeWidth(NodeConnectionHolder.WIDTH);

			layers[LayersName.NODE_CONNECTIONS].getChildren()
					.add(line);
		}
	}

	@Override
	public void onDragged(double x, double y) {
		if(targetA != null && line != null){
			NodeHolder tmp = manager.getNodeManager()
					.selectHolder(x, y);

			if(tmp == null){
				line.setEndX(x);
				line.setEndY(y);
			}else{
				line.setEndX(tmp.getX());
				line.setEndY(tmp.getY());
			}
		}
	}

	@Override
	public void onReleased(double x, double y) {
		if(targetA != null && line != null){
			NodeHolder targetB = manager.getNodeManager()
					.selectHolder(x, y);

			if(targetB != null){
				NodeConnectionHolder connectionHolder =
						new NodeConnectionHolder(line,targetA,targetB);

				Node.makeConnection(targetA.getTarget(),targetB.getTarget());
				manager.getNodeManager().push(connectionHolder);
			}else {
				layers[LayersName.NODE_CONNECTIONS]
						.getChildren()
						.remove(line);
			}
		}
	}
}
