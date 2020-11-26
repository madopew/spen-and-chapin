package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Spen-Chapin-Metrics.fxml"));
        primaryStage.setTitle("Spen and Chapin metrics");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static Stage getStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/*public class Main {
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
        Map<GroupType, List<String>> t = c.getChapinTypes();
        System.out.println(t);
    }
}
*/