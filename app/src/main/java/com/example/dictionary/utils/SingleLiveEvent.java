package com.example.dictionary.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final AtomicBoolean hasBeenHandled = new AtomicBoolean(false);

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, value -> {
            if (hasBeenHandled.compareAndSet(false, true)) {
                observer.onChanged(value);
            }
        });
    }

    @MainThread
    public void setValue(T value) {
        hasBeenHandled.set(false);
        super.setValue(value);
    }

    public void call() {
        setValue(null);
    }
}

