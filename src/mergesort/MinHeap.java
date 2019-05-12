package mergesort;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;

import javax.activation.DataSource;

import BPlus.FileUtils;
import BPlus.data;

public class MinHeap <P extends Comparable<P>,Q>{
   private data<P, Q>[] datas;
   public MinHeap(data<P, Q>[] datas) {
       this.datas = datas;
   }

   public void createHeap() {
       for (int i = (datas.length) / 2 - 1; i >= 0; i--) {
           heapIfy(i);
       }
   }
   public data[] getdatas() {
     return this.datas;
   }
   public void heapIfy(int value) {
       int lchild = left(value);
       int rchild = right(value);
       int smallest = value;
       if (lchild < datas.length && (!compare(datas[lchild] ,datas[value])))
           smallest = lchild;
       if (rchild < datas.length &&( !compare(datas[rchild] ,datas[smallest])))
           smallest = rchild;
       if (value == smallest)
           return;
       swap(value, smallest);
       heapIfy(smallest);
   }

   public int left(int value) {
       return ((value + 1) << 1) - 1;
   }

   public int right(int value) {
       return (value + 1) << 1;
   }

   public void swap(int i, int j) {
       data<P, Q> tmp = datas[i];
       datas[i] = datas[j];
       datas[j] = tmp;
   }

   public void setRoot(data<P, Q> value) {
       datas[0] = value;
       heapIfy(0);
   }

   public boolean compare(data<P, Q> data1,data<P, Q> data2) {
     if(data1!=null&&data2!=null) {
     if((data1.getA()).compareTo(data2.getA())>0) {
       return true;
     }
     return false;
     }
     return true;
   }
   
   public static void gen(int blocksize,int blocknum) throws IOException {
       //依次读入每个文件（磁盘块）
       for(int i=0;i<blocknum;i++) {
         int count=blocksize;
         String filepath=".\\src\\data"+i+".txt";
         File tempfile=new File(filepath);
         data<Integer,String>[] tempvalue=FileUtils.getfile(tempfile,blocksize,i);
         StringBuilder stringBuilder=new StringBuilder();
         MinHeap<Integer,String> heap = new MinHeap<Integer, String>(tempvalue);
         heap.createHeap();
         while(count>=1) {
         stringBuilder.append(heap.datas[0].express()+"\n");
         heap.datas[0].setA(100000000);
         heap.setRoot(heap.datas[0]);
        // System.out.println("+++"+heap.datas[0].getA());
         count--;
         }
         FileUtils.Write(filepath, stringBuilder.toString());
         //System.out.println();//flush
       }
   }
}
