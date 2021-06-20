package Utils;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONValueReplacer {

    public static String createJSON(String filePath, List<String> replacers) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return json.replace("username", replacers.get(0)).replace("role", replacers.get(1));
    }

    public static String generateData() throws IOException {
        Faker faker = new Faker();
        List<String> replacingValues = new ArrayList<>();
        String username = faker.name().fullName();
        String role = faker.company().profession();
        replacingValues.add(username);
        replacingValues.add(role);
        return createJSON("src/main/resources/testdata.json", replacingValues);
    }
}
