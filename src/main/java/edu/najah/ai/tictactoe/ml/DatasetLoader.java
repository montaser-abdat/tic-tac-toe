package edu.najah.ai.tictactoe.ml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads training data from CSV file in resources.
 */
public class DatasetLoader {
    
    public static class DataLine {
        public double[] features;
        public double label; // +1 for X wins, -1 for O wins
        
        public DataLine(double[] features, double label) {
            this.features = features;
            this.label = label;
        }
    }
    
    /**
     * Load dataset from CSV file.
     * Expected columns: f1_X_count, f2_O_count, f3_X_almost_win, f4_O_almost_win, f5_X_center, f6_X_corners, label
     */
    public static List<DataLine> loadDataset(String resourcePath) throws IOException {
        List<DataLine> dataset = new ArrayList<>();
        
        InputStream is = DatasetLoader.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Dataset file not found: " + resourcePath);
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            // Skip header line
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IOException("Dataset file is empty");
            }
            
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                try {
                    String[] parts = line.split(",");
                    if (parts.length != 7) {
                        System.err.println("Warning: Skipping malformed line " + lineNumber + ": " + line);
                        continue;
                    }
                    
                    double[] features = new double[6];
                    for (int i = 0; i < 6; i++) {
                        features[i] = Double.parseDouble(parts[i].trim());
                    }
                    
                    double label = Double.parseDouble(parts[6].trim());
                    dataset.add(new DataLine(features, label));
                    
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Skipping invalid line " + lineNumber + ": " + line);
                }
            }
        }
        
        System.out.println("Loaded " + dataset.size() + " training examples from " + resourcePath);
        return dataset;
    }
}
