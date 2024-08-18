package by.vitikova.plugin.constant

/**
 * Интерфейс `Constant` содержит набор констант, используемых в приложении.
 *
 * <p> Эти константы представляют собой строки, которые часто используются
 * в различных командах и операциях, связанных с системой контроля версий Git
 * и обработкой тегов.</p>
 *
 * <p> Использование интерфейса позволяет централизовать управление этими значениями,
 * что упрощает их изменение и поддержку в будущем.</p>
 *
 * @author VitMVit
 */
interface Constant {

    // command
    public static final String PUSH_TAG = 'pushTag'
    public static final String DEFAULT_TAG_VERSION = 'v0.1'

    public static final String ABBREV = '--abbrev='
    public static final String BRANCH = 'branch'
    public static final String DESCRIBE = 'describe'
    public static final String DIFF = 'diff'
    public static final String GIT = 'git'
    public static final String LIST = '-l'
    public static final String ORIGIN = 'origin'
    public static final String PUSH = 'push'
    public static final String RC = '-rc'
    public static final String SHOW_CURRENT = '--show-current'
    public static final String SNAPSHOT = '-SNAPSHOT'
    public static final String SORT = '--sort='
    public static final String TAG = 'tag'
    public static final String TAGS = '--tags'
    public static final String VERSION = 'version'

    // regex
    public static final String VERSION_REGEX = /\d+\.\d+/
    public static final String DOT = '\\.'
    public static final String POSTFIX_UNCOMMITTED = ".uncommitted"

    // message
    public static final String GIT_NOT_FOUND_MESSAGE = "Unable to locate Git on this machine. Please ensure it is installed and accessible from the command line!"
    public static final String UNCOMMITTED_CHANGES_NO_TAG_MESSAGE = "Uncommitted changes detected in the repository without any tags!"
    public static final String UNCOMMITTED_CHANGES_WITH_TAG_MESSAGE = "Uncommitted changes detected in the current tag version: "
    public static final String TAG_NAME_WARNING_MESSAGE = "The current commit is assigned tag: "
    public static final String TAG_ALREADY_EXISTS_MESSAGE = "The current state of the project is already tagged: "
    public static final String NO_TAG_MESSAGE = "fatal: No names found, cannot describe anything"
}
