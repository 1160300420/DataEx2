package BPlus;

public class BPlusTree <T,V extends Comparable<V>>{
  //���ڵ���
  private int max;
  //BPlusTree�Ľ�
  private int index;
  //���ڵ�
  public Node<T,V> root;
  //����ڵ�
  private LeafNode<T,V> leftnode;
  //��ʱ�ӽڵ����Ŀ
  private int current_son_num;
  //Ĭ�Ϲ������Ϊ1
  public BPlusTree() {
    this(1);
  }
  public BPlusTree(int num) {
    this.index=num;
    //���й����п��ܳ��ֳ��������������ʱ���з���
    this.max=this.index+1;
    this.root=new LeafNode<T,V>(this.max);
    this.current_son_num=0;
    this.leftnode=null;
  }
  //����
  public T search(V key) {
    T t=this.root.search(key);
    if(t==null) {
      System.out.println("�ýڵ㲻���ڣ�");
    }
    return t;
  }
  //����
  public void insert(T value,V key) {
    if(key!=null&&value!=null) {
      Node<T, V> node=this.root.insert(value, key);
      if(node!=null) {
        //�����¸�
        this.root=node;
      //  System.out.println("root:"+this.root.keyset[0]+this.root.keyset[1]+this.root.keyset[2]);
      }
      //��������ڵ�
      this.leftnode=(LeafNode<T, V>)this.root.refreshPointer();
      
    }else {
      System.out.println("������Ч���ݣ������������Ƿ�Ϊ��");
      return ;
    }
  }
  //ɾ��
  public void delete(V key) {
    if(key!=null) {
      Node<T, V> node=this.root.delete(key);
      if(node==null) {
        System.out.println("no such data");
      }else {
        System.out.println("delete success");
      }
    }
  }
  //��ӡ�����
  public void printtree(int num) {
    LeafNode<T,V> node11=this.leftnode;
    while(node11!=null) {
      for(int i=0;i<num;i++) {
      System.out.print(node11.keyset[i]+" ");
      }
      System.out.println();
      node11=node11.afternode;
    }
  }
  }
