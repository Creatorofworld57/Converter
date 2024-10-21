package ex.springsecurity_1805.services;

import java.util.HashMap;
import java.util.Map;

public class AlgorithmsOfRecomendations {
    public static class ContentBasedRecommender {

        // Метод для расчета косинусного сходства между двумя песнями
        public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;
            for (int i = 0; i < vectorA.length; i++) {
                dotProduct += vectorA[i] * vectorB[i];
                normA += Math.pow(vectorA[i], 2);
                normB += Math.pow(vectorB[i], 2);
            }
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }


        // Пример рекомендации песни на основе похожести
        public static void main(String[] args) {
            // Пример данных: каждая песня представлена вектором характеристик
            Map<String, double[]> songFeatures = new HashMap<>();
            songFeatures.put("Song1", new double[]{1, 0, 0, 1}); // жанр, темп, тональность и т.д.
            songFeatures.put("Song2", new double[]{0, 1, 1, 0});
            songFeatures.put("Song3", new double[]{1, 1, 0, 1});

            // Песня, для которой ищем рекомендации
            double[] targetSong = new double[]{1, 0, 1, 1};

            // Поиск песни с максимальным косинусным сходством
            String bestMatch = null;
            double maxSimilarity = -1;
            for (Map.Entry<String, double[]> entry : songFeatures.entrySet()) {
                double similarity = cosineSimilarity(targetSong, entry.getValue());
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    bestMatch = entry.getKey();
                }
            }

            System.out.println("Recommended song: " + bestMatch);
        }
    }
}
