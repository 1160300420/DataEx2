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
  private int blocknum;//���̿���Ŀ
  private int blocksize;//���̿�����ļ�¼��
  private int mergenum = 4;//·��
  private int mem;//�ڴ��С
  private int recordsize;//ÿ����¼�Ĵ�С
  public mergeSort(int n, int blocksize, int mem, int recordsize) {
    this.blocknum = n;
    this.blocksize = blocksize;
    this.mem = mem;
    this.recordsize = recordsize;
  }
  //��·�鲢��Ĭ��4·
  public void merge_sort() throws NumberFormatException, IOException {
    int fornum = (int) (Math.log(blocknum) / Math.log(mergenum));
    System.out.println("�鲢ѭ��������" + fornum);
    
    //�½��ļ�����鲢���̲���������
    int tempblocknum = blocknum;
    int tempfilenum = 0;
    while (tempblocknum != 1) {
      tempfilenum += tempblocknum / mergenum;
      tempblocknum = tempblocknum / mergenum;
    }
    String string = ".\\src\\tempdata";
    String string2 = ".\\src\\data";
    //�½�tempfilenum���ļ�
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
    int filenum_count = blocknum / mergenum;//���ڼ���ڶ������Ϲ鲢tempfile��name
    int temp = 0;
    for (int i = 0; i < fornum; i++) {
      //ִ�й鲢����
      int needfilenum = (int) (blocknum / Math.pow(mergenum, i + 1));//ÿ�ι鲢��Ҫ���ļ���,Ҳ�Ǳ��ι鲢��Ҫ���ú����Ĵ���
      if (i == 0) {
        //��һ�ֹ鲢
        for (int h = 0; h < needfilenum; h++) {
          for (int j = 0; j < mergenum; j++) {
            reFiles[j] = new File(string2 + (h * mergenum + j) + ".txt");
          }
          each_merge_sort(reFiles, string + h + ".txt");
        }
      } else {
        //�����鲢,tempfile�ı�� ��blocknum/mergenum��ʼ
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
  //reFilesΪ�鲢����������ļ�����,filepathΪ����ļ�·��
  public void each_merge_sort(File[] reFiles, String filepath) throws NumberFormatException, IOException {
    Map<File, BufferedReader> br_map = new HashMap<>();//��¼ÿ����֧��br
    int eachmerge = (mem / recordsize) / (mergenum + 1);//����ÿ���鲢��֧ÿ�ζ���ļ�¼��
    System.out.println("�鲢��֧ÿ�ζ���ļ�¼����" + eachmerge);
    Map<Integer, ArrayList<data>> map = new HashMap<>();//�½�ÿ����֧���뼰�������
    ArrayList<data> output = new ArrayList<>();//���������
    Map<data, Integer> markmap=new HashMap<>();//��¼ÿ��data�����ĸ���֧
    FileOutputStream fileOutputStream = new FileOutputStream(filepath);
    //��ʼ��
    for (int i = 0; i < mergenum; i++) {
      br_map.put(reFiles[i], new BufferedReader(new FileReader(reFiles[i])));
      System.out.println(reFiles[i].getPath());
      map.put(i, FileUtils.readeach(reFiles[i], br_map.get(reFiles[i]), eachmerge, i));
    }
    //��С�ѽ�������
    data[] value = new data[mergenum];
    for (int i = 0; i < mergenum; i++) {
      //ȡÿ����֧����Сֵ
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
      //�����֧�����뻺�������ˣ��Ӹ÷�֧��Ӧ���ļ�����һ������
      if (map.get(thismerge).size() == 0) {
        //���һ����֧���ڴ沿���Ѿ����겢�͵��������������Ϊ�÷�֧����
        map.put(thismerge,
            FileUtils.readeach(reFiles[thismerge], br_map.get(reFiles[thismerge]), eachmerge, thismerge));
      //  System.out.println("read again");
      }
      markmap.put(map.get(thismerge).get(0), thismerge);
      heap.setRoot(map.get(thismerge).get(0));
      if (output.size() == eachmerge) {
        //���������������ˣ���д��Ŀ���ļ�
        Iterator<data> daIterator = output.iterator();
        while (daIterator.hasNext()) {
          fileOutputStream.write((daIterator.next().express() + "\n").getBytes());
        }
       // System.out.println("д��");
        output=new ArrayList<>();
      }
      if (((Integer) value[0].getA()) == 100000000) {
        Iterator<data> daIterator = output.iterator();
        while (daIterator.hasNext()) {
          fileOutputStream.write((daIterator.next().express() + "\n").getBytes());
        }
       // System.out.println("д��");
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
    //��С���㷨����˳��
    long startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��
    MinHeap.gen(blocksize, blocknum);
    long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
    System.out.println("��������ʱ�䣺 "+(endTime-startTime)+"ms");
    //��·�鲢
    startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��
    merge_sort();
    endTime=System.currentTimeMillis(); //��ȡ����ʱ��
    System.out.println("�鲢����ʱ�䣺 "+(endTime-startTime)+"ms");
  }
}
