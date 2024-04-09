package effect;

import java.util.concurrent.Callable;

import static effect.BufferSample.SAMPLE_SIZE;

public class Delay extends Effect implements Callable<short[]> {
    private static final double RATIO_DRY_TO_WET = 0.7;
    public static final int DEFAULT_SIZE_BUFFER = 3;
    public BufferSample BufferSample;

    public Delay() {
        super();
        this.BufferSample = new BufferSample(DEFAULT_SIZE_BUFFER);
    }

    public Delay(int sizeBufferSample) {
        super();
        this.BufferSample = new BufferSample(sizeBufferSample);
    }

    @Override
    public synchronized short[] createEffect() {
        int indexCurrentSampleDelay = this.BufferSample.getIndexCurrentElement();
        for (int j = 0; j < SAMPLE_SIZE; j++) {
            this.inputAudioStream[j] = (short) (RATIO_DRY_TO_WET * (this.inputAudioStream[j])
                    + ((1 - RATIO_DRY_TO_WET) * this.BufferSample.getAmplitudeSampleDelay(indexCurrentSampleDelay, j)));
        }
        this.BufferSample.add(this.inputAudioStream);
        return this.inputAudioStream;
    }

    @Override
    public short[] call() {
        return createEffect();
    }
}

