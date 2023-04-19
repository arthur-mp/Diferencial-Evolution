import java.util.Comparator;

public class IndividuoComparator implements Comparator<Individual> {

    @Override
    public int compare(Individual i1, Individual i2) {
        return Double.compare(i2.crowdingDistance, i1.crowdingDistance);
    }
}
