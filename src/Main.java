import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // tamanho da população
        int np = 20;

        // Numero de geracoes
        int numGen = 0;

        // Numero maximo de geracoes
        int maxGen = 100;

        // peso aplicado ao vetor de diferenças (constante de mutação)
        double F = 0.5;

        // CR: constante de cruzamento
        Double crossover = 0.8;

        // Colocar para gerar valores entre -20 e +20
        int rangeMax = 20;
        int rangeMin = -20;

        // Individuos com seus valores gerado aletoriamente
        Individual[] populationInitial = new Individual[np];

        Random random = new Random();
        for (int i = 0; i < np; i++) {
            Double[] values = new Double[2];
            values[0] = random.nextDouble() * rangeMax * 2 + rangeMin;
            values[1] = random.nextDouble() * rangeMax * 2 + rangeMin;

            Individual individual = new Individual(values);
            avaliationIndividual(individual);
            populationInitial[i] = individual;
        }

        while (numGen <= maxGen){
            Individual[] newPopulation = new Individual[np];

            for (int i = 0; i < np; i++) {
                // Indices do vetor população inicial
                //TODO: Ideal que estas variaves (r1, r2, r3) sejam diferentes
                int r1 = random.nextInt(np - 1);
                int r2 = random.nextInt(np - 1);
                int r3 = random.nextInt(np - 1);

                Individual individual3 = populationInitial[r3];
                Individual individual2 = populationInitial[r2];
                Individual individual1 = populationInitial[r1];

                Individual u = generateU(individual1, individual2, individual3, F);

                Individual exp = generateExperimental(populationInitial[i], u, crossover);
                avaliationIndividual(exp);

                if(exp.getAvaliation() < populationInitial[i].getAvaliation()){
                    newPopulation[i] = exp;
                }else{
                    newPopulation[i] = populationInitial[i];
                }

            }
            logIndividual(populationInitial, numGen);

            populationInitial = newPopulation;
            numGen++;
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

    public static Individual generateExperimental(Individual individual, Individual u, Double cr){
        // CR: constante de cruzamento
        Double crossover = cr;

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

        // Função de avaliação: f(x1, x2) = x1² + x2²
        Double avaliation = Math.pow(individual.getValues()[0],2) + Math.pow(individual.getValues()[1], 2);
        individual.setAvaliation(avaliation);
    }

    public static void logIndividual(Individual[] individuals, int generation){
        double avaliation = individuals[0].getAvaliation();

        // Melhor individuo possui a avaliação minimizada

        for (int i = 1; i < individuals.length; i++) {
            if(individuals[i].getAvaliation() < avaliation){
                avaliation = individuals[i].getAvaliation();
            }
        }

        System.out.printf("Geração %d - Melhor individuo: %f \n", generation, avaliation);

    }
}