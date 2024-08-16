package by.vitikova.plugin.factory.impl

import by.vitikova.plugin.constant.Branch
import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.repository.impl.GitRepositoryImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Реализация `TagFactory`, которая создает имя тега, основываясь на
 * существующих тегах и ветке.
 *
 * <p> Реализует логику для различных веток разработки, включая
 * инкрементирование версии и добавление постфиксов, таких как RC и SNAPSHOT.</p>
 *
 * @author VitMVit
 */
class ExistingTagFactory implements TagFactory {

    private static final Logger logger = LoggerFactory.getLogger(ExistingTagFactory.class)
    def gitRepository = new GitRepositoryImpl()

    @Override
    String createTagName(String branchName, String latestTagVersion) {
        logger.info("EXISTING TAG FACTORY Creating tag name for branch: {}, with latest tag version: {}", branchName, latestTagVersion)
        switch (branchName) {
            case Branch.DEV.name() || Branch.QA.name():
                latestTagVersion = gitRepository.findLatestDevAndQATagByTagVersion(latestTagVersion)
                logger.debug("EXISTING TAG FACTORY Latest tag version for DEV/QA: {}", latestTagVersion)
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                def newTagVersion = incrementMinorVersion(tagNumbers)
                logger.info("EXISTING TAG FACTORY Incrementing minor version resulting in new tag version: {}", newTagVersion)
                return newTagVersion
            case Branch.STAGE.name():
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                def rcTagVersion = addRCPostfix(tagNumbers)
                logger.info("EXISTING TAG FACTORY Adding RC postfix, resulting in tag version: {}", rcTagVersion)
                return rcTagVersion
            case Branch.MASTER.name():
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                def majorTagVersion = incrementMajorVersion(tagNumbers)
                logger.info("EXISTING TAG FACTORY Incrementing major version resulting in new tag version: {}", majorTagVersion)
                return majorTagVersion
            default:
                latestTagVersion = gitRepository.findLatestSnapshotTagByTagVersion(latestTagVersion)
                logger.debug("EXISTING TAG FACTORY Latest tag version for SNAPSHOT: {}", latestTagVersion)
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                def snapshotTagVersion = addSnapshotPostfix(tagNumbers)
                logger.info("EXISTING TAG FACTORY Adding SNAPSHOT postfix, resulting in tag version: {}", snapshotTagVersion)
                return snapshotTagVersion
        }
    }

    private static def incrementMinorVersion(String[] tagNumbers) {
        tagNumbers[1] = (tagNumbers[1] as int) + 1
        "v${tagNumbers.join('.')}"
    }

    private static def incrementMajorVersion(String[] tagNumbers) {
        tagNumbers[0] = (tagNumbers[0] as int) + 1
        tagNumbers[1] = '0'
        "v${tagNumbers.join('.')}"
    }

    private static def addRCPostfix(String[] tagNumbers) {
        def tagName = incrementMinorVersion(tagNumbers)
        "$tagName$Constant.RC"
    }

    private static def addSnapshotPostfix(String[] tagNumbers) {
        def tagName = incrementMinorVersion(tagNumbers)
        "$tagName$Constant.SNAPSHOT"
    }

    private static def findAndSplitTagVersionByDot(String latestTagVersion) {
        latestTagVersion.find(Constant.VERSION_REGEX).split(Constant.DOT)
    }
}