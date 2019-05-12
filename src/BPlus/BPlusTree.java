package BPlus;

public class BPlusTree <T,V extends Comparable<V>>{
  //最大节点数
  private int max;
  //BPlusTree的阶
  private int index;
  //根节点
  public Node<T,V> root;
  //最左节点
  private LeafNode<T,V> leftnode;
  //此时子节点的数目
  private int current_son_num;
  //默认构造阶数为1
  public BPlusTree() {
    this(1);
  }
  public BPlusTree(int num) {
    this.index=num;
    //运行过程中可能出现超过上限情况，此时进行分裂
    this.max=this.index+1;
    this.root=new LeafNode<T,V>(this.max);
    this.current_son_num=0;
    this.leftnode=null;
  }
  //查找
  public T search(V key) {
    T t=this.root.search(key);
    if(t==null) {
      System.out.println("该节点不存在！");
    }
    return t;
  }
  //插入
  public void insert(T value,V key) {
    if(key!=null&&value!=null) {
      Node<T, V> node=this.root.insert(value, key);
      if(node!=null) {
        //返回新根
        this.root=node;
      //  System.out.println("root:"+this.root.keyset[0]+this.root.keyset[1]+this.root.keyset[2]);
      }
      //更新最左节点
      this.leftnode=(LeafNode<T, V>)this.root.refreshPointer();
      
    }else {
      System.out.println("插入无效数据，检查插入内容是否为空");
      return ;
    }
  }
  //删除
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
  //打印这棵树
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
