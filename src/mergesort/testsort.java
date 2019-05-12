package mergesort;

import java.io.IOException;

public class testsort {
  
  public static void main(String[] args) throws IOException {
    // TODO Auto-generated method stub
    mergeSort ms=new mergeSort(16, 62500,1024*1024,16);
    ms.sort();
  }
  
}
