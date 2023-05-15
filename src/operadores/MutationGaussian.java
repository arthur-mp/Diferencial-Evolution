package operadores;

import java.util.Random;

public class MutationGaussian implements Mutation{

    private double mutationProbability;
    private double mutationDistributionIndex;

    public MutationGaussian(double mutationProbability, double mutationDistributionIndex){
        this.mutationProbability = mutationProbability;
        this.mutationDistributionIndex = mutationDistributionIndex;
    }

    public double[] getMutation(double[] x, double[] lowerBound, double[] upperBound){
        Random random = new Random();
        double mutate;
        double r;
        boolean mutanted = false;

        double[] cloneGenesX = x;

        for(int i = 0; i < cloneGenesX.length; i++){
            mutate = random.nextDouble();

            if(mutate <= 0.1){
                if((cloneGenesX[i] + mutate) < lowerBound[i])
                    mutate = lowerBound[i];
                else if((cloneGenesX[i] + mutate) > upperBound[i])
                    mutate = upperBound[i];

                r = random.nextGaussian();
                cloneGenesX[i] = cloneGenesX[i] + r;
                mutanted = true;
            }
        }

        if(!mutanted){
            int positionRandom = random.nextInt(0, cloneGenesX.length);
            r = random.nextGaussian();

            cloneGenesX[positionRandom] = cloneGenesX[positionRandom] + r;
        }

        return cloneGenesX;
    }
}
