package by.vitikova.plugin.factory.impl.noexist


import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда тегов еще не существует,
 * для ветки MASTER.
 * <p>
 * Возвращает стандартную версию тега.
 * </p>
 */
class MasterNoExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(MasterNoExistingTagFactory.class);

    /**
     * Создает имя тега для ветки MASTER, возвращая стандартную версию.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега (не используется в данном случае).
     * @return стандартная версия тега.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        logger.info("MASTER NO EXISTING TAG FACTORY Creating tag for branch: {}", branchName);
        return Constant.DEFAULT_TAG_VERSION;
    }
}