package BPlus;

public abstract class Node<T,V extends Comparable<V> > {
  //父节点
  protected Node<T, V> fNode;
  //子节点集合
  protected Node<T,V>[] sNodelist;
  //自身节点数目
  protected int num;
  //子节点的键值
  protected Object[] keyset;
  //最大节点数
  protected int max;
  public Node(int max) {
    this.max=max;
    this.sNodelist=new Node[max];
    this.keyset=new Object[max];
    this.num=0;
    this.fNode=null;
  }
  public abstract T search(V key);
  public abstract Node<T,V> insert(T value,V key);
  public abstract Node<T, V> delete(V key);
  public abstract LeafNode<T,V> refreshPointer();
}

