package by.vitikova.plugin.builder

import by.vitikova.plugin.constant.Order

import static by.vitikova.plugin.constant.Constant.*

/**
 * Класс `CommandBuilder` предназначен для построения команд для последующего выполнения через процесс.
 *
 * <p> Данный класс позволяет последовательно накапливать команды и затем выполнять их,
 * возвращая результаты выполнения или сообщения об ошибках.</p>
 *
 * <p> Пример использования:
 * <pre>
 * CommandBuilder.builder()
 *     .git()
 *     .version()
 *     .execute()
 * </pre>
 * </p>
 *
 * @author VitMVit
 */
class CommandBuilder {

    List<String> commands

    private CommandBuilder() {
        commands = new ArrayList<>()
    }

    static def builder() {
        return new CommandBuilder()
    }

    /**
     * Добавляет команду `git`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder git() {
        return this.command(GIT)
    }

    /**
     * Добавляет команду `version`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder version() {
        return this.command(VERSION)
    }

    /**
     * Добавляет команду `describe`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder describe() {
        return this.command(DESCRIBE)
    }

    /**
     * Добавляет команду `tag`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder tag() {
        return this.command(TAG)
    }

    /**
     * Добавляет команду `tags`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder tags() {
        return this.command(TAGS)
    }

    /**
     * Добавляет команду `abbrev` с указанным числом.
     *
     * @param number число для команды `abbrev`.
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder abbrev(int number) {
        return this.command("$ABBREV$number")
    }

    /**
     * Добавляет команду `branch`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder branch() {
        return this.command(BRANCH)
    }

    /**
     * Добавляет команду `showCurrent`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder showCurrent() {
        return this.command(SHOW_CURRENT)
    }

    /**
     * Добавляет команду `push`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder push() {
        return this.command(PUSH)
    }

    /**
     * Добавляет команду `origin`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder origin() {
        return this.command(ORIGIN)
    }

    /**
     * Добавляет команду `diff`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder diff() {
        return this.command(DIFF)
    }

    /**
     * Добавляет команду `list`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder list() {
        return this.command(LIST)
    }

    /**
     * Добавляет команду `sort` с указанным порядком сортировки.
     *
     * @param by поле, по которому производится сортировка.
     * @param order порядок сортировки (восходящий или Descending).
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder sort(String by, Order order) {
        return this.command("$SORT${order.name()}$by")
    }

    /**
     * Добавляет указанную команду в список.
     *
     * @param command команда для добавления.
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder command(String command) {
        this.commands.add(command)
        return this
    }

    /**
     * Выполняет накопленные команды и возвращает результат.
     *
     * @return результат выполнения команд или сообщение об ошибке, если выполнение не удалось.
     */
    String execute() {
        if (commands.isEmpty()) {
            throw new IllegalStateException("Нет команд для выполнения.")
        }
        def error = new StringBuilder()
        def process = this.commands.execute()
        process.consumeProcessErrorStream(error)
        def result = process.in.text.trim()
        return error.isEmpty() ? result : error.toString().trim()
    }
}