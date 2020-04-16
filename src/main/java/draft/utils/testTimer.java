package draft.utils;

public class testTimer {
    public testTimer() throws InterruptedException {
        final long start = System.nanoTime();
        Thread.sleep(100);
        final long end = System.nanoTime();

        System.out.println("Took: " + ((end - start) / 1000000) + "ms");
        System.out.println("Took: " + (double)(end - start)/ 1000000000 + " seconds");


        long start2 = System.currentTimeMillis();
        Thread.sleep(100);
        long end2 = System.currentTimeMillis();
        System.out.println("Took: " + ((end2 - start2)) + "ms");
        System.out.println("Took: " + (double)(end2 - start2)/ 1000 + " seconds");
    }
}
