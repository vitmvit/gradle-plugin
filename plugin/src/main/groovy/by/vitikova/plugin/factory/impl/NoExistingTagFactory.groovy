package by.vitikova.plugin.factory.impl

import by.vitikova.plugin.constant.Branch
import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Реализация `TagFactory`, которая создает имя тега, основываясь на
 * отсутствующих тегах, использует значение по умолчанию для версии тега.
 *
 * <p> Реализует логику для различных веток разработки, добавляя постфиксы
 * для RC и SNAPSHOT если это необходимо.</p>
 *
 * @author VitMVit
 */
class NoExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(NoExistingTagFactory.class)

    @Override
    String createTagName(String branchName, String latestTagVersion) {
        latestTagVersion = Constant.DEFAULT_TAG_VERSION
        logger.info("NO EXISTING TAG FACTORY Creating tag name for branch: {}, using default version: {}", branchName, latestTagVersion)
        if (branchName == Branch.DEV.getName() || branchName == Branch.QA.getName() || branchName == Branch.MASTER.getName()) {
            logger.info("NO EXISTING TAG FACTORY Returning tag name for branch {}: {}", branchName, latestTagVersion)
            return latestTagVersion
        } else if (branchName == Branch.STAGE.getName()) {
            logger.info("NO EXISTING TAG FACTORY Adding RC postfix for branch {}: {}", branchName, latestTagVersion)
            return "$latestTagVersion$Constant.RC"
        } else {
            logger.info("NO EXISTING TAG FACTORY Adding SNAPSHOT postfix for branch {}: {}", branchName, latestTagVersion)
            return "$latestTagVersion$Constant.SNAPSHOT"
        }
    }
}