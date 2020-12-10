package ru.fbtw.navigator.map_builder.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.fbtw.navigator.map_builder.core.navigation.Node;
import ru.fbtw.navigator.map_builder.core.navigation.NodeType;

import java.util.ArrayList;

public class GraphJsonSerializerTest {
	private Gson gson;
	private Node testNode;
	private ArrayList<Node> testNodes;

	@Before
	public void init() {
		gson = new Gson();
		testNodes = new ArrayList<>();

		testNode = new Node(10, 100, "test", "some expr", NodeType.DESTINATION);
		Node nodeA = new Node(100, 100, "name", "some expr", NodeType.TEMP);
		Node nodeB = new Node(100, 50, "name A", "some expr", NodeType.TEMP);
		Node nodeC = new Node(100, 40, "name P", "some expr", NodeType.ZONE_CONNECTION);

		Node.makeConnection(testNode,nodeA);
		Node.makeConnection(testNode,nodeB);
		Node.makeConnection(testNode,nodeC);
		Node.makeConnection(testNode,testNode);

		testNodes.add(testNode);
		testNodes.add(nodeA);
		testNodes.add(nodeB);
		testNodes.add(nodeC);

	}


	@Test
	public void nodeTest() {
		printTest("node");
		try {
			GraphJsonSerializer serializer = GraphJsonSerializer.getInstance();

			JsonElement nodeJson = serializer.nodeToJsonEl(testNode);
			System.out.println(gson.toJson(nodeJson));

		}catch (Exception ex){
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void nodeArrTest() {
		printTest("node array");
		try {
			GraphJsonSerializer serializer = GraphJsonSerializer.getInstance();

			JsonArray nodeJson = serializer.nodesToJsonEl(testNodes);
			System.out.println(gson.toJson(nodeJson));
		}catch (Exception ex){
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void nodeConnectionsTest() {
		printTest("node connections array");
		try {
			GraphJsonSerializer serializer = GraphJsonSerializer.getInstance();

			JsonArray nodeJson = serializer.nodeConnectionsToJson(testNodes);
			System.out.println(gson.toJson(nodeJson));

			Assert.assertEquals(3,nodeJson.size());
		}catch (Exception ex){
			Assert.fail(ex.getMessage());
		}
	}





	private void printTest(String name){
		System.out.printf("\nTest %s:\n",name);
	}
}
