package example_benchmark;

import com.ambrosebs.criterium_from_java.Criterium;

public class Main {
  public static void main(String[] args) {
    Criterium.bench(new Runnable() {
      @Override
      public void run() {
        java.util.UUID.randomUUID();
        ("string"+"concatenation"+"benchmark").length();
      }
    });
  }
}
