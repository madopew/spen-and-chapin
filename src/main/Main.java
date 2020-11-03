package main;

import spen.SpenMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

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

        SpenMetrics s = new SpenMetrics(sb.toString());
        Map<String, Integer> spens = s.getSpens();
        spens.forEach((key, value) -> System.out.printf("(%s; %d)\n", key, value));
    }
}
