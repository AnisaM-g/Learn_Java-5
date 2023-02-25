package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }


    // метод создает клиента с дефолтными значениями, включая случайно созданный email
    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", "1234");
        data.put("username", "learqa");
        data.put("firstName", "learqa");
        data.put("lastName", "learqa");

        return data;
    }

    //метод, в который можно передать hashMap. В этом hashMap будут те данные,
    // которые мы хотим задать сами, а не получать дефолтные значения
    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defoltValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys){
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defoltValues.get(key));
            }
        }
        return userData;
    }


}
