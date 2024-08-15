package by.vitikova.plugin.exception

/**
 * Исключение `NotFoundException` представляет собой пользовательское исключение,
 * которое выбрасывается, когда запрашиваемый ресурс не найден.
 *
 * <p> Это исключение расширяет класс `RuntimeException`, позволяя легко обрабатывать
 * ошибки, связанные с отсутствующими ресурсами в приложении.</p>
 *
 * @author VitMVit
 */
class NotFoundException extends RuntimeException {

    NotFoundException(String message) {
        super(message)
    }
}