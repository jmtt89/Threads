package org.master.upv.threads.primeIntervalConcurrent;

/**
 * Created by jmtt_ on 3/1/2018.
 */

class Progress {
    private int progress;
    private long actual;

    public Progress() {
        this.progress = 0;
        this.actual = -1;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getActual() {
        return actual;
    }

    public void setActual(long actual) {
        this.actual = actual;
    }
}
