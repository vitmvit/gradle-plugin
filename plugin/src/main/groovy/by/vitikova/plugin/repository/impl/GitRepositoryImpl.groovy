package by.vitikova.plugin.repository.impl

import by.vitikova.plugin.builder.CommandBuilder
import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.constant.Order
import by.vitikova.plugin.repository.GitRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Реализация интерфейса `GitRepository`, которая предоставляет методы для
 * выполнения различных операций с репозиторием Git.
 *
 * <p> Использует `CommandBuilder` для создания и выполнения команд в Git,
 * таких как получение версии Git, нахождение тегов, распознавание изменений
 * и управление ветками.</p>
 *
 * @author VitMVit
 */
class GitRepositoryImpl implements GitRepository {

    private static final Logger logger = LoggerFactory.getLogger(GitRepositoryImpl.class)

    /**
     * Возвращает текущую версию Git, установленного на системе.
     *
     * @return строка, содержащая информацию о версии Git
     */
    @Override
    String findGitVersion() {
        logger.info("GIT REPOSITORY Finding Git version...")
        return executeGitCommand { builder ->
            builder.git().version().execute()
        }
    }

    /**
     * Возвращает список всех незакоммиченных измеений в репозитории.
     *
     * @return строка с описанием незакоммиченных изменений
     */
    @Override
    String findUncommittedChanges() {
        logger.info("GIT REPOSITORY Finding uncommitted changes...")
        return executeGitCommand { builder ->
            builder.git().diff().execute()
        }
    }

    /**
     * Находит и возвращает последнюю версию тега в репозитории Git.
     *
     * Этот метод использует команду Git для получения последнего тега, к которому
     * был применён коммит. Если тегов нет, возвращает пустую строку.
     *
     * @return строка, содержащая последнюю версию тега или пустую строку,
     * если тегов не найдено.
     */
    @Override
    String findLatestTagVersion() {
        logger.info("GIT REPOSITORY Finding latest tag version...")
        return executeGitCommand { builder ->
            String result = builder.git().describe().tags().abbrev(0).execute()
            if (result.contains(Constant.NO_TAG_MESSAGE)) {
                return ""
            }
            return result
        }
    }

    /**
     * Находит и возвращает текущую версию тега в репозитории Git.
     *
     * Этот метод использует команду Git для получения текущего тега, связанного
     * с последним коммитом. Если тегов нет, возвращает пустую строку.
     *
     * @return строка, содержащая текущую версию тега или пустую строку,
     * если тегов не найдено.
     */
    @Override
    String findCurrentTagVersion() {
        logger.info("GIT REPOSITORY Finding current tag version...")
        return executeGitCommand { builder ->
            String result = builder.git().describe().tags().execute()
            if (result.contains(Constant.NO_TAG_MESSAGE)) {
                return ""
            }
            return result
        }
    }

    /**
     * Находит и возвращает имя текущей ветки в репозитории.
     *
     * @return строка с именем текущей ветки
     */
    @Override
    String findCurrentBranchName() {
        logger.info("GIT REPOSITORY Finding current branch name...")
        return executeGitCommand { builder ->
            builder.git().branch().showCurrent().execute()
        }
    }

    /**
     * Находит последний тег для веток DEV и QA на основе указанной версии тега.
     *
     * <p> Исключает теги, заканчивающиеся на {@link Constant#SNAPSHOT} и
     * {@link Constant#RC}.</p>
     *
     * @param tagVersion строка, содержащая версию тега для поиска
     * @return строка с последней найденной версией тега или переданная версия, если тег не найден
     */
    @Override
    String findLatestDevAndQATagByTagVersion(String tagVersion) {
        logger.info("GIT REPOSITORY Finding latest DEV and QA tag for version: {}", tagVersion)
        return findLatestTagByPattern("${tagVersion.find(/v(\d+)/)}\\.*", false, tagVersion)
    }

    /**
     * Находит последний таг, который является снепшотом, по указанной версии тега.
     *
     * @param tagVersion строка, содержащая версию тега для поиска
     * @return строка с последней найденной версией снепшота или переданная версия, если тег не найден
     */
    @Override
    String findLatestSnapshotTagByTagVersion(String tagVersion) {
        logger.info("GIT REPOSITORY Finding latest snapshot tag for version: {}", tagVersion)
        return findLatestTagByPattern("${tagVersion.find(/v(\d+)/)}\\.*$Constant.SNAPSHOT", true, tagVersion)
    }

    /**
     * Создает новый тег в локальном репозитории.
     *
     * @param tagName имя тега, который нужно создать
     * @return строка с результатом выполнения команды
     */
    @Override
    String pushTagToLocal(String tagName) {
        logger.info("GIT REPOSITORY Pushing local tag: {}", tagName)
        return executeGitCommand { builder ->
            builder.git().tag().command(tagName).execute()
        }
    }

    /**
     * Пушит тег на удаленный репозиторий (origin).
     *
     * @param tagName имя тега, который нужно запушить
     * @return строка с результатом выполнения команды
     */
    @Override
    String pushTagToOrigin(String tagName) {
        logger.info("GIT REPOSITORY Pushing tag to origin: {}", tagName)
        return executeGitCommand { builder ->
            builder.git().push().origin().command(tagName).execute()
        }
    }

    /**
     * Выполняет команду Git с использованием переданного построителя команды.
     *
     * @param commandClosure замыкание для построения команды
     * @return результат выполнения команды
     */
    private static String executeGitCommand(Closure commandClosure) {
        logger.info("GIT REPOSITORY Executing Git command...")
        return CommandBuilder.builder().with(commandClosure)
    }

    /**
     * Находит последний тег по заданному шаблону. Исключает теги, заканчивающиеся на SNAPSHOT и RC, если specified не равно true.
     *
     * @param pattern шаблон, по которому будет осуществляться поиск
     * @param isSnapshot флаг, указывающий, нужно ли искать снепшотный тег
     * @param tagVersion версия тега, возвращаемая при отсутствии найденных тегов
     * @return строка с последней найденной версией тега или переданная версия, если тег не найден
     */
    private static String findLatestTagByPattern(String pattern, boolean isSnapshot, String tagVersion) {
        logger.info("GIT REPOSITORY Finding latest tag by pattern: {}, isSnapshot: {}", pattern, isSnapshot)

        def commandBuilder = CommandBuilder.builder()
        def result = commandBuilder.git()
                .tag()
                .list()
                .command(pattern)
                .sort('version:refname', Order.DESC)
                .execute()
                .lines()
        return isSnapshot ? result.find { it.endsWith(Constant.SNAPSHOT) } ?: tagVersion : result.find { it.endsWith(Constant.SNAPSHOT) || it.endsWith(Constant.RC) } ?: tagVersion
    }
}