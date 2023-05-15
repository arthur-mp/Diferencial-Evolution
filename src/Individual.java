import operadores.CrossOver;
import operadores.CrossOverBLXAlfa;
import operadores.Mutation;
import operadores.MutationNone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Individual {

    // Número de dimensões/variáveis (tamanho do cromossomo)
    private double[] values;
    private double[] avaliation;

    public double crowdingDistance;

    public CrossOver crossOver;
    public Mutation mutation;

    public Individual(double[] values, int qntAvaliation) {
        this( values, qntAvaliation, new CrossOverBLXAlfa(0.1), new MutationNone());
    }

    public Individual(double[] values, double[] qntAvaliation) {
        this( values, qntAvaliation, new CrossOverBLXAlfa(0.1), new MutationNone());
    }

    public Individual(double[]  values, int qntAvaliation, CrossOver crossOver, Mutation mutation){
        this.values = values;
        this.avaliation = new double[qntAvaliation];
        this.crossOver = crossOver;
        this.mutation = mutation;
    }

    public Individual(double[]  values, double[] qntAvaliation, CrossOver crossOver, Mutation mutation){
        this.values = values;
        this.avaliation = qntAvaliation;
        this.crossOver = crossOver;
        this.mutation = mutation;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double[] getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(double[] avaliation) {
        this.avaliation = avaliation;
    }

    public List<Individual> recombinar(Individual p2){
        List<Individual> filhos = new ArrayList<Individual>(2);

        double[] varsP1 = this.values;
        double[] varsP2 = p2.values;

        double[][] filhosMat = crossOver.getOffSpring(this.values, p2.values, new double[]{-10, -10, -10}, new double[]{10, 10, 10});

        Individual f1 = new Individual(this.values, filhosMat[0]);
        Individual f2 = new Individual(this.values, filhosMat[1]);

        filhos.add(f1);
        filhos.add(f2);

        return  filhos;
    }

    public void mutar(){
        this.values = mutation.getMutation(this.values, new double[]{-10, -10}, new double[]{10, 10});
    }

    @Override
    public String toString() {
        return "Individual{" +
                "avaliation=" + Arrays.toString(avaliation) +
                '}';
    }
}
