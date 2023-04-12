import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        // Quantidade de Avaliações
        int qntAvaliations = 3;
        // Dimensão coordenada
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

            List<Individual> intermediatePopulation = new ArrayList<>(List.of(populationInitial));

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

                intermediatePopulation.add(exp);

            }

            // ************

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
    public static boolean dominance(PontoIndividual a, PontoIndividual b){
        // Dominância, A domina B, se:
        // Para toda avaliação de A, tal que, Ai <= Bi
        // Pelo menos exista uma avaliação de A, tal que, Ai < Bi
        boolean dominanceCondition = false;

        Double[] coordernadasPI1 = a.coordenadas;
        Double[] coordernadasPI2 = b.coordenadas;

        for (int i = 0; i < coordernadasPI1.length; i++) {
            if(coordernadasPI2[i] < coordernadasPI1[i]) return false;
        }

        for (int i = 0; i < coordernadasPI1.length; i++) {
            if(coordernadasPI1[i] < coordernadasPI2[i]){
                dominanceCondition = true;
                break;
            }
        }

        return dominanceCondition;
    }

    // FNDS
    public static List<List<Individual>> fnds(List<Individual> listIndividual){
        List<PontoIndividual> listPontoIndividual = new ArrayList<PontoIndividual>(listIndividual.size());

        for (int i = 0; i < listIndividual.size(); i++) {
            Individual individual = listIndividual.get(i);
            PontoIndividual pontoIndividual = new PontoIndividual(individual);
            listPontoIndividual.add(pontoIndividual);
        }

        // F
        List<List<PontoIndividual>> listaFronteiras = new ArrayList<List<PontoIndividual>>();

        List<PontoIndividual> fronteira1 = new ArrayList<PontoIndividual>();

        //Parte 1
        for (int i = 0; i < listPontoIndividual.size(); i++) {
            PontoIndividual p = listPontoIndividual.get(i);
            p.S = new ArrayList<PontoIndividual>();
            p.n = 0;

            for (int j = 0; j < listPontoIndividual.size(); j++) {
                if(i != j){
                    PontoIndividual q = listPontoIndividual.get(j);

                    if(dominance(p,q)){
                        p.S.add(q);
                    }else if(dominance(q,p)){
                        p.n++;
                    }
                }
            }

            if(p.n == 0){
                p.rank = 1;
                fronteira1.add(p);
            }

        }
        listaFronteiras.add(fronteira1);

        // Parte 2
        int i = 0;
        List<PontoIndividual> fronteiraI = listaFronteiras.get(i);
        while(!fronteiraI.isEmpty()){
            // Q
            List<PontoIndividual> novaFronteira = new ArrayList<>();

            for (PontoIndividual pontoIndividual: fronteiraI) {
                // Lista de pontos dominadas por pontoIndividual
                List<PontoIndividual> Sp = pontoIndividual.S;

                for (PontoIndividual pontoIndividual2 : Sp) {
                    pontoIndividual2.n--;
                    if(pontoIndividual2.n == 0){
                        pontoIndividual2.rank = i + 1;
                        novaFronteira.add(pontoIndividual2);
                    }
                }
            }
            i++;
            fronteiraI = novaFronteira;
            listaFronteiras.add(novaFronteira);
        }

        List<List<Individual>> retornoIndividual = new ArrayList<List<Individual>>();

        for (int j = 0; j < listaFronteiras.size(); j++){
            List<PontoIndividual> fronteiraJ = listaFronteiras.get(j);
            List<Individual> fronteiraIndividualJ = new ArrayList<Individual>();

            if(!fronteiraJ.isEmpty()){
                for (int k = 0; k < fronteiraJ.size(); k++) {
                    PontoIndividual p = fronteiraJ.get(k);
                    Individual individual = p.getIndividual();

                    fronteiraIndividualJ.add(individual);
                }

                retornoIndividual.add(fronteiraIndividualJ);
            }
        }

        return retornoIndividual;
    }

    public static void logIndividual(Individual[] individuals){
        for (int i = 1; i < individuals.length; i++) {
            System.out.println(individuals[i].toString());
        }

    }
}