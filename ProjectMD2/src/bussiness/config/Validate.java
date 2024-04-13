package bussiness.config;

public class Validate {
    public static int validateInt() {
        int n;
        while (true) {
            try {
                n = Integer.parseInt(Config.scanner().nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Sai định dạng, mời nhập lại.");
            }
        }
        return n;
    }
    public static String validateString() {
        String s;
        while (true) {
            s = Config.scanner().nextLine();
            if (s.isEmpty()) {
                System.out.println("Không được để trống, mời nhập lại.");
            } else {
                break;
            }
        }
        return s;
    }
}
