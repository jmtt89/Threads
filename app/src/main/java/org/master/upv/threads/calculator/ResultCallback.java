package org.master.upv.threads.calculator;

public interface ResultCallback<T> {
    void onResult(T data);
}