package by.vitikova.plugin.service

import by.vitikova.plugin.constant.Branch
import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.exception.NotFoundException
import by.vitikova.plugin.exception.TagAlreadyExistException
import by.vitikova.plugin.exception.UncommittedException
import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.factory.impl.exist.*
import by.vitikova.plugin.factory.impl.noexist.*
import by.vitikova.plugin.repository.impl.GitRepositoryImpl
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Сервис для работы с Git, реализующий логику для создания и пуша тегов
 * в репозитории. Наследуется от `DefaultTask` и предназначен для использования
 * в Gradle задачах.
 *
 * <p> Основные функции включают в себя: </p>
 * <ul>
 *     <li>Проверка существования незакоммиченных изменений</li>
 *     <li>Создание тегов в зависимости от существующих тегов</li>
 *     <li>Пуш тегов в локальный и удаленный репозиторий</li>
 * </ul>
 *
 * <p>Класс использует следующие фабрики для управления тегами:
 * <ul>
 *     <li>{@link DevExistingTagFactory} - для создания тегов, когда тегов еще не существует, для dev.</li>
 *     <li>{@link QaExistingTagFactory} - для создания тегов, когда тегов еще не существует, для qa.</li>
 *     <li>{@link MasterExistingTagFactory} - для создания тегов, когда тегов еще не существует, для master.</li>
 *     <li>{@link StageExistingTagFactory} - для создания тегов, когда тегов еще не существует, для stage.</li>
 *     <li>{@link OtherBranchExistingTagFactory} - для создания тегов, когда тегов еще не существует, для других ветвей.</li>
 *
 *     <li>{@link DevExistingTagFactory} - для создания тегов, если они уже существуют, для dev.</li>
 *     <li>{@link QaExistingTagFactory} - для создания тегов, если они уже существуют, для qa.</li>
 *     <li>{@link MasterExistingTagFactory} - для создания тегов, если они уже существуют, для master.</li>
 *     <li>{@link StageExistingTagFactory} - для создания тегов, если они уже существуют, для stage.</li>
 *     <li>{@link OtherBranchExistingTagFactory} - для создания тегов, если они уже существуют, для других ветвей.</li>
 * </ul>
 * </p>
 *
 * <p> Убраны незакоммиченные изменения: если изменения есть, будет выброшено
 * {@link UncommittedException}. Если тег уже существует, будет выброшено
 * {@link TagAlreadyExistException}.</p>
 *
 * @author VitMVit
 */
class GitService extends DefaultTask {

    private static final Logger logger = LoggerFactory.getLogger(GitService.class)

    @Input
    def gitRepository = new GitRepositoryImpl()

    @TaskAction
    void pushTag() {
        logger.info("GIT SERVICE Starting to push tag...")
        validateGitVersion()
        checkForUncommittedChanges()
        def latestTagVersion = gitRepository.findLatestTagVersion()
        logger.info("GIT SERVICE Latest tag version found: {}", latestTagVersion)
        def branchName = gitRepository.findCurrentBranchName()
        logger.info("GIT SERVICE Current branch name: {}", branchName)
        if (latestTagVersion.isEmpty()) {
            handleNoExistingTag(branchName, Constant.DEFAULT_TAG_VERSION)
        } else {
            handleExistingTag(branchName, latestTagVersion)
        }
    }

    /**
     * Проверяет, установлена ли версия Git, и выбрасывает исключение, если ее нет.
     */
    private void validateGitVersion() {
        logger.info("GIT SERVICE Validating Git version...")
        try {
            logger.info("GIT SERVICE Git version is valid.")
            gitRepository.findGitVersion()
        } catch (IOException e) {
            logger.error("GIT SERVICE Git version not found: {}", e.getMessage())
            throw new NotFoundException(Constant.GIT_NOT_FOUND_MESSAGE)
        }
    }

