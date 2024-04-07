package example_benchmark;

import com.ambrosebs.criterium_from_java.Criterium;
import java.util.concurrent.Callable;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Map benchResults = Criterium.quickBench(new Callable<Integer>() {
      @Override
      public Integer call() throws InterruptedException {
        Thread.sleep(100);
        return 10*2;
      }
    });
    System.out.println("Results:");
    System.out.println(benchResults.toString());
  }
}
