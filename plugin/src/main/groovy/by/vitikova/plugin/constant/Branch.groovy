package by.vitikova.plugin.constant

import lombok.Getter

/**
 * Перечисление `Branch` представляет различные ветки в контексте разработки.
 *
 * <p> Каждая ветка имеет присвоенное ей имя, которое может быть использовано для удобного
 * обращения к веткам в коде.</p>
 *
 * @author VitMVit
 */
@Getter
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
}