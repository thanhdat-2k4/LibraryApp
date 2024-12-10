//tu dong dien
package com.example.oop25;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Lớp hỗ trợ tự động điền thông tin sách từ Google Books API.
 */
public class BookInfoFetcher {

    /**
     * Lấy thông tin sách từ Google Books API dựa trên ISBN.
     * @param isbn Mã ISBN của sách
     * @return Chuỗi thông tin sách theo định dạng: "Tên sách; Tác giả; Nhà xuất bản"
     */
    public static String fetchBookInfo(String isbn) {
        try {
            // Loại bỏ các ký tự không phải số từ ISBN (chỉ giữ lại số)
            isbn = isbn.replaceAll("[^\\d]", "");

            // Tạo URL API
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            URL url = new URL(apiUrl);

            // Mở kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // Thiết lập phương thức GET
            connection.connect();

            // Kiểm tra mã phản hồi HTTP
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                // Nếu phản hồi không thành công, in mã lỗi
                System.out.println("HTTP Error Code: " + responseCode);
                return null;
            }

            // Đọc dữ liệu trả về từ API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Chuyển chuỗi JSON thành đối tượng JSONObject
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");

            // Nếu tìm thấy sách, lấy thông tin chi tiết
            if (items != null && items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).optJSONObject("volumeInfo");

                if (volumeInfo == null) {
                    return null; // Nếu không có thông tin sách
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
                // Nếu không tìm thấy sách
                return null;
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có lỗi
            e.printStackTrace();
        }

        // Trả về null nếu có lỗi hoặc không tìm thấy thông tin
        return null;
    }
}
