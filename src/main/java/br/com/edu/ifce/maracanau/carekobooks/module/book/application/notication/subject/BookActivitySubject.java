package br.com.edu.ifce.maracanau.carekobooks.module.book.application.notication.subject;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.notification.subject.BaseSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notication.observer.BaseBookActivityObserver;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;

import java.util.ArrayList;
import java.util.List;

public class BookActivitySubject implements BaseSubject<BaseBookActivityObserver> {

    private final List<BaseBookActivityObserver> observers = new ArrayList<>();

    public void notify(BookActivityResponse response) {
        observers.forEach(observer -> observer.notify(response));
    }

    @Override
    public void addObserver(BaseBookActivityObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BaseBookActivityObserver observer) {
        observers.remove(observer);
    }

    @Override
    public List<BaseBookActivityObserver> getObservers() {
        return observers;
    }

}
