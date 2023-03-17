
public class Individual {

    // Número de dimensões/variáveis (tamanho do cromossomo)
    private int d = 2;
    private Double[] values = new Double[d];
    private Double avaliation;

    public  Individual(){

    }
    public Individual(Double[] values) {
        this.values = values;
    }

    public Double[] getValues() {
        return values;
    }

    public void setValues(Double[] values) {
        this.values = values;
    }

    public Double getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(Double avaliation) {
        this.avaliation = avaliation;
    }
}
