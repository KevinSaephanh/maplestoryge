package exceptions;

public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException(String e) {
		super(e);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
