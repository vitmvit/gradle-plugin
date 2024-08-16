package by.vitikova.plugin.builder

import by.vitikova.plugin.constant.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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

    private static final Logger logger = LoggerFactory.getLogger(CommandBuilder.class)
    List<String> commands

    private CommandBuilder() {
        commands = new ArrayList<>()
    }

    static def builder() {
        return new CommandBuilder()
    }

    /**
     * Добавляет команду `abbrev` с указанным числом.
     *
     * @param number число для команды `abbrev`.
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder abbrev(int number) {
        logger.debug("COMMAND BUILDER Adding command: abbrev {}", number)
        return this.command("$ABBREV$number")
    }

    /**
     * Добавляет команду `branch`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder branch() {
        logger.debug("COMMAND BUILDER Adding command: branch")
        return this.command(BRANCH)
    }

    /**
     * Добавляет команду `describe`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder describe() {
        logger.debug("COMMAND BUILDER Adding command: describe")
        return this.command(DESCRIBE)
    }

    /**
     * Добавляет команду `diff`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder diff() {
        logger.debug("COMMAND BUILDER Adding command: diff")
        return this.command(DIFF)
    }

    /**
     * Добавляет команду `git`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder git() {
        logger.debug("COMMAND BUILDER Adding command: git")
        return this.command(GIT)
    }

    /**
     * Добавляет команду `list`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder list() {
        logger.debug("COMMAND BUILDER Adding command: list")
        return this.command(LIST)
    }

    /**
     * Добавляет команду `origin`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder origin() {
        logger.debug("COMMAND BUILDER Adding command: origin")
        return this.command(ORIGIN)
    }

    /**
     * Добавляет команду `push`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder push() {
        logger.debug("COMMAND BUILDER Adding command: push")
        return this.command(PUSH)
    }

    /**
     * Добавляет команду `showCurrent`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder showCurrent() {
        logger.debug("COMMAND BUILDER Adding command: showCurrent")
        return this.command(SHOW_CURRENT)
    }

    /**
     * Добавляет команду `tag`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder tag() {
        logger.debug("COMMAND BUILDER Adding command: tag")
        return this.command(TAG)
    }

    /**
     * Добавляет команду `tags`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder tags() {
        logger.debug("COMMAND BUILDER Adding command: tags")
        return this.command(TAGS)
    }

    /**
     * Добавляет команду `version`.
     *
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder version() {
        logger.debug("COMMAND BUILDER Adding command: version")
        return this.command(VERSION)
    }

    /**
     * Добавляет команду `sort` с указанным порядком сортировки.
     *
     * @param by поле, по которому производится сортировка.
     * @param order порядок сортировки (восходящий или Descending).
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder sort(String by, Order order) {
        logger.debug("COMMAND BUILDER Adding command: sort by {} in order {}", by, order)
        return this.command("$SORT${order.name()}$by")
    }

    /**
     * Добавляет указанную команду в список.
     *
     * @param command команда для добавления.
     * @return текущий экземпляр `CommandBuilder`.
     */
    CommandBuilder command(String command) {
        logger.debug("COMMAND BUILDER Adding command: {}", command)
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
            logger.error("COMMAND BUILDER No command.")
            throw new IllegalStateException("No command.")
        }
        logger.info("COMMAND BUILDER Executing commands: {}", commands)

        def error = new StringBuilder()
        def process = this.commands.execute()
        process.consumeProcessErrorStream(error)

        def result = process.in.text.trim()
        logger.debug("COMMAND BUILDER Process result: {}", result)
        logger.debug("COMMAND BUILDER Process error: {}", error.toString().trim())
        return error.isEmpty() ? result : error.toString().trim()
    }
}