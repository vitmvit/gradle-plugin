package by.vitikova.plugin.factory.impl.exist


import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.repository.impl.GitRepositoryImpl
import by.vitikova.plugin.util.TagUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Фабрика для создания тегов в ситуации, когда теги уже существуют,
 * для других веток (не определенных в специальном списке).
 * <p>
 * Добавляет суффикс SNAPSHOT к последнему тегу версии.
 * </p>
 */
class OtherBranchExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(OtherBranchExistingTagFactory.class)
    def gitRepository = new GitRepositoryImpl()

    /**
     * Создает имя тега для других веток, добавляя суффикс SNAPSHOT к последней версии тега.
     *
     * @param branchName название ветки, для которой создается тег.
     * @param latestTagVersion последняя версия тега.
     * @return имя созданного тега с суффиксом SNAPSHOT.
     */
    @Override
    String createTagName(String branchName, String latestTagVersion) {
        latestTagVersion = gitRepository.findLatestSnapshotTagByTagVersion(latestTagVersion)
        logger.debug("EXISTING TAG FACTORY Latest tag version for SNAPSHOT: {}", latestTagVersion)
        def tagNumbers = TagUtil.findAndSplitTagVersionByDot(latestTagVersion)
        def snapshotTagVersion = TagUtil.addSnapshotPostfix(tagNumbers)
        logger.info("EXISTING TAG FACTORY Adding SNAPSHOT postfix, resulting in tag version: {}", snapshotTagVersion)
        return snapshotTagVersion
    }
}