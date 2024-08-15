package by.vitikova.plugin.exception

/**
 * Исключение `TagAlreadyExistException` представляет собой пользовательское исключение,
 * которое выбрасывается, когда происходит попытка создать тег, который уже существует.
 *
 * <p> Это исключение расширяет класс `RuntimeException`, позволяя обработать ситуацию
 * с уже существующими тегами в приложении.</p>
 *
 * @author VitMVit
 */
class TagAlreadyExistException extends RuntimeException {

    TagAlreadyExistException(String message) {
        super(message)
    }
}