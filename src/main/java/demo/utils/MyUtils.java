package demo.utils;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MyUtils {

    private MyUtils() {
        throw new IllegalStateException("MyUtils is a utility class, cannot be constructed");
    }


    public static void silentlySleep(long millis) {
        try {
            log.info("sleeping for {} millis", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
