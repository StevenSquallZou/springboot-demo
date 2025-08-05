package demo.exception;


public class RedisLockException extends RuntimeException {

    public RedisLockException(String message) {
        super(message);
    }

}
