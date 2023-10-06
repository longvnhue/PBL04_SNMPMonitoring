package testing;

public class HextoString {
	public static void main(String[] args) {
        String hexString = "53:6f:66:74:77:61:72:65:20:4c:6f:6f:70:62:61:63:6b:20:49:6e:74:65:72:66:61:63:65:20:31:00";
        String[] hexArray = hexString.split(":");
        byte[] bytes = new byte[hexArray.length];

        for (int i = 0; i < hexArray.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }

        String result = new String(bytes);

        System.out.println("Chuỗi kí tự: " + result);
    }
	
	public static String deoi(String hexString) {
		String[] hexArray = hexString.split(":");
        byte[] bytes = new byte[hexArray.length];

        for (int i = 0; i < hexArray.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }

        String result = new String(bytes);
        return result;
	}
}
