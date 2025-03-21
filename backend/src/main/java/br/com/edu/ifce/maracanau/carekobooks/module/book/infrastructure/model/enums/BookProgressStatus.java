package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums;

public enum BookProgressStatus {

    READING,
    PLANS_TO_READ,
    FINISHED;

    public BookActivityStatus toBookActivityStatus() {
        return switch (this) {
            case READING -> BookActivityStatus.READING;
            case PLANS_TO_READ -> BookActivityStatus.PLANS_TO_READ;
            case FINISHED -> BookActivityStatus.FINISHED;
        };
    }

    public BookActivityStatus toBookActivityStatus(boolean isFavorited) {
        return isFavorited
                ? BookActivityStatus.FAVORITED
                : toBookActivityStatus();
    }

}
