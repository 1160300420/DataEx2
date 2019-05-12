package BPlus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
  //���ļ�
  public static data[] gettxt(File file) {
     data[] datatemp=new data[1000000];
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
      String s = null;
      int count=0;
      while ((s = br.readLine()) != null&&(!s.isEmpty())) {//ʹ��readLine������һ�ζ�һ��
        String[] stemp=s.split("\t");
        datatemp[count] =new data<Integer,Integer>(Integer.parseInt(stemp[0]),count+1);
        count++;
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return datatemp;
  }
//���ļ�
  public static data<Integer,String>[] getfile(File file,int blocksize,int block) {
     data[] datatemp=new data[blocksize];
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
      String s = null;
      int count=0;
      while ((s = br.readLine()) != null&&(!s.isEmpty())) {//ʹ��readLine������һ�ζ�һ��
        String[] stemp=s.split("\t");
        datatemp[count] =new data<Integer,String>(Integer.parseInt(stemp[0]),stemp[1]);
        count++;
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return datatemp;
  }
  //���ļ�,ÿ�δ�ָ���ļ���ȡһ������
  public static ArrayList<data> readeach(File file,BufferedReader bufferedReader,int num,int seq) throws NumberFormatException, IOException {
    ArrayList<data> datatemp=new ArrayList<>();
    String s = null;
    int count=0;
    while ((s = bufferedReader.readLine()) != null&&(!s.isEmpty())) {//ʹ��readLine������һ�ζ�һ��
      String[] stemp=s.split("\t");
      datatemp.add(new data<Integer,String>(Integer.parseInt(stemp[0]),stemp[1]));
      count++;
      if(count==num) {
        break;
      }
    }
    if(count<num) {
      datatemp.add( new data<Integer,String>(100000000,"finish"));
    }
    return datatemp;
  }
  
  
  
  //д�ļ�
  public static void Write(String path,String s) throws IOException {
    FileOutputStream file = new FileOutputStream(path);
    file.write(s.getBytes());
    file.flush();
    file.close();
  }
  //�����ļ��ָ��setnum��С�ļ�
  public static void splitFile(File file,int originsize,int setnum) {
    int setsize=originsize/setnum+1;//����ÿ�����ļ��������ݵ���Ŀ
    String string=".\\src\\data";
    //�½�setnum���ļ�
    for(int i=0;i<setnum;i++) {
      File fileset=new File(string+i+".txt");
      if(!fileset.exists()) {
        try {
          fileset.createNewFile();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
      String s = null;
      int count=0;
      int name_count=0;
      FileOutputStream fileOutputStream = new FileOutputStream(string+name_count+".txt");
      while ((s = br.readLine()) != null&&(!s.isEmpty())) {//ʹ��readLine������һ�ζ�һ��
        s=s+"\n";
        fileOutputStream.write(s.getBytes());
        count++;
        if(count%(setsize-1)==0&&name_count<setnum) {
          //ÿ��setsize�����ݻ���һ���ļ�д�룬���ѵ�ǰ��������д�뵱ǰ�ļ�
          name_count++;
          fileOutputStream.flush();
          fileOutputStream.close();
          fileOutputStream = new FileOutputStream(string+name_count+".txt");
        }
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
