import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class DHT{
    //    Head stored as one node that cannot be deleted
    private DHTNode head;
    private static DHT instance;
    //    Stores baseNode number and also used for creating other node numbers as (pervNodeNumber + 1) * baseNode
    private int baseNode;

    //    Private Constructor to implement Singleton pattern
    private DHT(int baseNode){
        this.baseNode = baseNode;
        this.head = new DHTNode(baseNode);
        this.head.successor = head;
        this.head.predecessor = head;
    }

    //    Handles provisioning of single instance of the class
    public static synchronized DHT getInstance(int baseNode) {
        if (instance == null) {
            instance = new DHT(baseNode);
        }
        return instance;
    }

    //  Function to insert a new Node to Distributed Hash table
    public void insertNode(){
        DHTNode newNode = new DHTNode(head.predecessor.getNodeId() + this.baseNode);
        newNode.successor = head;
        newNode.predecessor = head.predecessor;
        head.predecessor.successor = newNode;
        head.predecessor = newNode;
        System.out.println("Node inserted " + newNode.getNodeId());
    }

    //    Function to print nodes (checking purposes only)
    public void printNode(){
        if(head != null){
            DHTNode newNode = head;
            do{
                System.out.println(newNode.getNodeId() + " " + newNode.predecessor.getNodeId() + " " + newNode.successor.getNodeId());
                newNode.printhash();
                newNode = newNode.successor;
            }while(newNode != head);
        }
    }

    //    Function for searching a key in hash table of a node
    public String searchKey(String keey){
        int key = gethash(keey);
        String value = new String();
        DHTNode newNode = head;
        do{
            if((newNode.getNodeId() < key && (newNode.successor.getNodeId() >= key || newNode.successor.getNodeId() == this.baseNode)) || (newNode.getNodeId() == this.baseNode  && key <= this.baseNode)){
                value = newNode.get(key);
                if(value == null) break;
                System.out.println("Key Found " + key + " to node " + newNode.getNodeId()/this.baseNode + " with value -> ' " + value + " '" );
                return value;
            }else{
                newNode = newNode.successor;
            }

        }while(newNode != head);
        System.out.println("Key " + keey + " not found.");
        return "Not Found";
    }

    //    Function for inserting a key in hash table of a node
    public void insertKey(String keey, String value){
        int key = gethash(keey);
        DHTNode newNode = head;
        do{
            if((newNode.getNodeId() < key && (newNode.successor.getNodeId() >= key || newNode.successor.getNodeId() == this.baseNode)) || (newNode.getNodeId() == this.baseNode  && key <= this.baseNode)){
                newNode.put(key,value);
                System.out.println("Key inserted " + key + " to node " + newNode.getNodeId()/this.baseNode );
                break;
            }else{
                newNode = newNode.successor;
            }

        }while(newNode != head);

    }

    //    Function for deleting a node in distributed hash table
    public void deleteNode(int nodeNumber){
        DHTNode newNode = head;
        Map<Integer,String> temphash;
        DHTNode newloc;
        if(nodeNumber == head.getNodeId()){
            System.out.println("Head or default node cannot be deleted.");
            return;
        }
        do{
            if(newNode.getNodeId() == nodeNumber){
                newNode.successor.predecessor = newNode.predecessor;
                newNode.predecessor.successor = newNode.successor;
                temphash = newNode.getKeyHash();
                newloc = newNode.predecessor;
                temphash.forEach(newloc::put);
                newNode.successor = null;
                newNode.predecessor = null;
                System.out.println("Node Deleted " + nodeNumber + " and Hash transferred to " + newloc.getNodeId());
                break;
            }else{
                newNode = newNode.successor;
            }
        }while(newNode != head);
    }

    //    Function creates a unique hash key from file names for storing in hash table of a node
    private int gethash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Extract the first 4 bytes (32 bits) and convert to an integer
            int result = 0;
            for (int i = 0; i < 4; i++) {
                result = (result << 8) | (hashBytes[i] & 0xFF);
            }

            return result%567;
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception, e.g., log it or throw a custom exception
            e.printStackTrace();
            return 0;
        }
    }

    //    Function to handle insertion of N nodes in Distributed Hash table
    public void insertNnodes(int n,DHT dh){
        for(int i=0;i<n;i++){
            dh.insertNode();
        }
    }
}