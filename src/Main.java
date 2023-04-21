import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    // tamanho da população
    public static int np = 20;
    // Numero da geracao
    public static int numGen = 0;
    // Numero maximo de geracoes
    public static int maxGen = 100;
    // peso aplicado ao vetor de diferenças (constante de mutação)
    public static double F = 0.5;
    // CR: constante de cruzamento
    public static Double crossover = 0.8;
    // Colocar para gerar valores entre Min e Max
    public static int rangeMax = 10;
    public static int rangeMin = -10;
    // Quantidade de Avaliações
    public static  int qntAvaliations = 2;
    // Dimensão coordenada
    public static int  qntVariaveis = 1;

    public static void main(String[] args) {

        // Individuos com seus valores gerado aletoriamente
        List<Individual> populationInitial = new ArrayList<>(np);

        Random random = new Random();
        for (int i = 0; i < np; i++) {
            Double[] values = new Double[qntVariaveis];
            for (int j = 0; j < qntVariaveis; j++) {
                values[j] = random.nextDouble() * rangeMax * 2 + rangeMin;
            }

            Individual individual = new Individual(values, qntAvaliations);
            avaliationIndividual(individual, qntAvaliations, qntVariaveis);
            populationInitial.add(individual);
        }

        List<Individual> newPopulation = new ArrayList<>();

        creatFile(populationInitial, "1");

        while (numGen <= maxGen){

            List<Individual> intermediatePopulation = new ArrayList<>(populationInitial);

            // Filhos
            for (int i = 0; i < np; i++) {
                // Indices do vetor população inicial
                //TODO: Ideal que estas variaves (r1, r2, r3) sejam diferentes
                int r1 = random.nextInt(np - 1);
                int r2 = random.nextInt(np - 1);
                int r3 = random.nextInt(np - 1);

                Individual individual3 = populationInitial.get(r3);
                Individual individual2 = populationInitial.get(r2);
                Individual individual1 = populationInitial.get(r1);

                Individual u = generateU(individual1, individual2, individual3, F, qntVariaveis, qntAvaliations);

                Individual exp = generateExperimental(populationInitial.get(i), u, crossover, qntVariaveis, qntAvaliations);
                avaliationIndividual(exp, qntAvaliations, qntVariaveis);

                intermediatePopulation.add(exp);

            }

            // ************

            // FNDS
            List<List<Individual>> fronteiras = fnds(intermediatePopulation);

            for (int i = 0; i < fronteiras.size(); i++) {
                if(newPopulation.size() >= np) break;

                if(fronteiras.get(i).size() + newPopulation.size() > np){
                    List<Individual>  individuoCD = new CrowdingDistance().avaliar(fronteiras.get(i));
                    for (int j = 0; j < individuoCD.size(); j++){
                        if(newPopulation.size() < np){
                            newPopulation.add(individuoCD.get(j));
                        }else break;
                    }

                }else{
                    newPopulation.addAll(fronteiras.get(i));
                }
            }

            populationInitial = newPopulation;
            newPopulation = new ArrayList<>();
            numGen++;

            if(numGen == 20){
                creatFile(populationInitial, String.valueOf(numGen));
            }
            if(numGen == 40){
                creatFile(populationInitial, String.valueOf(numGen));
            }
            if(numGen == 60){
                creatFile(populationInitial, String.valueOf(numGen));
            }
            if(numGen == maxGen){
                creatFile(populationInitial, String.valueOf(numGen));
            }
        }

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

    public static void avaliationIndividual(Individual individual, int qntAvaliations, int qntVariaveis){

        Double[] avaliation = new Double[qntAvaliations];

        switch (qntVariaveis){
            case 1:
                // Função de avaliação 1: f(x1) = x²
                avaliation[0] = Math.pow(individual.getValues()[0],2);

                // Função de avaliação 2: f(x1) = (x-1)¹
                avaliation[1] = Math.pow((individual.getValues()[0]  - 1),2);
            break;

            case 2:
                // Função de avaliação 1: f(x1, x2) = x1² + x2²
                avaliation[0] = Math.pow(individual.getValues()[0],2) + Math.pow(individual.getValues()[1],2);

                // Função de avaliação 2: f(x1, x2) = x1² + (x2 - 2)²
                avaliation[1] = Math.pow(individual.getValues()[0],2) + Math.pow((individual.getValues()[1] - 2),2);
            break;

            case 3:
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
            break;

        }
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

    public static void creatFile(List<Individual> geracaoIndividuo, String geracao){
        for (int j = 0; j < 2; j++){
            String path = "";
            switch (j){
                case 0:
                    path = "./src/files/geracao_"+geracao+"_variaveis.txt";
                break;
                case 1:
                    path = "./src/files/geracao_"+geracao+"_objetivos.txt";
                break;
            }
            File file = new File(path);
            try{
                if(!file.exists()){
                    file.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                for (int i = 0; i < geracaoIndividuo.size(); i++){
                    if(j == 0){
                        bufferedWriter.write(Arrays.toString(geracaoIndividuo.get(i).getValues()));
                        bufferedWriter.newLine();
                    }else if(j == 1){
                        bufferedWriter.write(Arrays.toString(geracaoIndividuo.get(i).getAvaliation()));
                        bufferedWriter.newLine();
                    }
                }

                bufferedWriter.close();
                fileWriter.close();
            }catch (IOException erro){
                System.out.printf("Erro: %s", erro.getMessage());
            }
        }
    }
}