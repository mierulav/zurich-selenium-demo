package utilities;

import com.opencsv.CSVReader;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads login test data from a CSV file so TC001_Login can run the same
 * workflow against multiple credential combinations without duplicating
 * test methods (SUCCESS / LOCKED_OUT / INVALID_CREDENTIALS scenarios).
 */
public class CsvDataProvider {

    private static final String CSV_PATH = "src/main/resources/testdata/logins.csv";

    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        List<String[]> rows = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // skip "username,password,expectedResult"
                    continue;
                }
                rows.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read login test data from " + CSV_PATH, e);
        }

        Object[][] data = new Object[rows.size()][3];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0]; // username
            data[i][1] = rows.get(i)[1]; // password
            data[i][2] = rows.get(i)[2]; // expectedResult
        }
        return data;
    }
}
