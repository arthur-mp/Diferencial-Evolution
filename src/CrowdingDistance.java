import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CrowdingDistance {

    public List<Individual>  avaliar(List<Individual> fronteira){
        int tamanhoFronteira = fronteira.size();

        for (Individual individual: fronteira) {
            individual.crowdingDistance = 0;
        }

        Individual individual0 = fronteira.get(0);
        //TODO: conferir se esses objetivos corresponde ao Avaliation ou ao Values
        int objetivos = individual0.getAvaliation().length;
        for (int i = 0; i < objetivos; i++) {
            sort(fronteira, objetivos);
            fronteira.get(0).crowdingDistance = Double.POSITIVE_INFINITY;
            fronteira.get(tamanhoFronteira - 1).crowdingDistance = Double.POSITIVE_INFINITY;

            for (int j = 1; j < tamanhoFronteira - 1; j++) {
                Individual anterior = fronteira.get(j+1);
                Individual posterior = fronteira.get(j-1);

                double aux = (posterior.getAvaliation()[i] - anterior.getAvaliation()[i])
                        /
                        (fronteira.get(tamanhoFronteira - 1).getAvaliation()[i] - fronteira.get(0).getAvaliation()[i])
                        ;

                fronteira.get(j).crowdingDistance += aux;
            }
        }

        Collections.sort(fronteira, new IndividuoComparator());

        return fronteira;
    }

    private void sort(List<Individual> fronteira, int objetivos){
        for (int i = 0; i < fronteira.size()-1; i++) {
            for (int j = i+1; j < fronteira.size(); j++) {
                if(fronteira.get(i).getAvaliation()[objetivos] > fronteira.get(j).getAvaliation()[objetivos]){
                    Individual aux = fronteira.get(i);
                    fronteira.set(i, fronteira.get(j));
                    fronteira.set(j, aux);
                }
            }
        }
    }
}
