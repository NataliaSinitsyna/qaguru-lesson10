import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class CreateAndReadZip {

    ClassLoader cl = CreateAndReadZip.class.getClassLoader();

    @Test
    void zipFiles() throws Exception {
        File zip = new File("src/test/resources/test.zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        ZipEntry pdf = new ZipEntry("reqpreview.pdf");
        ZipEntry xlsx = new ZipEntry("Стратегия тестирования.xlsx");
        ZipEntry csv = new ZipEntry("sample1.csv");
        out.putNextEntry(pdf);
        out.putNextEntry(xlsx);
        out.putNextEntry(csv);
        out.closeEntry();
        out.close();
    }


    @Test
    void readFilesInZip() throws Exception {

        try (
                ZipInputStream is = new ZipInputStream(Objects.requireNonNull(cl.getResourceAsStream("test.zip")))
        ) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                switch (entry.getName()) {
                    case "Стратегия тестирования.xlsx" -> {
                        try (InputStream stream = getClass().getClassLoader()
                                .getResourceAsStream("Стратегия тестирования.xlsx")) {
                            assert stream != null;
                            XLS xls = new XLS(stream);
                            assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(2)
                                    .getStringCellValue()).contains("Стратегия");
                        }
                    }
                    case "reqpreview.pdf" -> {
                        try (InputStream stream = getClass().getClassLoader()
                                .getResourceAsStream("reqpreview.pdf")) {
                            assert stream != null;
                            PDF content = new PDF(stream);
                            assertThat(content.text).contains("2310235603");
                        }
                    }
                    case "sample1.csv" -> {
                        try (InputStream stream = getClass().getClassLoader()
                                .getResourceAsStream("sample1.csv")) {
                            assert stream != null;
                            try (CSVReader reader = new CSVReader(new InputStreamReader(stream,
                                    StandardCharsets.UTF_8))) {
                                List<String[]> content = reader.readAll();
                                org.assertj.core.api.Assertions.assertThat(content.get(0)[0]).contains("Month");
                            }
                        }
                    }
                }
            }
        }
    }
}
