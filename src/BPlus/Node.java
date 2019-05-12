package BPlus;

public abstract class Node<T,V extends Comparable<V> > {
  //���ڵ�
  protected Node<T, V> fNode;
  //�ӽڵ㼯��
  protected Node<T,V>[] sNodelist;
  //����ڵ���Ŀ
  protected int num;
  //�ӽڵ�ļ�ֵ
  protected Object[] keyset;
  //���ڵ���
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

