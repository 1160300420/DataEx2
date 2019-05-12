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
      //ȫ���ӽڵ�key���ȴ�����ļ�ֵ��
      return null;
    }
    //�ݹ����
    return this.sNodelist[count].search(key);
  }
  
  @Override
  public Node<T, V> insert(T value, V key) {
    // TODO Auto-generated method stub
    int count = 0;
    while (count < this.num) {
      if (key.compareTo((V) this.keyset[count]) < 0) {
        //�ҵ���һ����ֵ�ȴ������ֵ���λ��
        break;
      }
      count++;
    }
    //��������һ���ڵ��ֵ����,�������һ���ڵ�Ķ��ӽڵ������в��루���øýڵ������µ�ǰ���һ���ڵ㣬�ò�����Ҷ�ӽڵ�����������ɣ�
    if (key.compareTo((V) this.keyset[this.num - 1]) > 0) {
      count--;
    }
    //�ݹ����
    return this.sNodelist[count].insert(value, key);
  }
  /**
   * �����ӽڵ���ѵ��µĲ���
   * @param node1 ���Ѻ�ĵ�һ���ӽڵ�
   * @param node2 ���Ѻ�ĵڶ����ӽڵ�
   * @param key ԭ���ѽڵ�ļ�ֵ
   * @return node�¸������û�в����¸��򷵻�null
   */
  public Node<T, V> insertNode(Node<T, V> node1, Node<T, V> node2, V key) {
    V old = null;
    if (this.num > 0) {
      old = (V) this.keyset[this.num - 1];
    }
    if (key == null || this.num <= 0) {
      //�ýڵ��ǿսڵ㣬���¸���ֱ�ӷ��������ڵ�
      this.keyset[0] = node1.keyset[node1.num - 1];
      this.keyset[1] = node2.keyset[node2.num - 1];
      this.sNodelist[0] = node1;
      this.sNodelist[1] = node2;
      this.num = 2;
      return this;//�����¸�
    }
    //�ýڵ�Ϊ�ǿսڵ㣬ɾ��ԭ���ӽڵ㣬���������½ڵ㣬�ж��Ƿ���Ҫ���
    int count = 0;
    while (key.compareTo((V) this.keyset[count]) != 0) {
      //�ҵ�ԭ���ӽڵ�
      count++;
    }
    //��ԭ���ӽڵ㴦����node1
    this.keyset[count] = node1.keyset[node1.num - 1];
    this.sNodelist[count] = node1;
    //��ԭ���ӽڵ�����node2����Ҫ��ԭ�ڵ���������ԭ���ӽڵ��Ĳ��ֽڵ�����������ƶ�һλ
    Object tempKeyset[] = new Object[max];//�������Ϊ�м�λ�õ��Ұ벿�ּ�ֵ����
    Object tempsnodelist[] = new Object[max];//�������Ϊ�м�λ�õ��Ұ벿��node����
    System.arraycopy(this.keyset, count + 1, tempKeyset, 1, this.keyset.length - count - 1);
    System.arraycopy(this.sNodelist, count + 1, tempsnodelist, 1, this.sNodelist.length - count - 1);
    tempKeyset[0] = node2.keyset[node2.num - 1];
    tempsnodelist[0] = node2;
    this.num++;
    System.arraycopy(tempKeyset, 0, this.keyset, count + 1, this.keyset.length - count - 1);
    System.arraycopy(tempsnodelist, 0, this.sNodelist, count + 1, this.sNodelist.length - count - 1);
    //�ж��Ƿ���
    if (this.num < max) {
      //�����
      //���¸ýڵ�ĸ��ڵ�keyset��ֵ
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
      //���
      int mid = this.num / 2;
      //�½��ڵ㱣���Ұ벿��
      TreeNode<T, V> newnode = new TreeNode<>(max);
      newnode.num = this.num - mid;
      this.num = mid;
      newnode.fNode = this.fNode;
      //������ڵ���null�������½��ڵ���Ϊ�¸�
      if (this.fNode == null) {
        TreeNode<T, V> newroot = new TreeNode<>(max);
        newnode.fNode = newroot;
        this.fNode = newroot;
        old = null;
      }
      //����ֵ��Ұ벿��
      System.arraycopy(this.keyset, mid, newnode.keyset, 0, newnode.num);
      System.arraycopy(this.sNodelist, mid, newnode.sNodelist, 0, newnode.num);
      //����ֵõ�����벿��
      //���ı���ֽڵ�Ķ��ӽڵ�����
      Object[] tempkeyset1 = new Object[max];
      Object[] tempnodelist1 = new Object[max];
      System.arraycopy(this.keyset, 0, tempkeyset1, 0, mid);
      System.arraycopy(this.sNodelist, 0, tempnodelist1, 0, mid);
      this.keyset = tempkeyset1;
      System.arraycopy(tempnodelist1, 0, this.sNodelist, 0, tempnodelist1.length);
      //���ı���ֵ��½ڵ����еĸ��ڵ�Ϊnewroot
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
        //�ҵ���һ����ֵ�ȴ������ֵ���λ��
        break;
      }
      count++;
    }
    //��������һ���ڵ��ֵ����,��˵���������ݼ���
    if (key.compareTo((V) this.keyset[this.num - 1]) > 0) {
      return null;
    }
    return this.sNodelist[count].delete(key);
  }
  //ɾ��һ���ڵ�
  public void deleteNode(Node n, V key) {
    //�ӱ��ڵ��keyset��snodelist��ɾ��n,��¼���ڵ��ڸ��ڵ��е�key�������ڸ��ڵ���ɾ�����ڵ�
    V old = null;
    if (this.num > 0) {
      old = (V) this.keyset[this.num - 1];
    }
    int count = 0;
    while (!n.equals(this.sNodelist[count])) {
      //�ҵ���ɾ���Ķ��ӽڵ�
      count++;
    }
    //ɾ���ýڵ�,���ɾ���󸸽ڵ㲻���ұ�ɾ���ڵ��Ǹ��ڵ�snodelist�����ģ�����ݹ���¸��㸸�ڵ��keyset�����Ҳ������
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
            //����ü�ֵ�Ǳ��ڵ������ֵ          
            int count_t = 0;
            while (count_t < tempfnode.num) {
              if (tempkey.compareTo((V) tempfnode.keyset[count_t]) == 0) {
                tempfnode.keyset[count_t] = tempnode.keyset[tempnode.num - 1];
                if (count_t == tempfnode.num - 1) {
                  //������ڵ��keyҲ�Ǹ��ڵ������ֵ
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
                //������ڵ��keyҲ�Ǹ��ڵ������ֵ
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
      //ɾ���ýڵ�
      ((TreeNode<T, V>) this.fNode).deleteNode(this, key);
    }
  }
}
