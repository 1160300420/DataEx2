package BPlus;

public class LeafNode<T, V extends Comparable<V>> extends Node<T, V> {
  //ָ�����ڵ�ǰ���Ҷ�ӽڵ�
  private LeafNode<T, V> beforenode;
  //ָ�����ڵĺ����Ҷ�ӽڵ�
  public LeafNode<T, V> afternode;
  //������ļ�¼����
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
      //�����ֵ���Ľڵ㣨���ڸ��ڵ�Ķ��ӽڵ��б�����Ǹ���
      old = (V) this.keyset[this.num - 1];
    }
    //��������
    int count = 0;
    Object tempkeyset[] = new Object[this.max];
    Object temprecord[] = new Object[this.max];
    while (count < this.num) {
      if (key.compareTo((V) this.keyset[count]) < 0) {
        //�ҵ���һ���ȴ������ֵ���λ��
        break;
      }
      count++;
    }
    boolean b = false;
    if (count == this.num) {
      //�����нڵ㶼�󣬲��뵽��󣻻��Ǹýڵ�Ϊ��
      this.keyset[count] = key;
      this.record[count] = value;
      tempkeyset = this.keyset;
      temprecord = this.record;
      b = true;
    } else {
      //���뵽�м�λ��
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
    //�ж��Ƿ���Ҫ���
    if (this.num > max - 1) {
      //���
      //�ҵ��м�ڵ㽫�ڵ�����һ��Ϊ��
      int mid = this.num / 2;
      //�½��Ұ���ӽڵ�
      LeafNode<T, V> templeafnode = new LeafNode<>(max);
      templeafnode.num = this.num - mid;
      templeafnode.fNode = this.fNode;
      //���������ӽڵ�����
      this.num = mid;
      this.keyset = new Object[max];
      this.record = new Object[max];
      System.arraycopy(tempkeyset, 0, this.keyset, 0, mid);
      System.arraycopy(temprecord, 0, this.record, 0, mid);
      //�����Ұ��������
      System.arraycopy(tempkeyset, mid, templeafnode.keyset, 0, tempkeyset.length - mid);
      System.arraycopy(temprecord, mid, templeafnode.record, 0, tempkeyset.length - mid);
      //����Ҷ�ӽڵ���ָ��
      templeafnode.afternode = this.afternode;
      templeafnode.beforenode = this;
      if (templeafnode.afternode != null) {
        templeafnode.afternode.beforenode = templeafnode;
      }
      this.afternode = templeafnode;
      //�������ɵ�Ҷ�ӽڵ����е�����ֵ���븸�ڵ�
      //������ڵ��ǿ�
      if (this.fNode == null) {
        //�½����ڵ�
        TreeNode<T, V> tempTreenode = new TreeNode<>(max);
        templeafnode.fNode = tempTreenode;
        this.fNode = tempTreenode;
        old = null;//������ڵ���null��ô���ڵ㲢û�����oldֵ
      }
      TreeNode<T, V> temp_fnode = (TreeNode<T, V>) this.fNode;
      return temp_fnode.insertNode(this, templeafnode, old);
    } else {
      //�����
      /* if(!b) {
      System.arraycopy(tempkeyset, 0, keyset, count, keyset.length-count);
      System.arraycopy(temprecord, 0,record , count, record.length-count);
      }*/
      //���������ǽڵ������ļ�ֵ��Ҫ���¸��ڵ�Ķ��ӽڵ�����
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
      //δ�ҵ�
      return null;
    } else {
      //���ҵ�
      //���ɾ��key�󱾽ڵ㲻Ϊ���ұ�ɾ����ֵ�Ǳ��ڵ�keyset�����ģ�����ݹ���±��ڵ��ڸ��ڵ��еļ�ֵ
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
              //����ü�ֵ�Ǳ��ڵ������ֵ
              int count = 0;
              while (count < tempfnode.num) {
                if (tempkey.compareTo((V) tempfnode.keyset[count]) == 0) {
                  tempfnode.keyset[count] = tempnode.keyset[tempnode.num - 2];
                  if (count == tempfnode.num - 1) {
                    //������ڵ��keyҲ�Ǹ��ڵ������ֵ
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
                  //������ڵ��keyҲ�Ǹ��ڵ������ֵ
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
      //����record��keyset������ƶ�
      Object tempkeyset[] = new Object[this.max];
      Object temprecord[] = new Object[this.max];
      System.arraycopy(this.keyset, find_seq + 1, tempkeyset, 0, this.keyset.length - find_seq - 1);
      System.arraycopy(tempkeyset, 0, this.keyset, find_seq, this.keyset.length - find_seq);
      System.arraycopy(this.record, find_seq + 1, temprecord, 0, this.keyset.length - find_seq - 1);
      System.arraycopy(temprecord, 0, this.record, find_seq, this.keyset.length - find_seq);
      this.num--;
      
      //ɾ�����ƽ��
      if (this.num != 0) {
        boolean is_patch = true;
        if (this.num < max / 2 && (this.afternode != null || this.beforenode != null)) {
          //����������ڽڵ��г���max/2�ģ����2�������Ƚ�ͬһ�����ڵ��
          if (this.afternode == null
              || (this.afternode != null && this.beforenode != null && this.beforenode.fNode.equals(this.fNode))) {
            if (this.beforenode.num > max / 2 + 1) {
              is_patch = false;
              V key1 = (V) this.beforenode.keyset[0];
              V key2 = (V) this.beforenode.keyset[1];
              LeafNode<T, V> node1 = (LeafNode<T, V>) this.beforenode.record[0];
              LeafNode<T, V> node2 = (LeafNode<T, V>) this.beforenode.record[1];
              //ɾ������ڵ��ǰ����record
              //����record��keyset������ƶ�
              Object tempkeyset1[] = new Object[this.max];
              Object temprecord1[] = new Object[this.max];
              System.arraycopy(this.beforenode.keyset, 2, tempkeyset1, 0, this.beforenode.keyset.length - 2);
              System.arraycopy(this.beforenode.record, 2, temprecord1, 0, this.beforenode.record.length - 2);
              System.arraycopy(tempkeyset1, 0, this.beforenode.keyset, 0, this.beforenode.keyset.length);
              System.arraycopy(temprecord1, 0, this.beforenode.record, 0, this.beforenode.record.length);
              this.beforenode.num -= 2;
              //��������record���뱾�ڵ�
              System.arraycopy(this.keyset, 2, tempkeyset1, 0, this.keyset.length - 3);
              System.arraycopy(this.record, 2, temprecord1, 0, this.record.length - 3);
              System.arraycopy(tempkeyset1, 0, this.keyset, 2, this.keyset.length - 3);
              System.arraycopy(temprecord1, 0, this.record, 2, this.keyset.length - 3);
              this.keyset[0] = key1;
              this.keyset[1] = key2;
              this.record[0] = node1;
              this.record[1] = node2;
              //���½ڵ�
              Node tempnode = this;
              while (tempnode.fNode != null) {
                Node tempfnode = tempnode.fNode;
                int count = 0;
                while (count < tempfnode.num) {
                  if (((V) this.keyset[this.num - 1]).compareTo((V) tempfnode.keyset[count]) == 0) {
                    tempfnode.keyset[count] = key2;
                    if (count == tempfnode.num - 1) {
                      //������ڵ��keyҲ�Ǹ��ڵ������ֵ
                      boolean stop = false;
                      while (tempfnode.fNode != null) {
                        Node tempffnode = tempfnode.fNode;
                        int count_it = 0;
                        while (count_it < tempffnode.num) {
                          //���ڸ��ڵ��ֵ�ڸ����ڵ���и���
                          if (((V) this.keyset[this.num - 1]).compareTo((V) tempffnode.keyset[count_it]) == 0) {
                            tempfnode.keyset[count_it] = key2;
                            if (count == tempfnode.num - 1) {
                              //������ڵ�ļ�ֵ�Ǹ����ڵ��ֵ���е����ֵ���������
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
              //ɾ������ڵ��ǰ����record
              //����record��keyset������ƶ�
              Object tempkeyset1[] = new Object[this.max];
              Object temprecord1[] = new Object[this.max];
              System.arraycopy(this.afternode.keyset, 2, tempkeyset1, 0, this.afternode.keyset.length - 2);
              System.arraycopy(this.afternode.record, 2, temprecord1, 0, this.afternode.record.length - 2);
              System.arraycopy(tempkeyset1, 0, this.afternode.keyset, 0, this.afternode.keyset.length);
              System.arraycopy(temprecord1, 0, this.afternode.record, 0, this.afternode.record.length);
              this.afternode.num -= 2;
              //��������record���뱾�ڵ�
              this.keyset[this.num] = key1;
              this.keyset[this.num + 1] = key2;
              this.record[this.num] = node1;
              this.record[this.num + 1] = node2;
              //���½ڵ�
              Node tempnode = this;
              while (tempnode.fNode != null) {
                Node tempfnode = tempnode.fNode;
                int count = 0;
                while (count < tempfnode.num) {
                  if (((V) this.keyset[this.num - 1]).compareTo((V) tempfnode.keyset[count]) == 0) {
                    tempfnode.keyset[count] = key2;
                    if (count == tempfnode.num - 1) {
                      //������ڵ��keyҲ�Ǹ��ڵ������ֵ
                      boolean stop = false;
                      while (tempfnode.fNode != null) {
                        Node tempffnode = tempfnode.fNode;
                        int count_it = 0;
                        while (count_it < tempffnode.num) {
                          //���ڸ��ڵ��ֵ�ڸ����ڵ���и���
                          if (((V) this.keyset[this.num - 1]).compareTo((V) tempffnode.keyset[count_it]) == 0) {
                            tempfnode.keyset[count_it] = key2;
                            if (count == tempfnode.num - 1) {
                              //������ڵ�ļ�ֵ�Ǹ����ڵ��ֵ���е����ֵ���������
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
          //����������ڽڵ㶼������max/2+1�ģ���ϲ������Ⱥϲ�ͬһ�����ڵ��
          if (is_patch&&(this.afternode!=null||this.beforenode!=null)) {
            if (this.afternode!=null&&this.afternode.fNode.equals(this.fNode)) {
              if (this.num + this.afternode.num < max) {
                //�ϲ�afternode��this
                System.arraycopy(this.afternode.keyset, 0, this.keyset, this.num, this.keyset.length - this.num);
                System.arraycopy(this.afternode.record, 0, this.record, this.num, this.record.length - this.num);
                //�����ڸ��ڵ��еļ�ֵ
                int ttt=0;
                while(ttt<this.fNode.num) {
                  if(this.keyset[this.num-1].equals(this.fNode.keyset[ttt])) {
                    this.fNode.keyset[ttt]=this.keyset[this.num+this.afternode.num-1];
                    break;
                  }
                  ttt++;
                }
                this.num = this.num + this.afternode.num;
               
                //ɾ��this.afternode
                ((TreeNode<T, V>) this.fNode).deleteNode(this.afternode,
                    (V) this.afternode.keyset[this.afternode.num - 1]);
                this.afternode.beforenode = this;
                this.afternode = this.afternode.afternode;
                
              }
            } else {
              //if(this.keyset.length+this.beforenode.keyset.length<max) {
              //�ϲ�beforenode��this
              V oldkey = (V) this.beforenode.keyset[this.beforenode.num - 1];
              System.arraycopy(this.keyset, 0, this.beforenode.keyset, this.beforenode.num, this.num);
              System.arraycopy(this.record, 0, this.beforenode.record, this.beforenode.num, this.num);
              this.beforenode.num = this.num + this.beforenode.num;
              //ɾ��this.afternode
             
              //����beforenode�ڸ��ڵ��е�ֵ
              Node tempnode = this.beforenode;
              boolean stop = false;
              while (tempnode.fNode != null) {
                V tempkey = (V) tempnode.keyset[tempnode.num - 1];
                Node tempfnode = tempnode.fNode;
                int count_it = 0;
                while (count_it < tempfnode.num) {
                  //���ڸ��ڵ��ֵ�ڸ����ڵ���и���
                  if (oldkey.compareTo((V) tempfnode.keyset[count_it]) == 0) {
                    tempfnode.keyset[count_it] = this.beforenode.keyset[this.beforenode.num - 1];
                    if (count_it == tempfnode.num - 1) {
                      //������ڵ�ļ�ֵ�Ǹ����ڵ��ֵ���е����ֵ���������
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
              //ɾ���ýڵ�
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
        //ɾ���ýڵ�
        ((TreeNode<T, V>) this.fNode).deleteNode(this, key);
        this.beforenode.afternode = this.afternode;
        this.afternode.beforenode = this.beforenode;
        
      }
      return this;
    }
  }
}

