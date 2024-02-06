package dnd.donworry.domain.constants;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResResult <T>{

    private ErrorCode errorCode;
    private ResponseCode responseCode;
    private String code;
    private String message;
    private T data;

    @Override
    public String toString() {
        return "ResResult{" +
                "responseCode=" + responseCode +
                ", code='" + code +'\'' +
                ", message='" + message +'\'' +
                ", data=" +data +
                '}';
    }
}
