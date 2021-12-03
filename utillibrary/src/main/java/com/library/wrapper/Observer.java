package com.library.wrapper;

public interface Observer<E, M, A> {
    void notify(E observable, M msg, A arg);
}
