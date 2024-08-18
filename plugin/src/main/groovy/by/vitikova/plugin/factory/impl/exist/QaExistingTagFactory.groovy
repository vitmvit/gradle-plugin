package by.vitikova.plugin.factory.impl.exist


import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.repository.impl.GitRepositoryImpl
import by.vitikova.plugin.util.TagUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда теги уже существуют,
 * для ветки QA.
 * <p>
 * Инкрементирует минорную версию последнего тега.
 * </p>
 */
class QaExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(QaExistingTagFactory.class)
    def gitRepository = new GitRepositoryImpl()

    /**
     * Создает имя тега для ветки QA, инкрементируя минорную версию последней версии тега.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега.
     * @return новая версия тега с инкрементированной минорной частью.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        latestTagVersion = gitRepository.findLatestDevAndQATagByTagVersion(latestTagVersion)
        logger.debug("EXISTING TAG FACTORY Latest tag version for QA: {}", latestTagVersion)
        def tagNumbers = TagUtil.findAndSplitTagVersionByDot(latestTagVersion)
        def newTagVersion = TagUtil.incrementMinorVersion(tagNumbers)
        logger.info("EXISTING TAG FACTORY Incrementing minor version resulting in new tag version: {}", newTagVersion)
        return newTagVersion
    }
}