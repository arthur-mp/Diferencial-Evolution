package operadores;

public class MutationNone implements Mutation{
    public MutationNone(){

    }

    @Override
    public double[] getMutation(double[] x, double[] lowerBound, double[] upperBound) {
        return x;
    }
}
