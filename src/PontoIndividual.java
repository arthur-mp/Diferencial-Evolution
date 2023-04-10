import java.util.List;

public class PontoIndividual {

    Double[] coordenadas;
    // Conjunto dos pontos dominados pelo PontoIndividual
    List<PontoIndividual> S;
    // Número de vezes que o PontoIndividual foi dominado
    int n;
    int rank;
    private Individual individual;

    public PontoIndividual(Individual individual){
        this.individual = individual;
        this.coordenadas = individual.getValues();
    }

    public Individual getIndividual(){
        return this.individual;
    }

}
