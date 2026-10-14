package br.com.latanks.finances.utils.csv;

import br.com.latanks.finances.exception.impl.InvalidBankCSVFormat;
import br.com.latanks.finances.models.Moviment;
import br.com.latanks.finances.models.dto.request.CreateMovimentDTO;
import br.com.latanks.finances.models.dto.response.CreatedMovimentDTO;
import br.com.latanks.finances.models.enums.MovimentType;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class CsvReaderUtil {

    private final File file;
    private final Banks bank;
    private List<CreateMovimentDTO> createMoviments;

    public CsvReaderUtil(File file, Banks bank) {
        this.file = file;
        this.bank = bank;
        this.createMoviments = new ArrayList<>();
    }

    public void readCsv() throws FileNotFoundException {
        FileReader fileReader = new FileReader(file);

        try (CSVReader csvReader = new CSVReader(fileReader)) {

            switch (bank) {
                case MERCADO_PAGO -> fromMercadoPago(csvReader);
                case BRADESCO -> fromBradesco(csvReader);
                case NUBANK -> fromNubank(csvReader);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void fromNubank(CSVReader csvReader) throws IOException, CsvValidationException {
        csvReader.readNext();

        String[] line;
        line = csvReader.readNext();

        while((line = csvReader.readNext()) != null) {

            try {
                LocalDate date = LocalDate.parse(line[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String description = line[3];

                String number = line[1];
                
                Double value = Double.parseDouble(number);

                CreateMovimentDTO moviment = new CreateMovimentDTO(value < 0 ? value * -1 : value, date, value > 0 ? MovimentType.IN : MovimentType.OUT, description, null);
                createMoviments.add(moviment);
            }catch(Exception e) {
                throw new InvalidBankCSVFormat("Formato CSV invalido do banco selecionado");
            }
        }
    }

    private void fromBradesco(CSVReader csvReader) throws IOException, CsvValidationException {
        csvReader.readNext();

        String[] line;
        line = csvReader.readNext();

        while((line = csvReader.readNext()) != null) {

            try {
                LocalDate date = LocalDate.parse(line[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String description = line[1];

                String number = line[4].isEmpty() || line[4] == null ? line[5] :  line[4];
                Double  value = Double.parseDouble(number.replace(".", "").replace(",", "."));

                CreateMovimentDTO moviment = new CreateMovimentDTO(value < 0 ? value * -1 : value, date, value > 0 ? MovimentType.IN : MovimentType.OUT, description, null);
                createMoviments.add(moviment);
            }catch(Exception e) {
                continue;
            }
        }
    }

    private void fromMercadoPago(CSVReader csvReader) throws IOException, CsvValidationException {
        csvReader.readNext();
        csvReader.readNext();
        csvReader.readNext();

        String[] line;
        line = csvReader.readNext();

        while((line = csvReader.readNext()) != null) {
            try {
                LocalDate date = LocalDate.parse(line[0].replace("-", "/"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String description = line[1];
                Double  value = Double.parseDouble(line[3].replace(",", "."));

                System.out.println(date + " " +  description + " " + value);
                CreateMovimentDTO moviment = new CreateMovimentDTO(value < 0 ? value * -1 : value, date, value > 0 ? MovimentType.IN : MovimentType.OUT, description, null);
                createMoviments.add(moviment);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidBankCSVFormat("Formato CSV invalido do banco selecionado, " + e.getMessage());
            }
        }
    }
}
