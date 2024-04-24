package equalizer;

import java.util.concurrent.Callable;

public class FilterThird implements Callable<short[]> {
    private short[] inputSignal;
    private short[] outputSignal;
    private double gain;
    private double[] coffsNumFilter;
    private int count_coffs;

    public void settings(final short[] inputSignal, final double[] coffsNumFilter) {
        this.inputSignal = inputSignal;
        this.coffsNumFilter = coffsNumFilter;
        this.outputSignal = new short[inputSignal.length];
        this.count_coffs = coffsNumFilter.length;
    }

    private void convolution() {
        double tmp;
        for(int i = 0; i <  this.inputSignal.length; i++) {
            tmp = 0;
            for(int j = 0; j < this.count_coffs; j++) {
                if(i - j >= 0)
                    tmp += coffsNumFilter[j] * this.inputSignal[i - j];
            }
            this.outputSignal[i] += (short) (this.gain * (short)(tmp / 8)); //делим на 8, чтобы не было перегруза на пересечении фильтров
        }
    }

    /*private void convolutionFIR() {
        //Функция свертки для БИХ-фильтра отличается от свертки для КИХ
        //Для БИХ-фильтра появляется обратная связь feedbackSignal
        double tmp;
        for(int i = 0; i <  this.inputSignal.length; i++) {
            tmp = 0;
            for(int j = 0; j < FilterInfo.COUNT_OF_COFFS; j++) {
                if (i - j >= 0) {
                    tmp += FilterInfo.COFFS_NUM[j] * this.inputSignal[i - j];
                    tmp -= FilterInfo.COFFS_DEN[j] * this.feedbackSignal[i - j];
                }
            }
            this.feedbackSignal[i] = tmp;
            this.outputSignal[i] = (short)(tmp);
        }
    }*/

    public void setGain(double gain) {
        this.gain = gain;
    }

    @Override
    public short[] call() {
        this.convolution();
        return this.outputSignal;
    }
}
