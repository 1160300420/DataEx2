package BPlus;

public class TreeNode<T, V extends Comparable<V>> extends Node<T, V> {
  
  public TreeNode(int max) {
    super(max);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public T search(V key) {
    // TODO Auto-generated method stub
    int count = 0;
    while (count < this.num) {
      if (key.compareTo((V) this.keyset[count]) <= 0) {
        
        break;
      }
      count++;
    }
    /* if(key.compareTo((V) this.keyset[count]) > 0) {
      count++;
    }*/
    if (this.num == count) {
      //全部子节点key均比待插入的键值大
      return null;
    }
    //递归查找
    return this.sNodelist[count].search(key);
  }
  
  @Override
  public Node<T, V> insert(T value, V key) {
    // TODO Auto-generated method stub
    int count = 0;
    while (count < this.num) {
      if (key.compareTo((V) this.keyset[count]) < 0) {
        //找到第一个键值比待插入键值大的位置
        break;
      }
      count++;
    }
    //如果比最后一个节点键值都大,则在最后一个节点的儿子节点序列中插入（并用该节点作更新当前最后一个节点，该步骤在叶子节点插入后向上完成）
    if (key.compareTo((V) this.keyset[this.num - 1]) > 0) {
      count--;
    }
    //递归插入
    return this.sNodelist[count].insert(value, key);
  }
  /**
   * 由于子节点分裂导致的插入
   * @param node1 分裂后的第一个子节点
   * @param node2 分裂后的第二个子节点
   * @param key 原分裂节点的键值
   * @return node新根，如果没有产生新根则返回null
   */
  public Node<T, V> insertNode(Node<T, V> node1, Node<T, V> node2, V key) {
    V old = null;
    if (this.num > 0) {
      old = (V) this.keyset[this.num - 1];
    }
    if (key == null || this.num <= 0) {
      //该节点是空节点，即新根，直接放入两个节点
      this.keyset[0] = node1.keyset[node1.num - 1];
      this.keyset[1] = node2.keyset[node2.num - 1];
      this.sNodelist[0] = node1;
      this.sNodelist[1] = node2;
      this.num = 2;
      return this;//返回新根
    }
    //该节点为非空节点，删除原儿子节点，插入两个新节点，判断是否需要拆分
    int count = 0;
    while (key.compareTo((V) this.keyset[count]) != 0) {
      //找到原儿子节点
      count++;
    }
    //在原儿子节点处插入node1
    this.keyset[count] = node1.keyset[node1.num - 1];
    this.sNodelist[count] = node1;
    //在原儿子节点后插入node2，需要对原节点序列中在原儿子节点后的部分节点序列先向后移动一位
    Object tempKeyset[] = new Object[max];//保存插入为中间位置的右半部分键值序列
    Object tempsnodelist[] = new Object[max];//保存插入为中间位置的右半部分node序列
    System.arraycopy(this.keyset, count + 1, tempKeyset, 1, this.keyset.length - count - 1);
    System.arraycopy(this.sNodelist, count + 1, tempsnodelist, 1, this.sNodelist.length - count - 1);
    tempKeyset[0] = node2.keyset[node2.num - 1];
    tempsnodelist[0] = node2;
    this.num++;
    System.arraycopy(tempKeyset, 0, this.keyset, count + 1, this.keyset.length - count - 1);
    System.arraycopy(tempsnodelist, 0, this.sNodelist, count + 1, this.sNodelist.length - count - 1);
    //判断是否拆分
    if (this.num < max) {
      //不拆分
      //更新该节点的父节点keyset的值
      Node tempnode = this;
      while (tempnode.fNode != null) {
        V tempkey = (V) tempnode.keyset[tempnode.num - 1];
        Node tempfnode = tempnode.fNode;
        if (tempkey.compareTo((V) tempfnode.keyset[tempfnode.num - 1]) > 0) {
          tempfnode.keyset[tempfnode.num - 1] = tempkey;
        }
        tempnode = tempfnode;
      }
      return null;
    } else {
      //拆分
      int mid = this.num / 2;
      //新建节点保存右半部分
      TreeNode<T, V> newnode = new TreeNode<>(max);
      newnode.num = this.num - mid;
      this.num = mid;
      newnode.fNode = this.fNode;
      //如果父节点是null，则需新建节点作为新根
      if (this.fNode == null) {
        TreeNode<T, V> newroot = new TreeNode<>(max);
        newnode.fNode = newroot;
        this.fNode = newroot;
        old = null;
      }
      //被拆分的右半部分
      System.arraycopy(this.keyset, mid, newnode.keyset, 0, newnode.num);
      System.arraycopy(this.sNodelist, mid, newnode.sNodelist, 0, newnode.num);
      //被拆分得到的左半部分
      //更改被拆分节点的儿子节点序列
      Object[] tempkeyset1 = new Object[max];
      Object[] tempnodelist1 = new Object[max];
      System.arraycopy(this.keyset, 0, tempkeyset1, 0, mid);
      System.arraycopy(this.sNodelist, 0, tempnodelist1, 0, mid);
      this.keyset = tempkeyset1;
      System.arraycopy(tempnodelist1, 0, this.sNodelist, 0, tempnodelist1.length);
      //更改被拆分的新节点序列的父节点为newroot
      for (int i = 0; i < newnode.num; i++) {
        if (newnode.sNodelist[i] != null) {
          newnode.sNodelist[i].fNode = newnode;
        }
      }
      TreeNode<T, V> newfnode = (TreeNode<T, V>) this.fNode;
      return newfnode.insertNode(this, newnode, old);
    }
  }
  
  @Override
  public LeafNode<T, V> refreshPointer() {
    // TODO Auto-generated method stub
    return this.sNodelist[0].refreshPointer();
  }
  
  @Override
  public Node<T, V> delete(V key) {
    // TODO Auto-generated method stub
    int count = 0;
    while (count < this.num) {
      if (key.compareTo((V) this.keyset[count]) <= 0) {
        //找到第一个键值比待插入键值大的位置
        break;
      }
      count++;
    }
    //如果比最后一个节点键值都大,则说明不在数据集中
    if (key.compareTo((V) this.keyset[this.num - 1]) > 0) {
      return null;
    }
    return this.sNodelist[count].delete(key);
  }
  //删除一个节点
  public void deleteNode(Node n, V key) {
    //从本节点的keyset和snodelist中删除n,记录本节点在父节点中的key，便于在父节点中删除本节点
    V old = null;
    if (this.num > 0) {
      old = (V) this.keyset[this.num - 1];
    }
    int count = 0;
    while (!n.equals(this.sNodelist[count])) {
      //找到被删除的儿子节点
      count++;
    }
    //删除该节点,如果删除后父节点不空且被删除节点是父节点snodelist中最大的，则需递归更新各层父节点的keyset，并且不需调整
    if (count == this.num - 1 && this.num > 1) {
      
      int seq = 1;
      Node tempnode = this;
      V tempkey = key;
      while (tempnode.fNode != null) {
        Node tempfnode = tempnode.fNode;
        boolean bb = false;
        if (seq == 1) {
          seq++;
        //  if ((tempkey.compareTo((V) tempnode.keyset[tempnode.num - 1])) == 0) {
            //如果该键值是本节点的最大键值          
            int count_t = 0;
            while (count_t < tempfnode.num) {
              if (tempkey.compareTo((V) tempfnode.keyset[count_t]) == 0) {
                tempfnode.keyset[count_t] = tempnode.keyset[tempnode.num - 1];
                if (count_t == tempfnode.num - 1) {
                  //如果本节点的key也是父节点的最大键值
                  tempkey = (V) tempfnode.keyset[count_t];
                  bb = true;
                }
                break;
              }
              count_t++;
            }
            if (bb) {
              tempnode = tempfnode;
            } else {
              break;
            }
         // }
        } else {
          int count_t = 0;
          while (count_t < tempfnode.num) {
            if (tempkey.compareTo((V) tempfnode.keyset[count_t]) == 0) {
              tempfnode.keyset[count_t] = tempkey;
              if (count_t == tempfnode.num - 1) {
                //如果本节点的key也是父节点的最大键值
                bb = true;
              }
              break;
            }
            count_t++;
          }
          if (bb) {
            tempnode = tempfnode;
          } else {
            break;
          }
        }
      }
    }
    Object tempkeyset[] = new Object[this.max];
    Object tempsnodelist[] = new Object[this.max];
    System.arraycopy(this.keyset, count + 1, tempkeyset, 0, this.keyset.length - count - 1);
    System.arraycopy(tempkeyset, 0, this.keyset, count, this.keyset.length - count);
    System.arraycopy(this.sNodelist, count + 1, tempsnodelist, 0, this.keyset.length - count - 1);
    System.arraycopy(tempsnodelist, 0, this.sNodelist, count, this.keyset.length - count);
    this.num--;
    
    if (this.num == 0 && this.fNode != null) {
      //删除该节点
      ((TreeNode<T, V>) this.fNode).deleteNode(this, key);
    }
  }
}
