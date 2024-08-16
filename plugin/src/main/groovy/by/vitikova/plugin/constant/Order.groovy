package by.vitikova.plugin.constant
/**
 * Перечисление `Order` определяет порядок сортировки, который может быть использован
 * при выполнении операций сортировки в приложении.
 *
 * <p> Доступные значения:</p>
 * <ul>
 *     <li>{@link #ASC} - сортировка в порядке возрастания</li>
 *     <li>{@link #DESC} - сортировка в порядке убывания</li>
 * </ul>
 *
 * @author VitMVit
 */
enum Order {

    ASC(''),
    DESC('-')

    private String name;

    Order(String name) {
        this.name = name
    }

    String getName() {
        return name
    }
}