    /**
     * Проверяет наличие незакоммиченных изменений в репозитории и выбрасывает исключение, если они есть.
     */
    private void checkForUncommittedChanges() {
        if (project.pushTag.isUncommitted) {
            logger.info("GIT SERVICE Checking for uncommitted changes...")
            def uncommitted = gitRepository.findUncommittedChanges()
            if (!uncommitted.isEmpty()) {
                def tagVersion = gitRepository.findCurrentTagVersion()
                def exceptionMessage = tagVersion.isEmpty()
                        ? Constant.UNCOMMITTED_CHANGES_NO_TAG_MESSAGE
                        : Constant.UNCOMMITTED_CHANGES_WITH_TAG_MESSAGE + tagVersion + Constant.POSTFIX_UNCOMMITTED;
                logger.error("GIT SERVICE Uncommitted changes found: {}", exceptionMessage)
                throw new UncommittedException(exceptionMessage)
            }
        }
    }

    /**
     * Обрабатывает случай, когда нет существующего тега.
     *
     * @param branchName имя текущей ветки
     * @param latestTagVersion последняя версия тега
     */
    private void handleNoExistingTag(String branchName, String latestTagVersion) {
        logger.info("GIT SERVICE Handling case of no existing tag...")
        TagFactory tagFactory = getTagFactory(branchName, false)
        def tagName = tagFactory.createTagName(branchName, latestTagVersion)
        logger.info("GIT SERVICE Creating new tag, Local tag pushed, Tag pushed to origin: {}", tagName)
        gitRepository.pushTagToLocal(tagName);
        gitRepository.pushTagToOrigin(tagName);
        logger.warn Constant.TAG_NAME_WARNING_MESSAGE + tagName
    }

    /**
     * Обрабатывает случай, когда тег уже существует.
     *
     * @param branchName имя текущей ветки
     * @param latestTagVersion последняя версия тега
     */
    private void handleExistingTag(String branchName, String latestTagVersion) {
        logger.info("GIT SERVICE Handling existing tag... Branch: {}, Latest Tag Version: {}", branchName, latestTagVersion)
        def currentTagVersion = gitRepository.findCurrentTagVersion()
        if (latestTagVersion == currentTagVersion) {
            logger.error("GIT SERVICE Tag already exists: {}", currentTagVersion)
            throw new TagAlreadyExistException(Constant.TAG_ALREADY_EXISTS_MESSAGE + currentTagVersion)
        } else {
            logger.info("GIT SERVICE Creating new tag because the latest tag version is different from the current tag version.")
            TagFactory tagFactory = getTagFactory(branchName, true)
            def tagName = tagFactory.createTagName(branchName, latestTagVersion)
            logger.info("GIT SERVICE Creating tag: {}", tagName)
            gitRepository.pushTagToLocal(tagName)
            gitRepository.pushTagToOrigin(tagName)
            logger.warn Constant.TAG_NAME_WARNING_MESSAGE + tagName
        }
    }

    /**
     * Возвращает соответствующую фабрику тегов в зависимости от существующих тегов для указанной ветки.
     *
     * @param branchName название ветки, для которой требуется фабрика тегов.
     * @param exists флаг, указывающий, существуют ли теги уже.
     * @return объект {@link TagFactory}, отвечающий за создание тегов
     * для указанной ветки.
     */
    private static TagFactory getTagFactory(String branchName, boolean exists) {
        switch (branchName) {
            case Branch.DEV.getName():
                return exists ? new DevExistingTagFactory() : new DevNoExistingTagFactory();
            case Branch.QA.getName():
                return exists ? new QaExistingTagFactory() : new QaNoExistingTagFactory();
            case Branch.MASTER.getName():
                return exists ? new MasterExistingTagFactory() : new MasterNoExistingTagFactory();
            case Branch.STAGE.getName():
                return exists ? new StageExistingTagFactory() : new StageNoExistingTagFactory();
            default:
                return exists ? new OtherBranchExistingTagFactory() : new OtherBranchNoExistingTagFactory();
        }
    }
}