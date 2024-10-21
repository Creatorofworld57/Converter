package ex.springsecurity_1805.services;

import ex.springsecurity_1805.Models.Img;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServiceHelperFunctions {
    public static Img toImgEntity(MultipartFile file) throws IOException {
        Img img = new Img();
        img.setName(file.getName());
        img.setOriginalFileName(file.getOriginalFilename());
        img.setContentType(file.getContentType());
        img.setSize(file.getSize());
        img.setBytes(file.getBytes());
        return img;

    }
    // Алгоритм Левенштейна для подсчёта расстояния между строками
    public static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
    //Перевод русской раскладки на английский

    public static class KeyboardLayoutConverter {

        // Создаем мапу для сопоставления русских символов с английскими
        private static final Map<Character, Character> layoutMap = new HashMap<>();

        static {
            layoutMap.put('й', 'q');
            layoutMap.put('ц', 'w');
            layoutMap.put('у', 'e');
            layoutMap.put('к', 'r');
            layoutMap.put('е', 't');
            layoutMap.put('н', 'y');
            layoutMap.put('г', 'u');
            layoutMap.put('ш', 'i');
            layoutMap.put('щ', 'o');
            layoutMap.put('з', 'p');
            layoutMap.put('х', '[');
            layoutMap.put('ъ', ']');
            layoutMap.put('ф', 'a');
            layoutMap.put('ы', 's');
            layoutMap.put('в', 'd');
            layoutMap.put('а', 'f');
            layoutMap.put('п', 'g');
            layoutMap.put('р', 'h');
            layoutMap.put('о', 'j');
            layoutMap.put('л', 'k');
            layoutMap.put('д', 'l');
            layoutMap.put('ж', ';');
            layoutMap.put('э', '\'');
            layoutMap.put('я', 'z');
            layoutMap.put('ч', 'x');
            layoutMap.put('с', 'c');
            layoutMap.put('м', 'v');
            layoutMap.put('и', 'b');
            layoutMap.put('т', 'n');
            layoutMap.put('ь', 'm');
            layoutMap.put('б', ',');
            layoutMap.put('ю', '.');
            // Добавляем заглавные буквы
            layoutMap.put('Й', 'Q');
            layoutMap.put('Ц', 'W');
            layoutMap.put('У', 'E');
            layoutMap.put('К', 'R');
            layoutMap.put('Е', 'T');
            layoutMap.put('Н', 'Y');
            layoutMap.put('Г', 'U');
            layoutMap.put('Ш', 'I');
            layoutMap.put('Щ', 'O');
            layoutMap.put('З', 'P');
            layoutMap.put('Х', '{');
            layoutMap.put('Ъ', '}');
            layoutMap.put('Ф', 'A');
            layoutMap.put('Ы', 'S');
            layoutMap.put('В', 'D');
            layoutMap.put('А', 'F');
            layoutMap.put('П', 'G');
            layoutMap.put('Р', 'H');
            layoutMap.put('О', 'J');
            layoutMap.put('Л', 'K');
            layoutMap.put('Д', 'L');
            layoutMap.put('Ж', ':');
            layoutMap.put('Э', '"');
            layoutMap.put('Я', 'Z');
            layoutMap.put('Ч', 'X');
            layoutMap.put('С', 'C');
            layoutMap.put('М', 'V');
            layoutMap.put('И', 'B');
            layoutMap.put('Т', 'N');
            layoutMap.put('Ь', 'M');
            layoutMap.put('Б', '<');
            layoutMap.put('Ю', '>');
        }

        // Функция для конвертации строки с русской раскладки в английскую
        public static String convertToEnglish(String text) {
            StringBuilder result = new StringBuilder();

            for (char ch : text.toCharArray()) {
                // Если символ есть в мапе, заменяем его, иначе оставляем как есть
                result.append(layoutMap.getOrDefault(ch, ch));
            }

            return result.toString();
        }


    }
}
