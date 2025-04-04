/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Controller;




public class Controller {
    private static final String nameRegex = "^[A-Za-zÀ-Ỹà-ỹ]+(?:[-'][A-Za-zÀ-Ỹà-ỹ]+)?( [A-Za-zÀ-Ỹà-ỹ]+(?:[-'][A-Za-zÀ-Ỹà-ỹ]+)?)+$";
    private static final String phoneRegex = "^0\\d{9}$";

    public static boolean checkSalary(float salary) { return salary >= 0 ; }
    public static boolean checkValidName(String name){
        return name.matches(nameRegex);
    }
    
    public static boolean checkValidPhone(String phone){
        return phone.matches(phoneRegex);
    }
    
    public static String formatFullName(String name){
        String s = "";
        name = name.trim().replace("\\s+", " ");
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
        result.append(Character.toUpperCase(word.charAt(0))) // Viết hoa chữ cái đầu
                     .append(word.substring(1).toLowerCase()) // Chuyển các chữ cái còn lại thành chữ thường
                     .append(" "); // Thêm khoảng trắng giữa các từ
        }
        return result.toString().trim();
    }
}
