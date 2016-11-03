package main;

@SuppressWarnings("serial")
public class InvalidSizeEx extends Exception {
	private int expected;
	private int found;
	
	public InvalidSizeEx(int expected, int found) {
		this(expected, found, null);
	}
	
	public InvalidSizeEx(int expected, int found, String msg) {
		super("Invalid size, expected "+expected+", found "+found+(msg!=null?" : "+msg:""));
		this.expected = expected;
		this.found = found;
	}
	
	public int getExpectedSize() {
		return expected;
	}
	
	public int getFoundSize() {
		return found;
	}
}
