package by.vitikova.plugin.factory.impl.exist


import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.util.TagUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда теги уже существуют,
 * для ветки STAGE.
 * <p>
 * Добавляет суффикс RC к последнему тегу версии.
 * </p>
 */
class StageExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(StageExistingTagFactory.class)

    /**
     * Создает имя тега для ветки STAGE, добавляя суффикс RC к последней версии тега.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега, к которой будет добавлен суффикс.
     * @return имя созданного тега с суффиксом RC.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        def tagNumbers = TagUtil.findAndSplitTagVersionByDot(latestTagVersion)
        def rcTagVersion = TagUtil.addRCPostfix(tagNumbers)
        logger.info("EXISTING TAG FACTORY Adding RC postfix, resulting in tag version: {}", rcTagVersion)
        return rcTagVersion
    }
}