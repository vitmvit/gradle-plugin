package by.vitikova.plugin.factory.impl.noexist

import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда тегов еще не существует,
 * для ветки STAGE.
 * <p>
 * Добавляет суффикс RC к последнему тегу версии.
 * </p>
 */
class StageNoExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(StageNoExistingTagFactory.class);

    /**
     * Создает имя тега для ветки STAGE, добавляя суффикс RC.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега, к которой будет добавлен суффикс.
     * @return имя созданного тега с суффиксом RC.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        logger.info("STAGE NO EXISTING TAG FACTORY Adding RC postfix for branch {}: {}", branchName, latestTagVersion)
        return "$latestTagVersion$Constant.RC"
    }
}