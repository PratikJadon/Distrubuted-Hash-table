import java.util.HashMap;
import java.util.Map;

public class DHTNode{
    private int nodeId;
    private Map<Integer,String> keyHash;
    public DHTNode predecessor;
    public DHTNode successor;
    public DHTNode(){}
    public DHTNode(int id){
        this.nodeId = id;
        this.keyHash = new HashMap<>();
    }

    //     Set Key in hash Table of a Node
    public void put(int key,String value){
        keyHash.put(key,value);
    }
    //    Get Key From the Hash table of a node
    public String get(int key){
        return keyHash.get(key);
    }

    public int getNodeId() {
        return nodeId;
    }
    public void printhash(){
        this.keyHash.forEach((key, value) -> System.out.println("------> Key is " + key + " and value is  " + value));
    }
    public Map<Integer,String> getKeyHash(){
        return this.keyHash;
    }
}
