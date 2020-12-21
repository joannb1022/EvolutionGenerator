package mapElements;

import java.util.Random;

public class Genotype {
    int[] genes;
    Genotype(){
        this.genes = new int[8];
        for(int i = 0; i < 8; i++)
            genes[i] = 1;
    }

    int[] getGenes(){
        return this.genes;
    }

    void pickGeno(Animal dad, Animal mom){
        for(int i = 0; i <24; i++){
            int x = new Random().nextInt(32), j = 0;
            while(x > 0 && j < 7){
                x -= (i<16) ? dad.getGeno().getGenes()[j++] : mom.getGeno().getGenes()[j++];
            }
            this.genes[j]++;
        }
    }

    public void randomGeno(){
        for(int i = 8; i < 32; i++)
            this.genes[new Random().nextInt(8)]++;
    }
}
