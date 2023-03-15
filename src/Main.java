import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // Individuos com seus valores gerado aletoriamente
        Individual[] populationInitial = new Individual[20];

        // Colocar para gerar valores entre -20 e +20
        int rangeMax = 20;
        int rangeMin = -20;
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Double[] values = new Double[2];
            values[0] = random.nextDouble() * rangeMax * 2 * rangeMin;
            values[1] = random.nextDouble() * rangeMax * 2 * rangeMin;

            Individual individual = new Individual(values);
            avaliationIndividual(individual);
            populationInitial[i] = individual;
        }

        // Numero de geracoes
        int numGen = 0;

        // Numero maximo de geracoes
        int maxGen = 0;

        double F = 0.5;

        while (numGen <= maxGen){
            Individual[] newPopulation = new Individual[20];

            for (int i = 0; i < 20; i++) {
                // Indices do vetor população inicial
                // Ideal que estas variaves sejam diferentes
                int r1 = random.nextInt(19);
                int r2 = random.nextInt(19);
                int r3 = random.nextInt(19);

                Individual individual3 = populationInitial[r3];
                Individual individual2 = populationInitial[r2];
                Individual individual1 = populationInitial[r1];

                Individual u = generateU(individual1, individual2, individual3, F);

                Individual exp = generateExperimental(populationInitial[i], u);
                avaliationIndividual(exp);

                if(exp.getAvaliation() < populationInitial[i].getAvaliation()){
                    newPopulation[i] = exp;
                }else{
                    newPopulation[i] = populationInitial[i];
                }

            }
            populationInitial = newPopulation;
        }
    }

    public static Individual generateU(Individual individual1, Individual individual2, Individual individual3, Double F){
        Individual individualIntemedium = new Individual();

        Double[] values = new Double[2];

        for (int i = 0; i < 2; i++) {
            values[i] = individual3.getValues()[i] + (F * (individual1.getValues()[i] - individual2.getValues()[i]));
        }

        individualIntemedium.setValues(values);
        return individualIntemedium;
    }

    public static Individual generateExperimental(Individual individual, Individual u){
        Double crossover = 0.8;

        Random random  = new Random();

        Individual filho = new Individual();

        Double[] values = new Double[2];

        // Indices dos genes
        for (int i = 0; i < 2; i++) {
            // 0 < r < 1
            Double r = random.nextDouble();

            if(r < crossover){
                values[i] = individual.getValues()[i];
            }else{
                values[i] = u.getValues()[i];
            }
        }

        filho.setValues(values);

        return filho;
    }

    public static void avaliationIndividual(Individual individual){
        Double avaliation = Math.pow(individual.getValues()[0],2) + Math.pow(individual.getValues()[1], 2);
        individual.setAvaliation(avaliation);
    }
}