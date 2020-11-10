package main;

import chapin.ChapinMetrics;
import chapin.ChapinVariable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String file = "./res/Small.kt";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(file)))) {
            String s = br.readLine();
            while(s != null) {
                sb.append(s).append("\n");
                s = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("File error");
        }

        ChapinMetrics c = new ChapinMetrics(sb.toString());

    }
}
