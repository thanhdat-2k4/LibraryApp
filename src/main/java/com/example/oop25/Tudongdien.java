package com.example.oop25;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//public class Tudongdien {
//
//    public static String Tudongdienthongtin(String isbn) {
//        try {
//            isbn = isbn.replaceAll("[^\\d]", "");
//
//            // Xây dựng URL API
//            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
//            URL url = new URL(apiUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.connect();
//
//            // Kiểm tra mã phản hồi HTTP
//            int responseCode = conn.getResponseCode();
//            if (responseCode != 200) {
//                // Nếu không nhận được phản hồi thành công từ API
//                System.out.println("HTTP Error Code: " + responseCode);
//                return null;
//            }
//
//            // Đọc dữ liệu trả về từ API
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = in.readLine()) != null) {
//                response.append(line);
//            }
//            in.close();
//
//            // Chuyển đổi chuỗi JSON thành đối tượng JSONObject
//            JSONObject jsonResponse = new JSONObject(response.toString());
//            JSONArray items = jsonResponse.optJSONArray("items");
//
//            // Nếu tìm thấy ít nhất một sách, lấy thông tin
//            if (items != null && items.length() > 0) {
//                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
//
//                // Lấy thông tin từ JSON
//                String title = volumeInfo.optString("title", "N/A");
//                String author = volumeInfo.optJSONArray("authors") != null
//                        ? volumeInfo.getJSONArray("authors").join(", ").replace("\"", "")
//                        : "N/A";
//                String publisher = volumeInfo.optString("publisher", "N/A");
//
//                // Trả về thông tin sách theo định dạng: Tên sách; Tác giả; Nhà xuất bản
//                return title + ";" + author + ";" + publisher;
//            } else {
//                // Nếu không tìm thấy sách, trả về null
//                return null;
//            }
//        } catch (Exception e) {
//            // In ra lỗi nếu có ngoại lệ
//            e.printStackTrace();
//        }
//        return null; // Trả về null nếu có lỗi hoặc không tìm thấy thông tin
//    }
//}
public class Tudongdien {

    public static String Tudongdienthongtin(String isbn) {
        try {
            // Loại bỏ các ký tự không phải số từ ISBN (chỉ giữ lại số)
            isbn = isbn.replaceAll("[^\\d]", "");

            // Xây dựng URL API
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Kiểm tra mã phản hồi HTTP
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                // Nếu không nhận được phản hồi thành công từ API
                System.out.println("HTTP Error Code: " + responseCode);
                return null;
            }

            // Đọc dữ liệu trả về từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Chuyển đổi chuỗi JSON thành đối tượng JSONObject
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");

            // Nếu tìm thấy ít nhất một sách, lấy thông tin
            if (items != null && items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).optJSONObject("volumeInfo");

                if (volumeInfo == null) {
                    return null;  // Nếu không có thông tin sách
                }

                // Lấy thông tin từ JSON
                String title = volumeInfo.optString("title", "N/A");
                String author = volumeInfo.optJSONArray("authors") != null
                        ? volumeInfo.getJSONArray("authors").join(", ").replace("\"", "")
                        : "N/A";
                String publisher = volumeInfo.optString("publisher", "N/A");

                // Trả về thông tin sách theo định dạng: Tên sách; Tác giả; Nhà xuất bản
                return title + ";" + author + ";" + publisher;
            } else {
                // Nếu không tìm thấy sách, trả về null
                return null;
            }
        } catch (Exception e) {
            // In ra lỗi nếu có ngoại lệ
            e.printStackTrace();
        }
        return null; // Trả về null nếu có lỗi hoặc không tìm thấy thông tin
    }
}
