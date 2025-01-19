package reactive.java.moviesservice.exception;

public class MoviesInfoClientException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public MoviesInfoClientException(String message, Integer statusCode) {
    this.message = message;
    this.statusCode = statusCode;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }
}
