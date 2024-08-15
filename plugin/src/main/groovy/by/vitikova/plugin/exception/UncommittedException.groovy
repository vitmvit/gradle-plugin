package by.vitikova.plugin.exception

/**
 * Исключение `UncommittedException` представляет собой пользовательское исключение,
 * которое выбрасывается, когда существуют незавершенные изменения в репозитории.
 *
 * <p> Это исключение расширяет класс `RuntimeException`, позволяя обработать ошибки,
 * связанные с незавершенными изменениями перед выполнением определенных операций.</p>
 *
 * @author VitMVit
 */
class UncommittedException extends RuntimeException {

    UncommittedException(String message) {
        super(message)
    }
}
