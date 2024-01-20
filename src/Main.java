class Main{
    public static void main(String[] args) {
        int base = 100;
//        Single Instance of Distributed Hash table class, ( based on Singleton Pattern )
        DHT dh = DHT.getInstance(base);
        dh.insertNnodes(6,dh);
        dh.insertKey("asda","Hello There");
        dh.insertKey("file7.txt","Pratik");
        dh.insertKey("Valorant","Rivansh");
        dh.insertKey("C","High");
        dh.searchKey("valorant");
        dh.deleteNode(1 * base);
        dh.deleteNode(2 * base);
        dh.printNode();
    }
}