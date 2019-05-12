package BPlus;

public class mindatatest {
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
     BPlusTree<data,Integer> tree=new BPlusTree<>(3);
    for(int i=5;i>0;i--) {
      data d=new data(i, "test1");
      tree.insert(d, (Integer)d.getA());
    }
    for(int i=15;i>10;i--) {
      data d=new data(i, "test2");
      tree.insert(d,(Integer) d.getA());
    }
    for(int i=10;i>5;i--) {
      data d=new data(i, "test3");
      tree.insert(d, (Integer)d.getA());
    }
    System.out.println("根：");
    System.out.print(tree.root.keyset[0]+" ");
    System.out.print(tree.root.keyset[1]+" ");
    System.out.print(tree.root.keyset[2]+"\n");
    System.out.println("节点：");
    tree.printtree(3);
    System.out.println(tree.search(5).express());
    tree.delete(10);
    tree.search(10);
    tree.delete(15);
    System.out.println("根：");
    System.out.print(tree.root.keyset[0]+" ");
    System.out.print(tree.root.keyset[1]+" ");
    System.out.print(tree.root.keyset[2]+"\n");
    System.out.println("节点：");
    tree.printtree(3);
  }
  
}
