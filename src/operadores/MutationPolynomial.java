package operadores;

public class MutationPolynomial implements Mutation{

    private double mutationProbability;
    private double mutationDistributionIndex;

    public MutationPolynomial(double mutationProbability, double mutationDistributionIndex){
        this.mutationProbability = mutationProbability;
        this.mutationDistributionIndex = mutationDistributionIndex;
    }

    public double[] getMutation(double[] x, double[] lowerBound, double[] upperBound){
        double rnd, delta1, delta2, mutPow, deltaq;
        double y, y1, yu,val, xy;

        for(int i = 0; i < x.length; i++){
            if(Math.random() <= mutationProbability){
                y = x[i];
                y1 = lowerBound[i];
                yu = upperBound[i];
                if(y1 == yu){
                    y = y1;
                }else{
                    delta1 = (y - y1) / (yu - y1);
                    delta2 = (yu - y) / (yu - y1);
                }
            }
        }
    }
}
