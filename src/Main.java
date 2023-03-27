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

        // Colocar para gerar valores entre Min e Max
        int rangeMax = 10;
        int rangeMin = -10;

        int qntAvaliations = 3;

        int  lengthDimension = 3;

        // Individuos com seus valores gerado aletoriamente
        Individual[] populationInitial = new Individual[np];

        Random random = new Random();
        for (int i = 0; i < np; i++) {
            Double[] values = new Double[lengthDimension];
            for (int j = 0; j < lengthDimension; j++) {
                values[j] = random.nextDouble() * rangeMax * 2 + rangeMin;
            }

            Individual individual = new Individual(values, qntAvaliations);
            avaliationIndividual(individual, qntAvaliations);
            populationInitial[i] = individual;
        }
        Individual[] newPopulation = new Individual[np];

        while (numGen <= maxGen){
            for (int i = 0; i < np; i++) {
                // Indices do vetor população inicial
                //TODO: Ideal que estas variaves (r1, r2, r3) sejam diferentes
                int r1 = random.nextInt(np - 1);
                int r2 = random.nextInt(np - 1);
                int r3 = random.nextInt(np - 1);

                Individual individual3 = populationInitial[r3];
                Individual individual2 = populationInitial[r2];
                Individual individual1 = populationInitial[r1];

                Individual u = generateU(individual1, individual2, individual3, F, lengthDimension, qntAvaliations);

                Individual exp = generateExperimental(populationInitial[i], u, crossover, lengthDimension, qntAvaliations);
                avaliationIndividual(exp, qntAvaliations);

                if(dominance(exp, populationInitial[i], qntAvaliations)){
                    newPopulation[i] = exp;
                }else if(dominance(populationInitial[i], exp, qntAvaliations)){
                    newPopulation[i] = populationInitial[i];
                }else{
                    Random random1 = new Random();
                    boolean sort = random1.nextBoolean();
                    newPopulation[i] = sort ? exp : populationInitial[i];
                }

            }

            populationInitial = newPopulation;
            numGen++;
        }

        logIndividual(newPopulation);
    }

    public static Individual generateU(Individual individual1, Individual individual2, Individual individual3, Double F, int lengthDimension, int qntAvaliations){
        Individual individualIntemedium = new Individual(lengthDimension, qntAvaliations);

        Double[] values = new Double[lengthDimension];

        for (int i = 0; i < lengthDimension; i++) {
            values[i] = individual3.getValues()[i] + (F * (individual1.getValues()[i] - individual2.getValues()[i]));
        }

        individualIntemedium.setValues(values);
        return individualIntemedium;
    }

    public static Individual generateExperimental(Individual individual, Individual u, Double cr, int lengthDimension, int qntAvaliations){
        // CR: constante de cruzamento
        Double crossover = cr;

        Random random  = new Random();

        Individual filho = new Individual(lengthDimension, qntAvaliations);

        Double[] values = new Double[lengthDimension];

        // Indices dos genes
        for (int i = 0; i < lengthDimension; i++) {
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

    public static void avaliationIndividual(Individual individual, int qntAvaliations){

        Double[] avaliation = new Double[qntAvaliations];

        /*
        *  Caso 2, 1 variável
        */
        // Função de avaliação 1: f(x1) = x²
        //avaliation[0] = Math.pow(individual.getValues()[0],2);

        // Função de avaliação 2: f(x1) = (x-1)¹
        //avaliation[1] = Math.pow((individual.getValues()[0]  - 1),2);

        /*
         *  Caso 3, 2 variáveis
         */
        // Função de avaliação 1: f(x1, x2) = x1² + x2²
        //avaliation[0] = Math.pow(individual.getValues()[0],2) + Math.pow(individual.getValues()[1],2);

        // Função de avaliação 2: f(x1, x2) = x1² + (x2 - 2)²
        //avaliation[1] = Math.pow(individual.getValues()[0],2) + Math.pow((individual.getValues()[1] - 2),2);

        /*
         *  Caso 4, 3 variáveis
         */
        // Função de avaliação 1: f(x1, x2, x3) = (x1 - 1)² + x2² + x3²
        avaliation[0] = Math.pow((individual.getValues()[0] - 1),2)
                + Math.pow(individual.getValues()[1], 2)
                + Math.pow(individual.getValues()[2],2);

        // Função de avaliação 2: f(x1, x2, x3) = x1² + (x2 - 1)² + x3²
        avaliation[1] = Math.pow(individual.getValues()[0],2)
                + Math.pow((individual.getValues()[1] - 1), 2)
                + Math.pow(individual.getValues()[2], 2);

        // Função de avaliação 3: f(x1, x2, x3) = x1² + x2² + (x3 - 1)²
        avaliation[2] = Math.pow(individual.getValues()[0],2)
                + Math.pow(individual.getValues()[1], 2)
                + Math.pow((individual.getValues()[2] - 1), 2);

        individual.setAvaliation(avaliation);
    }
    public static boolean dominance(Individual a, Individual b, int qntAvaliations){
        // Dominância, A domina B, se:
        // Para toda avaliação de A, tal que, Ai <= Bi
        // Pelo menos exista uma avaliação de A, tal que, Ai < Bi
        boolean dominanceCondition = false;

        for (int i = 0; i < qntAvaliations; i++) {
            if(a.getAvaliation()[i] > b.getAvaliation()[i]) return false;
            if(a.getAvaliation()[i] < b.getAvaliation()[i]) dominanceCondition = true;
        }

        return dominanceCondition;
    }

    public static void logIndividual(Individual[] individuals){
        for (int i = 1; i < individuals.length; i++) {
            System.out.println(individuals[i].toString());
        }

    }
}