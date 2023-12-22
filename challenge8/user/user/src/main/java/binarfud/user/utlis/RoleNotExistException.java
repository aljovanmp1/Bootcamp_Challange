package binarfud.user.utlis;

public class RoleNotExistException extends Exception{
    public RoleNotExistException(String message){
        super(message);
    }
}
