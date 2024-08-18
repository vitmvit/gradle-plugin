package by.vitikova.plugin.factory.impl.noexist


import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда тегов еще не существует,
 * для других веток (не определенных в специальном списке).
 * <p>
 * Добавляет суффикс SNAPSHOT к последнему тегу версии.
 * </p>
 */
class OtherBranchNoExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(OtherBranchNoExistingTagFactory.class);

    /**
     * Создает имя тега для других веток, добавляя суффикс SNAPSHOT.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега, к которой будет добавлен суффикс.
     * @return имя созданного тега с суффиксом SNAPSHOT.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        logger.info("OTHER NO EXISTING TAG FACTORY Adding SNAPSHOT postfix for branch {}: {}", branchName, latestTagVersion)
        return "$latestTagVersion$Constant.SNAPSHOT"
    }
}