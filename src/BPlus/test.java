package BPlus;

import java.io.File;
import java.nio.channels.DatagramChannel;
import java.util.Date;

public class test {
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    BPlusTree<data,Integer> tree=new BPlusTree<>(50);
    data[] datas=FileUtils.gettxt(new File(".\\src\\data.txt"));
    for(int i=0;i<datas.length;i++) {
      tree.insert(datas[i], (Integer)datas[i].getA());
    }
    
    System.out.println("root:");
    for(int i=0;i<50;i++) {
      System.out.print(tree.root.keyset[i]+" ");
    }
    System.out.println();
    tree.delete(36883);
    System.out.println("search 5:"+tree.search(5).express());
    tree.insert(new data(1000000, "test"), 1000000);
    System.out.println("root:");
    for(int i=0;i<50;i++) {
      System.out.print(tree.root.keyset[i]+" ");
    }
    System.out.println();
    
   // tree.printtree(50);
    FileUtils.splitFile(new File(".\\src\\data.txt"), 1000000, 16);
  }
  
}

