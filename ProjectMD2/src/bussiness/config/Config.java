package bussiness.config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Config<T> {
    public static Scanner scanner() {
        return new Scanner(System.in);
    }
    // các biến hằng số cho đường dẫn các file
    public static final String URL_USERS = "./src/bussiness/data/users.txt";
    public static final String URL_USER_LOGIN = "./src/bussiness/data/userLogin.txt";
    public static final String URL_EXAMS = "./src/bussiness/data/exams.txt";
    public static final String URL_CATEGORY = "./src/bussiness/data/category.txt";
    public static final String URL_ANSWERS = "./src/bussiness/data/answers";
    public static final String URL_RESULTS = "./src/bussiness/data/results.txt";
    public static final String URL_RESULTDETAIL = "./src/bussiness/data/resultDetail.txt";

    // phương thức ghi, ghi 1 list đối tượng vào file
    public static <T> void writeFile(String PATH_FILE, List<T> t) {
        File file = new File(PATH_FILE);
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(t);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
// phương thức đọc dữ liệu từ file và trả về 1 list đối tượng
    public static <T> List<T> readData(String path){
        List<T> list = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{

            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            list = (List<T>) ois.readObject();

        }catch (EOFException e){

        }catch (IOException e){
            System.out.println("IO Exception");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois!=null){
                    ois.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return list;

    }
    // ghi và đọc file của đối tượng đăng nhập
    public static <T> void writeFileLogin(String PATH_FILE, T t) {
        File file = new File(PATH_FILE);
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(t);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    public static <T> T  readDataLogin(String path){
        T t = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{

            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            t = (T) ois.readObject();

        }catch (EOFException e){

        }catch (IOException e){
            System.out.println("IO Exception");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois!=null){
                    ois.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return t;

    }

}
