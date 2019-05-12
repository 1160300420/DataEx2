package BPlus;

public class data<P extends Comparable<P>,Q>{
  private P a;
  private Q b;
  public data(P aa,Q bb) {
    this.a=aa;
    this.b=bb;
  }
  public P getA() {
    return a;
  }
  public void setA(P newa) {
    this.a=newa;
  }
  public Q getB(){
    return b;
  }
  public String express() {
    return a+"\t"+b;
  }

}