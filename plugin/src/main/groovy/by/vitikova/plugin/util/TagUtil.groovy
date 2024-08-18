package by.vitikova.plugin.util

import by.vitikova.plugin.constant.Constant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Утилита для работы с тегами.
 * <p>
 * Этот класс предоставляет статические методы для управления версиями тегов,
 * включая инкрементирование версий и добавление постфиксов.
 * </p>
 */
class TagUtil {

    private static final Logger logger = LoggerFactory.getLogger(TagUtil.class)

    /**
     * Увеличивает минорную версию тега.
     *
     * @param tagNumbers массив строк, содержащий части версии тега (например, [major, minor, patch]).
     * @return новая строка с увеличенной минорной версией тега.
     */
    static def incrementMinorVersion(String[] tagNumbers) {
        logger.info("TAG UTIL increment minor version for tag: {}", tagNumbers)
        tagNumbers[1] = (tagNumbers[1] as int) + 1
        var result = "v${tagNumbers.join('.')}"
        logger.info("TAG UTIL result: {}", tagNumbers)
        return result
    }

    /**
     * Увеличивает мажорную версию тега.
     *
     * @param tagNumbers массив строк, содержащий части версии тега (например, [major, minor, patch]).
     * @return новая строка с увеличенной мажорной версией тега.
     */
    static def incrementMajorVersion(String[] tagNumbers) {
        logger.info("TAG UTIL increment major version for tag: {}", tagNumbers)
        tagNumbers[0] = (tagNumbers[0] as int) + 1
        tagNumbers[1] = '0'
        var result = "v${tagNumbers.join('.')}"
        logger.info("TAG UTIL result: {}", tagNumbers)
        return result
    }

    /**
     * Добавляет постфикс "RC" к минорной версии тега.
     *
     * @param tagNumbers массив строк, содержащий части версии тега (например, [major, minor, patch]).
     * @return новая строка с увеличенной минорной версией и постфиксом "RC".
     */
    static def addRCPostfix(String[] tagNumbers) {
        logger.info("TAG UTIL rc postfix for tag: {}", tagNumbers)
        def tagName = incrementMinorVersion(tagNumbers)
        var result = "$tagName$Constant.RC"
        logger.info("TAG UTIL result: {}", tagNumbers)
        return result
    }

    /**
     * Добавляет постфикс "SNAPSHOT" к минорной версии тега.
     *
     * @param tagNumbers массив строк, содержащий части версии тега (например, [major, minor, patch]).
     * @return новая строка с увеличенной минорной версией и постфиксом "SNAPSHOT".
     */
    static def addSnapshotPostfix(String[] tagNumbers) {
        logger.info("TAG UTIL snapshot postfix for tag: {}", tagNumbers)
        def tagName = incrementMinorVersion(tagNumbers)
        var result = "$tagName$Constant.SNAPSHOT"
        logger.info("TAG UTIL result: {}", tagNumbers)
        return result
    }

    /**
     * Находит и разбивает строку версии тега по точке.
     *
     * @param latestTagVersion строка, представляющая последнюю версию тега.
     * @return массив строк, содержащий части версии, разделенные точкой.
     */
    static def findAndSplitTagVersionByDot(String latestTagVersion) {
        logger.info("TAG UTIL find and split tag version by dot: {}", latestTagVersion)
        var result = latestTagVersion.find(Constant.VERSION_REGEX).split(Constant.DOT)
        logger.info("TAG UTIL result: {}", result)
        return result
    }
}