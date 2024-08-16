package by.vitikova.plugin.constant
/**
 * Перечисление `Branch` представляет различные ветки в контексте разработки.
 *
 * <p> Каждая ветка имеет присвоенное ей имя, которое может быть использовано для удобного
 * обращения к веткам в коде.</p>
 *
 * @author VitMVit
 */
enum Branch {

    /** Ветка разработки. */
    DEV('dev'),

    /** Ветка тестирования. */
    QA('qa'),

    /** Ветка предрелизного тестирования. */
    STAGE('stage'),

    /** Основная ветка разработки. */
    MASTER('master')

    private String name

    Branch(String name) {
        this.name = name
    }

    String getName() {
        return name
    }
}