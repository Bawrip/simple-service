package simple.response;

public class ResponseDto<T> {
    private ResponseStatus status;
    private T data;

    public ResponseDto() {
        status = null;
        data = null;
    }

    public ResponseDto(ResponseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public static <T> ResponseDto of(ResponseStatus status, T data) {
        return new ResponseDto(status, data);
    }

    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.of(ResponseStatus.OK, data);
    }

    public static ResponseDto<Void> success() {
        return ResponseDto.of(ResponseStatus.OK, null);
    }

    public boolean hasFailed() {
        return ResponseStatus.ERROR == status;
    }

    public boolean hasSucceeded() {
        return ResponseStatus.OK == status;
    }
}
