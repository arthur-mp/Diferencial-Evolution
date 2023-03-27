import java.util.Arrays;

public class Individual {

    // Número de dimensões/variáveis (tamanho do cromossomo)
    private Double[] values;
    private Double[] avaliation;

    public  Individual(int  lengthDimension, int qntAvaliation){
        values = new Double[lengthDimension];
        avaliation = new Double[qntAvaliation];
    }
    public Individual(Double[] values, int qntAvaliation) {
        this.values = values;
        avaliation = new Double[qntAvaliation];
    }

    public Double[] getValues() {
        return values;
    }

    public void setValues(Double[] values) {
        this.values = values;
    }

    public Double[] getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(Double[] avaliation) {
        this.avaliation = avaliation;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "avaliation=" + Arrays.toString(avaliation) +
                '}';
    }
}
