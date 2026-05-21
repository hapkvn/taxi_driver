

---

# 📱 Android PHP API - XAMPP Backend Integration

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![PHP](https://img.shields.io/badge/PHP-777BB4?style=for-the-badge&logo=php&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)

Dự án này là một ví dụ hoàn chỉnh về cách kết nối ứng dụng Android (Frontend) với cơ sở dữ liệu MySQL thông qua API viết bằng PHP (Backend). Sử dụng thư viện **OkHttp** để xử lý các luồng gọi mạng (Network Requests).

---

## 🚀 Hướng Dẫn Cài Đặt (Getting Started)

Làm theo các bước dưới đây để thiết lập môi trường và chạy dự án trên máy cá nhân (Localhost).

### 1️⃣ Yêu Cầu Hệ Thống (Prerequisites)
* **XAMPP** (Môi trường chạy Apache & MySQL server).
* **Android Studio** (IDE để chạy code App).
* **Git** (để clone dự án).

---

### 2️⃣ Cài Đặt CSDL (Database Setup)
1. Mở **XAMPP Control Panel** và nhấn **Start** cho 2 module: `Apache` và `MySQL`.
2. Mở trình duyệt, truy cập vào trang quản lý: `http://localhost/phpmyadmin`
3. Tạo một database mới tên là: `android_api` (Bảng mã `utf8mb4_unicode_ci`).
4. Chạy đoạn SQL sau để tạo bảng dữ liệu người dùng (`users`):

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    point INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

```

---

### 3️⃣ Cấu Hình Backend (PHP API)

1. Truy cập vào thư mục `htdocs` của XAMPP:
* Windows: `C:\xampp\htdocs\`
* Linux/Ubuntu: `/opt/lampp/htdocs/`


2. Clone repository hoặc copy thư mục chứa API (ví dụ: `android_user_api`) vào trong `htdocs`.
3. Mở file `db_connect.php` và cấu hình mật khẩu MySQL cho khớp với máy của bạn:

```php
$server = "localhost"; // Đổi thành localhost:3307 nếu bạn bị trùng port
$user = "root";
$pass = "MẬT_KHẨU_CỦA_BẠN"; // Để trống "" nếu XAMPP chưa cài mật khẩu
$db = "android_api";

```

> 💡 **Tip:** Sử dụng [Thunder Client] hoặc [Postman] gọi thử phương thức POST tới `http://localhost/android_user_api/register.php` để chắc chắn API đã hoạt động trước khi chạy App.

---

### 4️⃣ Cấu Hình Frontend (Android Studio)

Vì Android và XAMPP chạy trên các môi trường cục bộ khác nhau, bạn cần đổi IP cho file gọi mạng. Mở dự án bằng Android Studio và cấu hình:

1. Mở file chứa link URL API (Ví dụ: `MainActivity.java` hoặc `Config.java`).
2. Sửa đường dẫn API cho phù hợp với môi trường test của bạn:
* **Nếu dùng Máy ảo (Emulator) của Android Studio:**
```java
public static final String API_URL = "[http://10.0.2.2/android_user_api/register.php](http://10.0.2.2/android_user_api/register.php)";

```


* **Nếu dùng Điện thoại thật (Cắm cáp / Wi-Fi Debugging):** Mở CMD gõ `ipconfig` lấy địa chỉ IPv4 và thay vào (Hai thiết bị phải chung mạng Wi-Fi):
```java
public static final String API_URL = "[http://192.168.1.](http://192.168.1.)X/android_user_api/register.php";

```




3. **Lưu ý Quan Trọng:** App yêu cầu quyền truy cập HTTP (Cleartext). Đảm bảo `AndroidManifest.xml` của bạn đã được cấp quyền:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<application
    android:usesCleartextTraffic="true"
    ...>

```



---

### 5️⃣ Chạy Ứng Dụng (Run Project)

1. Đảm bảo XAMPP vẫn đang chạy (Apache & MySQL màu xanh).
2. Kết nối máy ảo hoặc máy thật.
3. Nhấn nút **▶ Run (Shift + F10)** trên Android Studio.
4. Test thử tính năng **Đăng ký** trên App và mở lại phpMyAdmin để kiểm tra luồng dữ liệu bay vào database thành công! 🎉

---

## 🛠️ Công Nghệ Sử Dụng

* **Frontend:** Java (Android SDK), XML.
* **Network Library:** OkHttp3.
* **Backend:** PHP (Native), MySQL.

---

*Tác giả: [Huy Hoàng]*

```

```
