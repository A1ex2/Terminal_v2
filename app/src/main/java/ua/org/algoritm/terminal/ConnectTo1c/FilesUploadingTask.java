package ua.org.algoritm.terminal.ConnectTo1c;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FilesUploadingTask extends AsyncTask<Void, Void, String> {

    // Конец строки
    private String lineEnd = "\r\n";
    // Два тире
    private String twoHyphens = "==";
    // Разделитель
    private String boundary = "==WebKitFormBoundary9xFB2hiUhzqbBQ4M";

    // Переменные для считывания файла в оперативную память
    private int bytesRead, bytesAvailable, bufferSize;
    private byte[] buffer;
    private int maxBufferSize = 1 * 1024 * 1024;

    // Путь к файлу в памяти устройства
    private String filePath;

    // авторизация
    private String username;
    private String password;


    // Адрес метода api для загрузки файла на сервер
    public static final String API_FILES_UPLOADING_PATH = "http://192.168.1.10/blg_log/hs/upload_photo/upload";

    // Ключ, под которым файл передается на сервер
    public static final String FORM_FILE_NAME = "file1";

    public FilesUploadingTask(String filePath, String username, String password) {
        this.filePath = filePath;
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... params) {
        // Результат выполнения запроса, полученный от сервера
        String result = null;

        try {
            // Создание ссылки для отправки файла
            URL uploadUrl = new URL(API_FILES_UPLOADING_PATH);

            // Создание соединения для отправки файла
            HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();

            // Разрешение ввода соединению
            connection.setDoInput(true);
            // Разрешение вывода соединению
            connection.setDoOutput(true);
            // Отключение кеширования
            connection.setUseCaches(false);

            // Задание запросу типа POST
            connection.setRequestMethod("POST");

            // авторизация
            String userpass = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);
            connection.setReadTimeout(50000);
            connection.setConnectTimeout(50000);
            connection.setRequestProperty("Authorization", auth);
            //connection.connect();

            // Задание необходимых свойств запросу
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            File file = new File(filePath);
            String fileName = file.getName();
            connection.setRequestProperty("fileName", fileName);

            // Создание потока для записи в соединение
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            // Формирование multipart контента

            // Начало контента
//            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            // Заголовок элемента формы
//            outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
//                    FORM_FILE_NAME + "\"; filename=\"" + filePath + "\"" + lineEnd);
            // Тип данных элемента формы
//            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            // Конец заголовка
//            outputStream.writeBytes(lineEnd);

            // Поток для считывания файла в оперативную память

            FileInputStream fileInputStream = new FileInputStream(file);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Считывание файла в оперативную память и запись его в соединение
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // Конец элемента формы
//            outputStream.writeBytes(lineEnd);
//            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Получение ответа от сервера
            int serverResponseCode = connection.getResponseCode();

            // Закрытие соединений и потоков
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            // Считка ответа от сервера в зависимости от успеха
            if (serverResponseCode == 200) {
                result = readStream(connection.getInputStream());
            } else {
                result = readStream(connection.getErrorStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Считка потока в строку
    public static String readStream(InputStream inputStream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        String string = buffer.toString();
        reader.close();
        return string;
    }
}
