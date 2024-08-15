package by.vitikova.plugin.factory.impl

import by.vitikova.plugin.constant.Branch
import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory
import by.vitikova.plugin.repository.impl.GitRepositoryImpl

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

    def gitRepository = new GitRepositoryImpl()

    @Override
    String createTagName(String branchName, String latestTagVersion) {
        switch (branchName) {
            case Branch.DEV.name() || Branch.QA.name():
                latestTagVersion = gitRepository.findLatestDevAndQATagByTagVersion(latestTagVersion)
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                incrementMinorVersion(tagNumbers)
                break
            case Branch.STAGE.name():
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                addRCPostfix(tagNumbers)
                break
            case Branch.MASTER.name():
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                incrementMajorVersion(tagNumbers)
                break
            default:
                latestTagVersion = gitRepository.findLatestSnapshotTagByTagVersion(latestTagVersion)
                def tagNumbers = findAndSplitTagVersionByDot(latestTagVersion)
                addSnapshotPostfix(tagNumbers)
                break
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