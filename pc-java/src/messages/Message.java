package messages;

public abstract class Message 
{
	public static final char PING_FLAG = 'P';
	public static final char ROTATE_FLAG = 'R';
	public static final char BEGIN_STATIONARY_OPERATION_COMMAND_FLAG = 'B';
	public static final char END_STATIONARY_OPERATION_COMMAND_FLAG = 'E';
	public static final char STIR_FLAG = 'S';
	public static final char ICE_FLAG = 'I';
	public static final char BOTTLE_DELIMITER = ','; 
	public static final char OPERATION_COMPLETE_FLAG = 'C';
	public static final char PING_RESPONSE_FLAG = 'A';
	
	public abstract String getStringRepresentation();

}
