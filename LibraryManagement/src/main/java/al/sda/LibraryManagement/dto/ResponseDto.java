package al.sda.LibraryManagement.dto;

public class ResponseDto {
    private String message;
    private Boolean success;
    
    public ResponseDto() {
    }
    
    public ResponseDto(Boolean success,String message) {
        this.message = message;
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    @Override
    public String toString() {
        return "ResponseDto{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
