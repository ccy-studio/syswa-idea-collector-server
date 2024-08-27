package cn.saisiawa.ideacollector.common.exception;

/**
 * JSON 反序列化异常
 */
public class JsonDeserializeException extends RuntimeException {

    public JsonDeserializeException(Throwable cause) {
        super(cause);
    }
}
