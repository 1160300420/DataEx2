package BPlus;

public class LeafNode<T, V extends Comparable<V>> extends Node<T, V> {
  //指向相邻的前面的叶子节点
  private LeafNode<T, V> beforenode;
  //指向相邻的后面的叶子节点
  public LeafNode<T, V> afternode;
  //自身保存的记录集合
  public Object record[];
  public LeafNode(int max) {
    // TODO Auto-generated constructor stub
    super(max);
    this.record = new Object[max];
    this.beforenode = null;
    this.afternode = null;
  }
  @Override
  public T search(V key) {
    // TODO Auto-generated method stub
    if (this.num <= 0) {
      return null;
    }
    int start = 0;
    int end = this.num;
    int mid = (start + end) / 2;
    while (start < end) {
      V midkey = (V) this.keyset[mid];
      if (key.compareTo(midkey) == 0) {
        return (T) this.record[mid];
      } else if (key.compareTo(midkey) < 0) {
        end = mid;
      } else {
        start = mid;
      }
      int tempmid = mid;
      mid = (start + end) / 2;
      if (tempmid == mid) {
        return null;
      }
    }
    return null;
  }
  @Override
  public Node<T, V> insert(T value, V key) {
    // TODO Auto-generated method stub
    V old = null;
    if (this.num > 0) {
      //保存键值最大的节点（即在父节点的儿子节点中保存的那个）
      old = (V) this.keyset[this.num - 1];
    }
    //插入数据
    int count = 0;
    Object tempkeyset[] = new Object[this.max];
    Object temprecord[] = new Object[this.max];
    while (count < this.num) {
      if (key.compareTo((V) this.keyset[count]) < 0) {
        //找到第一个比待插入键值大的位置
        break;
      }
      count++;
    }
    boolean b = false;
    if (count == this.num) {
      //比所有节点都大，插入到最后；或是该节点为空
      this.keyset[count] = key;
      this.record[count] = value;
      tempkeyset = this.keyset;
      temprecord = this.record;
      b = true;
    } else {
      //插入到中间位置
      System.arraycopy(keyset, count, tempkeyset, 1, keyset.length - count - 1);
      System.arraycopy(record, count, temprecord, 1, record.length - count - 1);
      tempkeyset[0] = key;
      temprecord[0] = value;
      System.arraycopy(tempkeyset, 0, keyset, count, keyset.length - count);
      System.arraycopy(temprecord, 0, record, count, record.length - count);
      tempkeyset = keyset;
      temprecord = record;
    }
    this.num++;
    //判断是否需要拆分
    if (this.num > max - 1) {
      //拆分
      //找到中间节点将节点序列一分为二
      int mid = this.num / 2;
      //新建右半儿子节点
      LeafNode<T, V> templeafnode = new LeafNode<>(max);
      templeafnode.num = this.num - mid;
      templeafnode.fNode = this.fNode;
      //更改左半儿子节点序列
      this.num = mid;
      this.keyset = new Object[max];
      this.record = new Object[max];
      System.arraycopy(tempkeyset, 0, this.keyset, 0, mid);
      System.arraycopy(temprecord, 0, this.record, 0, mid);
      //更改右半儿子序列
      System.arraycopy(tempkeyset, mid, templeafnode.keyset, 0, tempkeyset.length - mid);
      System.arraycopy(temprecord, mid, templeafnode.record, 0, tempkeyset.length - mid);
      //建立叶子节点间的指针
      templeafnode.afternode = this.afternode;
      templeafnode.beforenode = this;
      if (templeafnode.afternode != null) {
        templeafnode.afternode.beforenode = templeafnode;
      }
      this.afternode = templeafnode;
      //将新生成的叶子节点序列的最大键值插入父节点
      //如果父节点是空
      if (this.fNode == null) {
        //新建父节点
        TreeNode<T, V> tempTreenode = new TreeNode<>(max);
        templeafnode.fNode = tempTreenode;
        this.fNode = tempTreenode;
        old = null;//如果父节点是null那么父节点并没有这个old值
      }
      TreeNode<T, V> temp_fnode = (TreeNode<T, V>) this.fNode;
      return temp_fnode.insertNode(this, templeafnode, old);
    } else {
      //不拆分
      /* if(!b) {
      System.arraycopy(tempkeyset, 0, keyset, count, keyset.length-count);
      System.arraycopy(temprecord, 0,record , count, record.length-count);
      }*/
      //如果插入的是节点中最大的键值需要更新父节点的儿子节点序列
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
    }
  }
  @Override
  public LeafNode<T, V> refreshPointer() {
    // TODO Auto-generated method stub
    if (this.num <= 0) {
      return null;
    }
    return this;
  }
  @Override
  public Node<T, V> delete(V key) {
    // TODO Auto-generated method stub
    int find_seq = -1;
    if (this.num <= 0) {
      return null;
    }
    int start = 0;
    int end = this.num;
    int mid = (start + end) / 2;
    while (start < end) {
      V midkey = (V) this.keyset[mid];
      if (key.compareTo(midkey) == 0) {
        find_seq = mid;
        break;
      } else if (key.compareTo(midkey) < 0) {
        end = mid;
      } else {
        start = mid;
      }
      mid = (start + end) / 2;
    }
    if (find_seq == -1) {
      //未找到
      return null;
    } else {
      //已找到
      //如果删除key后本节点不为空且被删除键值是本节点keyset中最大的，则需递归更新本节点在父节点中的键值
      if (this.num > 1) {
        int seq = 1;
        Node tempnode = this;
        V tempkey = key;
        while (tempnode.fNode != null) {
          Node tempfnode = tempnode.fNode;
          boolean bb = false;
          if (seq == 1) {
            seq++;
            if ((tempkey.compareTo((V) tempnode.keyset[tempnode.num - 1])) == 0) {
              //如果该键值是本节点的最大键值
              int count = 0;
              while (count < tempfnode.num) {
                if (tempkey.compareTo((V) tempfnode.keyset[count]) == 0) {
                  tempfnode.keyset[count] = tempnode.keyset[tempnode.num - 2];
                  if (count == tempfnode.num - 1) {
                    //如果本节点的key也是父节点的最大键值
                    tempkey = (V) tempfnode.keyset[count];
                    bb = true;
                  }
                  break;
                }
                count++;
              }
              if (bb) {
                tempnode = tempfnode;
              } else {
                break;
              }
            }
          } else {
            int count = 0;
            while (count < tempfnode.num) {
              if (key.compareTo((V) tempfnode.keyset[count]) == 0) {
                tempfnode.keyset[count] = tempkey;
                if (count == tempfnode.num - 1) {
                  //如果本节点的key也是父节点的最大键值
                  bb = true;
                }
                break;
              }
              count++;
            }
            if (bb) {
              tempnode = tempfnode;
            } else {
              break;
            }
          }
        }
      }
      //进行record和keyset数组的移动
      Object tempkeyset[] = new Object[this.max];
      Object temprecord[] = new Object[this.max];
      System.arraycopy(this.keyset, find_seq + 1, tempkeyset, 0, this.keyset.length - find_seq - 1);
      System.arraycopy(tempkeyset, 0, this.keyset, find_seq, this.keyset.length - find_seq);
      System.arraycopy(this.record, find_seq + 1, temprecord, 0, this.keyset.length - find_seq - 1);
      System.arraycopy(temprecord, 0, this.record, find_seq, this.keyset.length - find_seq);
      this.num--;
      
      //删除后的平衡
      if (this.num != 0) {
        boolean is_patch = true;
        if (this.num < max / 2 && (this.afternode != null || this.beforenode != null)) {
          //如果左右相邻节点有超过max/2的，则借2个，优先借同一个父节点的
          if (this.afternode == null
              || (this.afternode != null && this.beforenode != null && this.beforenode.fNode.equals(this.fNode))) {
            if (this.beforenode.num > max / 2 + 1) {
              is_patch = false;
              V key1 = (V) this.beforenode.keyset[0];
              V key2 = (V) this.beforenode.keyset[1];
              LeafNode<T, V> node1 = (LeafNode<T, V>) this.beforenode.record[0];
              LeafNode<T, V> node2 = (LeafNode<T, V>) this.beforenode.record[1];
              //删除被借节点的前两个record
              //进行record和keyset数组的移动
              Object tempkeyset1[] = new Object[this.max];
              Object temprecord1[] = new Object[this.max];
              System.arraycopy(this.beforenode.keyset, 2, tempkeyset1, 0, this.beforenode.keyset.length - 2);
              System.arraycopy(this.beforenode.record, 2, temprecord1, 0, this.beforenode.record.length - 2);
              System.arraycopy(tempkeyset1, 0, this.beforenode.keyset, 0, this.beforenode.keyset.length);
              System.arraycopy(temprecord1, 0, this.beforenode.record, 0, this.beforenode.record.length);
              this.beforenode.num -= 2;
              //将这两个record加入本节点
              System.arraycopy(this.keyset, 2, tempkeyset1, 0, this.keyset.length - 3);
              System.arraycopy(this.record, 2, temprecord1, 0, this.record.length - 3);
              System.arraycopy(tempkeyset1, 0, this.keyset, 2, this.keyset.length - 3);
              System.arraycopy(temprecord1, 0, this.record, 2, this.keyset.length - 3);
              this.keyset[0] = key1;
              this.keyset[1] = key2;
              this.record[0] = node1;
              this.record[1] = node2;
              //更新节点
              Node tempnode = this;
              while (tempnode.fNode != null) {
                Node tempfnode = tempnode.fNode;
                int count = 0;
                while (count < tempfnode.num) {
                  if (((V) this.keyset[this.num - 1]).compareTo((V) tempfnode.keyset[count]) == 0) {
                    tempfnode.keyset[count] = key2;
                    if (count == tempfnode.num - 1) {
                      //如果本节点的key也是父节点的最大键值
                      boolean stop = false;
                      while (tempfnode.fNode != null) {
                        Node tempffnode = tempfnode.fNode;
                        int count_it = 0;
                        while (count_it < tempffnode.num) {
                          //对于父节点键值在父父节点进行更新
                          if (((V) this.keyset[this.num - 1]).compareTo((V) tempffnode.keyset[count_it]) == 0) {
                            tempfnode.keyset[count_it] = key2;
                            if (count == tempfnode.num - 1) {
                              //如果父节点的键值是父父节点键值序列的最大值则迭代更新
                              tempfnode = tempffnode;
                            } else {
                              stop = true;
                            }
                            break;
                          }
                          count_it++;
                        }
                        if (stop) {
                          break;
                        }
                      }
                    }
                  }
                  count++;
                }
              }
              this.num += 2;
            }
          } else if (this.beforenode == null
              || (this.afternode != null && this.beforenode != null && this.afternode.fNode.equals(this.fNode))
              || (this.afternode != null && this.beforenode != null && (!this.afternode.fNode.equals(this.fNode))
                  && (!this.beforenode.fNode.equals(this.fNode)))) {
            if (this.afternode.num > max / 2 + 1) {
              is_patch = false;
              V key1 = (V) this.afternode.keyset[0];
              V key2 = (V) this.afternode.keyset[1];
              LeafNode<T, V> node1 = (LeafNode<T, V>) this.afternode.record[0];
              LeafNode<T, V> node2 = (LeafNode<T, V>) this.afternode.record[1];
              //删除被借节点的前两个record
              //进行record和keyset数组的移动
              Object tempkeyset1[] = new Object[this.max];
              Object temprecord1[] = new Object[this.max];
              System.arraycopy(this.afternode.keyset, 2, tempkeyset1, 0, this.afternode.keyset.length - 2);
              System.arraycopy(this.afternode.record, 2, temprecord1, 0, this.afternode.record.length - 2);
              System.arraycopy(tempkeyset1, 0, this.afternode.keyset, 0, this.afternode.keyset.length);
              System.arraycopy(temprecord1, 0, this.afternode.record, 0, this.afternode.record.length);
              this.afternode.num -= 2;
              //将这两个record加入本节点
              this.keyset[this.num] = key1;
              this.keyset[this.num + 1] = key2;
              this.record[this.num] = node1;
              this.record[this.num + 1] = node2;
              //更新节点
              Node tempnode = this;
              while (tempnode.fNode != null) {
                Node tempfnode = tempnode.fNode;
                int count = 0;
                while (count < tempfnode.num) {
                  if (((V) this.keyset[this.num - 1]).compareTo((V) tempfnode.keyset[count]) == 0) {
                    tempfnode.keyset[count] = key2;
                    if (count == tempfnode.num - 1) {
                      //如果本节点的key也是父节点的最大键值
                      boolean stop = false;
                      while (tempfnode.fNode != null) {
                        Node tempffnode = tempfnode.fNode;
                        int count_it = 0;
                        while (count_it < tempffnode.num) {
                          //对于父节点键值在父父节点进行更新
                          if (((V) this.keyset[this.num - 1]).compareTo((V) tempffnode.keyset[count_it]) == 0) {
                            tempfnode.keyset[count_it] = key2;
                            if (count == tempfnode.num - 1) {
                              //如果父节点的键值是父父节点键值序列的最大值则迭代更新
                              tempfnode = tempffnode;
                            } else {
                              stop = true;
                            }
                            break;
                          }
                          count_it++;
                        }
                        if (stop) {
                          break;
                        }
                      }
                    }
                  }
                  count++;
                }
              }
              this.num += 2;
            }
          }
          //如果左右相邻节点都不超过max/2+1的，则合并，优先合并同一个父节点的
          if (is_patch&&(this.afternode!=null||this.beforenode!=null)) {
            if (this.afternode!=null&&this.afternode.fNode.equals(this.fNode)) {
              if (this.num + this.afternode.num < max) {
                //合并afternode和this
                System.arraycopy(this.afternode.keyset, 0, this.keyset, this.num, this.keyset.length - this.num);
                System.arraycopy(this.afternode.record, 0, this.record, this.num, this.record.length - this.num);
                //更新在父节点中的键值
                int ttt=0;
                while(ttt<this.fNode.num) {
                  if(this.keyset[this.num-1].equals(this.fNode.keyset[ttt])) {
                    this.fNode.keyset[ttt]=this.keyset[this.num+this.afternode.num-1];
                    break;
                  }
                  ttt++;
                }
                this.num = this.num + this.afternode.num;
               
                //删除this.afternode
                ((TreeNode<T, V>) this.fNode).deleteNode(this.afternode,
                    (V) this.afternode.keyset[this.afternode.num - 1]);
                this.afternode.beforenode = this;
                this.afternode = this.afternode.afternode;
                
              }
            } else {
              //if(this.keyset.length+this.beforenode.keyset.length<max) {
              //合并beforenode和this
              V oldkey = (V) this.beforenode.keyset[this.beforenode.num - 1];
              System.arraycopy(this.keyset, 0, this.beforenode.keyset, this.beforenode.num, this.num);
              System.arraycopy(this.record, 0, this.beforenode.record, this.beforenode.num, this.num);
              this.beforenode.num = this.num + this.beforenode.num;
              //删除this.afternode
             
              //更新beforenode在父节点中的值
              Node tempnode = this.beforenode;
              boolean stop = false;
              while (tempnode.fNode != null) {
                V tempkey = (V) tempnode.keyset[tempnode.num - 1];
                Node tempfnode = tempnode.fNode;
                int count_it = 0;
                while (count_it < tempfnode.num) {
                  //对于父节点键值在父父节点进行更新
                  if (oldkey.compareTo((V) tempfnode.keyset[count_it]) == 0) {
                    tempfnode.keyset[count_it] = this.beforenode.keyset[this.beforenode.num - 1];
                    if (count_it == tempfnode.num - 1) {
                      //如果父节点的键值是父父节点键值序列的最大值则迭代更新
                      tempnode = tempfnode;
                    } else {
                      stop = true;
                    }
                    break;
                  }
                  count_it++;
                }
                if(stop) {
                  break;
                }
              }
              //删除该节点
              ((TreeNode<T, V>) this.fNode).deleteNode(this, (V) this.keyset[this.num - 1]);
              if(this.beforenode!=null) {
              this.beforenode.afternode = this.afternode;
              }
              if(this.afternode!=null) {
              this.afternode.beforenode = this.beforenode;
              }
            }
          }
          //}
        }
      }
      if (this.num == 0 && this.fNode != null) {
        //删除该节点
        ((TreeNode<T, V>) this.fNode).deleteNode(this, key);
        this.beforenode.afternode = this.afternode;
        this.afternode.beforenode = this.beforenode;
        
      }
      return this;
    }
  }
}

