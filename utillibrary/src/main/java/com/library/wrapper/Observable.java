package com.library.wrapper;

public class Observable<E, M, A> {
    static final Object[] NULL_OBS = new Object[0];
    protected Object[] observers = NULL_OBS;

    public Observable() {
    }

    public final boolean hasObservers() {
        return observers.length > 0;
    }

    public final int countObservers() {
        return observers.length;
    }

    public E getSource() {
        return (E) this;
    }

    public static class Proxy<E, M, A> extends Observable<E, M, A> {
        final E source;

        public Proxy(E source) {
            this.source = source;
        }

        @Override
        public E getSource() {
            return source;
        }
    }

    public synchronized void addObserver(Observer<E, M, A> observer) {
        if (observer == null) {
            throw new NullPointerException("null observer");
        }
        System.arraycopy(observers, 0, observers = new Object[observers.length + 1], 0, observers.length - 1);
        observers[observers.length - 1] = observer;
    }

    public synchronized void removeObserver(Observer<E, M, A> observer) {
        if (observer == null) {
            throw new NullPointerException("null observer");
        }
        for (int i = observers.length; --i >= 0; ) {
            if (observers[i] == observer) {
                if (observers.length == 1) {
                    observers = NULL_OBS;
                } else {
                    Object[] obs = new Object[observers.length - 1];
                    System.arraycopy(observers, 0, obs, 0, i);
                    if (i + 1 < observers.length) {
                        System.arraycopy(observers, i + 1, obs, i, observers.length - (i + 1));
                    }
                    observers = obs;
                }
            }
        }
    }

    public void removeAllObservers() {
        observers = NULL_OBS;
    }

    public void notifyObservers(M msg, A arg) {
        final Object[] observers = this.observers;
        for (Object observer : observers) {
            notifyObserver((Observer<E, M, A>) observer, msg, arg);
        }
    }

    public void notifyObserver(Observer<E, M, A> observer, M msg, A arg) {
        if (observer != null) {
            observer.notify(getSource(), msg, arg);
        }
    }
}
