package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.observer.BaseObserver;

import java.util.List;

public interface BaseSubject<T extends BaseObserver> {

    void addObserver(T observer);
    void removeObserver(T observer);
    List<T> getObservers();

}
