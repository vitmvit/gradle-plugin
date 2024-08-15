package by.vitikova.plugin.factory.impl

import by.vitikova.plugin.constant.Branch
import by.vitikova.plugin.constant.Constant
import by.vitikova.plugin.factory.TagFactory

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

    @Override
    String createTagName(String branchName, String latestTagVersion) {
        latestTagVersion = Constant.DEFAULT_TAG_VERSION

        if (branchName in [Branch.DEV.name(), Branch.QA.name(), Branch.MASTER.name()]) {
            return latestTagVersion
        } else if (branchName == Branch.STAGE.name()) {
            return "$latestTagVersion$Constant.RC"
        } else {
            return "$latestTagVersion$Constant.SNAPSHOT"
        }
    }
}