package by.vitikova.plugin.factory.impl.exist

import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.util.TagUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда теги уже существуют,
 * для ветки MASTER.
 * <p>
 * Инкрементирует мажорную версию последнего тега.
 * </p>
 */
class MasterExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(MasterExistingTagFactory.class)

    /**
     * Создает имя тега для ветки MASTER, инкрементируя мажорную версию последней версии тега.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега.
     * @return новая версия тега с инкрементированной мажорной частью.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        logger.info("DEV EXISTING TAG FACTORY Latest tag version for MASTER: {}", latestTagVersion)
        def tagNumbers = TagUtil.findAndSplitTagVersionByDot(latestTagVersion)
        def majorTagVersion = TagUtil.incrementMajorVersion(tagNumbers)
        logger.info("MASTER EXISTING TAG FACTORY Incrementing major version resulting in new tag version: {}", majorTagVersion)
        return majorTagVersion
    }
}