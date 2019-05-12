package BPlus;

import java.io.IOException;
import java.util.Random;

public class GenRandom {
  public static void gen(int n) {
    StringBuilder string = new StringBuilder();
    String characters1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    int[] x = new int[n];
    for (int i = 0; i < n; i++) {
      x[i] = i;
    }
    Random r = new Random();
    for (int i = 0; i < n; i++) {
      int in = r.nextInt(n - i) + i;
      int t = x[in];
      x[in] = x[i];
      x[i] = t;
    }
    for (int i = 0; i < n; i++) {
      string.append(x[i] + "\t");
      Random random = new Random();
      for (int j = 0; j < 12; j++) {
        string.append(characters1.charAt(random.nextInt(36)));
      }
      string.append("\n");
    }
    String ss = string.toString();
    try {
      FileUtils.Write("data.txt", ss);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    
    double b = 9.7;
    
    int a = (int) b;
    
    String c = b + "";
    
    String d = c.substring(c.indexOf("."), c.length());
    
    System.out.println(a);
    
    System.out.println(d);
    
  }
}

