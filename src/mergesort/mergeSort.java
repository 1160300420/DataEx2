package mergesort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import BPlus.FileUtils;
import BPlus.data;

public class mergeSort {
  private int blocknum;//磁盘块数目
  private int blocksize;//磁盘块包含的记录数
  private int mergenum = 4;//路数
  private int mem;//内存大小
  private int recordsize;//每个记录的大小
  public mergeSort(int n, int blocksize, int mem, int recordsize) {
    this.blocknum = n;
    this.blocksize = blocksize;
    this.mem = mem;
    this.recordsize = recordsize;
  }
  //多路归并，默认4路
  public void merge_sort() throws NumberFormatException, IOException {
    int fornum = (int) (Math.log(blocknum) / Math.log(mergenum));
    System.out.println("归并循环次数：" + fornum);
    
    //新建文件保存归并过程产生的数据
    int tempblocknum = blocknum;
    int tempfilenum = 0;
    while (tempblocknum != 1) {
      tempfilenum += tempblocknum / mergenum;
      tempblocknum = tempblocknum / mergenum;
    }
    String string = ".\\src\\tempdata";
    String string2 = ".\\src\\data";
    //新建tempfilenum个文件
    for (int i = 0; i < tempfilenum; i++) {
      File fileset = new File(string + i + ".txt");
      if (!fileset.exists()) {
        try {
          fileset.createNewFile();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    File[] reFiles = new File[mergenum];
    int filenum_count = blocknum / mergenum;//用于计算第二次以上归并tempfile的name
    int temp = 0;
    for (int i = 0; i < fornum; i++) {
      //执行归并排序
      int needfilenum = (int) (blocknum / Math.pow(mergenum, i + 1));//每次归并需要的文件数,也是本次归并需要调用函数的次数
      if (i == 0) {
        //第一轮归并
        for (int h = 0; h < needfilenum; h++) {
          for (int j = 0; j < mergenum; j++) {
            reFiles[j] = new File(string2 + (h * mergenum + j) + ".txt");
          }
          each_merge_sort(reFiles, string + h + ".txt");
        }
      } else {
        //继续归并,tempfile的标号 从blocknum/mergenum开始
        for (int h = 0; h < needfilenum; h++) {
          for (int j = 0; j < mergenum; j++) {
            reFiles[j] = new File(string + (temp + h * mergenum + j) + ".txt");
          }
          each_merge_sort(reFiles, string + (h + filenum_count) + ".txt");
        }
        temp += filenum_count;
        filenum_count += filenum_count / mergenum;
        if (needfilenum == 1) {
          break;
        }
      }
    }
  }
  //reFiles为归并排序输入的文件集合,filepath为输出文件路径
  public void each_merge_sort(File[] reFiles, String filepath) throws NumberFormatException, IOException {
    Map<File, BufferedReader> br_map = new HashMap<>();//记录每个分支的br
    int eachmerge = (mem / recordsize) / (mergenum + 1);//计算每个归并分支每次读入的记录数
    System.out.println("归并分支每次读入的记录数：" + eachmerge);
    Map<Integer, ArrayList<data>> map = new HashMap<>();//新建每个分支输入及输出数组
    ArrayList<data> output = new ArrayList<>();//输出缓冲区
    Map<data, Integer> markmap=new HashMap<>();//记录每个data属于哪个分支
    FileOutputStream fileOutputStream = new FileOutputStream(filepath);
    //初始化
    for (int i = 0; i < mergenum; i++) {
      br_map.put(reFiles[i], new BufferedReader(new FileReader(reFiles[i])));
      System.out.println(reFiles[i].getPath());
      map.put(i, FileUtils.readeach(reFiles[i], br_map.get(reFiles[i]), eachmerge, i));
    }
    //最小堆进行排序
    data[] value = new data[mergenum];
    for (int i = 0; i < mergenum; i++) {
      //取每个分支的最小值
      value[i] = map.get(i).get(0);
      markmap.put(value[i], i);
    //  System.out.print(value[i].getA() + " ");
    }
   // System.out.println();
    MinHeap<Integer, String> heap = new MinHeap<Integer, String>(value);
    heap.createHeap();
    while (true) {
      value = heap.getdatas();
      output.add(value[0]);
      int thismerge=markmap.get(value[0]);
      map.get(thismerge).remove(value[0]);
      markmap.remove(value[0]);
      //如果分支的输入缓冲区空了，从该分支对应的文件读入一组数据
      if (map.get(thismerge).size() == 0) {
        //如果一个分支的内存部分已经排完并送到输出缓冲区，则为该分支读入
        map.put(thismerge,
            FileUtils.readeach(reFiles[thismerge], br_map.get(reFiles[thismerge]), eachmerge, thismerge));
      //  System.out.println("read again");
      }
      markmap.put(map.get(thismerge).get(0), thismerge);
      heap.setRoot(map.get(thismerge).get(0));
      if (output.size() == eachmerge) {
        //如果输出缓冲区满了，则写入目标文件
        Iterator<data> daIterator = output.iterator();
        while (daIterator.hasNext()) {
          fileOutputStream.write((daIterator.next().express() + "\n").getBytes());
        }
       // System.out.println("写入");
        output=new ArrayList<>();
      }
      if (((Integer) value[0].getA()) == 100000000) {
        Iterator<data> daIterator = output.iterator();
        while (daIterator.hasNext()) {
          fileOutputStream.write((daIterator.next().express() + "\n").getBytes());
        }
       // System.out.println("写入");
        break;
      }
    }
    for (int i = 0; i < mergenum; i++) {
      br_map.get(reFiles[i]).close();
    }
    fileOutputStream.flush();
    fileOutputStream.close();
  }
  public void sort() throws IOException {
    //最小堆算法产生顺串
    long startTime=System.currentTimeMillis();   //获取开始时间
    MinHeap.gen(blocksize, blocknum);
    long endTime=System.currentTimeMillis(); //获取结束时间
    System.out.println("内排运行时间： "+(endTime-startTime)+"ms");
    //多路归并
    startTime=System.currentTimeMillis();   //获取开始时间
    merge_sort();
    endTime=System.currentTimeMillis(); //获取结束时间
    System.out.println("归并运行时间： "+(endTime-startTime)+"ms");
  }
}
